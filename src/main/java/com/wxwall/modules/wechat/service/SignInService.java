package com.wxwall.modules.wechat.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wxwall.common.entity.Result;
import com.wxwall.common.service.BaseService;
import com.wxwall.common.utils.CommonUtils;
import com.wxwall.common.utils.DateUtils;
import com.wxwall.modules.wechat.dao.RActivityWeChatUserDao;
import com.wxwall.modules.wechat.dao.WeChatActivityDao;
import com.wxwall.modules.wechat.dao.WeChatUserDao;
import com.wxwall.modules.wechat.entity.RActivityWeChatUser;
import com.wxwall.modules.wechat.entity.WeChatActivity;
import com.wxwall.modules.wechat.entity.WeChatApp;
import com.wxwall.modules.wechat.entity.WeChatUser;

@Service
@Transactional
public class SignInService extends BaseService {
	
	@Autowired private WeChatActivityDao weChatActivityDao;
	@Autowired private WeChatUserDao weChatUserDao;
	@Autowired private RActivityWeChatUserDao rActivityWeChatUserDao;
	
	/**
	 * 非认证公众号OAuth2方式签到
	 * 并获取用户信息
	 * 
	 * @param mid
	 * @param openId
	 * @param secret
	 * @return
	 */
	public Result OAuth2SignIn(String mid, String openId, String secret) {
		Result result = new Result();
		
		WeChatActivity weChatActivity = weChatActivityDao.findByActivityMid(mid);
		
		if (weChatActivity == null) {
			LOG.error("授权认证失败，活动未发现,活动MID：" + mid);
			result.setMessage("很抱歉，<br>活动不存在，请确认！");
			result.setSuccess(false);
			return result;
		}
		
		if(!StringUtils.equalsIgnoreCase(weChatActivity.getSignKeyWord(), secret)) {
			result.setMessage("很抱歉，<br>房间号不正确！");
			result.setSuccess(false);
			return result;
		}
		
		if (weChatActivity.beforeEnd()) {
			if (!weChatActivity.isfSignIn()) {// 活动是否开启了签到模式
				result.setSuccess(false);
				result.setMessage("活动暂时不允许签到，请联系主办方!");
				return result;
			}
			// 正在进行的已签到活动
			// 取消所有以前的状态
			WeChatUser weChatUser = weChatUserDao.findByWeChatAppIDAndOpenID(weChatActivity.getWeChatApp().getId(), openId);
			if (weChatUser == null) {
				result.setSuccess(false);
				result.setMessage("很抱歉，<br>活动可能已经结束，请联系主办方!");
				return result;
			}
			
			rActivityWeChatUserDao.updateStatusRActWeUserByOpenID(openId,
					RActivityWeChatUser.USER_STATUS.SIGN.getIndex(),
					RActivityWeChatUser.USER_STATUS.CANCEL_SIGN.getIndex());
			
			RActivityWeChatUser rActivityWeChatUser = rActivityWeChatUserDao
					.findRActWeUserByWeActMIDAndOpenID(
							weChatActivity.getActivityMid(), openId);
			if (rActivityWeChatUser == null) {
				String uuid = CommonUtils.generateUUID();
				rActivityWeChatUser = new RActivityWeChatUser(weChatUser,
						weChatActivity,
						RActivityWeChatUser.USER_STATUS.SIGN.getIndex(), uuid);
			} else {
				if (StringUtils.isBlank(rActivityWeChatUser.getUuid())) {
					String uuid = CommonUtils.generateUUID();
					rActivityWeChatUser.setUuid(uuid);
				}
				rActivityWeChatUser
						.setStatus(RActivityWeChatUser.USER_STATUS.SIGN
								.getIndex());
				rActivityWeChatUser.setUpdateTime(DateUtils.now());
				rActivityWeChatUser.setSignInDate(rActivityWeChatUser.getUpdateTime());
			}
			
			rActivityWeChatUserDao.save(rActivityWeChatUser);

			// 发送自动回复
			result.setSuccess(true);
			result.setMessage("恭喜您，<br>签到成功，发送任意消息即可上墙!");
			return result;
		} else {
			result.setSuccess(false);
			result.setMessage("很抱歉，<br>活动可能已经结束，请联系主办方!");
			return result;
		}
	}
	
	/**
	 * 网页认证
	 */
	public Result noOAuth(String activityMid, String sourceOpenId) {
		Result result = new Result();
		try {
			Map<String, String> map = new HashMap<String, String>();
			WeChatActivity weChatActivity = weChatActivityDao.findByActivityMid(activityMid);
			
			if (weChatActivity == null) {
				LOG.error("授权认证失败，活动未发现,活动MID：" + activityMid);
				result.setSuccess(false);
				return result;
			}
			if (weChatActivity.afterProgress()) {
				result.setMessage("活动已经结束!");
				result.setSuccess(false);
				return result;
			}
			WeChatApp weChatApp = weChatActivity.getWeChatApp();
			
			// 保存或者更新关注用户
			WeChatUser weChatUser = weChatUserDao.findByWeChatAppIDAndOpenID(weChatApp.getId(), sourceOpenId);
			if (weChatUser == null) {// 新关注用户
				LOG.error("授权认证失败，用户未未发现,weChatAppID:" + weChatApp.getId() + "用户原始ID：" + sourceOpenId);
				result.setSuccess(false);
				return result;
			} 
			weChatUser = weChatUserDao.save(weChatUser);
			map.put("mid", weChatActivity.getActivityMid());
			map.put("activitySubject", weChatActivity.getSubject());
			map.put("userAvatar", weChatUser.getHeadImgUrl());
			map.put("nickName", weChatUser.getNickName());
			map.put("openId", weChatUser.getOpenID());
			result.setData(map);
		}catch (Exception e) {
			LOG.error("OAuth认证异常！", e);
			result.setSuccess(false);
			return result;
		}
		
		return result;
	}
	
	/**
	 * 网页OAuth2认证读取用户信息
	 */
//	public Result oAuth2UserInfo(String code, String activityMid,String sourceOpenId) {
//		Result result = new Result();
//		try {
//			Map<String, String> map = new HashMap<String, String>();
//			WeChatActivity weChatActivity = weChatActivityDao.findByActivityMid(activityMid);
//			
//			if (weChatActivity == null) {
//				LOG.error("授权认证失败，活动未发现,活动MID：" + activityMid);
//				result.setSuccess(false);
//				return result;
//			}
//			// 通过code换取网页授权access_token
//			WeixinOauth2Token weiXinOauth2Token = OAuthUtil.getOauth2AccessToken(
//					CommonUtils.getSystemAppId(), CommonUtils.getSystemAppSecret(),
//					code);
//			if (weiXinOauth2Token == null) {
//				LOG.error("OAuth2获取网页授权凭证失败");
//				result.setSuccess(false);
//				return result;
//			}
//			// 拉取用户信息(需scope为 snsapi_userinfo)
//			SNSUserInfo snsUserInfo = OAuthUtil.getSNSUserInfo(
//					weiXinOauth2Token.getAccessToken(),
//					weiXinOauth2Token.getOpenId());
//			
//			if (snsUserInfo == null) {
//				LOG.error("OAuth2获取用户信息失败");
//				result.setSuccess(false);
//				return result;
//			}
//			// 生成一个新的用户信息对象
//			String nickName = snsUserInfo.getNickname();
//			byte sex = (byte) (snsUserInfo.getSex());
//			String language = snsUserInfo.getLanguage();
//			String city = snsUserInfo.getCity();
//			String province = snsUserInfo.getProvince();
//			String country = snsUserInfo.getCountry();
//			String headimgurl = snsUserInfo.getHeadImgUrl();
//			Date subscribeTime = snsUserInfo.getSubscribeTime();
//			WeChatApp weChatApp = weChatActivity.getWeChatApp();
//			
//			// 保存或者更新关注用户
//			WeChatUser weChatUser = weChatUserDao.findByWeChatAppIDAndOpenID(weChatApp.getId(), sourceOpenId);
//			if (weChatUser == null) {// 新关注用户
//				weChatUser = new WeChatUser(sourceOpenId, nickName, sex, language,
//						city, province, country, headimgurl, subscribeTime,
//						weChatApp);
//			} else {
//				weChatUser.setOpenID(sourceOpenId);
//				weChatUser.setNickName(FaceUtils.filterEmoji(nickName));
//				weChatUser.setSex(sex);
//				weChatUser.setHeadImgUrl(headimgurl);
//				weChatUser.setLanguage(language);
//				weChatUser.setCity(city);
//				weChatUser.setProvince(province);
//				weChatUser.setCountry(country);
//				weChatUser.setSubscribeTime(subscribeTime);
//				weChatUser.setUnSubscribeTime(null);
//				weChatUser.setWeChatApp(weChatApp);
//			}
//			weChatUser = weChatUserDao.save(weChatUser);
//			
//			// 判断活动是否结束
//			if (weChatActivity.afterProgress()) {
//				result.setMessage("活动已经结束!");
//				result.setSuccess(false);
//				return result;
//			}
//			
//			map.put("mid", weChatActivity.getActivityMid());
//			map.put("activitySubject", weChatActivity.getSubject());
//			map.put("userAvatar", weChatUser.getHeadImgUrl());
//			map.put("nickName", weChatUser.getNickName());
//			map.put("openId", weChatUser.getOpenID());
//			result.setData(map);
//		}catch (Exception e) {
//			LOG.error("OAuth认证异常", e);
//			result.setSuccess(false);
//			return result;
//		}
//		
//		return result;
//	}
}
