package com.wxwall.modules.user.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wxwall.common.entity.IdEntity;

/**
 * 记录用户操作日志
 * 
 * @author Locki
 * @date 2015年1月11日
 */
@Entity
@Table(name = "t_user_log")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserLog extends IdEntity {

	private static final long serialVersionUID = 1L;
	public static final String TYPE_REGISTER = "register";
	public static final String TYPE_LOGIN = "login";
	public static final String TYPE_ERROR = "error";

	private String type; // 日志类型（1:注册日志；2：登录日志；3：错误日志）
	private String remoteAddr; // 操作用户的IP地址
	private String userAgent; // 操作用户代理信息
	private String browser; // 操作用户浏览器
	private String operatingSystem; // 操作用户操作系统

	private String requestUri; // 操作的URI
	private String method; // 操作的方式
	private String params; // 操作提交的数据
	private String exception; // 异常信息

	public UserLog(String type, User user, String remoteAddr, String userAgent,
			String browser, String operatingSystem) {
		this(type, remoteAddr, userAgent, browser, operatingSystem);
		//this.createBy = user;
	}
	
	public UserLog(){}

	public UserLog(String type, String remoteAddr, String userAgent,
			String browser, String operatingSystem) {
		this.type = type;
		this.createDate = new Date();
		this.remoteAddr = remoteAddr;
		this.userAgent = userAgent;
		this.browser = browser;
		this.operatingSystem = operatingSystem;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
}
