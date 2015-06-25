package com.wxwall.wechat.api.simulation;

public class WxMessage {

	private int id;
	private int type; // 1：文本或表情，2：图片
	private int date_time;
	private String content;
	private long fakeid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDate_time() {
		return date_time;
	}

	public void setDate_time(int date_time) {
		this.date_time = date_time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getFakeid() {
		return fakeid;
	}

	public void setFakeid(long fakeid) {
		this.fakeid = fakeid;
	}

}
