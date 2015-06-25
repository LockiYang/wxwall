package com.wxwall.modules.wechat.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wxwall.common.service.BaseService;
import com.wxwall.common.utils.CommonUtils;
import com.wxwall.common.utils.DateUtils;
import com.wxwall.common.utils.FaceUtils;
import com.wxwall.modules.activity.dao.WechatUserFatePairDao;
import com.wxwall.modules.wechat.dao.ActivityImageDao;
import com.wxwall.modules.wechat.dao.ActivityPhotoDao;
import com.wxwall.modules.wechat.dao.RActivityWeChatUserDao;
import com.wxwall.modules.wechat.dao.WeChatActivityDao;
import com.wxwall.modules.wechat.dao.WeChatAppDao;
import com.wxwall.modules.wechat.dao.WeChatUserDao;
import com.wxwall.modules.wechat.dao.WeChatUserMsgDao;
import com.wxwall.modules.wechat.entity.ActivityAutoReply;
import com.wxwall.modules.wechat.entity.ActivityImage;
import com.wxwall.modules.wechat.entity.ActivityPhoto;
import com.wxwall.modules.wechat.entity.AutoReply;
import com.wxwall.modules.wechat.entity.RActivityWeChatUser;
import com.wxwall.modules.wechat.entity.WeChatActivity;
import com.wxwall.modules.wechat.entity.WeChatApp;
import com.wxwall.modules.wechat.entity.WeChatAppAutoReply;
import com.wxwall.modules.wechat.entity.WeChatUser;
import com.wxwall.modules.wechat.entity.WeChatUserMsg;
import com.wxwall.wechat.api.advanced.model.PersonalInf;
import com.wxwall.wechat.api.advanced.util.GetPersoninf;
import com.wxwall.wechat.api.advanced.util.OAuthUtil;
import com.wxwall.wechat.api.basic.model.SendArticle;
import com.wxwall.wechat.api.basic.util.SendBasicMessageUtil;
import com.wxwall.wechat.api.util.SignUtil;
import com.wxwall.wechat.api.util.WeixinUtil;

@Service
@Transactional
public class WeChatMsgProcessor extends BaseService {

	@Autowired
	private WeChatAppDao weChatAppDao;
	@Autowired
	private WeChatUserDao weChatUserDao;
	@Autowired
	private RActivityWeChatUserDao rActivityWeChatUserDao;
	@Autowired
	private WeChatUserMsgDao weChatUserMsgDao;
	@Autowired
	private WeChatActivityDao weChatActivityDao;
	@Autowired
	private WechatUserFatePairDao wechatUserFatePairDao;
	@Autowired
	private ActivityImageDao activityImageDao;
	@Autowired
	private ActivityPhotoDao activityPhotoDao;
	
	
	/**
	 * 验证服务器地址的有效性
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param token
	 * @return
	 */
	public boolean checkSignature(String signature, String timestamp,
			String nonce, String token) {
		return SignUtil.checkSignature(signature, timestamp, nonce, token);
	}

	public String processWechatMsg(String serverUrl, WeChatApp weChatApp,
			Map<String, String> reqMsgMap) {
		String respMessage = "";
		try {
			// 发送方帐号（一个OpenID）
			String fromUserName = reqMsgMap.get("FromUserName");
			// 开发者微信号
			String toUserName = reqMsgMap.get("ToUserName");
			// 消息类型
			String msgType = reqMsgMap.get("MsgType");

			// 文本消息
			if (msgType.equals(WeixinUtil.RECRIVE_TEXT)) {
				/**
				 * 去除重复消息
				 */
				String msgId = reqMsgMap.get("MsgId");
				String msgIdAndFromUser = fromUserName + msgId;
				if (CommonUtils.getMsgIdLRuCache().containsKey(msgIdAndFromUser)) {
					return null;
				} else {
					CommonUtils.getMsgIdLRuCache().put(msgIdAndFromUser, 1);
				}
				//
				String reqContent = reqMsgMap.get("Content");
				reqContent = StringUtils.trim(reqContent);
				respMessage = handleTextMsg(weChatApp, fromUserName,
						toUserName, reqContent, serverUrl);
			}
			// 图片消息
			else if (msgType.equals(WeixinUtil.RECRIVE_IMAGE)) {
				/**
				 * 去除重复消息
				 */
				String msgId = reqMsgMap.get("MsgId");
				String msgIdAndFromUser = fromUserName + msgId;
				if (CommonUtils.getMsgIdLRuCache().containsKey(msgIdAndFromUser)) {
					return null;
				} else {
					CommonUtils.getMsgIdLRuCache().put(msgIdAndFromUser, 1);
				}
				
				//
				String picUrl = reqMsgMap.get("PicUrl");
				respMessage = handleImgMsg(weChatApp, fromUserName, toUserName,
						picUrl, serverUrl);
			}
			// 地理位置消息
			else if (msgType.equals(WeixinUtil.RECRIVE_LOCATION)) {
			}
			// 链接消息
			else if (msgType.equals(WeixinUtil.RECRIVE_LINK)) {
			}
			// 音频消息
			else if (msgType.equals(WeixinUtil.RECRIVE_VOICE)) {
			}
			// 事件推送
			else if (msgType.equals(WeixinUtil.RECRIVE_EVENT)) {
				// 事件类型
				String eventType = reqMsgMap.get("Event");
				// 订阅
				if (eventType.equals(WeixinUtil.EVENT_QRCODE_SUBSCRIBE)) {
					// EventKey: 事件KEY值，qrscene_为前缀，后面为二维码的参数值，带参数的二维码才有
					String eventKey = reqMsgMap.get("EventKey");
					respMessage = handleSubscribe(weChatApp, fromUserName,
							toUserName, eventKey, serverUrl);
				}
				// 扫描带参数二维码
				else if (eventType.equals(WeixinUtil.EVENT_QRCODE_SCAN)) {
					String eventKey = reqMsgMap.get("EventKey");
					respMessage = handleScan(weChatApp, fromUserName,
							toUserName, eventKey, serverUrl);
				}
				// 取消订阅
				else if (eventType.equals(WeixinUtil.EVENT_UNSUBSCRIBE)) {
					handleUnSubscribe(weChatApp, fromUserName);
				}
				// 自定义菜单点击事件
				else if (eventType.equals(WeixinUtil.EVENT_CLICK)) {
					// TODO 自定义菜单权没有开放，暂不处理该类消息
				}
			}

			// 最终自动回复
			if (StringUtils.isBlank(respMessage)) {
				respMessage = null;
			}
		} catch (Exception e) {
			LOG.error("微信服务端信息处理失败", e);
		}

		return respMessage;
	}

	/**
	 * 处理文本消息
	 * 
	 * @param weChatApp
	 * @param fromUserName
	 * @param toUserName
	 * @param content
	 * @return
	 */
	public String handleTextMsg(WeChatApp weChatApp, String fromUserName,
			String toUserName, String content, String serverUrl) {
		
		// 判断系统中是否存在此活动
		String respContent = null;
		String respMessage = null;
		// 1、处理签到
		
		// 用户在该公众号下是否签到状态
		RActivityWeChatUser rActivityWeChatUser = rActivityWeChatUserDao.findSignRActWeUserByWeAppIDAndOpenIDAndEndDate(
						weChatApp.getId(), fromUserName, DateUtils.now());
		
		// 1、还没签到
		if (rActivityWeChatUser == null) {
			// 自动回复
			WeChatUser weChatUser = weChatUserDao.findByWeChatAppIDAndOpenID(weChatApp.getId(), fromUserName);
			/**
			 * 经过微信认证的公众号直接从此处获取用户信息
			 */
			if (weChatApp.getAccountType() != WeChatApp.ACCOUNT_TYPE.UNAUTH.getIndex()) {
				PersonalInf personalInf = GetPersoninf.getPersonalInf(
						weChatApp.getAccessToken(), fromUserName);
				if (personalInf == null) {
					respContent = "读取信息失败，请重新扫描二维码!";
					respMessage = SendBasicMessageUtil.sendTextmessage(fromUserName,
							toUserName, respContent);
					return respMessage;
				}

				// 保存或者更新关注用户
				if (weChatUser == null) {// 新关注用户
					weChatUser = new WeChatUser(personalInf, weChatApp);
				} else {
					weChatUser.setOpenID(personalInf.getOpenID());
					weChatUser.setNickName(FaceUtils.filterEmoji(personalInf
							.getNickname()));
					weChatUser.setSex((byte) personalInf.getSex());
					weChatUser.setHeadImgUrl(personalInf.getHeadImgUrl());
					weChatUser.setLanguage(personalInf.getLanguage());
					weChatUser.setCity(personalInf.getCity());
					weChatUser.setProvince(personalInf.getProvince());
					weChatUser.setCountry(personalInf.getCountry());
					weChatUser.setSubscribeTime(personalInf.getSubScribeTime());
					weChatUser.setUnSubscribeTime(null);
					weChatUser.setWeChatApp(weChatApp);
				}
				weChatUser = weChatUserDao.save(weChatUser);
			}
			
			if (weChatUser == null) {
				return noSignInResponce(weChatApp, serverUrl, fromUserName, toUserName, false, false);
			} else {
				return noSignInResponce(weChatApp, serverUrl, fromUserName, toUserName, false, true);
			}
		} else {//2、已签到
			rActivityWeChatUser.setUpdateTime(DateUtils.now());
			WeChatActivity weChatActivity= rActivityWeChatUser.getWeChatActivity();
			// 操作菜单
			// 退出活动
			if (StringUtils.equalsIgnoreCase(content, "退出")) {
				rActivityWeChatUser.setStatus(RActivityWeChatUser.USER_STATUS.CANCEL_SIGN.getIndex());
				rActivityWeChatUserDao.save(rActivityWeChatUser);
				respContent = "已经退出活动!\n【回复任意消息获取活动最新信息】";
				respMessage = SendBasicMessageUtil.sendTextmessage(fromUserName, toUserName, respContent);
				return respMessage;
			} else if (StringUtils.equalsIgnoreCase(content, WeChatActivity.openActivityPhotoForAdminToKeyWord)) {
				if (!weChatActivity.isfActivityPhoto()) {
					respContent = "精彩瞬间活动功能未开启，请先开启功能!";
					respMessage = SendBasicMessageUtil.sendTextmessage(fromUserName, toUserName, respContent);
					return respMessage;
				}
				
				if (!weChatActivity.isfActivityPhotoForAdmin()) {
					respContent = "精彩瞬间管理员微信上传图片功能未开启，请先开启功能!";
					respMessage = SendBasicMessageUtil.sendTextmessage(fromUserName, toUserName, respContent);
					return respMessage;
				}
				rActivityWeChatUser.setfActivityPhotoForAdmin(true);
				rActivityWeChatUserDao.save(rActivityWeChatUser);
				respContent = "精彩瞬间管理员微信上传图片功能已经开启，上传的图片将进入精彩瞬间模块!";
				respMessage = SendBasicMessageUtil.sendTextmessage(fromUserName, toUserName, respContent);
				return respMessage;
			} else if (StringUtils.equalsIgnoreCase(content, WeChatActivity.closeActivityPhotoForAdminToKeyWord)) {
				rActivityWeChatUser.setfActivityPhotoForAdmin(false);
				rActivityWeChatUserDao.save(rActivityWeChatUser);
				respContent = "精彩瞬间管理员微信上传图片功能已经关闭!";
				respMessage = SendBasicMessageUtil.sendTextmessage(fromUserName, toUserName, respContent);
				return respMessage;
			}
			// 添加上墙消息
			if (StringUtils.isNotBlank(content)) {
				
				//活动还未开始，限制上墙次数
				if (weChatActivity.beforeProgress()) {
					long msgNum = weChatUserMsgDao.findTotalUserMsgByActivityMID(weChatActivity
									.getActivityMid());
					if (msgNum >= WeChatActivity.DEF_USER_MSG_NUM_BEFORE_PROGRESS) {
						respContent = "活动还未开始，上墙消息数量已经超过默认值20条，请等待活动开始时再发消息！";
						respMessage = SendBasicMessageUtil.sendTextmessage(fromUserName, toUserName, respContent);
						return respMessage;
					}
				}
				//活动已经结束,此处应该不会进行
				if (weChatActivity.afterProgress()) {
//					rActivityWeChatUser.setStatus(RActivityWeChatUser.USER_STATUS.CANCEL_SIGN.getIndex());
//					rActivityWeChatUserDao.save(rActivityWeChatUser);
					return noSignInResponce(weChatApp, serverUrl, fromUserName, toUserName, false, true);
				}
				
				byte infoStatus = WeChatUserMsg.MSG_STATUS.CHECKING.getIndex();
				// 开启自动上墙
				if (rActivityWeChatUser.getWeChatActivity().isfAutoUpWall()) {
					infoStatus = WeChatUserMsg.MSG_STATUS.SUCCESS.getIndex();
					content = CommonUtils.replaceSensitiveWord(content);
				}

				WeChatUserMsg weChatUserMsg = new WeChatUserMsg(content, null,
						infoStatus, rActivityWeChatUser);
				weChatUserMsg = weChatUserMsgDao.save(weChatUserMsg);
			}
			return upWallResponce(weChatActivity, rActivityWeChatUser.getUuid(), serverUrl, fromUserName, toUserName);
		}
	}

	/**
	 * 处理图片消息
	 * 
	 * @param weChatApp
	 * @param fromUserName
	 * @param toUserName
	 * @param img
	 * @return
	 */
	public String handleImgMsg(WeChatApp weChatApp, String fromUserName,
			String toUserName, String img, String serverUrl) {
		// 判断系统中是否存在此活动
		String respContent = null;
		String respMessage = null;
		
		RActivityWeChatUser rActivityWeChatUser = rActivityWeChatUserDao.findSignRActWeUserByWeAppIDAndOpenIDAndEndDate(
				weChatApp.getId(), fromUserName, DateUtils.now());
		if (rActivityWeChatUser == null) {// 没有签到活动
			WeChatUser weChatUser = weChatUserDao.findByWeChatAppIDAndOpenID(
					weChatApp.getId(), fromUserName);
			/**
			 * 经过微信认证的公众号直接从此处获取用户信息
			 */
			if (weChatApp.getAccountType() != WeChatApp.ACCOUNT_TYPE.UNAUTH.getIndex()) {
				PersonalInf personalInf = GetPersoninf.getPersonalInf(
						weChatApp.getAccessToken(), fromUserName);
				if (personalInf == null) {
					respContent = "读取信息失败，请重新扫描二维码!";
					respMessage = SendBasicMessageUtil.sendTextmessage(fromUserName,
							toUserName, respContent);
					return respMessage;
				}

				// 保存或者更新关注用户
				if (weChatUser == null) {// 新关注用户
					weChatUser = new WeChatUser(personalInf, weChatApp);
				} else {
					weChatUser.setOpenID(personalInf.getOpenID());
					weChatUser.setNickName(FaceUtils.filterEmoji(personalInf
							.getNickname()));
					weChatUser.setSex((byte) personalInf.getSex());
					weChatUser.setHeadImgUrl(personalInf.getHeadImgUrl());
					weChatUser.setLanguage(personalInf.getLanguage());
					weChatUser.setCity(personalInf.getCity());
					weChatUser.setProvince(personalInf.getProvince());
					weChatUser.setCountry(personalInf.getCountry());
					weChatUser.setSubscribeTime(personalInf.getSubScribeTime());
					weChatUser.setUnSubscribeTime(null);
					weChatUser.setWeChatApp(weChatApp);
				}
				weChatUser = weChatUserDao.save(weChatUser);
			}
			if (weChatUser == null) {
				return noSignInResponce(weChatApp, serverUrl, fromUserName, toUserName, false, false);
			} else {
				return noSignInResponce(weChatApp, serverUrl, fromUserName, toUserName, false, true);
			}
		} else {// 已签到活动
			// 更新用户交互时间
			rActivityWeChatUser.setUpdateTime(Calendar.getInstance(
					TimeZone.getDefault()).getTime());
			rActivityWeChatUserDao.save(rActivityWeChatUser);
			// 判断活动是否还未开始
			WeChatActivity weChatActivity = rActivityWeChatUser
					.getWeChatActivity();
			if (weChatActivity.beforeProgress()) {
				long msgNum = weChatUserMsgDao
						.findTotalUserMsgByActivityMID(weChatActivity
								.getActivityMid());
				if (msgNum >= WeChatActivity.DEF_USER_MSG_NUM_BEFORE_PROGRESS) {
					respContent = "活动还未开始，上墙消息数量已经超过默认值20条，请等待活动开始时再发消息！";
					respMessage = SendBasicMessageUtil.sendTextmessage(
							fromUserName, toUserName, respContent);
					return respMessage;
				}
			}
			
			//活动已经结束的时候
			if (weChatActivity.afterProgress()) {
				rActivityWeChatUser.setStatus(RActivityWeChatUser.USER_STATUS.CANCEL_SIGN.getIndex());
				rActivityWeChatUserDao.save(rActivityWeChatUser);
				return noSignInResponce(weChatApp, serverUrl, fromUserName, toUserName, false, true);
			}
			
			if (weChatActivity.isfActivityPhoto()) {
				if (weChatActivity.isfActivityPhotoForSignUser()) {
					ActivityPhoto activityPhoto = new ActivityPhoto(img, null, ActivityPhoto.SIGN_USER_UPLOAD, weChatActivity);
					activityPhotoDao.save(activityPhoto);
				} else if (weChatActivity.isfActivityPhotoForAdmin() && rActivityWeChatUser.isfActivityPhotoForAdmin()) {
					ActivityPhoto activityPhoto = new ActivityPhoto(img, null, ActivityPhoto.SYSTEM_UPLOAD, weChatActivity);
					activityPhotoDao.save(activityPhoto);
					respContent = "图片上传成功！";
					respMessage = SendBasicMessageUtil.sendTextmessage(
							fromUserName, toUserName, respContent);
					return respMessage;
				}
			}

			byte infoStatus = WeChatUserMsg.MSG_STATUS.CHECKING.getIndex();
			if (weChatActivity.isfAutoUpWall()) {
				infoStatus = WeChatUserMsg.MSG_STATUS.SUCCESS.getIndex();
			}
			// 添加上墙消息
			WeChatUserMsg weChatUserMsg = new WeChatUserMsg(null, img,
					infoStatus, rActivityWeChatUser);
			weChatUserMsg = weChatUserMsgDao.save(weChatUserMsg);
			
			return upWallResponce(weChatActivity, rActivityWeChatUser.getUuid(), serverUrl, fromUserName, toUserName);
		}
	}

	/**
	 * 处理取消订阅消息
	 * 
	 * @param weChatApp
	 * @param fromUserName
	 * @return
	 */
	public String handleUnSubscribe(WeChatApp weChatApp, String fromUserName) {
		WeChatUser weChatUser = weChatUserDao.findByWeChatAppIDAndOpenID(
				weChatApp.getId(), fromUserName);
		if (weChatUser != null) {
			// 删除用户配对信息
			wechatUserFatePairDao.deleteByRActivityUserID(weChatUser.getId());
			weChatUserDao.delete(weChatUser.getId());
		}
		return null;
	}

	/**
	 * 处理用户扫描事件,扫描签到（认证服务号有权限）
	 * 
	 * @param weChatApp
	 * @param fromUserName
	 * @param toUserName
	 * @param eventKey
	 * @return
	 */
	public String handleScan(WeChatApp weChatApp, String fromUserName,
			String toUserName, String eventKey, String serverUrl) {
		String respContent = null;
		String respMessage = null;

		PersonalInf personalInf = GetPersoninf.getPersonalInf(weChatApp.getAccessToken(), fromUserName);
		// 获取用户信息是否失败
		if (personalInf == null) {
			respContent = "读取信息失败，请重新扫描二维码!";
			respMessage = SendBasicMessageUtil.sendTextmessage(fromUserName,
					toUserName, respContent);
			return respMessage;
		}

		// 更新用户信息
		WeChatUser weChatUser = weChatUserDao.findByWeChatAppIDAndOpenID(
				weChatApp.getId(), fromUserName);
		if (weChatUser == null) {// 新关注用户
			weChatUser = new WeChatUser(personalInf, weChatApp);
		} else {
			weChatUser.setOpenID(personalInf.getOpenID());
			weChatUser.setNickName(FaceUtils.filterEmoji(personalInf
					.getNickname()));
			weChatUser.setSex((byte) personalInf.getSex());
			weChatUser.setHeadImgUrl(personalInf.getHeadImgUrl());
			weChatUser.setLanguage(personalInf.getLanguage());
			weChatUser.setCity(personalInf.getCity());
			weChatUser.setProvince(personalInf.getProvince());
			weChatUser.setCountry(personalInf.getCountry());
			weChatUser.setSubscribeTime(personalInf.getSubScribeTime());
			weChatUser.setUnSubscribeTime(null);
			weChatUser.setWeChatApp(weChatApp);
		}
		weChatUser = weChatUserDao.save(weChatUser);

		if (StringUtils.isNotBlank(eventKey)) {
			int sceneID = Integer.parseInt(eventKey);
			WeChatActivity weChatActivity = weChatActivityDao.findBySceneIDAndWeChatID(weChatApp.getId(), sceneID);
			if (weChatActivity == null) {
				respContent = "不好意思，活动可能已过期!";
				respMessage = SendBasicMessageUtil.sendTextmessage(fromUserName, toUserName, respContent);
				return respMessage;
			} else {
				// 活动正在进行
				if (weChatActivity.beforeEnd()) {
					if (!weChatActivity.isfSignIn()) {// 活动是否开启了签到模式
						respContent = "活动可能还未开始，请继续关注!";
						respMessage = SendBasicMessageUtil.sendTextmessage(fromUserName, toUserName, respContent);
						return respMessage;
					}
					
					// 取消所有该公众号中以前的签到
					rActivityWeChatUserDao.updateStatusRActWeUserByOpenID(
									fromUserName,
									RActivityWeChatUser.USER_STATUS.SIGN.getIndex(),
									RActivityWeChatUser.USER_STATUS.CANCEL_SIGN.getIndex());
					
					// 是否有此活动签到，插入或更新
					RActivityWeChatUser rActivityWeChatUser = rActivityWeChatUserDao.findRActWeUserByWeActMIDAndOpenID(
									weChatActivity.getActivityMid(),
									fromUserName);
					if (rActivityWeChatUser == null) {
						rActivityWeChatUser = new RActivityWeChatUser(
								weChatUser, weChatActivity,
								RActivityWeChatUser.USER_STATUS.SIGN
										.getIndex(), CommonUtils.generateUUID());
					} else {
						if (StringUtils.isBlank(rActivityWeChatUser
								.getUuid())) {
							rActivityWeChatUser.setUuid(CommonUtils.generateUUID());
						}
						rActivityWeChatUser.setStatus(RActivityWeChatUser.USER_STATUS.SIGN
										.getIndex());
						rActivityWeChatUser.setUpdateTime(DateUtils.now());
					}
					rActivityWeChatUserDao.save(rActivityWeChatUser);
					
					// 发送活动签到自动回复
					List<SendArticle> sendArticleList = new ArrayList<SendArticle>();
					ActivityAutoReply activityAutoReply = weChatActivity.getActivityAutoReply();
					if (activityAutoReply.getSiginAutoReply()
							.getReplyType() == AutoReply.REPLY_TYPE.TEXT
							.getIndex()) {
						respContent = activityAutoReply.getSiginAutoReply().getWeChatText().getContent();
						if (StringUtils.isBlank(respContent)) {
							return null;
						}
						
						SendArticle UpbangSendArticle = new SendArticle();
						String upbangTitle = respContent;
						UpbangSendArticle.setTitle(upbangTitle);
						sendArticleList.add(UpbangSendArticle);

						
					} else {
						SendArticle sendArticle = new SendArticle();
						String title = activityAutoReply
								.getSiginAutoReply().getWeChatImageText()
								.getTitle();
						String description = activityAutoReply
								.getSiginAutoReply().getWeChatImageText()
								.getDescription();
						String img = activityAutoReply.getSiginAutoReply()
								.getWeChatImageText().getImg();
						String imgUrl = activityAutoReply
								.getSiginAutoReply().getWeChatImageText()
								.getImgUrl();
						if (StringUtils.isNotBlank(title)) {
							sendArticle.setTitle(title);
						}

						if (StringUtils.isNotBlank(description)) {
							sendArticle.setDescription(description);
						}

						if (StringUtils.isNotBlank(img)) {
							sendArticle.setPicUrl(serverUrl + "/" + img);
						}

						if (StringUtils.isNotBlank(imgUrl)) {
							sendArticle.setUrl(imgUrl);
						}
						if (StringUtils.isBlank(sendArticle.getTitle())
								&& StringUtils.isBlank(sendArticle
										.getDescription())
								&& StringUtils.isBlank(sendArticle
										.getPicUrl())
								&& StringUtils
										.isBlank(sendArticle.getUrl())) {
							return null;
						}
						sendArticleList.add(sendArticle);
					}
					
					// 摇一摇自动回复
					SendArticle shakeSendArticle = new SendArticle();
					String shakeTitle = "点击参加摇一摇游戏或发送消息上墙";
					String shakeImgPath = serverUrl
							+ "/statics/images/yaoyiyao.png";
					String shakeImgUrl = serverUrl
							+ "/statics/shake.html?activityMid="
							+ rActivityWeChatUser.getWeChatActivity()
									.getActivityMid() + "&uuid="
							+ rActivityWeChatUser.getUuid();
					shakeSendArticle.setTitle(shakeTitle);
					shakeSendArticle.setPicUrl(shakeImgPath);
					shakeSendArticle.setUrl(shakeImgUrl);

					sendArticleList.add(shakeSendArticle);
					respMessage = SendBasicMessageUtil.sendNewsmessage(
							fromUserName, toUserName, sendArticleList);

				} else {
					respContent = "活动还没开始或已经结束!";
					respMessage = SendBasicMessageUtil.sendTextmessage(
							fromUserName, toUserName, respContent);
					return respMessage;
				}
			}
		}

		return respMessage;
	}

	/**
	 * 处理订阅用户信息
	 * 
	 * @param weChatApp
	 * @param fromUserName
	 * @param toUserName
	 * @param eventKey
	 * @return
	 */
	public String handleSubscribe(WeChatApp weChatApp, String fromUserName,
			String toUserName, String eventKey, String serverUrl) {
		String respContent = null;
		String respMessage = null;
		
		// 是否关注过
		WeChatUser weChatUser = weChatUserDao.findByWeChatAppIDAndOpenID(
				weChatApp.getId(), fromUserName);
		
		// 认证的公众号(服务号 订阅号)直接从此处获取用户信息
		if (weChatApp.getAccountType() != WeChatApp.ACCOUNT_TYPE.UNAUTH.getIndex()) {
			PersonalInf personalInf = GetPersoninf.getPersonalInf(
					weChatApp.getAccessToken(), fromUserName);
			if (personalInf == null) {
				respContent = "读取信息失败，请重新扫描二维码!";
				respMessage = SendBasicMessageUtil.sendTextmessage(fromUserName,
						toUserName, respContent);
				return respMessage;
			}

			// 保存或者更新关注用户
			if (weChatUser == null) {// 新关注用户
				weChatUser = new WeChatUser(personalInf, weChatApp);
			} else {
				weChatUser.setOpenID(personalInf.getOpenID());
				weChatUser.setNickName(FaceUtils.filterEmoji(personalInf
						.getNickname()));
				weChatUser.setSex((byte) personalInf.getSex());
				weChatUser.setHeadImgUrl(personalInf.getHeadImgUrl());
				weChatUser.setLanguage(personalInf.getLanguage());
				weChatUser.setCity(personalInf.getCity());
				weChatUser.setProvince(personalInf.getProvince());
				weChatUser.setCountry(personalInf.getCountry());
				weChatUser.setSubscribeTime(personalInf.getSubScribeTime());
				weChatUser.setUnSubscribeTime(null);
				weChatUser.setWeChatApp(weChatApp);
			}
			weChatUser = weChatUserDao.save(weChatUser);
		}
		// 扫描带参数的二维码或不带参数的(普通订阅|活动现场签到订阅)
		// 带参数的二维码扫描关注，认证服务号
		if (StringUtils.startsWith(eventKey, "qrscene_")) {
			int sceneID = Integer.parseInt(StringUtils.substring(eventKey, 8));
			WeChatActivity weChatActivity = weChatActivityDao
					.findBySceneIDAndWeChatID(weChatApp.getId(), sceneID);

			if (weChatActivity == null) {// 活动不存在
				respContent = "不好意思，活动可能已过期!";
				respMessage = SendBasicMessageUtil.sendTextmessage(
						fromUserName, toUserName, respContent);
				return respMessage;
			} else {
				if (weChatActivity.beforeEnd()) {
					if (!weChatActivity.isfSignIn()) {// 活动是否开启了签到模式
						respContent = "活动可能还未开始，请继续关注!";
						respMessage = SendBasicMessageUtil.sendTextmessage(
								fromUserName, toUserName, respContent);
						return respMessage;
					}
					// 正在进行的已签到活动
					// 取消所有以前的状态
					rActivityWeChatUserDao
							.updateStatusRActWeUserByOpenID(
									fromUserName,
									RActivityWeChatUser.USER_STATUS.SIGN
											.getIndex(),
									RActivityWeChatUser.USER_STATUS.CANCEL_SIGN
											.getIndex());
					// 添加新签到
					RActivityWeChatUser rActivityWeChatUser = rActivityWeChatUserDao
							.findRActWeUserByWeActMIDAndOpenID(
									weChatActivity.getActivityMid(),
									fromUserName);
					if (rActivityWeChatUser == null) {
						String uuid = CommonUtils.generateUUID();
						rActivityWeChatUser = new RActivityWeChatUser(
								weChatUser, weChatActivity,
								RActivityWeChatUser.USER_STATUS.SIGN
										.getIndex(), uuid);
					} else {
						if (StringUtils.isBlank(rActivityWeChatUser
								.getUuid())) {
							String uuid = CommonUtils.generateUUID();
							rActivityWeChatUser.setUuid(uuid);
						}
						rActivityWeChatUser
								.setStatus(RActivityWeChatUser.USER_STATUS.SIGN
										.getIndex());
						rActivityWeChatUser.setUpdateTime(Calendar
								.getInstance(TimeZone.getDefault())
								.getTime());
						rActivityWeChatUser.setSignInDate(rActivityWeChatUser.getUpdateTime());
					}

					// 插入新的状态
					rActivityWeChatUserDao.save(rActivityWeChatUser);

					// 发送自动回复
					ActivityAutoReply activityAutoReply = weChatActivity
							.getActivityAutoReply();
					if (activityAutoReply.getSiginAutoReply()
							.getReplyType() == AutoReply.REPLY_TYPE.TEXT
							.getIndex()) {
						respContent = activityAutoReply.getSiginAutoReply()
								.getWeChatText().getContent();
						if (StringUtils.isBlank(respContent)) {
							return null;
						}
						List<SendArticle> sendArticleList = new ArrayList<SendArticle>();
						SendArticle UpbangSendArticle = new SendArticle();
						String upbangTitle = respContent;
						UpbangSendArticle.setTitle(upbangTitle);
						sendArticleList.add(UpbangSendArticle);
						
						// 参加自定义相册
						if (weChatActivity.isfAlbum() == true) {
							SendArticle albumSendArticle = new SendArticle();
							List<ActivityImage> listAlbum = activityImageDao.findByWeChatActivityMID(weChatActivity.getActivityMid(), ActivityImage.IMAGE_TYPE.ALBUM.getName());
							if (StringUtils.isNotBlank(weChatActivity.getAlbumSubject()) && listAlbum != null && listAlbum.size() > 0) {
								String albumTitle = weChatActivity.getAlbumSubject();
								String albumImgPath = serverUrl + "/" + listAlbum.get(0).getRelativePath();
								String albumImgUrl = serverUrl + "/activity-screen/mobile_album?activityMid="
										+ weChatActivity.getActivityMid();
								albumSendArticle.setTitle(albumTitle);
								albumSendArticle.setPicUrl(albumImgPath);
								albumSendArticle.setUrl(albumImgUrl);
								sendArticleList.add(albumSendArticle);
							}
						}

						SendArticle shakeSendArticle = new SendArticle();
						String shakeTitle = "点击参加摇一摇游戏或发送消息上墙";
						String shakeImgPath = serverUrl
								+ "/statics/images/yaoyiyao.png";
						String shakeImgUrl = serverUrl
								+ "/statics/shake.html?activityMid="
								+ rActivityWeChatUser.getWeChatActivity()
										.getActivityMid() + "&uuid="
								+ rActivityWeChatUser.getUuid();
						shakeSendArticle.setTitle(shakeTitle);
						shakeSendArticle.setPicUrl(shakeImgPath);
						shakeSendArticle.setUrl(shakeImgUrl);

						sendArticleList.add(shakeSendArticle);
						respMessage = SendBasicMessageUtil.sendNewsmessage(
								fromUserName, toUserName, sendArticleList);
						return respMessage;
					} else {
						List<SendArticle> sendArticleList = new ArrayList<SendArticle>();
						SendArticle sendArticle = new SendArticle();
						String title = activityAutoReply
								.getSiginAutoReply().getWeChatImageText()
								.getTitle();
						String description = activityAutoReply
								.getSiginAutoReply().getWeChatImageText()
								.getDescription();
						String img = activityAutoReply.getSiginAutoReply()
								.getWeChatImageText().getImg();
						String imgUrl = activityAutoReply
								.getSiginAutoReply().getWeChatImageText()
								.getImgUrl();
						if (StringUtils.isNotBlank(title)) {
							sendArticle.setTitle(title);
						}

						if (StringUtils.isNotBlank(description)) {
							sendArticle.setDescription(description);
						}

						if (StringUtils.isNotBlank(img)) {
							sendArticle.setPicUrl(serverUrl + "/" + img);
						}

						if (StringUtils.isNotBlank(imgUrl)) {
							sendArticle.setUrl(imgUrl);
						}
						if (StringUtils.isBlank(sendArticle.getTitle())
								&& StringUtils.isBlank(sendArticle.getDescription())
								&& StringUtils.isBlank(sendArticle.getPicUrl())
								&& StringUtils.isBlank(sendArticle.getUrl())) {
							return null;
						}
						sendArticleList.add(sendArticle);
						
						// 参加自定义相册
						if (weChatActivity.isfAlbum() == true) {
							SendArticle albumSendArticle = new SendArticle();
							List<ActivityImage> listAlbum = activityImageDao.findByWeChatActivityMID(weChatActivity.getActivityMid(), ActivityImage.IMAGE_TYPE.ALBUM.getName());
							if (StringUtils.isNotBlank(weChatActivity.getAlbumSubject()) && listAlbum != null && listAlbum.size() > 0) {
								String albumTitle = weChatActivity.getAlbumSubject();
								String albumImgPath = serverUrl + "/" + listAlbum.get(0).getRelativePath();
								String albumImgUrl = serverUrl + "/activity-screen/mobile_album?activityMid="
										+ weChatActivity.getActivityMid();
								albumSendArticle.setTitle(albumTitle);
								albumSendArticle.setPicUrl(albumImgPath);
								albumSendArticle.setUrl(albumImgUrl);
								sendArticleList.add(albumSendArticle);
							}
						}
						
						SendArticle shakeSendArticle = new SendArticle();
						String shakeTitle = "点击参加摇一摇游戏或发送消息上墙";
						String shakeImgPath = serverUrl
								+ "/statics/images/yaoyiyao.png";
						String shakeImgUrl = serverUrl
								+ "/statics/shake.html?activityMid="
								+ rActivityWeChatUser.getWeChatActivity()
										.getActivityMid() + "&uuid="
								+ rActivityWeChatUser.getUuid();
						shakeSendArticle.setTitle(shakeTitle);
						shakeSendArticle.setPicUrl(shakeImgPath);
						shakeSendArticle.setUrl(shakeImgUrl);

						sendArticleList.add(shakeSendArticle);
						respMessage = SendBasicMessageUtil.sendNewsmessage(
								fromUserName, toUserName, sendArticleList);
						return respMessage;
					}
				} else {// 已经结束
					respContent = "活动还没开始或已经结束!";
					respMessage = SendBasicMessageUtil.sendTextmessage(
							fromUserName, toUserName, respContent);
					return respMessage;
				}
			}
		} 
		// 认证订阅号或未认证，普通关注
		else {
			if (weChatUser == null) {
				return noSignInResponce(weChatApp, serverUrl, fromUserName, toUserName, true, false);
			} else {
				return noSignInResponce(weChatApp, serverUrl, fromUserName, toUserName, true, true);
			}
		}
	}

	/**
	 * 更新微信用户信息
	 * 
	 * @param openID
	 * @param nickName
	 * @param sex
	 * @param language
	 * @param city
	 * @param province
	 * @param country
	 * @param headImgUrl
	 * @param subscribeTime
	 * @param weChatApp
	 */
	public WeChatUser updateWeChatUser(String openId, String nickName, byte sex,
			String language, String city, String province, String country,
			String headImgUrl, Date subscribeTime, String activityMid) {

		WeChatActivity weChatActivity = weChatActivityDao.findByActivityMid(activityMid);
		WeChatApp weChatApp = weChatActivity.getWeChatApp();
		WeChatUser weChatUser = weChatUserDao.findByWeChatAppIDAndOpenID(
				weChatApp.getId(), openId);
		if (weChatUser == null) { //新关注用户
			weChatUser = new WeChatUser(openId, nickName, sex, language, city,
					province, country, headImgUrl, subscribeTime, weChatApp);
		} else {
			weChatUser.setOpenID(openId);
			weChatUser.setNickName(FaceUtils.filterEmoji(nickName));
			weChatUser.setSex(sex);
			weChatUser.setHeadImgUrl(headImgUrl);
			weChatUser.setLanguage(language);
			weChatUser.setCity(city);
			weChatUser.setProvince(province);
			weChatUser.setCountry(country);
			weChatUser.setSubscribeTime(subscribeTime);
			weChatUser.setUnSubscribeTime(null);
			weChatUser.setWeChatApp(weChatApp);
		}
		return weChatUserDao.save(weChatUser);
	}
	
	/**
	 * 上墙成功用户发送消息（图片或文字）回复
	 */
	private String upWallResponce(WeChatActivity weChatActivity,String rActivityWeChatUseruuid, String serverUrl, String fromUserName, String toUserName) {
		String respContent = weChatActivity.getActivityAutoReply().getUpbangAutoReply()
				.getWeChatText().getContent();
		if (StringUtils.isBlank(respContent)) {
			return null;
		}
		
		int userMsgNum = weChatUserMsgDao.findTotalUserMsgByActivityMIDAndOpendID(weChatActivity.getActivityMid(), fromUserName);
		
		if (userMsgNum != 1) {
			if(userMsgNum % 10 != 0) {
				return SendBasicMessageUtil.sendTextmessage(fromUserName, toUserName, respContent+"【已发送" + userMsgNum + "条消息】【发送关键字\"退出\"退出活动】");
			}
		}
		
		// 上墙返回结果
		List<SendArticle> sendArticleList = new ArrayList<SendArticle>();
		SendArticle UpbangSendArticle = new SendArticle();
		UpbangSendArticle.setTitle(respContent+"【已发送" + userMsgNum + "条消息】【发送关键字\"退出\"退出活动】");
		sendArticleList.add(UpbangSendArticle);
		
		// 参加自定义相册
		if (weChatActivity.isfAlbum() == true) {
			SendArticle albumSendArticle = new SendArticle();
			List<ActivityImage> listAlbum = activityImageDao.findByWeChatActivityMID(weChatActivity.getActivityMid(), ActivityImage.IMAGE_TYPE.ALBUM.getName());
			if (StringUtils.isNotBlank(weChatActivity.getAlbumSubject()) && listAlbum != null && listAlbum.size() > 0) {
				String albumTitle = weChatActivity.getAlbumSubject();
				String albumImgPath = serverUrl + "/" + listAlbum.get(0).getRelativePath();
				String albumImgUrl = serverUrl + "/activity-screen/mobile_album?activityMid="
						+ weChatActivity.getActivityMid();
				albumSendArticle.setTitle(albumTitle);
				albumSendArticle.setPicUrl(albumImgPath);
				albumSendArticle.setUrl(albumImgUrl);
				sendArticleList.add(albumSendArticle);
			}
		}
				
		// 参加摇一摇活动
		SendArticle shakeSendArticle = new SendArticle();
		String shakeTitle = "点击参加摇一摇游戏或继续发送消息上墙";
		String shakeImgPath = serverUrl + "/statics/images/yaoyiyao.png";
		String shakeImgUrl = serverUrl + "/statics/shake.html?activityMid="
				+ weChatActivity.getActivityMid() + "&uuid=" + rActivityWeChatUseruuid;
		shakeSendArticle.setTitle(shakeTitle);
		shakeSendArticle.setPicUrl(shakeImgPath);
		shakeSendArticle.setUrl(shakeImgUrl);

		sendArticleList.add(shakeSendArticle);
		
		return SendBasicMessageUtil.sendNewsmessage(fromUserName,
				toUserName, sendArticleList);
	}
	
	/**
	 * 未签到时用户发送消息（图片或文字）回复
	 * @param weChatApp
	 * @param serverUrl
	 * @param fromUserName
	 * @param toUserName
	 * @param isSubscribe  是否是订阅消息回复或者自动回复
	 * @param existedUser 系统是否已经存在此用户
	 * @return
	 */
	private String noSignInResponce(WeChatApp weChatApp, String serverUrl, String fromUserName, String toUserName, boolean isSubscribe
			, boolean existedUser) {
		String respContent = null;
		String respMessage = null;
		WeChatAppAutoReply weChatAppAutoReply = weChatApp.getWeChatAppAutoReply();
		AutoReply autoReply = null;
		if (isSubscribe) {
			autoReply = weChatAppAutoReply.getSubscribeAutoReply();
		} else {
			autoReply = weChatAppAutoReply.getAutoReply();
		}
		List<WeChatActivity> listWeChatActivity = weChatActivityDao.
				findByWeChatAppAndBeforeEndProgress(weChatApp.getId(), DateUtils.now());
		
		// 1自动回复文字和图文
		List<SendArticle> sendArticleList = new ArrayList<SendArticle>();
		// 1-1回复文本
		if (autoReply.getReplyType() == AutoReply.REPLY_TYPE.TEXT.getIndex()) {
			respContent = autoReply.getWeChatText().getContent();
			if (StringUtils.isBlank(respContent)) {
				return null;
			}
			if ((listWeChatActivity == null || listWeChatActivity.size() == 0) ||
					weChatApp.getAccountType() == WeChatApp.ACCOUNT_TYPE.SERVICER.getIndex()) {
				return SendBasicMessageUtil.sendTextmessage(fromUserName, toUserName, respContent);
			} else {
				SendArticle UpbangSendArticle = new SendArticle();
				String upbangTitle = respContent;
				UpbangSendArticle.setTitle(upbangTitle);
				sendArticleList.add(UpbangSendArticle);
			}
		}
		// 1-2回复图文
		else if (autoReply.getReplyType() == AutoReply.REPLY_TYPE.IMG_TEXT.getIndex()) {
			SendArticle sendArticle = new SendArticle();
			String title = autoReply.getWeChatImageText().getTitle();
			String description = autoReply.getWeChatImageText().getDescription();
			String img = autoReply.getWeChatImageText().getImg();
			String imgUrl = autoReply.getWeChatImageText().getImgUrl();
			
			if (StringUtils.isBlank(title)
					&& StringUtils.isBlank(description)
					&& StringUtils.isBlank(img)
					&& StringUtils.isBlank(imgUrl)) {
				return null;
			}
			if (StringUtils.isNotBlank(title)) sendArticle.setTitle(title);
			if (StringUtils.isNotBlank(description)) sendArticle.setDescription(description);
			if (StringUtils.isNotBlank(img)) sendArticle.setPicUrl(serverUrl + "/" + img);
			if (StringUtils.isNotBlank(imgUrl)) sendArticle.setUrl(imgUrl);
			
			sendArticleList.add(sendArticle);
		}
		
		// 正在进行的活动信息，签到列表，已认证的公众号不会出现
		// 该公众号正在进行的活动
		if (weChatApp.getAccountType() != WeChatApp.ACCOUNT_TYPE.SERVICER.getIndex()) {
			for (WeChatActivity activity : listWeChatActivity) {
				SendArticle SignArticle = new SendArticle();
				String activitySignTitle = "点击参加\"" + activity.getSubject() + "\"";
				String activitySignImgPath = serverUrl + "/statics/screen/images/help_icon.png";
				String activitySignImgUrl = null;
				if (existedUser) {
					activitySignImgUrl = OAuthUtil.generateNoOathUrl(activity.getActivityMid(), fromUserName, serverUrl);
				} else {
					activitySignImgUrl = OAuthUtil.generateOathUrl(activity.getActivityMid(), fromUserName, serverUrl);
				}
				
				SignArticle.setTitle(activitySignTitle);
				SignArticle.setPicUrl(activitySignImgPath);
				SignArticle.setUrl(activitySignImgUrl);
				sendArticleList.add(SignArticle);
			}
		}
		
		respMessage = SendBasicMessageUtil.sendNewsmessage(fromUserName,
				toUserName, sendArticleList);
		
		return respMessage;
	}
}
