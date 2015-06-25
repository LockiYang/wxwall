package com.wxwall.common.listener;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.wxwall.common.utils.ShakeManager;
import com.wxwall.modules.wechat.entity.WeChatApp;
import com.wxwall.modules.wechat.service.WeChatService;
import com.wxwall.wechat.api.advanced.model.AccessToken;
import com.wxwall.wechat.api.util.CommonUtil;
import com.wxwall.wechat.api.util.WxAuthChecker;

/**
 * <p>
 * 描述:监听循环类，负责定期执行特定任务
 * </p>
 * 
 * @see
 * @author ganjun
 * 
 */
@Service
public class CommonListener {
	private static final Logger LOG = Logger.getLogger(CommonListener.class);

	@Autowired
	private WeChatService weChatService;

	/**
	 * 每个小时准时更新AccessToken状态
	 */
	@Scheduled(cron = "0 0 * * * ?")
	public void checkWeChatAppAccessTokenStatus() {
		try {
			LOG.info("check weChatApp accessToken expire status start!");
			Iterable<WeChatApp> iterWeChatApp = weChatService
					.getWeChatAppByExpire();
			int count = 0;
			if (iterWeChatApp != null) {
				Iterator<WeChatApp> iter = iterWeChatApp.iterator();
				while(iter.hasNext()) {
					count++;
					WeChatApp weChatApp = iter.next();
					// 连接微信服务器，初始化AccessToken以及expire过期时间,尝试3次
					for (int i=0; i<3; i++) {
						AccessToken accessToken = CommonUtil.getAccessToken(
								weChatApp.getAppId(), weChatApp.getAppSecret());
						if (accessToken != null) {
							Calendar calendar = Calendar.getInstance(TimeZone
									.getDefault());
							Date expire = new Date(calendar.getTime().getTime()
									+ accessToken.getExpiresin() * 1000);
							weChatApp.setAccessToken(accessToken.getAccesstoken());
							weChatApp.setExpire(expire);
							byte authRole = WxAuthChecker.getAuthRole(weChatApp.getAccessToken());
							weChatApp.setAccountType(authRole);
							weChatService.save(weChatApp, false);
							break;
						} else {
							LOG.error("'" + weChatApp.getAppId() + "'获取accessToken失败!");
						}
						//睡眠2秒重新请求
						Thread.sleep(2000);
					}
				}
			}
			LOG.info("check weChatApp accessToken expire status nums:"
					+ count);
			LOG.info("check weChatApp accessToken expire status end!");
			
			LOG.info("clear activity shake cache start!");
			ShakeManager.getInstance().cleanCache();
			LOG.info("clear activity shake cache end!");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
