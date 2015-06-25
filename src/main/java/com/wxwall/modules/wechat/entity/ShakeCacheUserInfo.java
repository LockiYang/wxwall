package com.wxwall.modules.wechat.entity;

public class ShakeCacheUserInfo {
	private long id;
	private String nick;
	private String avatar;
	private int shakeNum;
	private int index;
	private boolean loadUserInfo;
	
	public ShakeCacheUserInfo() {
		this.id = -1;
		this.nick = null;
		this.avatar = null;
		this.shakeNum = -1;
		this.index = -1;
		this.loadUserInfo = false;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getShakeNum() {
		return shakeNum;
	}
	public void setShakeNum(int shakeNum) {
		this.shakeNum = shakeNum;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public boolean isLoadUserInfo() {
		return loadUserInfo;
	}
	public void setLoadUserInfo(boolean loadUserInfo) {
		this.loadUserInfo = loadUserInfo;
	}
}
