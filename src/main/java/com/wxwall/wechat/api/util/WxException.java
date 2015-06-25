package com.wxwall.wechat.api.util;

/**
 * 微信异常处理类
 * @author ganjun
 *
 */
public class WxException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//public static final int 
	private int errorCode;
	private String errorMsg;
	
	public WxException(int errorCode, String errorMsg, String errorTips) {
		super(errorTips);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
