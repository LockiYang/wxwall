package com.wxwall.common.service;

/**
 * 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚
 * 
 * @author locki
 * @date 2015年2月13日
 *
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -6569114790354409540L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
