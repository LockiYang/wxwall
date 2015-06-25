package com.wxwall.common.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.wxwall.modules.user.entity.UserLog;
import com.wxwall.modules.user.service.UserLogService;

import eu.bitwalker.useragentutils.UserAgent;

public abstract class LogController extends BaseController {
	
	@Autowired
	private UserLogService logService;

	public UserLog getBaseLog(String type, HttpServletRequest request) {

		String userAgentString = request.getHeader("User-Agent");
		UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

		UserLog userLog = new UserLog(type, this.getClientIp(request),
				userAgentString, userAgent.getBrowser() + "-"
						+ userAgent.getBrowserVersion(), userAgent
						.getOperatingSystem().getName());

		return userLog;
	}

	/**
	 * 获得客户端的IP
	 * 
	 * @param request
	 * @return
	 */
	public String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}

	/**
	 * 获得客户端浏览器
	 * 
	 * @param request
	 * @return
	 */
	public String getBrowser(HttpServletRequest request) {
		UserAgent userAgent = UserAgent.parseUserAgentString(request
				.getHeader("User-Agent"));
		return userAgent.getBrowser() + "-" + userAgent.getBrowserVersion();
	}

	/**
	 * 获取客户端操作系统信息
	 * 
	 * @param request
	 * @return
	 */
	public String getOperatingSystem(HttpServletRequest request) {
		UserAgent userAgent = UserAgent.parseUserAgentString(request
				.getHeader("User-Agent"));
		return userAgent.getOperatingSystem().getName();
	}
}
