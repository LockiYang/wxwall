package com.wxwall.common.utils;

import java.io.File;

import javax.servlet.http.HttpServletRequest;


public class PathUtils {

	public static String getWebRootRealPath(HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath("/");
	}
	
	public static String getRealPath(String relativePath, HttpServletRequest request) {
		
		return getWebRootRealPath(request) + File.separator + relativePath;
	}
	
	public static String getUrl(String relativePath, HttpServletRequest request) {
		return getWebRootUrl(request) + "/" + relativePath;
	}
	
	public static String getWebRootUrl(HttpServletRequest request) {
		String serverUrl = request.getScheme() + "://" + request.getServerName() + 
				((request.getServerPort() != 80) ? (":"+ 
					request.getServerPort()) : "") + request.getContextPath();
		return serverUrl;
	}
}
