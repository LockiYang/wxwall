package com.wxwall.common.entity;

import java.util.Date;

import com.wxwall.modules.wechat.entity.WeChatUser;

/**
 * 活动用户封装
 * 
 * @author ganjun
 * 
 */
public class ActivityUser {
	
	private long userId;
	private String nick;// 上墙用户名
	private String avatar; // 头像
    private String city;// 微信用户城市
    private String province;// 微信用户省份
    private String country;// 微信用户国家
    private Byte sex; // 微信用户性别
	private Date updateTime;
	
	public ActivityUser() {
	}
	
	public ActivityUser(WeChatUser weChatUser, Date updateTime) {
		this.userId = weChatUser.getId();
		this.nick = weChatUser.getNickName();
		this.avatar = weChatUser.getHeadImgUrl();
		this.sex = weChatUser.getSex();
		this.country = weChatUser.getCountry();
		this.province = weChatUser.getProvince();
		this.city = weChatUser.getCity();
		this.updateTime = updateTime;
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
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Byte getSex() {
		return sex;
	}

	public void setSex(Byte sex) {
		this.sex = sex;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
