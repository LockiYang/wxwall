package com.wxwall.modules.wechat.web.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wxwall.common.entity.Result;
import com.wxwall.common.utils.CommonUtils;
import com.wxwall.common.utils.FileUpload;
import com.wxwall.common.utils.PathUtils;
import com.wxwall.common.web.BaseController;
import com.wxwall.modules.activity.service.ActivityFuncService;
import com.wxwall.modules.activity.service.ActivityMsgService;
import com.wxwall.modules.activity.service.ActivityService;
import com.wxwall.modules.wechat.entity.ActivityImage;
import com.wxwall.modules.wechat.entity.ActivityPhoto;
import com.wxwall.modules.wechat.entity.WeChatActivity;
import com.wxwall.modules.wechat.service.WeChatService;

/**
 * 活动功能定义控制器
 *
 * @author Locki<lockiyang@qq.com>
 * @since 2015年3月10日
 *
 */
@Controller
@RequestMapping("/func")
public class ActivityFuncController extends BaseController {
	@Autowired private ActivityService activityService;
	@Autowired private WeChatService weChatService;
	@Autowired private ActivityMsgService activityMsgService;
	@Autowired private ActivityFuncService activityFuncService;
	
	@ResponseBody
	@RequestMapping(value = "/swith")
	public Result funcSwith(@RequestParam(value = "activityMid", defaultValue = "-1")String activityMid, HttpServletRequest request, Model model) {
		Result result = new Result();
		if (activityMid == null) {
			result.setMessage("请求参数错误");
			result.setSuccess(false);
			return result;
		}
		
		String fDraw = request.getParameter("fDraw");// 是否抽奖
		String fLogo = request.getParameter("fLogo");; // 是否自定义logo
		String fBackground = request.getParameter("fBackground"); // 允许自定义背景
		String fAutoUpWall = request.getParameter("fAutoUpWall");// 自动上墙
		String fAutoFiter  = request.getParameter("fAutoFiter"); // 自动上墙过滤
		String fVote = request.getParameter("fVote"); // 投票
		String fShake = request.getParameter("fShake"); // 摇一摇
		String fPair = request.getParameter("fPair"); // 对对碰
		String fSms = request.getParameter("fSms"); // 短信
		String fSignIn = request.getParameter("fSignIn"); // 签到
		String fScheduler = request.getParameter("fScheduler");//日程安排
		String fAlbum = request.getParameter("fAlbum");//日程安排
		String fActivityPhoto = request.getParameter("fActivityPhoto");//日程安排
		
		if (StringUtils.isBlank(fDraw) && StringUtils.isBlank(fLogo) && StringUtils.isBlank(fBackground) &&
				StringUtils.isBlank(fAutoUpWall) && StringUtils.isBlank(fAutoFiter) && StringUtils.isBlank(fVote) &&
				StringUtils.isBlank(fShake) && StringUtils.isBlank(fPair) && StringUtils.isBlank(fSms) &&
				StringUtils.isBlank(fSignIn) && StringUtils.isBlank(fScheduler) && StringUtils.isBlank(fAlbum) && StringUtils.isBlank(fActivityPhoto)) {
			//错误处理
			result.setMessage("请求参数错误");
			result.setSuccess(false);
			return result;
		}
		result = activityFuncService.funcSwith(activityMid, fDraw, fLogo, fBackground, fAutoUpWall, 
				fAutoFiter, fVote, fShake, fPair, fSms, fSignIn, fScheduler, fAlbum, fActivityPhoto);
		return result;
	}
	
//	@RequestMapping(value = "/{activityMid}/album", method = RequestMethod.GET)
//	public String album(@PathVariable("activityMid") String activityMid, HttpServletRequest request, Model model) { 
//		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
//		
//		model.addAttribute("weChatActivity", weChatActivity);
//		return "func-album";
//	}
//	
	@RequestMapping(value = "/{activityMid}/signin", method = RequestMethod.GET)
	public String signIn(@PathVariable("activityMid") String activityMid, HttpServletRequest request, Model model) { 
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		
		model.addAttribute("weChatActivity", weChatActivity);
		return "func-signin";
	}
	
	@RequestMapping(value = "/{activityMid}/shake", method = RequestMethod.GET)
	public String shake(@PathVariable("activityMid") String activityMid, HttpServletRequest request, Model model) { 
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		
		model.addAttribute("weChatActivity", weChatActivity);
		return "func-shake";
	}
	
	@RequestMapping(value = "/{activityMid}/pair", method = RequestMethod.GET)
	public String pair(@PathVariable("activityMid") String activityMid, HttpServletRequest request, Model model) { 
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		
		model.addAttribute("weChatActivity", weChatActivity);
		return "func-pair";
	}
	
	@RequestMapping(value = "/{activityMid}/autoupwall", method = RequestMethod.GET)
	public String autoupwall(@PathVariable("activityMid") String activityMid, HttpServletRequest request, Model model) { 
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		
		model.addAttribute("weChatActivity", weChatActivity);
		return "func-autoupwall";
	}
	
	@RequestMapping(value = "/{activityMid}/ad", method = RequestMethod.GET)
	public String ad(@PathVariable("activityMid") String activityMid, HttpServletRequest request, Model model) { 
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		
		model.addAttribute("weChatActivity", weChatActivity);
		return "func-ad";
	}
	
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public String signInSetting(String activityMid, String signKeyWord, HttpServletRequest request, RedirectAttributes ra,
			Model model) {
		if (StringUtils.isBlank(activityMid)) {
			//错误处理
			LOG.error("signin页面请求参数错误！");
			return null;
		}
		
		if (StringUtils.isBlank(signKeyWord)) {
			//错误处理
			LOG.error("signin页面请求参数错误！");
			return null;
		}
		
		Result result = activityFuncService.SetupSignIn(activityMid, signKeyWord);
		if (!result.isSuccess()) {
			ra.addFlashAttribute("errorMsg", result.getMessage());
		}
		
		return "redirect:/func/" + activityMid + "/signin";
	}
	
	/**
	 * 广告设置请求
	 */
	@RequestMapping(value = "/ad", method = RequestMethod.POST)
	public String setupReply(String activityMid, String setupType, String signReplyType, String signContent,
			String signTitle, String signUrl, String signImg, String upbangContent, String tipId, 
			String tipContent, String albumSubject, HttpServletRequest request, RedirectAttributes ra,
			Model model) {
		if (StringUtils.isBlank(activityMid)) {
			//错误处理
			LOG.error("ad-setup页面请求参数错误！");
			return null;
		}
		
		if (StringUtils.isBlank(setupType)) {
			//错误处理
			LOG.error("ad-setup页面请求参数错误！");
			return null;
		}
		
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		Result result = activityFuncService.SetupActivityAd(activityMid, setupType, signReplyType, signContent, 
				signTitle, signUrl, signImg, upbangContent, tipId, tipContent, albumSubject, rootPath);
		if (!result.isSuccess()) {
			ra.addFlashAttribute("errorMsg", result.getMessage());
		}
		
		return "redirect:/func/" + activityMid + "/ad";
	}
	
	/**
	 * 相册主题设置
	 */
	@RequestMapping(value = "/albumInfo", method = RequestMethod.POST)
	public String setupAlbumInfo(String activityMid, String albumSubject, HttpServletRequest request, RedirectAttributes ra,
			Model model) {
		if (StringUtils.isBlank(activityMid)) {
			//错误处理
			LOG.error("ad-setup页面请求参数错误！");
			return null;
		}
		
		Result result = activityFuncService.SetupActivityAlbumSubject(activityMid, albumSubject);
		if (!result.isSuccess()) {
			ra.addFlashAttribute("errorMsg", result.getMessage());
		}
		
		return "redirect:/func/" + activityMid + "/album";
	}
	
	/**
	 * 精彩瞬间设置
	 */
	@RequestMapping(value = "/photoPileInfo", method = RequestMethod.POST)
	public String setupPhotoPileInfo(String activityMid, String adminUpload, String signUserUpload, String systemUpload, HttpServletRequest request, RedirectAttributes ra,
			Model model) {
		if (StringUtils.isBlank(activityMid)) {
			//错误处理
			LOG.error("ad-setup页面请求参数错误！");
			return null;
		}
		String errorMsg = null;
		if (StringUtils.isBlank(adminUpload)) {
			errorMsg = "系统内部错误!";
		}
		
		if (StringUtils.isBlank(signUserUpload)) {
			errorMsg = "系统内部错误!";
		}
		
		if (StringUtils.isBlank(systemUpload)) {
			errorMsg = "系统内部错误!";
		}
		
		if (!StringUtils.isBlank(errorMsg)) {
			ra.addFlashAttribute("errorMsg", errorMsg);
			return "redirect:/func/" + activityMid + "/photoPile";
		}
		Result result = activityFuncService.SetupActivityPhotoPile(activityMid, adminUpload, signUserUpload, systemUpload);
		if (!result.isSuccess()) {
			ra.addFlashAttribute("errorMsg", result.getMessage());
		}
		
		return "redirect:/func/" + activityMid + "/photoPile";
	}
	
	/**
	 * 相册摇一摇信息
	 */
	@RequestMapping(value = "/shake", method = RequestMethod.POST)
	public String setupShake(String activityMid, String endShakeNum, String endShakeTime, 
			HttpServletRequest request, RedirectAttributes ra,
			Model model) {
		if (StringUtils.isBlank(activityMid)) {
			//错误处理
			LOG.error("ad-setup页面请求参数错误！");
			return null;
		}
		
		if (StringUtils.isBlank(endShakeNum)) {
			LOG.error("ad-setup页面请求参数错误！");
			return null;
		}
		
		if (StringUtils.isBlank(endShakeTime)) {
			LOG.error("ad-setup页面请求参数错误！");
			return null;
		}
		
		int iEndShakeNum = Integer.parseInt(endShakeNum);
		int iEndShakeTime = Integer.parseInt(endShakeTime);
		
		if (iEndShakeNum <=0 || iEndShakeTime <= 0) {
			ra.addFlashAttribute("errorMsg", "摇一摇截止次数或者摇一摇截止时间参数设置错误，必须大于0");
		} else {
			Result result = activityFuncService.
					SetupActivityShakeInfo(activityMid, iEndShakeNum, iEndShakeTime);
			if (!result.isSuccess()) {
				ra.addFlashAttribute("errorMsg", result.getMessage());
			}
		}
		
		return "redirect:/func/" + activityMid + "/shake";
	}
	
	/**
	 * 删除活动页面顶部提示信息
	 */
	@RequestMapping(value = "/del_ad", method = RequestMethod.GET)
	public String deleteAdSetup(String activityMid, String setupType, String tipId, HttpServletRequest request, Model model,
			RedirectAttributes ra) {
		if (StringUtils.isBlank(activityMid)) {
			//错误处理
			LOG.error("ad-setup页面请求参数错误！");
			return null;
		}
		
		if (StringUtils.isBlank(setupType)) {
			//错误处理
			LOG.error("ad-setup页面请求参数错误！");
			return null;
		}
		
		Result result = activityFuncService.deleteSetupActivityAd(activityMid, setupType, tipId);
		if (!result.isSuccess()) {
			ra.addFlashAttribute("errorMsg", result.getMessage());
		}
		
		return "redirect:/func/" + activityMid + "/ad";
	}
	
	@RequestMapping(value = "/{activityMid}/sch", method = RequestMethod.GET)
	public String scheduler(@PathVariable("activityMid") String activityMid, HttpServletRequest request, Model model) { 
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		
		model.addAttribute("weChatActivity", weChatActivity);
		return "func-scheduler";
	}
	

	/**
	 * 新增或者更新日程安排
	 */
	@RequestMapping(value = "/sch", method = RequestMethod.POST)
	public String addOrUpdateScheduler(String activityMid, String schedulerId, String noticeTime, String noticeContent,
			HttpServletRequest request, Model model,
			RedirectAttributes ra) {
		String errorMsg = null;
		if (StringUtils.isBlank(activityMid)) {
			//错误处理
			LOG.error("scheduler页面请求参数错误！");
			return null;
		}
		
		try {
			WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
			if (weChatActivity == null) {
				//错误处理
				LOG.error("scheduler页面请求参数错误！在系统中未发现活动MID：" + activityMid);
				return "redirect:/func/" + activityMid + "/sch";
			}
			
			Date dNoticeTime = null;
			if (StringUtils.isBlank(noticeTime)) {
				//错误处理
				LOG.error("scheduler页面请求参数错误！推送时间不能为空！");
				errorMsg = "推送时间不能为空！";
				ra.addFlashAttribute("errorMsg", errorMsg);
				return "redirect:/func/" + activityMid + "/sch";
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat(
						CommonUtils.DATE_TIME_NO_SS_FORMAT);
					dNoticeTime = sdf.parse(noticeTime);
			}
				
			
			if (StringUtils.isBlank(noticeContent)) {
				//错误处理
				LOG.error("scheduler页面请求参数错误！推送内容不能为空！");
				errorMsg = "推送内容不能为空！";
				ra.addFlashAttribute("errorMsg", errorMsg);
				return "redirect:/func/" + activityMid + "/sch";
			}
			
			Result result = activityFuncService.addOrUpdateWeActivityScheduler(weChatActivity, schedulerId, dNoticeTime, noticeContent);
			
			if (!result.isSuccess()) {
				ra.addFlashAttribute("errorMsg", result.getMessage());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(), e);
			ra.addFlashAttribute("errorMsg", "系统内部错误");
		}
		
		return "redirect:/func/" + activityMid + "/sch";
	}
	/**
	 * 删除日程安排
	 * @param activityId
	 * @param schedulerId
	 * @param request
	 * @param model
	 * @param ra
	 * @return
	 */
	@RequestMapping(value = "/del_sch")
	public String deleteScheduler(String activityMid, String schedulerId, HttpServletRequest request, Model model,
			RedirectAttributes ra) {
		if (StringUtils.isBlank(activityMid)) {
			//错误处理
			LOG.error("draw页面请求参数错误！");
			return null;
		}
		
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		if (weChatActivity == null) {
			//错误处理
			LOG.error("draw页面请求参数错误！在系统中未发现活动ID：" + activityMid);
			return "redirect:/func/" + activityMid + "/sch";
		}
		Result result = activityFuncService.deleteWeActivityScheduler(weChatActivity, Long.parseLong(schedulerId));
		if (!result.isSuccess()) {
			ra.addFlashAttribute("errorMsg", result.getMessage());
		}
		
		return "redirect:/func/" + activityMid + "/sch";
	}
	
	
	/**
	 * 返回自定义logo页面
	 */
	@RequestMapping(value = "/{activityMid}/logo", method = RequestMethod.GET)
	public String defineLogo(@PathVariable(value = "activityMid") String activityMid, HttpServletRequest request,
			Model model) {
		
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		if (weChatActivity == null) {
			//错误处理
			LOG.error("define-logo页面请求参数错误！在系统中未发现活动MID：" + activityMid);
			return null;
		}
		model.addAttribute("weChatActivity", weChatActivity);
		return "func-logo";
	}
	
	@ResponseBody
	@RequestMapping(value = "/logo", method = RequestMethod.POST)
	public Result uploadLogo(String activityMid, HttpServletRequest request) {
		Result result = new Result();
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		
		FileUpload fileUpload = new FileUpload(request);
		String relativePath = CommonUtils.generateNewDir(CommonUtils.STORE_PICTURE_DIR);
		String storePath = PathUtils.getRealPath(relativePath, request);
		
		File file = null;
		try {
			file = fileUpload.upload(storePath, FileUpload.maxImgSize, true);
			String sourceFileName = weChatActivity.getLogo();
			weChatActivity.setLogo(relativePath + File.separator + file.getName());
			if (StringUtils.isNotBlank(sourceFileName)) {
				String rootPath = request.getSession().getServletContext().getRealPath("/");
				File sourceFile = new File(rootPath + File.separator + sourceFileName);
				if (sourceFile.exists()) {
					FileUtils.forceDelete(sourceFile);
					LOG.info("delete activity[" + weChatActivity.getId() + "] logo:" + sourceFile.getAbsolutePath() + " successed!");
				}
			}
			
			activityService.save(weChatActivity);
			result.setSuccess(true);
		} catch (Exception e) {
			LOG.error("error", e);
			result.setSuccess(false);
			result.setMessage(e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 设置活动背景
	 */
	@RequestMapping(value = "/{activityMid}/background", method = RequestMethod.GET)
	public String defineBackground(@PathVariable("activityMid") String activityMid,String delId, HttpServletRequest request, Model model) { 
		if (StringUtils.isNotEmpty(delId)) {
			long id = Long.parseLong(delId);
			try {
				String rootPath = request.getSession().getServletContext().getRealPath("/");
				activityService.delImage(id, rootPath);
			} catch (Exception e) {
				LOG.error("error", e);
				model.addAttribute("errorMsg", "系统内部错误!");
			}
		}
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		List<ActivityImage> listBackGround = activityService.findBackGroundByActivityMid(activityMid);
		
		model.addAttribute("weChatActivity", weChatActivity);
		model.addAttribute("listBackGround", listBackGround);
		return "func-background";
	}
	
	@ResponseBody
	@RequestMapping(value = "/background", method = RequestMethod.POST)
	public Result uploadBackground(HttpServletRequest request, String activityMid) { 
		Result result = new Result();
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		
		if (weChatActivity.getBackgroudImgs().size() >2 ) {
			result.setSuccess(false);
			result.setMessage("背景图片的数量已达上限！");
			return result;
		}
		
		FileUpload fileUpload = new FileUpload(request);
		String relativePath = CommonUtils.generateNewDir(CommonUtils.STORE_PICTURE_DIR);
		String storePath = PathUtils.getRealPath(relativePath, request);
		
		File file = null;
		try {
			file = fileUpload.upload(storePath, FileUpload.maxImgSize, true);
			
			ActivityImage image = new ActivityImage(relativePath + File.separator + file.getName(), ActivityImage.IMAGE_TYPE.BACKGROUND.getName(), weChatActivity);
			weChatActivity.addBackgroudImg(image);
			
			activityService.save(weChatActivity);
			result.setSuccess(true);
		} catch (Exception e) {
			LOG.error("error", e);
			result.setSuccess(false);
			result.setMessage(e.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/{activityMid}/album", method = RequestMethod.GET)
	public String defineAlbum(@PathVariable("activityMid") String activityMid,String delId, HttpServletRequest request, Model model) { 
		if (StringUtils.isNotEmpty(delId)) {
			long id = Long.parseLong(delId);
			try {
				String rootPath = request.getSession().getServletContext().getRealPath("/");
				activityService.delImage(id, rootPath);
			} catch (Exception e) {
				LOG.error("error", e);
				model.addAttribute("errorMsg", "系统内部错误!");
			}
		}
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		List<ActivityImage> listAlbum = activityService.findAlbumByActivityMid(activityMid);
		
		model.addAttribute("weChatActivity", weChatActivity);
		model.addAttribute("listAlbum", listAlbum);
		return "func-album";
	}
	
	@RequestMapping(value = "/{activityMid}/photoPile", method = RequestMethod.GET)
	public String definePhotoPile(@PathVariable("activityMid") String activityMid,String delId, HttpServletRequest request, Model model) { 
		if (StringUtils.isNotEmpty(delId)) {
			long id = Long.parseLong(delId);
			try {
				String rootPath = request.getSession().getServletContext().getRealPath("/");
				activityService.delPhotoPile(id, rootPath);
			} catch (Exception e) {
				LOG.error("error", e);
				model.addAttribute("errorMsg", "系统内部错误!");
			}
		}
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		List<ActivityPhoto> listPhoto = weChatActivity.getPhotoPiles();
		
		model.addAttribute("weChatActivity", weChatActivity);
		model.addAttribute("listPhoto", listPhoto);
		return "func-photopile";
	}
	
	@ResponseBody
	@RequestMapping(value = "/album", method = RequestMethod.POST)
	public Result uploadAlbum(HttpServletRequest request, String activityMid) { 
		Result result = new Result();
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		
		if (weChatActivity.getBackgroudImgs().size() >20 ) {
			result.setSuccess(false);
			result.setMessage("背景图片的数量已达上限！");
			return result;
		}
		
		FileUpload fileUpload = new FileUpload(request);
		String relativePath = CommonUtils.generateNewDir(CommonUtils.STORE_PICTURE_DIR);
		String storePath = PathUtils.getRealPath(relativePath, request);
		
		File file = null;
		try {
			file = fileUpload.upload(storePath, FileUpload.middleImgSize, true);
			
			ActivityImage image = new ActivityImage(relativePath + File.separator + file.getName(), ActivityImage.IMAGE_TYPE.ALBUM.getName(), weChatActivity);
			weChatActivity.addBackgroudImg(image);
			
			activityService.save(weChatActivity);
			result.setSuccess(true);
		} catch (Exception e) {
			LOG.error("error", e);
			result.setSuccess(false);
			result.setMessage(e.getMessage());
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/photoPile", method = RequestMethod.POST)
	public Result uploadPhotoPile(HttpServletRequest request, String activityMid) { 
		Result result = new Result();
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		
		FileUpload fileUpload = new FileUpload(request);
		String relativePath = CommonUtils.generateNewDir(CommonUtils.STORE_PICTURE_DIR);
		String storePath = PathUtils.getRealPath(relativePath, request);
		
		File file = null;
		try {
			file = fileUpload.upload(storePath, FileUpload.middleImgSize, true);
			ActivityPhoto activityPhoto = new ActivityPhoto(null, relativePath + File.separator + file.getName(), ActivityPhoto.SYSTEM_UPLOAD, weChatActivity);
			activityService.saveActivityPhoto(activityPhoto);
			result.setSuccess(true);
		} catch (Exception e) {
			LOG.error("error", e);
			result.setSuccess(false);
			result.setMessage(e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 返回奖项设置页面
	 */
	@RequestMapping(value = "/{activityMid}/draw", method = RequestMethod.GET)
	public String draw(@PathVariable(value = "activityMid") String activityMid, HttpServletRequest request, Model model) {
		
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		if (weChatActivity == null) {
			//错误处理
			LOG.error("draw页面请求参数错误！在系统中未发现活动MID：" + activityMid);
			return null;
		}
		
		model.addAttribute("weChatActivity", weChatActivity);
		return "func-draw";
	}
	
	
	/**
	 * 添加奖项
	 */
	@RequestMapping(value = "/draw", method = RequestMethod.POST)
	public String addDraw(String activityMid, String prizeId, String prizeName, String description,
			String img, String winNum, HttpServletRequest request, Model model,
			RedirectAttributes ra) {
		String errorMsg = null;
		if (StringUtils.isBlank(activityMid)) {
			//错误处理
			LOG.error("draw页面请求参数错误！");
//			return "redirect:/activity/draw";
			return null;
		}
		
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		if (weChatActivity == null) {
			//错误处理
			LOG.error("draw页面请求参数错误！在系统中未发现活动MID：" + activityMid);
			return "redirect:/func/" + activityMid + "/draw";
		}
		
		if (StringUtils.isBlank(prizeName)) {
			//错误处理
			LOG.error("draw页面请求参数错误！奖项名称不能为空！");
			errorMsg = "奖项名称不能为空！";
			ra.addFlashAttribute("errorMsg", errorMsg);
			return "redirect:/func/" + activityMid + "/draw";
		}
		
		if (StringUtils.isBlank(description)) {
			//错误处理
			LOG.error("draw页面请求参数错误！奖品名称不能为空！");
			errorMsg = "奖品名称不能为空！";
			ra.addFlashAttribute("errorMsg", errorMsg);
			return "redirect:/func/" + activityMid + "/draw";
		}
		
		if (StringUtils.isBlank(winNum)) {
			//错误处理
			LOG.error("draw页面请求参数错误！中奖人数不能为空！");
			errorMsg = "中奖人数不能为空！";
			ra.addFlashAttribute("errorMsg", errorMsg);
			return "redirect:/func/" + activityMid + "/draw";
		}
		
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		Result result = activityFuncService.addOrUpdateActivityPrize(weChatActivity, prizeId, prizeName,
				description, img, Integer.parseInt(winNum), rootPath);
		
		if (!result.isSuccess()) {
			ra.addFlashAttribute("errorMsg", result.getMessage());
		}
		
		return "redirect:/func/" + activityMid + "/draw";
	}
	
	/**
	 * 删除奖项
	 */
	@RequestMapping(value = "/delete_draw")
	public String deleteDraw(String activityMid, String prizeId, HttpServletRequest request, Model model,
			RedirectAttributes ra) {
		if (StringUtils.isBlank(activityMid)) {
			//错误处理
			LOG.error("draw页面请求参数错误！");
//			return "redirect:/activity/draw";
			return null;
		}
		
		WeChatActivity weChatActivity = activityService.findByActivityMid(activityMid);
		if (weChatActivity == null) {
			//错误处理
			LOG.error("draw页面请求参数错误！在系统中未发现活动ID：" + activityMid);
			return "redirect:/func/" + activityMid + "/draw";
		}
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		Result result = activityFuncService.deleteActivityPrize(weChatActivity, Long.parseLong(prizeId), rootPath);
		if (!result.isSuccess()) {
			ra.addFlashAttribute("errorMsg", result.getMessage());
		}
		
		return "redirect:/func/" + activityMid + "/draw";
	}

}
