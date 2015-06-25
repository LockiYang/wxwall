package com.wxwall.modules.user.web.controller;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wxwall.common.entity.Result;
import com.wxwall.common.utils.CommonUtils;
import com.wxwall.common.utils.FileUpload;
import com.wxwall.common.utils.PathUtils;
import com.wxwall.modules.user.entity.User;
import com.wxwall.modules.user.service.UserService;
import com.wxwall.modules.user.utils.UserUtils;
import com.wxwall.modules.wechat.entity.WeChatApp;
import com.wxwall.modules.wechat.service.WeChatService;
import com.wxwall.wechat.api.advanced.model.AccessToken;
import com.wxwall.wechat.api.util.CommonUtil;
import com.wxwall.wechat.api.util.WxAuthChecker;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	protected Logger LOG = Logger.getLogger(getClass());
	@Autowired private UserService userService;

	@Autowired private WeChatService weChatService;
	
	@RequestMapping(value = "/profile/1", method = RequestMethod.GET)
	public String  profileFirstForm(Model model) {
		User user = UserUtils.getUser();
		if (user != null) {
			if (user.needComplete()) {
				model.addAttribute("user", user);
			} else {
				return "redirect:/user/profile/2";
			}
		}
		return "first-entry-step-one";
	}
	
	@RequestMapping(value = "/profile/2", method = RequestMethod.GET)
	public String profileSecondForm(Model model) {
		User user = UserUtils.getUser();
		if (user != null) {
			model.addAttribute("user", user);
			WeChatApp weChatApp = user.getWeChatApp();
			if (weChatApp != null) {
				if (weChatApp.needComplete()) {
					model.addAttribute("weChatApp", weChatApp);
				} else {
					return "redirect:/user/profile/3";
				}
			}
		}
		return "first-entry-step-two";
	}
	
	@RequestMapping(value = "/profile/3", method = RequestMethod.GET)
	public String profileThreeForm(HttpServletRequest request, Model model) {
		User user = UserUtils.getUser();
		if (user != null) {
			model.addAttribute("user", user);
			WeChatApp weChatApp = user.getWeChatApp();
			if (weChatApp != null) {
				if (weChatApp.getAccountType() == WeChatApp.ACCOUNT_TYPE.UNAUTH.getIndex()) {
//					model.addAttribute("errorMsg", "公众号还未进行认证，暂时不能申请活动，请转到公众平台进行认证!");
				} else if (weChatApp.getAccountType() == WeChatApp.ACCOUNT_TYPE.SUBSCRIBER.getIndex()) {
//					model.addAttribute("errorMsg", "公众号为订阅号，部分功能将会受限，请转到公众平台进行升级!");
				}
				weChatApp.setMid(CommonUtils.getServerUrl(request) + "/wechat/process?mid=" + weChatApp.getMid());
				model.addAttribute("weChatApp", weChatApp);
			}
		}
		return "first-entry-step-three";
	}
	
	@RequestMapping(value = "/profile/1", method = RequestMethod.POST)
	public String profileFirst(Model model,
			String userName,
			String linkman,
			String mobilePhone,
			String plainPassword) {
		
		try {
			User user = userService.update(userName, linkman, mobilePhone, plainPassword);
		} catch (Exception e) {
			
		}
		
		return "redirect:/user/profile/2";
	}
	
	@RequestMapping(value = "/profile/2", method = RequestMethod.POST)
	public ModelAndView profileSecond(String weChatOriginId, String weChatName, String appId, String appSecret) {
		ModelAndView mv = new ModelAndView("redirect:/user/profile/3");
		String errorMsg = null;
		
		User user = UserUtils.getUser();
		if (user == null) {
			mv.setViewName("logout");
			return mv;
		}
		mv.addObject("user", user);
		
		//判断公众号原始ID是否为空
		if (StringUtils.isBlank(weChatOriginId)) {
			errorMsg = "公众号原始ID未填写!";
		}
		weChatOriginId = StringUtils.trim(weChatOriginId);
		//判断公众号名是否为空
		if (StringUtils.isBlank(weChatName)) {
			errorMsg = "公众号名未填写!";
		}
		weChatName = StringUtils.trim(weChatName);
		//判断公众号应用ID是否为空
		if (StringUtils.isBlank(appId)) {
			errorMsg = "公众号应用ID未填写!";
		}
		appId = StringUtils.trim(appId);
		//判断公众号应用密钥是否为空
		if (StringUtils.isBlank(appSecret)) {
			errorMsg = "公众号应用密钥未填写!";
		}
		appSecret = StringUtils.trim(appSecret);
				
		WeChatApp weChatApp = user.getWeChatApp();
		if (weChatApp != null) {
			weChatApp.setAppId(appId);
			weChatApp.setWeChatName(weChatName);
			weChatApp.setWeChatOriginId(weChatOriginId);
			weChatApp.setAppSecret(appSecret);
			//判断传输是否有错
			if (!StringUtils.isBlank(errorMsg)) {
				mv.setViewName("redirect:/user/profile/2");
				mv.addObject("weChatApp", weChatApp);
				mv.addObject("errorMsg", errorMsg);
				return mv;
			}
		} else {
			//判断传输是否有错
			if (!StringUtils.isBlank(errorMsg)) {
				mv.setViewName("redirect:/user/profile/2");
				mv.addObject("errorMsg", errorMsg);
				return mv;
			}
			weChatApp = new WeChatApp();
			// 生成mid和token，为微信验证提供条件
			String mid = CommonUtils.generateUUID();
			String token = CommonUtils.generateUUID();
			// 判断系统以前是否存在同一个UUID
			while (true) {
				WeChatApp weChatAppExisted = weChatService
						.getWeChatAppByMid(mid);
				if (weChatAppExisted != null) {
					mid = CommonUtils.generateUUID();
				} else {
					break;
				}
			}
			weChatApp.setAppId(appId);
			weChatApp.setWeChatName(weChatName);
			weChatApp.setWeChatOriginId(weChatOriginId);
			weChatApp.setAppSecret(appSecret);
			weChatApp.setMid(mid);
			weChatApp.setToken(token);
			weChatApp.setUser(user);
		}
		//获取全局密钥
		AccessToken accessToken = CommonUtil.getAccessToken(weChatApp.getAppId(), weChatApp.getAppSecret());
		if (accessToken != null) {
			Calendar calendar=Calendar.getInstance(TimeZone.getDefault());
			Date expire = new Date(calendar.getTime().getTime() + accessToken.getExpiresin() * 1000);
			weChatApp.setAccessToken(accessToken.getAccesstoken());
			weChatApp.setExpire(expire);
		} else {// 如果返回空值，则表示连接错误
			errorMsg = "连接微信服务器错误,请确定AppID或者AppSecret是否正确!";
			mv.setViewName("redirect:/user/profile/2");
			mv.addObject("weChatApp", weChatApp);
			mv.addObject("errorMsg", errorMsg);
			return mv;
		}
		
		//检查权限
		byte authRole = WxAuthChecker.getAuthRole(accessToken.getAccesstoken());
		weChatApp.setAccountType(authRole);
		weChatService.save(weChatApp, true);
		
		mv.addObject("weChatApp", weChatApp);
		return mv;
	}
	
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public ModelAndView userInfoForm() {
		ModelAndView mv = new ModelAndView("user-info");
		mv.addObject("user", UserUtils.getUser());
		return mv;
	}
	
	@RequestMapping(value = "/info", method = RequestMethod.POST)
	public String userInfo(Model model,
			String userName,
			String linkman,
			String mobilePhone,
			String plainPassword) {
		User user = userService.update(userName, linkman, mobilePhone, plainPassword);
		model.addAttribute("user", user);
		return "redirect:/user/info";
	}
	
	@RequestMapping(value = "/wechat_info", method = RequestMethod.GET)
	public ModelAndView weChatAppInfoForm(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("public-account-info");
		User currentUser = UserUtils.getUser();
		WeChatApp weChatApp = currentUser.getWeChatApp();
		if (weChatApp == null) {
			//异常
		} else {
			weChatApp.setMid(CommonUtils.getServerUrl(request) + "/wechat/process?mid=" + weChatApp.getMid());
			weChatApp.getExpire();
			mv.addObject("weChatApp", weChatApp);
		}
		
		return mv;
	}
	
	@RequestMapping(value = "/wechat_advance_info", method = RequestMethod.GET)
	public ModelAndView weChatAppAdvanceInfoForm(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("public-account-advance-info");
		User currentUser = UserUtils.getUser();
		WeChatApp weChatApp = currentUser.getWeChatApp();
		if (weChatApp == null) {
			//异常
		} else {
			weChatApp.setMid(CommonUtils.getServerUrl(request) + "/wechat/process?mid=" + weChatApp.getMid());
			weChatApp.getExpire();
			mv.addObject("weChatApp", weChatApp);
		}
		
		return mv;
	}
	
	@RequestMapping(value = "/wechat_advance_info", method = RequestMethod.POST)
	public String weChatAppAdvanceInfo(String setupType, String autoType, String content,
			String title, String imgUrl, String img, HttpServletRequest request, RedirectAttributes ra,
			Model model) {
		if (StringUtils.isBlank(setupType)) {
			//错误处理
			LOG.error("ad-setup页面请求参数错误！");
			return null;
		}
		
		if (StringUtils.isBlank(autoType)) {
			//错误处理
			LOG.error("ad-setup页面请求参数错误！");
			return null;
		}
		
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		Result result = weChatService.SetupWeChatAppAutoReply(setupType, autoType, content, title, imgUrl, img, rootPath);
		if (!result.isSuccess()) {
			ra.addFlashAttribute("errorMsg", result.getMessage());
		}
		
		return "redirect:/user/wechat_advance_info";
	}
	
	@RequestMapping(value = "/wechat_info", method = RequestMethod.POST)
	public String weChatAppInfo(WeChatApp weChatApp,RedirectAttributes ra) {
		String errorMsg = null;
		User user = UserUtils.getUser();
		WeChatApp currentWeChatApp = user.getWeChatApp();
		if (currentWeChatApp == null) {
			errorMsg = "没有绑定公众号";
		}else {
			if (StringUtils.isNotBlank(weChatApp.getAppSecret()) && StringUtils.isNotBlank(weChatApp.getAppId())) {
				AccessToken accessToken = CommonUtil.getAccessToken(weChatApp.getAppId(), weChatApp.getAppSecret());
				if (accessToken != null) {
					Calendar calendar=Calendar.getInstance(TimeZone.getDefault());
					Date expire = new Date(calendar.getTime().getTime() + accessToken.getExpiresin() * 1000);
					currentWeChatApp.setAppId(weChatApp.getAppId());
					currentWeChatApp.setAppSecret(weChatApp.getAppSecret());
					currentWeChatApp.setAccessToken(accessToken.getAccesstoken());
					currentWeChatApp.setExpire(expire);
					byte authRole = WxAuthChecker.getAuthRole(currentWeChatApp.getAccessToken());
					currentWeChatApp.setAccountType(authRole);
				} else {// 如果返回空值，则表示连接错误
					errorMsg = "请设置正确的应用ID和应用密钥";
					
				}
			} else if (StringUtils.isNotBlank(weChatApp.getAppSecret()) || StringUtils.isNotBlank(weChatApp.getAppId())) {
				errorMsg = "请同时设置应用ID和应用密钥";
			}
			
			if (StringUtils.isNotBlank(weChatApp.getWeChatOriginId())) {
				currentWeChatApp.setWeChatOriginId(weChatApp.getWeChatOriginId());
			}
			if (StringUtils.isNotBlank(weChatApp.getWeChatName())) {
				currentWeChatApp.setWeChatName(weChatApp.getWeChatName());
			}
		}
		
		if (StringUtils.isBlank(errorMsg)) {
			weChatService.update(currentWeChatApp);
		} else {
			ra.addFlashAttribute("errorMsg", errorMsg);
		}
		return "redirect:/user/wechat_info";
	}
	
	@ResponseBody
	@RequestMapping(value = "/wechat_info_ticket", method = RequestMethod.POST)
	public Result uploadWechatTicket(HttpServletRequest request, String activityMid) { 
		Result result = new Result();
		User currentUser = UserUtils.getUser();
		WeChatApp weChatApp = currentUser.getWeChatApp();
		if (weChatApp == null) {
			//异常
		} else {
			FileUpload fileUpload = new FileUpload(request);
			String relativePath = CommonUtils.generateNewDir(CommonUtils.STORE_PICTURE_DIR);
			String storePath = PathUtils.getRealPath(relativePath, request);
			
			File file = null;
			try {
				file = fileUpload.upload(storePath, FileUpload.maxImgSize, true);
				
				//ActivityImage image = new ActivityImage(relativePath + File.separator + file.getName());
				String sourcePath = storePath + File.separator + weChatApp.getWeChatImage();
				weChatApp.setWeChatImage(relativePath + File.separator + file.getName());
				weChatService.update(weChatApp);
				if (StringUtils.isNotBlank(sourcePath)) {
					File sourceFile = new File(sourcePath);
					if (sourceFile.exists()) {
						FileUtils.forceDelete(sourceFile);
					}
				}
				result.setSuccess(true);
			} catch (Exception e) {
				LOG.error("error", e);
				result.setSuccess(false);
				result.setMessage("系统内部错误!");
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/wechat_test", method = RequestMethod.GET)
	public String weChatAppTest(RedirectAttributes ra) {
		
		String errorMsg = null;
		WeChatApp weChatApp = UserUtils.getUser().getWeChatApp();
		if (weChatApp == null) {
			errorMsg = "请设置正确的应用ID和应用密钥";
			ra.addFlashAttribute("errorMsg", errorMsg);
		} else {
			if (StringUtils.isNotBlank(weChatApp.getAppSecret()) && StringUtils.isNotBlank(weChatApp.getAppId())) {
				AccessToken accessToken = CommonUtil.getAccessToken(weChatApp.getAppId(), weChatApp.getAppSecret());
				if (accessToken != null) {
					Calendar calendar=Calendar.getInstance(TimeZone.getDefault());
					Date expire = new Date(calendar.getTime().getTime() + accessToken.getExpiresin() * 1000);
					weChatApp.setAccessToken(accessToken.getAccesstoken());
					weChatApp.setExpire(expire);
				} else {// 如果返回空值，则表示连接错误
					errorMsg = "请设置正确的应用ID和应用密钥";
					ra.addFlashAttribute("errorMsg", errorMsg);
				}
			} else if (StringUtils.isNotBlank(weChatApp.getAppSecret()) || StringUtils.isNotBlank(weChatApp.getAppId())) {
				errorMsg = "请同时设置应用ID和应用密钥";
				ra.addFlashAttribute("errorMsg", errorMsg);
			}
		}
		byte authRole = WxAuthChecker.getAuthRole(weChatApp.getAccessToken());
		weChatApp.setAccountType(authRole);
		if (StringUtils.isBlank(errorMsg)) {
			weChatService.update(weChatApp);
		}
		return "redirect:/user/wechat_info";
	}
	
	@RequestMapping(value = "/wechat_menu_setting", method = RequestMethod.GET)
	public ModelAndView weChatAppMenuSetting(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("wachat-menu-setting");
		User currentUser = UserUtils.getUser();
		WeChatApp weChatApp = currentUser.getWeChatApp();
		if (weChatApp == null) {
			//异常
		} else {
			weChatApp.setMid(CommonUtils.getServerUrl(request) + "/wechat/process?mid=" + weChatApp.getMid());
			weChatApp.getExpire();
			mv.addObject("weChatApp", weChatApp);
		}
		
		return mv;
	}
	
}
