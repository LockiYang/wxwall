package com.wxwall.modules.user.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wxwall.common.web.LogController;
import com.wxwall.common.web.validator.VlidatorGroup.CreateGroup;
import com.wxwall.modules.user.entity.User;
import com.wxwall.modules.user.service.UserInvitationCodeService;
import com.wxwall.modules.user.service.UserService;

@Controller
@RequestMapping(value = "/register")
public class RegisterController extends LogController {

	@Autowired private UserService userService;
	
	@Autowired private UserInvitationCodeService userInvitationCodeService;

	@RequestMapping(method = RequestMethod.GET)
	public String registerForm() {
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.isAuthenticated()) {
			return "redirect:/activity/list";
		}
		return "indexs/register";
	}

	/**
	 * @Validated @Valid区别： @Validated支持groups
	 * RedirectAttributes和Model
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String register(
			@Validated(CreateGroup.class)@ModelAttribute("user") User user,
			@RequestParam(required=true) String invitationCode,
			RedirectAttributes ra,Model model, Errors errors,
			HttpServletRequest request) {
		
		// 绑定错误处理
		if (errors.hasErrors()) {
			
		}
		
		try {
//			UserLog userLog = this.getBaseLog(UserLog.TYPE_REGISTER, request);
			userService.register(user, invitationCode);
			// 登录
			UsernamePasswordToken token = new UsernamePasswordToken();
			token.setUsername(user.getLoginEmail());
			token.setPassword(user.getPlainPassword().toCharArray());
//			token.setRememberMe(true);
			SecurityUtils.getSubject().login(token);
			return "redirect:/activity/list";
		} catch (Exception e) {
			ra.addFlashAttribute("errorMsg", e.getMessage());
			ra.addFlashAttribute("user", user);
			return "redirect:/register";
			
			// redirectAttributes.addFlashAttribute("username", user.getLoginMail()); //对象重定向传参
			// return "redirect:/login"; //防止刷新重复提交
		}
	}

	/**
	 * Ajax请求校验username是否唯一。
	 */
	@RequestMapping(value = "check_login_email")
	@ResponseBody
	public String checkLoginName(
			@RequestParam(value="loginEmail", required=true) String loginEmail) {
		if (userService.get(loginEmail) == null) {
			return "true";
		} else {
			return "false";
		}
	}
}
