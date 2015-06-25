package com.wxwall.modules.wechat.web.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wxwall.common.entity.Result;
import com.wxwall.common.utils.CommonUtils;
import com.wxwall.common.utils.PathUtils;
import com.wxwall.common.web.BaseController;
import com.wxwall.modules.activity.service.ActivityFuncService;
import com.wxwall.modules.activity.service.ActivityMsgService;
import com.wxwall.modules.activity.service.ActivityService;
import com.wxwall.modules.wechat.entity.ActivityImage;
import com.wxwall.modules.wechat.entity.ActivityPhoto;
import com.wxwall.modules.wechat.entity.WeChatActivity;
import com.wxwall.modules.wechat.entity.WeChatUser;
import com.wxwall.modules.wechat.service.SignInService;
import com.wxwall.modules.wechat.service.WeChatService;

/**
 * 
 * <p>
 * 描述:活动功能管理访问接口controller
 * </p>
 * 
 * @see
 * @author ganjun
 * 
 */
@Controller
@RequestMapping("/activity-screen")
public class ActivityScreenController extends BaseController {

	@Autowired private ActivityService activityService;
	@Autowired private WeChatService weChatService;
	@Autowired private SignInService signInService;
	@Autowired private ActivityFuncService activityFuncService;
	@Autowired private ActivityMsgService activityMsgService;
	/**
	 * 非认证号码手动签到
	 * 
	 * @param request
	 * @param model
	 * @param mid
	 * @param secret
	 * @param openId
	 * @return
	 */
	@RequestMapping(value = "/mobile_signin", method=RequestMethod.POST)
	public String mobileSignIn(HttpServletRequest request,Model model,
			String mid,
			String secret,
			String openId) {
		
		if (StringUtils.isBlank(mid) || StringUtils.isBlank(secret)
				|| StringUtils.isBlank(openId)) {
			model.addAttribute("msg", "很抱歉，<br>授权失败，请稍后重试！");
			return "mobile-signin-result";
		}
		Result result = null;
		try {
			result = signInService.OAuth2SignIn(mid, openId, secret);
		} catch (Exception e) {
			LOG.error("签到失败，请稍后重试！", e);
			
			if (result == null) {
				result = new Result();
			}
			result.setSuccess(false);
			result.setMessage("很抱歉，<br>签到失败，请稍后重试！");
		}
		
		model.addAttribute("data", result);
		return "mobile-signin-result";
	}
	
	/**
	 * 返回自定义相册
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/mobile_album")
	public String mobileAlbum(HttpServletRequest request,HttpServletResponse response ,Model model) {
		String activityMid = request.getParameter("activityMid");
		if (StringUtils.isBlank(activityMid)) {
			LOG.warn("微信无效请求，微信请求参数不正确,sourceOpenId为空或者mid为空!");
			model.addAttribute("msg", "很抱歉，<br>请重新获取签到链接！");
			return "mobile_album";
		}
		WeChatActivity WeChatActivity = activityService.findByActivityMid(activityMid);
		List<ActivityImage> listAlbum = activityService.findAlbumByActivityMid(activityMid);
		model.addAttribute("WeChatActivity", WeChatActivity);
		model.addAttribute("listAlbum", listAlbum);
		return "mobile-album";
	}
	
	/**
	 * 返回消息列表
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
//	@ResponseBody
//	@RequestMapping(value = "/upbang-msg-list")
//	public Result upbangMsgList(HttpServletRequest request,
//			HttpServletResponse response, HttpSession session){
//		Result result = new Result();
//		try {
//			String activityMid = request.getParameter("activityMid");
//			String updateTime = request.getParameter("updateTime");
//			String strlimit = request.getParameter("limit");
//			// 判断活动主题是否为空
//			if (StringUtils.isBlank(activityMid)) {
//				LOG.warn("活动ID参数未发现!");
//				result.setSuccess(false);
//				result.setMessage("请求参数错误");
//				return result;
//			}
//			Date queryDate = null;
//			if (StringUtils.isNotBlank(updateTime)) {
//				queryDate = new Date(Long.parseLong(updateTime));
//			}
//			
//			int limit = 10;
//			if (StringUtils.isNotBlank(strlimit)) {
//				limit = Integer.parseInt(strlimit);
//			}
//			String serverUrl = CommonUtils.getServerUrl(request);
//			result = activityService.getUpbangListMsg(activityMid, serverUrl, queryDate, limit);
//		} catch(Exception e) {
//			LOG.error(e.getMessage(), e);
//			result.setSuccess(false);
//			result.setMessage("系统内部错误");
//			return result;
//		}
//		
//		return result;
//	}
	
	/**
	 * 返回单条消息
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/upbang-msg")
	public Result upbangMsg(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Result result = new Result();
		try {
			String activityMid = request.getParameter("activityMid");
			String updateTime = request.getParameter("updateTime");
			// 判断活动主题是否为空
			if (StringUtils.isBlank(activityMid)) {
				LOG.warn("活动ID参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}
			Date queryDate = null;
			if (StringUtils.isNotBlank(updateTime)) {
				queryDate = new Date(Long.parseLong(updateTime));
			}
			String serverUrl = CommonUtils.getServerUrl(request);
			result = activityMsgService.getUpbangMsg(activityMid, queryDate, serverUrl);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
		
		return result;
	}
	/**
	 * 返回活动页面头部信息
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/head-info")
	public Result headInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Result result = new Result();
		try {
			String activityMid = request.getParameter("activityMid");
			// 判断活动主题是否为空
			if (StringUtils.isBlank(activityMid)) {
				LOG.warn("活动ID参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}
			
			String showUrl = CommonUtils.getServerUrl(request);
			result = activityService.getHeadInfo(activityMid, showUrl);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
		
		return result;
	}
	/**
	 * 返回活动页面主题
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/theme-preview-list")
	public Result themePreviewList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Result result = new Result();
		try {
			String activityMid = request.getParameter("activityMid");
			// 判断活动主题是否为空
			if (StringUtils.isBlank(activityMid)) {
				LOG.warn("活动ID参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}
			
			String showUrl = CommonUtils.getServerUrl(request);
			String rootPath = request.getSession().getServletContext().getRealPath("/"); // 项目根目录的实际路径
			result = activityService.getThemePreviewList(activityMid, showUrl, rootPath);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
		
		return result;
	}
	/**
	 * 返回可抽奖用户
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/lucky-draw-list")
	public Result luckyDrawList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Result result = new Result();
		try {
			String activityMid = request.getParameter("activityMid");
			// 判断活动主题是否为空
			if (StringUtils.isBlank(activityMid)) {
				LOG.warn("活动ID参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}
			
			result = activityFuncService.listAllUserForPrize(activityMid);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/pairing-list")
	public Result pairingList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Result result = new Result();
		try {
			String activityMid = request.getParameter("activityMid");
			// 判断活动主题是否为空
			if (StringUtils.isBlank(activityMid)) {
				LOG.warn("活动ID参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}
			
			result = activityFuncService.listAllUserForPairing(activityMid);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
		
		return result;
	}
	
	/**
	 * 获取已中奖用户列表
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/winners-list")
	public Result winnersList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Result result = new Result();
		try {
			String activityMid = request.getParameter("activityMid");
			// 判断活动主题是否为空
			if (StringUtils.isBlank(activityMid)) {
				LOG.warn("活动MID参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}
			
			String webRootUrl = PathUtils.getWebRootUrl(request) + "/";
			result = activityFuncService.getWinnersListForPrize(activityMid, webRootUrl);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
		
		return result;
	}
	/**
	 * 返回配对成功用户
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/lucky-pairing-list")
	public Result luckyPairingList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Result result = new Result();
		try {
			String activityMid = request.getParameter("activityMid");
			// 判断活动主题是否为空
			if (StringUtils.isBlank(activityMid)) {
				LOG.warn("活动MID参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}
			
			result = activityFuncService.getLuckyPairingList(activityMid);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
		
		return result;
	}
	
	/**
	 * 设置中奖用户
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/send-lucky")
	public Result sendLucky(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Result result = new Result();
		try {
			String strUserId = request.getParameter("userId");
			String strPrizeId = request.getParameter("prizeId");
			
			// 判断活动主题是否为空
			if (StringUtils.isBlank(strUserId)) {
				LOG.warn("抽中用户ID参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}
						
			// 判断活动主题是否为空
			if (StringUtils.isBlank(strPrizeId)) {
				LOG.warn("奖项ID参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}		
			
			long prizeId = Long.parseLong(StringUtils.trim(strPrizeId));
			long rActivityWeChatUserID = Long.parseLong(StringUtils.trim(strUserId));
			result = activityFuncService.addUserForPrize( prizeId, rActivityWeChatUserID);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
		return result;
	}
	/**
	 * 碰对列表
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/send-pairing")
	public Result sendPairing(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Result result = new Result();
		try {
			String activityMid = request.getParameter("activityMid");
			String strLuckyBoyId = request.getParameter("luckyBoyId");
			String strLuckyGirlId = request.getParameter("luckyGirlId");
			// 判断活动主题是否为空
			if (StringUtils.isBlank(activityMid)) {
				LOG.warn("活动ID参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}
			
			// 判断活动主题是否为空
			if (StringUtils.isBlank(strLuckyBoyId)) {
				LOG.warn("抽中用户ID参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}
						
			// 判断活动主题是否为空
			if (StringUtils.isBlank(strLuckyGirlId)) {
				LOG.warn("抽中用户ID参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}		
			
			long luckyBoyId = Long.parseLong(StringUtils.trim(strLuckyBoyId));
			long luckyGirlId = Long.parseLong(StringUtils.trim(strLuckyGirlId));
			result = activityFuncService.addUserForPairing(activityMid, luckyBoyId, luckyGirlId);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
		return result;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/invalid-lucky")
	public Result invalidLucky(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Result result = new Result();
		try {
			String activityMid = request.getParameter("activityMid");
			String strUserId = request.getParameter("userId");
			String strPrizeId = request.getParameter("prizeId");
			String invalidType = request.getParameter("invalidType");
			long prizeId = -1;
			long rActivityWeChatUserID = -1;
			
			// 判断活动主题是否为空
			if (!StringUtils.isBlank(strUserId)) {
				prizeId = Long.parseLong(StringUtils.trim(strPrizeId));
			}
						
			// 判断活动主题是否为空
			if (!StringUtils.isBlank(strPrizeId)) {
				rActivityWeChatUserID = Long.parseLong(StringUtils.trim(strUserId));
			}
			
			if (StringUtils.endsWithIgnoreCase(invalidType, "user")) {
				result = activityFuncService.clearUserForPrize(prizeId, rActivityWeChatUserID);
			} else if (StringUtils.endsWithIgnoreCase(invalidType, "prize")){
				result = activityFuncService.clearUserForPrizeByActivityPrizeID(prizeId);
			} else if (StringUtils.endsWithIgnoreCase(invalidType, "activity")){
				result = activityFuncService.clearUserForPrizeByActivityID(activityMid);
			} else {
				result.setSuccess(true);
			}
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
		
		return result;
	}
	/**
	 * 清楚配对用户
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/invalid-pairing")
	public Result invalidPairing(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Result result = new Result();
		try {
			String activityMid = request.getParameter("activityMid");
			String strPairingId = request.getParameter("pairingId");
			String invalidType = request.getParameter("invalidType");
			long pairingId = -1;
			
			// 判断配对ID是否为空
			if (!StringUtils.isBlank(strPairingId)) {
				pairingId = Long.parseLong(StringUtils.trim(strPairingId));
			}
			
			if (StringUtils.endsWithIgnoreCase(invalidType, "pairing")){
				result = activityFuncService.clearUserForPiring(pairingId);
			} else if (StringUtils.endsWithIgnoreCase(invalidType, "activity")){
				result = activityFuncService.clearUserForPairingByActivityID(activityMid);
			} else {
				result.setSuccess(true);
			}
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
		
		return result;
	}
	/**
	 * 初始化摇一摇数据
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/init-shake-info")
	public Result initShakeInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Result result = new Result();
		try {
			String activityMid = request.getParameter("activityMid");
			// 判断活动主题是否为空
			if (StringUtils.isBlank(activityMid)) {
				LOG.warn("活动MID参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}
			
			result = activityFuncService.initUserInfoForShake(activityMid);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
		return result;
	}
	
	/**
	 * 返回摇一摇排名
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/shake-list")
	public Result shakeList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Result result = new Result();
		try {
			String activityMid = request.getParameter("activityMid");
			String strlimit = request.getParameter("limit");
			// 判断活动主题是否为空
			if (StringUtils.isBlank(activityMid)) {
				LOG.warn("活动MID参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}
			int limit = 10;
			if (StringUtils.isNotBlank(strlimit)) {
				limit = Integer.parseInt(strlimit);
			}
			result = activityFuncService.getShakeList(activityMid, limit);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
		return result;
	}
	/**
	 * 活动手机摇动信息
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/shake")
	public Result shake(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Result result = new Result();
		try {
			String activityMid = request.getParameter("activityMid");
			String uuid = request.getParameter("uuid");
			String strShakeNum = request.getParameter("shakeNum");
			// 判断活动主题是否为空
			if (StringUtils.isBlank(activityMid)) {
				LOG.warn("活动ID参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}
			if (StringUtils.isBlank(uuid)) {
				LOG.warn("用户uuid参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}
			if (StringUtils.isBlank(strShakeNum)) {
				LOG.warn("摇动次数参数未发现!");
				result.setSuccess(false);
				result.setMessage("请求参数错误");
				return result;
			}
			
			int shakeNum = Integer.parseInt(StringUtils.trim(strShakeNum));
			result = activityFuncService.updateActivityShake(activityMid, uuid, shakeNum);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
		return result;
	}
	
	/**
	 * 按签到顺序返回单个签到用户
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/single_signin_user")
	public Result singleSignInUser(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="mid", required=true)String mid,
			@RequestParam(value="serial", required=true)int serial) {
		Result result = new Result();
		try {
			WeChatUser signInUser = activityFuncService.getSignInUser(mid, serial-1);
			// TODO 妈的 JSONIGNORE不起作用
			Map<String, Object> signInUserMapper = new LinkedHashMap<String, Object>();
			signInUserMapper.put("avatar", signInUser.getHeadImgUrl());
			signInUserMapper.put("nickName", signInUser.getNickName());
			result.setData(signInUserMapper);
		}catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("获取签到用户失败");
		}
		
		return result;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/listPhotoPile")
	public Result listPhotoPile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="activityMid", required=true)String activityMid) {
		Result result = new Result();
		try {
			String serverUrl = CommonUtils.getServerUrl(request); // 项目根目录的实际路径
			result = activityFuncService.listPhotoPile(activityMid, 40, serverUrl);
		}catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("获取精彩瞬间图片失败");
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/loadPhotoPile")
	public Result loadPhotoPile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="activityMid", required=true)String activityMid,
			@RequestParam(value="serial", required=true)int serial) {
		Result result = new Result();
		try {
			ActivityPhoto activityPhoto = activityFuncService.getSignPhotoPile(activityMid, serial-1);
			if (activityPhoto != null) {
				Map<String, String> obj = new HashMap<String, String>();
				if (activityPhoto.getType().equalsIgnoreCase(ActivityPhoto.SYSTEM_UPLOAD)) {
					String serverUrl = CommonUtils.getServerUrl(request); // 项目根目录的实际路径
					obj.put("imgSrc", serverUrl + File.separator + activityPhoto.getRelativePath());
				} else {
					obj.put("imgSrc", activityPhoto.getUrl());
				}
				result.setData(obj);
			}
		}catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("获取签到用户失败");
		}
		
		return result;
	}
	
	/**
	 * 功能开关
	 * @param request
	 * @param response
	 * @param mid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/get_func_switch")
	public Result getFuncSwitch(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="mid")String mid) {
		Result result = new Result();
		try {
			WeChatActivity activity = activityService.findByActivityMid(mid);
			Map<String, Boolean> switchs = new HashMap<String, Boolean>();
			if (activity == null) {
				switchs.put("fexist", false);
			} else {
				switchs.put("fexist", true);
				switchs.put("fdraw", activity.isfDraw());
				switchs.put("fpair", activity.isfPair());
				switchs.put("fshake", activity.isfShake());
				switchs.put("fsignin", activity.isfSignIn());
			}
			result.setData(switchs);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("获得功能开关失败");
		}
		
		return result;
	}
	
}
