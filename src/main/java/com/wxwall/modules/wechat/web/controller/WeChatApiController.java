/*
 * <p>
 *  版权所有 : ©2014 80通讯科技有限公司. 
 * Copyright © PING AN INSURANCE (GROUP) COMPANY OF CHINA ,LTD. All Rights Reserved
 * </p>
 */
package com.wxwall.modules.wechat.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.cache.LoadingCache;
import com.wxwall.common.cache.CacheManager;
import com.wxwall.common.cache.OAuthCacheUserInfo;
import com.wxwall.common.entity.Result;
import com.wxwall.common.utils.CommonUtils;
import com.wxwall.common.utils.PathUtils;
import com.wxwall.common.utils.PropertiesUtils;
import com.wxwall.common.web.BaseController;
import com.wxwall.modules.activity.service.ActivityService;
import com.wxwall.modules.wechat.aes.AuthProcess;
import com.wxwall.modules.wechat.entity.WeChatActivity;
import com.wxwall.modules.wechat.entity.WeChatApp;
import com.wxwall.modules.wechat.entity.WeChatUser;
import com.wxwall.modules.wechat.service.SignInService;
import com.wxwall.modules.wechat.service.WeChatMsgProcessor;
import com.wxwall.modules.wechat.service.WeChatService;
import com.wxwall.wechat.api.advanced.model.SNSUserInfo;
import com.wxwall.wechat.api.advanced.model.WeixinOauth2Token;
import com.wxwall.wechat.api.advanced.util.OAuthUtil;
import com.wxwall.wechat.api.util.XmlMessageUtil;

/**
 * 
 * <p>
 * 描述:微信服务器访问接口controller
 * </p>
 * 
 * @see
 * @author ganjun
 * 
 */
@Controller
@RequestMapping("/wechat")
public class WeChatApiController extends BaseController {

	@Autowired private WeChatService weChatService;
	@Autowired private ActivityService activityService;
	@Autowired private SignInService signInService;
	@Autowired private WeChatMsgProcessor weChatMsgProcessor;

	/**
	 * 开发者通过检验signature对请求进行校验（下面有校验方式）。
	 * 若确认此次GET请求来自微信服务器，请原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败。
	 * 加密/校验流程如下：
			1. 将token、timestamp、nonce三个参数进行字典序排序
			2. 将三个参数字符串拼接成一个字符串进行sha1加密
			3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	 */
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param mid 公众帐号唯一mid
	 * @param signature 微信加密签名
	 * @param timestamp 时间戳
	 * @param nonce 随机数
	 * @param echostr 随机字符串
	 */
	@ResponseBody
	@RequestMapping(value = "/process", method = RequestMethod.GET)
	public String checkSignature(HttpServletRequest request, HttpServletResponse response,
			String mid,
			String signature,
			String timestamp,
			String nonce,
			String echostr) {
		LOG.info("校验微信服务器的请求，参数  mid:" + mid + " ,signature:" + signature+ " ,timestamp:" + timestamp+ " ,nonce:" + nonce+ " ,echostr:" + echostr);
		
		String remoteReqMsg = "请求服务器信息(" + "addr:"
				+ request.getRemoteAddr() + ",hostname:"
				+ request.getRemoteHost() + ",port:"
				+ request.getRemotePort() + ",user:"
				+ request.getRemoteUser() + ") : ";
		
		try {
			// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");

			// 判断请求参数是否有效
			if (StringUtils.isBlank(mid)
					||StringUtils.isBlank(signature)
					||StringUtils.isBlank(timestamp)
					||StringUtils.isBlank(nonce)
					||StringUtils.isBlank(echostr)) {
				LOG.warn(remoteReqMsg + "微信无效请求，微信请求参数不能为空");
				return null;
			}

			WeChatApp weChatApp = weChatService.getWeChatAppByMid(mid);
			if (weChatApp == null) {
				LOG.warn(remoteReqMsg + "微信无效请求，公众帐号唯一id参数错误");
				return null;
			}

			// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
			boolean isAuth = weChatMsgProcessor.checkSignature(signature, timestamp, nonce, weChatApp.getToken());
			if (!isAuth) {
				LOG.warn(remoteReqMsg + "微信请求校验信息错误!");
				return null;
			}

		} catch (Exception e) {
			LOG.error(remoteReqMsg + "微信请求校验失败，服务器异常。", e);
			return null;
		}
		return echostr;
	}

	/**
	 * 接受微信服务器的普通消息
	 * 
	 * 微信服务器在五秒内收不到响应会断掉连接，并且重新发起请求，总共重试三次
	 * 假如服务器无法保证在五秒内处理并回复，可以直接回复空串，微信服务器不会对此作任何处理，并且不会发起重试。
	 * 
	 * @param request
	 * @param response
	 * @param mid
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/process", method = RequestMethod.POST)
	public String process(HttpServletRequest request, HttpServletResponse response,
			String mid,
			String signature,
			String timestamp,
			String nonce,
			String echostr,
			String encrypt_type,
			String msg_signature) throws Exception {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		LOG.info("接受微信服务器的消息，参数  mid:" + mid + " ,signature:" + signature+ " ,timestamp:" + timestamp+ " ,nonce:" + nonce+ " ,echostr:" + echostr+ " ,encrypt_type:" + encrypt_type+ " ,msg_signature:" + msg_signature);

		String remoteReqMsg = "请求服务器信息(" + "addr:"
				+ request.getRemoteAddr() + ",hostname:"
				+ request.getRemoteHost() + ",port:"
				+ request.getRemotePort() + ",user:"
				+ request.getRemoteUser() + ") : ";
		
		// 判断请求参数是否有效
		if (StringUtils.isBlank(mid)
				||StringUtils.isBlank(signature)
				||StringUtils.isBlank(timestamp)
				||StringUtils.isBlank(nonce)) {
			LOG.warn(remoteReqMsg + "微信无效请求，微信请求参数不能为空");
			return null;
		}
		
		String encodingAESKey = PropertiesUtils.getString("ENCODING_AES_KEY");
		// 获取微信服务器xml消息
		String originalXml = XmlMessageUtil.parseReqMessage(request);
		String decryptXml = null; // 解密后消息
		
		WeChatApp weChatApp = weChatService.getWeChatAppByMid(mid);
		
		// 公众号不存在，则不需要返回消息
		if (weChatApp == null) {
			LOG.warn(remoteReqMsg + "微信无效请求，公众帐号唯一id不存在");
			return null;
		}

		boolean isAuth = weChatMsgProcessor.checkSignature(signature, timestamp, nonce, weChatApp.getToken());
		if (!isAuth) {
			LOG.warn(remoteReqMsg + "微信校验信息错误!");
			return null;
		}

		// 加密类型有两种情况1.raw原生不加密2.aes加密算法
		AuthProcess authProcess = null;
		boolean isEncrypt = StringUtils.isBlank(encrypt_type) || encrypt_type.equals("raw"); //是否加密
		if (isEncrypt) {
			decryptXml = originalXml;
		} else {
			authProcess = new AuthProcess(weChatApp.getToken(), encodingAESKey, weChatApp.getAppId());
			decryptXml = authProcess.decryptMsg(msg_signature, timestamp, nonce,originalXml);
		}
		
		LOG.info("WeChat originalXml:" + originalXml);
		
		// xml请求解析
		Map<String, String> reqMsgMap = XmlMessageUtil.parseXml(decryptXml);
		//测试使用
//		Map<String, String> reqMsgMap = new HashMap<String, String>();
//		reqMsgMap.put("ToUserName", "gh_47e2ebdf351e");
//		reqMsgMap.put("FromUserName", "om0lAt8fvqbiIozZ0KdCw-s3jPPM");
//		reqMsgMap.put("CreateTime", "1428569610");
//		reqMsgMap.put("Content", "我是测试");
//		reqMsgMap.put("MsgType", "text");
//		reqMsgMap.put("MsgId", UUID.randomUUID().toString());
		
		String serverUrl = PathUtils.getWebRootUrl(request);
		// 调用核心业务类接收消息、处理消息
		String replyMsg = weChatMsgProcessor.processWechatMsg(serverUrl, weChatApp, reqMsgMap);
		
		String transformMsg = null;
		if (isEncrypt) {
			transformMsg = replyMsg;
		} else {
			transformMsg = authProcess.encryptMsg(timestamp, nonce, replyMsg);
		}
		
		if (StringUtils.isNotBlank(transformMsg)) {
			return transformMsg;
		} else {
			return null;
		}
	}

	/**
	 * 微信OAuth2网页认证回调方法
	 * 存在重复请求
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws Exception
	 */
	@RequestMapping(value = "/authOpenID")
	public String authOpenID(HttpServletRequest request,HttpServletResponse response ,Model model,
			String code,
			@RequestParam(value="state")String activityMid,
			String sourceOpenId) throws Exception {
		
		LOG.info("处理OAuth认证的回调，参数 code:" + code + " ActivityMid:" + activityMid + " sourceOpenId:" + sourceOpenId);
		
		if (StringUtils.isBlank(code) || StringUtils.isBlank(activityMid)
				|| StringUtils.isBlank(sourceOpenId)) {
			LOG.warn("无效请求，请求参数不全!");
			model.addAttribute("msg", "很抱歉，<br>请重新获取签到链接！");
			return "mobile-signin-result";
		}
		
		SNSUserInfo snsUserInfo = null;
		WeChatActivity weChatActivity = null;
		WeChatUser weChatUser = null;
		String accessToken = null;
		String openId = null;
		String refreshAccessToken = null;
		
		LoadingCache<String, OAuthCacheUserInfo> oAuthUserCache = CacheManager.getOAuthUserCache();
		String key = sourceOpenId;
		OAuthCacheUserInfo oauthCacheUserInfo = oAuthUserCache.getIfPresent(key);
		if (oauthCacheUserInfo != null) {
			LOG.warn("缓存中存在用户相关信息: " + sourceOpenId);
			snsUserInfo = oauthCacheUserInfo.getSnsUserInfo();
		}
		
		if (snsUserInfo == null) {
			try {
				// 通过code换取网页授权access_token
				WeixinOauth2Token weiXinOauth2Token = OAuthUtil.getOauth2AccessToken(
						CommonUtils.getSystemAppId(), CommonUtils.getSystemAppSecret(),
						code);
				
				if (weiXinOauth2Token == null) {
					//通过code获取网页授权失败，进入缓存或系统数据库查找
					LOG.warn("OAuth2获取网页授权凭证失败");
					//查看缓存是否存在此前查询的值
					if (oauthCacheUserInfo == null) {
						LOG.warn("OAuth2缓存中不存在用户相关信息");
						//数据库查询是否已经存在此用户
						weChatActivity = activityService.findByActivityMid(activityMid);
						if (weChatActivity == null) {
							LOG.error("OAuth2数据库中未查询到此活动");
							throw new RuntimeException();
						}
						weChatUser = weChatService.getWeChatUserByWeChatIdAndOpenId(weChatActivity.getWeChatApp().getId(), sourceOpenId);
						if (weChatUser == null) {
							LOG.error("OAuth2数据库中未查询到此用户");
							throw new RuntimeException();
						}
					} else {
						//缓存查找用户信息
						accessToken = oauthCacheUserInfo.getAccessToken();
						openId = oauthCacheUserInfo.getOpenId();
						refreshAccessToken = oauthCacheUserInfo.getRefreshToken();
						
						if (accessToken == null || openId == null) {
							LOG.error("OAuth2缓存中未发现此用户相关accessToken或者openId");
							throw new RuntimeException();
						} else {
							
							// 拉取用户信息(需scope为 snsapi_userinfo)
							snsUserInfo = OAuthUtil.getSNSUserInfo(
									accessToken, openId);
							if (snsUserInfo == null) {
								LOG.warn("OAuth2用缓存中的accessToken获取用户信息失败，刷新accessToken");
								WeixinOauth2Token refreshWeixinOauth2Token = OAuthUtil.refreshOauth2Token(CommonUtils.getSystemAppId(), refreshAccessToken);
								if (refreshWeixinOauth2Token == null) {
									LOG.error("OAuth2刷新accessToken失败");
									throw new RuntimeException();
								}
								
								oauthCacheUserInfo.setAccessToken(refreshWeixinOauth2Token.getAccessToken());
								oauthCacheUserInfo.setOpenId(refreshWeixinOauth2Token.getOpenId());
								oauthCacheUserInfo.setRefreshToken(refreshWeixinOauth2Token.getRefreshToken());
								accessToken = refreshWeixinOauth2Token.getAccessToken();
								openId = refreshWeixinOauth2Token.getOpenId();
								
								snsUserInfo = OAuthUtil.getSNSUserInfo(
										accessToken, openId);
								if (snsUserInfo == null) {
									LOG.error("OAuth2刷新accessToken后获取用户信息失败");
									throw new RuntimeException();
								}
								LOG.error("OAuth2刷新accessToken后获取用户信息成功");
								oauthCacheUserInfo.setSnsUserInfo(snsUserInfo);
								oauthCacheUserInfo.setCode(code);
							} else {
								LOG.warn("OAuth2用缓存中的accessToken获取用户信息成功");
								oauthCacheUserInfo.setSnsUserInfo(snsUserInfo);
								oauthCacheUserInfo.setCode(code);
							}
						}
					}
				} else {
					LOG.warn("OAuth2直接获取accessToken成功");
					if (oauthCacheUserInfo == null) {
						oauthCacheUserInfo = new OAuthCacheUserInfo(code);
						oauthCacheUserInfo.setAccessToken(weiXinOauth2Token.getAccessToken());
						oauthCacheUserInfo.setOpenId(weiXinOauth2Token.getOpenId());
						oauthCacheUserInfo.setRefreshToken(weiXinOauth2Token.getRefreshToken());
						oAuthUserCache.put(key, oauthCacheUserInfo);
					} else {
						oauthCacheUserInfo.setCode(code);
						oauthCacheUserInfo.setAccessToken(weiXinOauth2Token.getAccessToken());
						oauthCacheUserInfo.setOpenId(weiXinOauth2Token.getOpenId());
						oauthCacheUserInfo.setRefreshToken(weiXinOauth2Token.getRefreshToken());
					}
					
					// 拉取用户信息(需scope为 snsapi_userinfo)
					snsUserInfo = OAuthUtil.getSNSUserInfo(
							weiXinOauth2Token.getAccessToken(),
							weiXinOauth2Token.getOpenId());
					if (snsUserInfo == null) {
						LOG.error("OAuth2获取用户信息失败");
						throw new RuntimeException();
					}
					LOG.warn("OAuth2直接获取用户信息成功");
					oauthCacheUserInfo.setSnsUserInfo(snsUserInfo);
				}
				
			} catch (Exception e) {
				model.addAttribute("msg", "很抱歉，<br>授权失败，请稍后重试！");
				return "mobile-signin-result";
			}
		} else {
			LOG.warn("从缓存中取用户信息成功: " + sourceOpenId);
		}
		
		try {
			if (weChatUser == null) {
				weChatUser = weChatMsgProcessor.updateWeChatUser(sourceOpenId, 
						snsUserInfo.getNickname(),
						(byte) snsUserInfo.getSex(),
						snsUserInfo.getLanguage(),
						snsUserInfo.getCity(),
						snsUserInfo.getProvince(),
						snsUserInfo.getCountry(),
						snsUserInfo.getHeadImgUrl(),
						snsUserInfo.getSubscribeTime(),
						activityMid);
			}
		} catch (Exception e) {
			model.addAttribute("msg", "很抱歉，<br>获取信息失败，请稍后重试！");
			return "mobile-signin-result";
		}
		
		try {
			if (weChatActivity == null) {
				weChatActivity = activityService.findByActivityMid(activityMid);
			}
			
			Result result = new Result();
			Map<String, String> map = new HashMap<String, String>();
			map.put("mid", activityMid);
			map.put("activitySubject", weChatActivity.getSubject());
			map.put("userAvatar", weChatUser.getHeadImgUrl());
			map.put("nickName", weChatUser.getNickName());
			map.put("openId", weChatUser.getOpenID());
			result.setData(map);
			
			model.addAttribute("data", result.getData());
			return "mobile-signin";
		}catch (Exception e) {
			model.addAttribute("msg", "很抱歉，<br>授权失败，请稍后重试！");
			return "mobile-signin-result";
		}
	}
	/**
	 * 认证后的网页签到界面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/noAuthOpenID")
	public String noAuthOpenID(HttpServletRequest request,HttpServletResponse response ,Model model) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String activityMid = request.getParameter("activityMid");
		String sourceOpenId = request.getParameter("sourceOpenId");
		if (StringUtils.isBlank(activityMid) || 
				StringUtils.isBlank(sourceOpenId)) {
			LOG.warn("微信无效请求，微信请求参数不正确,sourceOpenId为空或者mid为空!");
			model.addAttribute("msg", "很抱歉，<br>请重新获取签到链接！");
			return "mobile-signin-result";
		}
		LOG.info("ActivityMid:" + activityMid + "\nsourceOpenId:" + sourceOpenId);
		Result result = signInService.noOAuth(activityMid, sourceOpenId);
		if (result.isSuccess()) { //认证成功
			model.addAttribute("data", result.getData());
			return "mobile-signin";
		} else {// 没认证成功
			if (StringUtils.isBlank(result.getMessage())) {
				model.addAttribute("msg", "很抱歉，<br>授权失败，请稍后重试！");
			} else {
				model.addAttribute("msg", "很抱歉，<br>" + result.getMessage());
			}
			return "mobile-signin-result";
		}
	}

}
