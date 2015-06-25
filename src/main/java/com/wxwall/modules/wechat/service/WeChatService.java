package com.wxwall.modules.wechat.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wxwall.common.entity.Result;
import com.wxwall.common.service.BaseService;
import com.wxwall.common.utils.CommonUtils;
import com.wxwall.modules.user.entity.User;
import com.wxwall.modules.user.utils.UserUtils;
import com.wxwall.modules.wechat.dao.WeChatAppAutoReplyDao;
import com.wxwall.modules.wechat.dao.WeChatAppDao;
import com.wxwall.modules.wechat.dao.WeChatAppMenuDao;
import com.wxwall.modules.wechat.dao.WeChatAppSubMenuDao;
import com.wxwall.modules.wechat.dao.WeChatImageTextDao;
import com.wxwall.modules.wechat.dao.WeChatTextDao;
import com.wxwall.modules.wechat.dao.WeChatUserDao;
import com.wxwall.modules.wechat.entity.AutoReply;
import com.wxwall.modules.wechat.entity.WeChatApp;
import com.wxwall.modules.wechat.entity.WeChatAppAutoReply;
import com.wxwall.modules.wechat.entity.WeChatAppMenu;
import com.wxwall.modules.wechat.entity.WeChatAppSubMenu;
import com.wxwall.modules.wechat.entity.WeChatImageText;
import com.wxwall.modules.wechat.entity.WeChatText;
import com.wxwall.modules.wechat.entity.WeChatUser;

@Service
@Transactional
public class WeChatService extends BaseService {

	@Autowired
	private WeChatAppDao weChatAppDao;
	@Autowired
	private WeChatUserDao weChatUserDao;
	@Autowired
	private WeChatAppMenuDao weChatAppMenuDao;
	@Autowired
	private WeChatAppSubMenuDao weChatAppSubMenuDao;
	@Autowired
	private WeChatTextDao weChatTextDao;
	@Autowired
	private WeChatImageTextDao weChatImageTextDao;
	@Autowired
	private WeChatAppAutoReplyDao weChatAppAutoReplyDao;

	/**
	 * 通过mid获得微信公众号信息
	 * 
	 * @param mid
	 * @return
	 */
	public WeChatApp getWeChatAppByMid(String mid) {
		return weChatAppDao.findByMid(mid);
	}
	/**
	 * 根据公众号ID与openId获取用户信息
	 * @param weChatId
	 * @param openId
	 * @return
	 */
	public WeChatUser getWeChatUserByWeChatIdAndOpenId(long weChatId, String openId) {
		return weChatUserDao.findByWeChatAppIDAndOpenID(weChatId, openId);
	}

	/**
	 * 通过mid获得微信公众号信息
	 * 
	 * @param mid
	 * @return
	 */
	public WeChatApp getWeChatAppByUserID(long userID) {

		return weChatAppDao.findByUserId(userID);
	}

	/**
	 * 获得ACCESSTOKEN过期的微信公众号
	 * 
	 * @return
	 */
	public Iterable<WeChatApp> getWeChatAppByExpire() {
		Iterable<WeChatApp> iterWeChatApp = weChatAppDao.findAll();
		return iterWeChatApp;
	}

	/**
	 * 用户更新公众号信息
	 * 
	 * @param weChatApp
	 */
	public void update(WeChatApp weChatApp) {
		weChatAppDao.save(weChatApp);
	}

	/**
	 * 添加一级菜单
	 * 
	 * @param name
	 * @return
	 */
	public Result addMenu(String name) {
		Result result = new Result();
		User user = UserUtils.getUser();
		WeChatApp currentWeChatApp = user.getWeChatApp();
		if (currentWeChatApp == null) {
			throw new RuntimeException("没有绑定公众号");
		}

		List<WeChatAppMenu> weChatAppMenus = currentWeChatApp
				.getWeChatAppMenus();
		if (currentWeChatApp.getWeChatAppMenus() != null
				&& weChatAppMenus.size() >= 3) {
			result.setSuccess(false);
			result.setMessage("一级菜单不能超过三个");
			return result;
		}

		WeChatAppMenu weChatAppMenu = new WeChatAppMenu(name, currentWeChatApp);
		weChatAppMenuDao.save(weChatAppMenu);
		result.setSuccess(true);
		return result;
	}

	public Result addSubMenu(long menuId, String name) {
		Result result = new Result();
		User user = UserUtils.getUser();
		WeChatApp currentWeChatApp = user.getWeChatApp();
		if (currentWeChatApp == null) {
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}

		WeChatAppMenu weChatAppMenus = weChatAppMenuDao
				.findByMenuIDAndWeChatID(currentWeChatApp.getId(), menuId);
		if (weChatAppMenus == null) {
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
		List<WeChatAppSubMenu> weChatAppSubMenus = weChatAppMenus
				.getWeChatAppSubMenus();
		if (weChatAppSubMenus != null && weChatAppSubMenus.size() >= 5) {
			result.setSuccess(false);
			result.setMessage("二级菜单不能超过五个");
			return result;
		}

		WeChatAppSubMenu weChatAppSubMenu = new WeChatAppSubMenu(name,
				weChatAppMenus);
		weChatAppSubMenuDao.save(weChatAppSubMenu);
		result.setSuccess(true);
		return result;
	}

	/**
	 * 
	 * @param weChatApp
	 * @return
	 */
	public WeChatApp save(WeChatApp weChatApp, boolean isCheckUser) {
		User user = UserUtils.getUser();
		if (isCheckUser == false || user.getWeChatApp() != null) {
			return weChatAppDao.save(weChatApp);
		} else {
			weChatAppDao.save(weChatApp);
			/**
			 * 添加自动回复
			 */

			WeChatImageText subscribeWeChatImageText = new WeChatImageText();
			WeChatText subscribeweChatText = new WeChatText("欢迎关注!");
			weChatImageTextDao.save(subscribeWeChatImageText);
			weChatTextDao.save(subscribeweChatText);

			AutoReply subscribeAutoReply = new AutoReply(
					AutoReply.REPLY_TYPE.TEXT.getIndex(),
					subscribeWeChatImageText, subscribeweChatText);

			WeChatImageText weChatImageText = new WeChatImageText();
			WeChatText weChatText = new WeChatText("欢迎光临我们的活动!");
			weChatImageTextDao.save(weChatImageText);
			weChatTextDao.save(weChatText);

			AutoReply autoReply = new AutoReply(
					AutoReply.REPLY_TYPE.TEXT.getIndex(), weChatImageText,
					weChatText);

			WeChatAppAutoReply weChatAppAutoReply = new WeChatAppAutoReply(
					subscribeAutoReply, autoReply, weChatApp);
			weChatAppAutoReplyDao.save(weChatAppAutoReply);

			return weChatApp;
		}
	}

	/**
	 * 设置公众号自动回复
	 * 
	 * @param setupType
	 * @param autoType
	 * @param content
	 * @param title
	 * @param imgUrl
	 * @param img
	 * @param rootPath
	 * @return
	 */
	public Result SetupWeChatAppAutoReply(String setupType, String autoType,
			String content, String title, String imgUrl, String img,
			String rootPath) {
		Result result = new Result();
		try {
			User user = UserUtils.getUser();
			WeChatApp weChatApp = user.getWeChatApp();
			if (weChatApp == null) {
				result.setMessage("系统内部错误!");
				return result;
			}

			WeChatAppAutoReply weChatAutoReply = weChatApp
					.getWeChatAppAutoReply();
			if (StringUtils.equalsIgnoreCase("subscribe", setupType)) {// 处理签到欢迎回复
				if (StringUtils.equals("text", autoType)) {
					weChatAutoReply.getSubscribeAutoReply().setReplyType(
							AutoReply.REPLY_TYPE.TEXT.getIndex());
				} else {
					weChatAutoReply.getSubscribeAutoReply().setReplyType(
							AutoReply.REPLY_TYPE.IMG_TEXT.getIndex());
				}

				weChatAutoReply.getSubscribeAutoReply().getWeChatText()
						.setContent(content);

				weChatAutoReply.getSubscribeAutoReply().getWeChatImageText()
						.setTitle(title);

				if (StringUtils.isNotBlank(imgUrl)) {
					weChatAutoReply.getSubscribeAutoReply()
							.getWeChatImageText().setImgUrl(null);
				} else {
					weChatAutoReply.getSubscribeAutoReply()
							.getWeChatImageText().setImgUrl(imgUrl);
				}

				String orignPreViewImg = null;
				if (StringUtils.isNoneBlank(img)) {
					File tmpFile = new File(img);
					// 检查临时文件是否存在
					if (!tmpFile.exists()) {
						LOG.error("系统未发现已上传的图片！路径：" + img);
						result.setMessage("系统未发现已上传的图片！");
						result.setSuccess(false);
						return result;
					}

					String preViewImgName = tmpFile.getName();
					String relativePath = CommonUtils
							.generateNewDir(CommonUtils.STORE_PICTURE_DIR);
					String strUploadLogoPath = rootPath + File.separator
							+ relativePath;
					File destFile = new File(strUploadLogoPath, preViewImgName);
					FileUtils.copyFile(tmpFile, destFile, true);
					orignPreViewImg = rootPath
							+ File.separator
							+ weChatAutoReply.getSubscribeAutoReply()
									.getWeChatImageText().getImg();
					weChatAutoReply
							.getSubscribeAutoReply()
							.getWeChatImageText()
							.setImg(relativePath + File.separator
									+ preViewImgName);
				}
				weChatAppAutoReplyDao.save(weChatAutoReply);
				if (StringUtils.isNotBlank(orignPreViewImg)) {
					File orignLogoFile = new File(orignPreViewImg);
					if (orignLogoFile.exists()) {
						FileUtils.forceDelete(orignLogoFile);
					}
				}
				result.setSuccess(true);
				return result;
			} else if (StringUtils.equalsIgnoreCase("autoReply", setupType)) {// 处理上墙自动回复
				if (StringUtils.equals("text", autoType)) {
					weChatAutoReply.getAutoReply().setReplyType(
							AutoReply.REPLY_TYPE.TEXT.getIndex());
				} else {
					weChatAutoReply.getAutoReply().setReplyType(
							AutoReply.REPLY_TYPE.IMG_TEXT.getIndex());
				}

				weChatAutoReply.getAutoReply().getWeChatText()
						.setContent(content);

				weChatAutoReply.getAutoReply().getWeChatImageText()
						.setTitle(title);

				if (StringUtils.isNotBlank(imgUrl)) {
					weChatAutoReply.getAutoReply().getWeChatImageText()
							.setImgUrl(null);
				} else {
					weChatAutoReply.getAutoReply().getWeChatImageText()
							.setImgUrl(imgUrl);
				}

				String orignPreViewImg = null;
				if (StringUtils.isNoneBlank(img)) {
					File tmpFile = new File(img);
					// 检查临时文件是否存在
					if (!tmpFile.exists()) {
						LOG.error("系统未发现已上传的图片！路径：" + img);
						result.setMessage("系统未发现已上传的图片！");
						result.setSuccess(false);
						return result;
					}

					String preViewImgName = tmpFile.getName();
					String relativePath = CommonUtils
							.generateNewDir(CommonUtils.STORE_PICTURE_DIR);
					String strUploadLogoPath = rootPath + File.separator
							+ relativePath;
					File destFile = new File(strUploadLogoPath, preViewImgName);
					FileUtils.copyFile(tmpFile, destFile, true);
					orignPreViewImg = rootPath
							+ File.separator
							+ weChatAutoReply.getAutoReply()
									.getWeChatImageText().getImg();
					weChatAutoReply
							.getAutoReply()
							.getWeChatImageText()
							.setImg(relativePath + File.separator
									+ preViewImgName);
				}
				weChatAppAutoReplyDao.save(weChatAutoReply);
				if (StringUtils.isNotBlank(orignPreViewImg)) {
					File orignLogoFile = new File(orignPreViewImg);
					if (orignLogoFile.exists()) {
						FileUtils.forceDelete(orignLogoFile);
					}
				}
				result.setSuccess(true);
				return result;
			} else {// 新增/修改页面提示信息
				result.setSuccess(true);
				return result;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误！");
			return result;
		}
	}

}
