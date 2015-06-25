package com.wxwall.modules.user.utils;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;

import com.google.common.collect.Maps;
import com.wxwall.common.utils.SpringContextHolder;
import com.wxwall.modules.user.entity.User;
import com.wxwall.modules.user.service.ShiroDbRealm.ShiroPrincipal;
import com.wxwall.modules.user.service.UserService;

/**
 * 用户工具类
 * 
 * @author Locki
 * @date 2015年1月14日
 * 
 */
public class UserUtils {

	private static UserService userService = SpringContextHolder
			.getBean(UserService.class);

	public static final String CACHE_USER = "user";

//	private static boolean isTestMode = false;
//
//	public static void initTest() {
//		isTestMode = true;
//	}
	
	/**
	 * 判断当前用户是否有权限
	 * 
	 * @param role
	 * @return
	 */
	public static boolean hasRole(String role) {
		if (StringUtils.isNotBlank(role)) {
			Subject subject = SecurityUtils.getSubject();
			if (subject.hasRole(role)) {
				return true;
			}
		} 
		return false;
	}

	/**
	 * 取当前登录用户
	 * 
	 * @return
	 */
	public static User getUser() {

		// 测试模式
//		if (isTestMode) {
//			User user = userService.get("test@qq.com");
//			return user;
//		}

		/**
		 * User user = (User) getCache(CACHE_USER); if (user == null) { try {
		 * Subject subject = SecurityUtils.getSubject(); ShiroPrincipal
		 * principal = (ShiroPrincipal) subject.getPrincipal(); if (principal !=
		 * null) { user = userService.get(principal.getId());
		 * putCache(CACHE_USER, user); } } catch
		 * (UnavailableSecurityManagerException e) {
		 * 
		 * } catch (InvalidSessionException e) {
		 * 
		 * } } if (user == null) { user = new User(); try {
		 * SecurityUtils.getSubject().logout(); } catch
		 * (UnavailableSecurityManagerException e) {
		 * 
		 * } catch (InvalidSessionException e) {
		 * 
		 * } } return user;
		 **/
		User user = null;
		try {
			Subject subject = SecurityUtils.getSubject();
			ShiroPrincipal principal = (ShiroPrincipal) subject.getPrincipal();
			if (principal != null) {
				user = userService.get(principal.getId());
			}
		} catch (UnavailableSecurityManagerException e) {

		} catch (InvalidSessionException e) {

		}
		
		return user;
	}

	// ============== User Cache ============== //
	public static Object getCache(String key) {
		return getCache(key, null);
	}

	public static Object getCache(String key, Object defaultValue) {
		Object obj = getUserCache().get(key);
		return obj == null ? defaultValue : obj;
	}

	public static void putCache(String key, Object value) {
		getUserCache().put(key, value);
	}

	public static void removeCache(String key) {
		getUserCache().remove(key);
	}

	private static Map<String, Object> getUserCache() {
		Map<String, Object> map = Maps.newHashMap();
		try {
			Subject subject = SecurityUtils.getSubject();
			ShiroPrincipal principal = (ShiroPrincipal) subject.getPrincipal();
			return principal != null ? principal.getCache() : map;
		} catch (UnavailableSecurityManagerException e) {

		} catch (InvalidSessionException e) {

		}
		return map;
	}

}
