package com.wxwall.modules.activity.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wxwall.common.entity.Result;
import com.wxwall.common.service.BaseService;
import com.wxwall.common.service.ServiceException;
import com.wxwall.common.utils.CommonUtils;
import com.wxwall.common.utils.DateUtils;
import com.wxwall.modules.user.dao.UserDao;
import com.wxwall.modules.user.entity.User;
import com.wxwall.modules.user.utils.UserUtils;
import com.wxwall.modules.wechat.dao.ActivityAutoReplyDao;
import com.wxwall.modules.wechat.dao.ActivityImageDao;
import com.wxwall.modules.wechat.dao.ActivityPhotoDao;
import com.wxwall.modules.wechat.dao.RActivityWeChatUserDao;
import com.wxwall.modules.wechat.dao.WeChatActivityDao;
import com.wxwall.modules.wechat.dao.WeChatImageTextDao;
import com.wxwall.modules.wechat.dao.WeChatTextDao;
import com.wxwall.modules.wechat.dao.WeChatUserMsgDao;
import com.wxwall.modules.wechat.entity.ActivityAutoReply;
import com.wxwall.modules.wechat.entity.ActivityImage;
import com.wxwall.modules.wechat.entity.ActivityPhoto;
import com.wxwall.modules.wechat.entity.AutoReply;
import com.wxwall.modules.wechat.entity.RActivityWeChatUser;
import com.wxwall.modules.wechat.entity.WeActivityTips;
import com.wxwall.modules.wechat.entity.WeChatActivity;
import com.wxwall.modules.wechat.entity.WeChatApp;
import com.wxwall.modules.wechat.entity.WeChatImageText;
import com.wxwall.modules.wechat.entity.WeChatText;
import com.wxwall.modules.wechat.entity.WeChatUserMsg;
import com.wxwall.wechat.api.advanced.util.CreateQRCode;
import com.wxwall.wechat.api.advanced.util.GetQRCode;


@Service
@Transactional
public class ActivityService extends BaseService {

	@Autowired private WeChatActivityDao weChatActivityDao;
	@Autowired private WeChatUserMsgDao weChatUserMsgDao;
	@Autowired private RActivityWeChatUserDao rActivityWeChatUserDao;
	@Autowired private ActivityImageDao activityImageDao;
	@Autowired private ActivityPhotoDao activityPhotoDao;
	@Autowired private WeChatTextDao weChatTextDao;
	@Autowired private WeChatImageTextDao weChatImageTextDao;
	@Autowired private ActivityAutoReplyDao activityAutoReplyDao;
	@Autowired private UserDao userDao;

	/**
	 * 删除活动图片
	 * @param id
	 * @param rootPath
	 * @throws IOException
	 */
	public void delImage(long id, String rootPath) throws IOException {
		ActivityImage activityImage = activityImageDao.findOne(id);
		if (activityImage == null) {
			return;
		}
		String sourceFileName = activityImage.getRelativePath();
		activityImageDao.delete(id);
		if (StringUtils.isNotBlank(sourceFileName)) {
			File sourceFile = new File(rootPath + File.separator + sourceFileName);
			if (sourceFile.exists()) {
				FileUtils.forceDelete(sourceFile);
				LOG.info("delete backGround image:" + sourceFile.getAbsolutePath() + " successed!");
			}
		}
	}
	
	/**
	 * 删除精彩瞬间图片
	 * @param id
	 * @param rootPath
	 * @throws IOException
	 */
	public void delPhotoPile(long id, String rootPath) throws IOException {
		ActivityPhoto activityPhoto = activityPhotoDao.findOne(id);
		if (activityPhoto == null) {
			return;
		}
		String sourceFileName = null;
		if (activityPhoto.getType() == ActivityPhoto.SYSTEM_UPLOAD) {
			sourceFileName = activityPhoto.getRelativePath();
		}
		
		activityPhotoDao.delete(id);
		if (StringUtils.isNotBlank(sourceFileName)) {
			File sourceFile = new File(rootPath + File.separator + sourceFileName);
			if (sourceFile.exists()) {
				FileUtils.forceDelete(sourceFile);
				LOG.info("delete backGround image:" + sourceFile.getAbsolutePath() + " successed!");
			}
		}
	}

	public void save(WeChatActivity weChatActivity) {
		weChatActivityDao.save(weChatActivity);
	}
	
	public void saveActivityPhoto(ActivityPhoto activityPhoto) {
		activityPhotoDao.save(activityPhoto);
	}
	
	public WeChatActivity findByActivityMid(String activityMid) {
		return weChatActivityDao.findByActivityMid(activityMid);
	}
	
	public List<ActivityImage> findBackGroundByActivityMid(String activityMid) {
		return activityImageDao.findByWeChatActivityMID(activityMid, ActivityImage.IMAGE_TYPE.BACKGROUND.getName());
	}
	
	public List<ActivityImage> findAlbumByActivityMid(String activityMid) {
		return activityImageDao.findByWeChatActivityMID(activityMid, ActivityImage.IMAGE_TYPE.ALBUM.getName());
	}
	
	/**
	 * 手动结束活动
	 * 
	 * @param activityId
	 * @return
	 */
	public void endActivity(String activityMid) {
		User user = UserUtils.getUser();
		if (user == null || user.getWeChatApp() == null) {
			throw new RuntimeException("没有绑定公众号");
		}
		
		WeChatActivity myActivity = weChatActivityDao.findByWeChatAppAndActivityMid(user.getWeChatApp(), activityMid);
		if (myActivity == null) {
			throw new RuntimeException("活动不存在");
		} else {
			if (DateUtils.biggerThan(myActivity.getStartDate(), DateUtils.now())) {
				myActivity.setStartDate(DateUtils.now());
			}
			myActivity.setEndDate(DateUtils.now());
			weChatActivityDao.save(myActivity);
		}
	}
	
	/**
	 * 检查当前用户是否有新建活动的权限
	 * @return
	 */
	public boolean checkNewVipActivity() {
		if (UserUtils.hasRole(User.VIP_USER)) {
			return true;
		} else {
			User user = UserUtils.getUser();
			if (user != null) {
				if (user.getVipTimes() >= 1) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 新建活动
	 * 
	 * @param weChatActivity
	 * @return
	 * @throws IOException 
	 */
	public WeChatActivity newWeChatActivity(WeChatActivity weChatActivity, String rootPath) {
		User currentUser = UserUtils.getUser();
		
		if (!checkNewVipActivity()) {// 检查创建活动的权限
			throw new ServiceException("剩余活动次数不足，请升级VIP.");
		}
		
		WeChatApp weChatApp = currentUser.getWeChatApp();
		if (weChatApp == null) {
			throw new RuntimeException("没有绑定公众号");
		} else {
			weChatActivity.setWeChatApp(weChatApp);
		}
		
		// 服务号处理二维码
		if (weChatApp.getAccountType() == WeChatApp.ACCOUNT_TYPE.SERVICER.getIndex()) {//服务号
			//获取存储目录
			String relativePath = CommonUtils.generateNewDir(CommonUtils.STORE_PICTURE_DIR);
			String storePath = rootPath + File.separator + relativePath;
			File distDir = new File(storePath);
			
			if (!distDir.exists()) {
				distDir.mkdirs();
			}
			//生成新的文件名
			String strDestFile = CommonUtils.generateNewFileName(currentUser.getId(), "ticket.jpg");
			int maxSceneID = 0;
			//max函数在mysql里面如果不存在记录则会返回空值
			if (weChatApp.getWeChatActivitys().size() != 0) {
				maxSceneID = weChatActivityDao.findByMaxSceneId(weChatApp.getId());
			}
			weChatActivity.setSceneID(maxSceneID + 1);
			
			//生成永久带参数二维码
			String ticket = CreateQRCode.createPermanentQRCode(weChatApp.getAccessToken(), maxSceneID + 1);
			if (StringUtils.isBlank(ticket)) {
				throw new ServiceException("创建二维码失败，请检查公众号绑定是否成功.");
			} else {
				// 保存二维码图片
				String absolutePath = GetQRCode.getQRCode(ticket, storePath, strDestFile);
				if (absolutePath == null) {
					throw new ServiceException("连接微信服务器失败，请重试.");
				} else {
					weChatActivity.setTicket(relativePath + File.separator + strDestFile);
				}
			}
		}
		
		weChatActivity.setSignKeyWord(WeChatActivity.DEF_SIGN_KEY_WORD);
		// 生成mid和token，为活动验证提供条件
		String mid = CommonUtils.generateUUID();
		weChatActivity.setActivityMid(mid);
		weChatActivity.setfAutoUpWall(true);
		weChatActivity.setfSignIn(true);
		//weChatActivity.setShakeTime(61);
		weChatActivityDao.save(weChatActivity);
		/**
		 * 默认签到文本回复
		 */
		WeChatText weChatText = new WeChatText(ActivityAutoReply.DEF_SIGN_CONTENT);
		weChatTextDao.save(weChatText);
		/**
		 * 默认签到图文回复
		 */
		//默认签到图文回复
//		String destFileName = CommonUtils.generateNewFileName(currentUser.getId(), "pre.jpg");
//		String sourceFileName = ActivityAutoReply.DEF_SIGN_IMG;
//		File sourceFile = new File(rootPath + File.separator + sourceFileName);
//		
//		String relativePath = CommonUtils.generateNewDir(CommonUtils.STORE_PICTURE_DIR);
//		String destFilePath = rootPath + File.separator + relativePath;
//		
//		File destFile = new File(destFilePath, destFileName);
//		try {
//			FileUtils.copyFile(sourceFile, destFile, true);
//		} catch (IOException e) {
//			LOG.error(e.getMessage(), e);
//			throw new ServiceException("文件可能不存在，请重试.");
//		}
//		
//		String preFixFileName = relativePath + File.separator + destFileName;
//		
//		WeChatImageText weChatImageText = new WeChatImageText(ActivityAutoReply.DEF_SIGN_CONTENT, preFixFileName, null, null, null);
		WeChatImageText weChatImageText = new WeChatImageText();
		weChatImageTextDao.save(weChatImageText);
		
		AutoReply signAutoReply = new AutoReply(AutoReply.REPLY_TYPE.TEXT.getIndex(), weChatImageText, weChatText);
		
		/**
		 * 默认上墙成功文本回复
		 */
		WeChatText upbangWeChatText = new WeChatText(ActivityAutoReply.DEF_UPBANG_CONTENT);
		weChatTextDao.save(upbangWeChatText);
		AutoReply upbangAutoReply = new AutoReply(AutoReply.REPLY_TYPE.TEXT.getIndex(), null, upbangWeChatText);
		
		// 设置活动自动广告
		ActivityAutoReply activityAutoReply = new ActivityAutoReply(signAutoReply, upbangAutoReply, weChatActivity);
		activityAutoReplyDao.save(activityAutoReply);
		
		//设置活动页面顶部自动提示
//		WeActivityTips weActivityTips = new WeActivityTips(weChatActivity.getSubject(), weChatActivity);
//		weActivityTipsDao.save(weActivityTips);
		
		// 更新剩余使用次数
		if (!UserUtils.hasRole(User.VIP_USER)) {
			currentUser.setVipTimes(currentUser.getVipTimes() - 1);
			userDao.save(currentUser);
		}
		
		return weChatActivity;
	}
	
	/**
	 * 返回我的活动列表
	 * @param page
	 * @param size
	 * @param type
	 * @return
	 */
	public Page<WeChatActivity> getMyActivitys(int page, int size, String type) {
		
		User user = UserUtils.getUser();
		if (user == null || user.getWeChatApp() == null) {
			throw new RuntimeException("没有绑定公众号");
		}
		
		long weChatAppId = user.getWeChatApp().getId();
		
		Page<WeChatActivity> myActivitys = null;
		
		PageRequest pageRequest = new PageRequest(page, size, Direction.DESC, new String[] { "startDate", "createDate"});
		
		if (StringUtils.equalsIgnoreCase("prep", type)) {
			myActivitys = weChatActivityDao.findByWeChatAppAndBeforeProgress(weChatAppId, DateUtils.now(), pageRequest);
		} else if (StringUtils.equalsIgnoreCase("done", type)) {
			myActivitys = weChatActivityDao.findByWeChatAppAndAfterProgress(weChatAppId, DateUtils.now(), pageRequest);
		} else {
			myActivitys = weChatActivityDao.findByWeChatAppAndInProgress(weChatAppId, DateUtils.now(), pageRequest);
		}
		
		return myActivitys;
	}
	/**
	 * 返回活动用户
	 * @param activityMid  活动MID
	 * @param status 用户状态
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<RActivityWeChatUser> listActivityUser(String activityMid, byte status, int page, int size) {
		PageRequest pageRequest = new PageRequest(page, size, Direction.DESC, new String[] { "updateTime"});
		Page<RActivityWeChatUser> listUsers = rActivityWeChatUserDao.findUsersByWeChatActivityMIDAndStatus(activityMid, status, pageRequest);
		return listUsers;
	}

	/**
	 * 返回活动页面头部信息
	 * 
	 * @param activityID
	 * @param showUrl
	 * @return
	 */
	public Result getHeadInfo(String activityMid, String showUrl) {
		// TODO
		Result result = new Result();
		WeChatActivity weChatActivity = weChatActivityDao.findByActivityMid(activityMid);
		if (weChatActivity == null) {
			LOG.warn("活动ID[" + activityMid + "]在数据库中未存在!");
			result.setSuccess(false);
			result.setMessage("请求参数错误");
			return result;
		}

		result.setSuccess(true);
		Map<String, Object> headInfo = new HashMap<String, Object>();
		headInfo.put("appType", weChatActivity.getWeChatApp().getAccountType());
		
		if (weChatActivity.getWeChatApp().getAccountType() != WeChatApp.ACCOUNT_TYPE.SERVICER.getIndex()) {
			headInfo.put("secret", weChatActivity.getSignKeyWord());
		}
		headInfo.put("appName", weChatActivity.getWeChatApp().getWeChatName());
		headInfo.put("fLogo", weChatActivity.isfLogo());
		if (StringUtils.isNotBlank(weChatActivity.getLogo())) {
			headInfo.put("logo",
					showUrl + File.separator + weChatActivity.getLogo());
		} else {
			headInfo.put("logo", null);
		}
		if(StringUtils.isNotBlank(weChatActivity.getTicket())) {
			headInfo.put("ticket",
					showUrl + File.separator + weChatActivity.getTicket());
		} else {
			headInfo.put("ticket",
					showUrl + File.separator + weChatActivity.getWeChatApp().getWeChatImage());
		}
		
		result.setData(headInfo);
		Date msgTimeTag = weChatActivity.getMsgTimeTag();
		if (msgTimeTag == null) {
			headInfo.put("totalUpbangMsgNum", 0);
		} else {
			long totalUpbangMsgNum = weChatUserMsgDao.findTotalByActivityMIDStatusDownUpdateTime(activityMid, 
					(byte) WeChatUserMsg.MSG_STATUS.SUCCESS.getIndex(), msgTimeTag);
			headInfo.put("totalUpbangMsgNum", totalUpbangMsgNum);
		}
		List<WeActivityTips> weActivityTipsList = weChatActivity.getTips();
		List<Object> switchMsgItems = new ArrayList<Object>();
		//添加活动主题提示
		Map<String, String> subJectTip = new HashMap<String, String>();
		subJectTip.put("text", weChatActivity.getSubject());
		switchMsgItems.add(subJectTip);
		for (WeActivityTips weActivityTips : weActivityTipsList) { 
			Map<String, String> tip = new HashMap<String, String>();
			tip.put("text", weActivityTips.getTipContent());
			switchMsgItems.add(tip);
		}
		headInfo.put("switchMsgItems", switchMsgItems);
		return result;
	}

	/**
	 * 返回主题列表
	 * @param activityID
	 * @param showUrl
	 * @param rootPath
	 * @return
	 */
	public Result getThemePreviewList(String activityMid, String showUrl, String rootPath) {
		// TODO
		Result result = new Result();
		WeChatActivity weChatActivity = weChatActivityDao.findByActivityMid(activityMid);
		if (weChatActivity == null) {
			LOG.warn("活动ID[" + activityMid + "]在数据库中未存在!");
			result.setSuccess(false);
			result.setMessage("请求参数错误");
			return result;
		}

		result.setSuccess(true);
		//加载默认背景
		List<Object> items = new ArrayList<Object>();
		Map<String, String> defActivityImgs = ActivityImage.defActivityImgs;
		for(String key : defActivityImgs.keySet()) {
			Map<String, Object> theme = new HashMap<String, Object>();
			theme.put("id", "theme-" + key);
			theme.put("index", key);
			theme.put("name", key);
			theme.put("src", showUrl + "/" + defActivityImgs.get(key));
			items.add(theme);
		}
		
		if (weChatActivity.isfBackground()) {//判断活动背景开关
			//加载自定义背景
			List<ActivityImage> backgroudImgs = activityImageDao.findByWeChatActivityMID(activityMid, ActivityImage.IMAGE_TYPE.BACKGROUND.getName());
			for(ActivityImage activityImage : backgroudImgs) {
				String key = CommonUtils.getOriginFileName(rootPath, activityImage.getRelativePath());
				Map<String, Object> theme = new HashMap<String, Object>();
				theme.put("id", "theme-" + key);
				theme.put("index", key);
				theme.put("name", key);
				theme.put("src", showUrl + "/" + activityImage.getRelativePath());
				items.add(theme);
			}
		}
		result.setItems(items);
		return result;
	}
	
//	/**
//	 * 返回已上墙的消息列表，此方法作废
//	 * 
//	 * @param activityID
//	 * @return
//	 */
//	public Result getUpbangListMsg(String activityMid, String serverUrl, Date queryDate, int limit) {
//		// TODO
//		Result result = new Result();
//		WeChatActivity weChatActivity = weChatActivityDao.findByActivityMid(activityMid);
//		if (weChatActivity == null) {
//			LOG.warn("活动ID[" + activityMid + "]在数据库中未存在!");
//			result.setSuccess(false);
//			result.setMessage("请求参数错误");
//			return result;
//		}
//		PageRequest pageRequest = new PageRequest(0, limit, Direction.DESC,
//				new String[] { "updateTime" });
//		// 查询系统已经上墙的时间戳
//		Date updateTime = null;
//		/*if (msgTimeTag == null && queryDate == null) {// 判断是不是第一次获取上墙信息
//			updateTime = CommonUtils.INIT_DATE_TIME;
//		} else if (msgTimeTag != null && queryDate == null) {// 判断是不是获取上墙最新信息
//			updateTime = msgTimeTag;
//		} else if (msgTimeTag != null && queryDate != null) {// 判断获取某时间之后上墙信息
//			if (queryDate.compareTo(msgTimeTag) <= 0) {
//				updateTime = queryDate;
//			} else {
//				LOG.warn("活动ID[" + activityID + "]在数据库中未存在!");
//				result.setSuccess(false);
//				result.setMessage("请求参数错误");
//				return result;
//			}
//		}*/
//		
//		updateTime = CommonUtils.INIT_DATE_TIME;
//
//		Page<WeChatUserMsg> weChatUserMsgs = weChatUserMsgDao
//				.findByActivityMIDStatusDownUpdateTime(activityMid,
//						WeChatUserMsg.MSG_STATUS.SUCCESS.getIndex(),
//						updateTime, pageRequest);
//		if (weChatUserMsgs.getContent().size() == 0) {
//			result.setSuccess(true);
//			return result;
//		} else {
//			// 取出消息
//			List<UpbangMsg> listUpbangMsgs = new ArrayList<UpbangMsg>();
//			for (WeChatUserMsg weChatUserMsg : weChatUserMsgs.getContent()) {
//				WeChatUser weChatUser = weChatUserMsg.getrActivityWeChatUser().getWeChatUser();
//				UpbangMsg upbangMsg = new UpbangMsg(weChatUserMsg, weChatUser, serverUrl);
//				listUpbangMsgs.add(upbangMsg);
//			}
//
//			result.setSuccess(true);
//			result.setItems(listUpbangMsgs);
//			return result;
//		}
//	}
}
