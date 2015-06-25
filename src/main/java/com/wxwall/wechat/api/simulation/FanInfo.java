package com.wxwall.wechat.api.simulation;


public class FanInfo {

	//{"fake_id":671570718,"nick_name":"杨明","user_name":"","signature":"左手诗歌，右手代码~","city":"深圳","province":"广东","country":"中国","gender":1,"remark_name":"","group_id":0}
	private String fake_id;
	private String nick_name;
	private String user_name;
	private String signature;
	private String country;
	private String province;
	private String city;
	private String gender;
	private String remark_name;
	
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getRemark_name() {
		return remark_name;
	}
	public void setRemark_name(String remark_name) {
		this.remark_name = remark_name;
	}
	public String getFake_id() {
		return fake_id;
	}
	public void setFake_id(String fake_id) {
		this.fake_id = fake_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
}
