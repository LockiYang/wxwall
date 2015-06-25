package com.wxwall.modules.user.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.wxwall.common.web.BaseController;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * 
 * 真正登录的POST请求由Filter完成,
 * 
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController extends BaseController{

	@RequestMapping(method = RequestMethod.GET)
	public String login() {
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.isAuthenticated()) {
			return "redirect:/activity/list";
		}
		return "indexs/login";
	}

//	@ResponseBody
//	@RequestMapping(method = RequestMethod.POST)
//	public RestResponseBody ajaxLogin(String username, String password, boolean rememberMe) {
//		RestResponseBody resp = new RestResponseBody();
//		
//		Subject currentUser = SecurityUtils.getSubject();
//		if (!currentUser.isAuthenticated()) {
//			UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
//			try{
//	            currentUser.login(token);
//	            resp.setRe_code(0);
//	            resp.setRedirect_url("/");
//	        }catch(UnknownAccountException ex){
//	        	resp.setRe_code(-2);
//	            LOG.info("账号错误");
//	        }catch(IncorrectCredentialsException ex){
//	        	resp.setRe_code(-3);
//	            LOG.info("密码错误");
//	        }catch(LockedAccountException ex){
//	        	resp.setRe_code(-4);
//	            LOG.info("账号已被锁定，请与系统管理员联系");
//	        }catch(AuthenticationException ex){
//	        	resp.setRe_code(-5);
//	            LOG.info("您没有授权!");
//	        }
//		} else {
//			resp.setRe_code(0);
//            resp.setRedirect_url("/");
//		}
//		
//		return resp;
//	}
	
	/**
	 * 请求先经过authc(FormAuthenticationFilter)过滤器
	 * authc可以配置登录成功跳转链接，就不会进这个方法
	 * 登录之后应该跳转到登录之前的页面,FormAuthenticationFilter默认能处理
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model, HttpServletRequest request) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
		String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		if (StringUtils.isNotBlank(error)) {
			model.addAttribute("errorMsg", "该帐号不存在或密码错误!");
		}
		
		return "indexs/login";
	}
}
