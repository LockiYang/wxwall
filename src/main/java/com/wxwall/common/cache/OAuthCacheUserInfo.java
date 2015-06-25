package com.wxwall.common.cache;

import com.wxwall.wechat.api.advanced.model.SNSUserInfo;

public class OAuthCacheUserInfo {
	private String code;//网页授权时获得的token
	private String accessToken;//网页授权时获得的accessToken
	private String openId;//网页授权时获得的第三方openId
	private String refreshToken;//网页授权时获得的刷新refreshToken
	private SNSUserInfo snsUserInfo;//网页授权时获得的用户信息
	
	public OAuthCacheUserInfo(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public SNSUserInfo getSnsUserInfo() {
		return snsUserInfo;
	}

	public void setSnsUserInfo(SNSUserInfo snsUserInfo) {
		this.snsUserInfo = snsUserInfo;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
