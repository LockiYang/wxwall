package com.wxwall.wechat.api.util;

import org.apache.log4j.Logger;

import com.wxwall.modules.wechat.entity.WeChatApp;
import com.wxwall.wechat.api.advanced.util.CreateQRCode;
import com.wxwall.wechat.api.advanced.util.GetUserList;

public class WxAuthChecker {
	private static Logger LOG = Logger.getLogger(WxAuthChecker.class);
	/**
	 * 检查公众号是否认证
	 * @param accessToken
	 * @return
	 */
	public static boolean isAuthChecker(String accessToken) {
		try {
			GetUserList.getUserList(accessToken, "");
			return true;
		} catch (WxException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage() + ";error code:" + e.getErrorCode() + "\terror msg:" + e.getErrorMsg(), e);
			return false;
		}
	}
	/**
	 * 检查公众号是否是服务号
	 * @param accessToken
	 * @return
	 */
	public static boolean isServiceChecker(String accessToken) {
		try {
			CreateQRCode.createTemporaryQRCode(accessToken, 1800, 1);
			return true;
		} catch (WxException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage() + ";error code:" + e.getErrorCode() + "\terror msg:" + e.getErrorMsg(), e);
			return false;
		}
	}
	/**
	 * 返回角色
	 * @param accessToken
	 * @return
	 */
	public static byte getAuthRole(String accessToken) {
		boolean isAuth = WxAuthChecker.isAuthChecker(accessToken);
		if (!isAuth) {
			return WeChatApp.ACCOUNT_TYPE.UNAUTH.getIndex();
		} else {
			boolean isService = WxAuthChecker.isServiceChecker(accessToken);
			if (!isService) {
				return WeChatApp.ACCOUNT_TYPE.SUBSCRIBER.getIndex();
			} else {
				return WeChatApp.ACCOUNT_TYPE.SERVICER.getIndex();
			}
		}
	}
	
	public static void main(String[] args) {
		String accessToken=CommonUtil.getAccessToken("wxa15b5a3c0cc56dd3", "abe1c14cd5bf04719ace3c3197f06068").getAccesstoken();
		boolean isAuth = isAuthChecker(accessToken);
		System.out.println(isAuth);
		boolean isService = isServiceChecker(accessToken);
		System.out.println(isService);
	}
}
