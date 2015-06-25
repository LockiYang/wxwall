/*
 * <p>
 *  版权所有 : ©2014 80通讯科技有限公司. 
 * Copyright © PING AN INSURANCE (GROUP) COMPANY OF CHINA ,LTD. All Rights Reserved
 * </p>
 */
package com.wxwall.modules.wechat.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wxwall.common.entity.ActivityUser;
import com.wxwall.common.entity.UpbangMsg;
import com.wxwall.common.service.ServiceException;
import com.wxwall.common.utils.CommonUtils;
import com.wxwall.common.utils.DateUtils;
import com.wxwall.common.web.BaseController;
import com.wxwall.modules.activity.service.ActivityMsgService;
import com.wxwall.modules.activity.service.ActivityService;
import com.wxwall.modules.user.entity.User;
import com.wxwall.modules.user.utils.UserUtils;
import com.wxwall.modules.wechat.entity.RActivityWeChatUser;
import com.wxwall.modules.wechat.entity.WeChatActivity;
import com.wxwall.modules.wechat.entity.WeChatApp;
import com.wxwall.modules.wechat.entity.WeChatUserMsg;
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
@RequestMapping("/activity")
public class ActivityController extends BaseController {

	@Autowired private ActivityService activityService;
	
	@Autowired private WeChatService weChatService;
	
	@Autowired private ActivityMsgService activityMsgService;
	
	@RequestMapping(value = "/apply", method = RequestMethod.GET)
	public String applyForm(HttpServletRequest request, Model model) {
		User user = UserUtils.getUser();
		// TODO 权限检查
		if (user != null) {
			model.addAttribute("user", user);
			WeChatApp weChatApp = user.getWeChatApp();
			if (weChatApp != null) {
				if (weChatApp.getAccountType() == WeChatApp.ACCOUNT_TYPE.UNAUTH.getIndex()) {
					//model.addAttribute("errorMsg", "公众号还未进行认证，暂时不能申请活动，请转到公众平台进行认证!");
					if (StringUtils.isBlank(weChatApp.getWeChatImage())) {
						String serverUrl = CommonUtils.getServerUrl(request);
						String errorMsg = "创建活动失败，请先上传<a style=\"padding:3px 8px;background-color:#01CF97;color:#fff;\" href=\""
								+ serverUrl + "/user/wechat_info\">公众号二维码</a>!";
						model.addAttribute("errorMsg", errorMsg);
					} else {
						model.addAttribute("errorMsg", "公众号为订阅号，部分功能将会受限，请转到公众平台进行升级!");
					}
				} else if (weChatApp.getAccountType() == WeChatApp.ACCOUNT_TYPE.SUBSCRIBER.getIndex()) {
					if (StringUtils.isBlank(weChatApp.getWeChatImage())) {
						String serverUrl = CommonUtils.getServerUrl(request);
						String errorMsg = "创建活动失败，请先上传<a style=\"padding:3px 8px;background-color:#01CF97;color:#fff;\" href=\""
								+ serverUrl + "/user/wechat_info\">公众号二维码</a>!";
						model.addAttribute("errorMsg", errorMsg);
					} else {
						model.addAttribute("errorMsg", "公众号为订阅号，部分功能将会受限，请转到公众平台进行升级!");
					}
				} else {
					if (StringUtils.isBlank(weChatApp.getWeChatImage())) {
						String serverUrl = CommonUtils.getServerUrl(request);
						String errorMsg = "创建活动失败，请先上传<a style=\"padding:3px 8px;background-color:#01CF97;color:#fff;\" href=\""
								+ serverUrl + "/user/wechat_info\">公众号二维码</a>!";
						model.addAttribute("errorMsg", errorMsg);
					}
				}
			}
		}
		return "activity-apply";
	}

	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public String apply(WeChatActivity weChatActivity, 
			@RequestParam(value="startDate") String strStartDate,
			HttpServletRequest request,RedirectAttributes ra) {
		String errorMsg = null;
		try {
			//判断开始时间和截至时间格式
			if (StringUtils.isBlank(strStartDate)) {
				errorMsg = "申请活动失败，请设置活动开始时间!";
				ra.addFlashAttribute("errorMsg", errorMsg);
				ra.addFlashAttribute("weChatActivity", weChatActivity);
				return "redirect:/activity/apply";
			} else {
				Date startDate = DateUtils.parseDate(strStartDate);
				weChatActivity.setStartDate(startDate);
				weChatActivity.setEndDate(DateUtils.addDays(startDate, 3));
			}
			
			User user = UserUtils.getUser();
			WeChatApp weChatApp = user.getWeChatApp();
			
			
			/**
			 * 1.未进行认证的公众号不允许创建活动
			 * 2.进行认证的订阅号不允许创建活动二维码
			 * 3.进行认证的服务号运行创建活动二维码
			 */
			/*if (weChatApp.getAccountType() == WeChatApp.ACCOUNT_TYPE.NO_SUBSCRIBER.getIndex()) {//没认证
				errorMsg = "创建活动失败，公众号未进行认证";
				ra.addFlashAttribute("errorMsg", errorMsg);
				ra.addFlashAttribute("weChatActivity", weChatActivity);
				return "redirect:/activity/apply";
			} */
			if (StringUtils.isBlank(weChatApp.getWeChatImage())) {
				String serverUrl = CommonUtils.getServerUrl(request);
				errorMsg = "创建活动失败，请先上传<a style=\"padding:3px 8px;background-color:#01CF97;color:#fff;\" href=\""
						+ serverUrl + "/user/wechat_info\">公众号二维码</a>!";
				ra.addFlashAttribute("errorMsg", errorMsg);
				ra.addFlashAttribute("weChatActivity", weChatActivity);
				return "redirect:/activity/apply";
			}
			
			
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			activityService.newWeChatActivity(weChatActivity, rootPath);
		} catch(ServiceException e) {
			LOG.error(e.getMessage(), e);
			errorMsg = e.getMessage();
			ra.addFlashAttribute("errorMsg", errorMsg);
			ra.addFlashAttribute("weChatActivity", weChatActivity);
			return "redirect:/activity/apply";
		}
		return "redirect:/activity/list";
	}
	
	@RequestMapping(value = "/{activityMid}/end", method = RequestMethod.GET)
	public String end(HttpServletRequest request,
			@PathVariable("activityMid") String activityMid) {
		
		activityService.endActivity(activityMid);
		return "redirect:/activity/list";
	}
	
	/**
	 * 修改活动
	 * @return
	 */
	@RequestMapping(value = "/update")
	public String update(HttpServletRequest request, Model model, 
			@RequestParam(value = "activityMid") String activityMid, 
			String subject, String organisers, String description) {
		
		if (StringUtils.isNotBlank(activityMid)) {
			WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
			if (weChatActivity != null) {
				weChatActivity.setOrganisers(organisers);
				weChatActivity.setDescription(description);
				weChatActivity.setSubject(subject);
			}
			activityService.save(weChatActivity);
		}
		
		return "redirect:/activity/list";
	}
	
	/**
	 * 返回活动页面
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, Model model,
			@RequestParam(value = "type", defaultValue = "inprogress") String type,
			@RequestParam(value = "page", defaultValue = "1") byte page,
			@RequestParam(value = "size", defaultValue = "5") byte size) {
		
		if (page > 0) {
			page = (byte) (page -1);
		} else {
			page = 0;
		}
		if (size <= 0)
			size = 0;
		
		Page<WeChatActivity> myActivitys = activityService.getMyActivitys(page, size, type);
		
		List<WeChatActivity> activityList = new ArrayList<WeChatActivity>();
		for (WeChatActivity weChatActivity : myActivitys) {
			activityList.add(weChatActivity);
		}
		
		model.addAttribute("activityList", activityList);
		model.addAttribute("type",type);
		model.addAttribute("pageNum", page+1);
		model.addAttribute("totalNum", myActivitys.getTotalPages());
		
		return "activity-list";
	}

	/**
	 * 上墙消息数据页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/{activityMid}/data_list")
	public String msgData(@PathVariable("activityMid") String activityMid,
			HttpServletRequest request, String msg_type, String type, String msgId,
			@RequestParam(value = "page", defaultValue = "1") byte page,
			@RequestParam(value = "size", defaultValue = "10") byte size, 
			Model model) {
		
		byte msgStatus = WeChatUserMsg.MSG_STATUS.CHECKING.getIndex();
		if (StringUtils.isNotEmpty(msg_type)) {
			if (msg_type.endsWith("faild")) {
				msgStatus = WeChatUserMsg.MSG_STATUS.FAILED.getIndex();
			} else if (msg_type.endsWith("success")) {
				msgStatus = WeChatUserMsg.MSG_STATUS.SUCCESS.getIndex();
			}
		}
		
		if(StringUtils.isNotBlank(type)) {
			if (StringUtils.equalsIgnoreCase(type, "downBang")) {
				activityMsgService.updateMsgStatus(Long.parseLong(msgId), WeChatUserMsg.MSG_STATUS.CHECKING.getIndex());
			} else if (StringUtils.equalsIgnoreCase(type, "upBang")) {
				activityMsgService.updateMsgStatus(Long.parseLong(msgId), WeChatUserMsg.MSG_STATUS.SUCCESS.getIndex());
			}
		}
		
		if (page > 0) {
			page = (byte) (page -1);
		} else {
			page = 0;
		}
		if (size <= 0)
			size = 0;
		
		Page<WeChatUserMsg> activityMsgs = activityMsgService.getActivityMsgList(activityMid, page, size, msgStatus);
		
		List<UpbangMsg> activityMsgList = new ArrayList<UpbangMsg>();
		String serverUrl = CommonUtils.getServerUrl(request);
		for (WeChatUserMsg weChatUserMsg : activityMsgs) {
			UpbangMsg upbangMsg = new UpbangMsg(weChatUserMsg, weChatUserMsg.getrActivityWeChatUser().getWeChatUser(), serverUrl);
			activityMsgList.add(upbangMsg);
		}
		model.addAttribute("activityMsgList", activityMsgList);
		model.addAttribute("msgStatus",msgStatus);
		model.addAttribute("pageNum", page+1);
		model.addAttribute("activityMid", activityMid);
		model.addAttribute("totalNum", activityMsgs.getTotalPages());

		return "data-list";
	}
	
	@RequestMapping(value = "/{activityMid}/user_list")
	public String userData(@PathVariable("activityMid") String activityMid,
			HttpServletRequest request, String user_status, 
			@RequestParam(value = "page", defaultValue = "1") byte page,
			@RequestParam(value = "size", defaultValue = "10") byte size, 
			Model model) {
		
		byte userStatus = RActivityWeChatUser.USER_STATUS.SIGN.getIndex();
		if (StringUtils.isNotEmpty(user_status)) {
			if (user_status.endsWith("sign")) {
				userStatus = RActivityWeChatUser.USER_STATUS.SIGN.getIndex();
			} else if (user_status.endsWith("cancelSign")) {
				userStatus = RActivityWeChatUser.USER_STATUS.CANCEL_SIGN.getIndex();
			}
		}
		
		if (page > 0) {
			page = (byte) (page -1);
		} else {
			page = 0;
		}
		if (size <= 0)
			size = 0;
		
		Page<RActivityWeChatUser> listRActivityWeChatUsera = activityService.listActivityUser(activityMid, userStatus, page, size);
		
		List<ActivityUser> userList = new ArrayList<ActivityUser>();
		for (RActivityWeChatUser rActivityWeChatUser : listRActivityWeChatUsera) {
			ActivityUser activityUser = new ActivityUser(rActivityWeChatUser.getWeChatUser(), rActivityWeChatUser.getUpdateTime());
			userList.add(activityUser);
		}
		
		model.addAttribute("userList", userList);
		model.addAttribute("userStatus",userStatus);
		model.addAttribute("pageNum", page+1);
		model.addAttribute("activityMid", activityMid);
		model.addAttribute("totalNum", listRActivityWeChatUsera.getTotalPages());

		return "user-list";
	}

}
