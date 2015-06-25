package com.wxwall.common.entity;

import java.util.Date;

import com.wxwall.common.emoji.EmojiUtils;
import com.wxwall.common.utils.CommonUtils;
import com.wxwall.common.utils.FaceUtils;
import com.wxwall.modules.wechat.entity.WeChatUser;
import com.wxwall.modules.wechat.entity.WeChatUserMsg;

/**
 * 上墙消息封装
 * 
 * @author ganjun
 * 
 */
public class UpbangMsg {
	
	private long msgId;
	private String nick;// 上墙用户名
	private String avatar; // 头像
	private String messageText; // 上墙消息
	private String messagePhoto;// 上墙图片
	private byte status;
	private Date updateTime;
	
	public UpbangMsg() {
	}
	
	public UpbangMsg(WeChatUserMsg weChatUserMsg, WeChatUser weChatUser, String serverUrl) {
		this.msgId = weChatUserMsg.getId();
		this.nick = weChatUser.getNickName();
		this.avatar = weChatUser.getHeadImgUrl();
		this.messageText = FaceUtils.converQqFace(weChatUserMsg.getContent());
		this.messageText = EmojiUtils.getInstance().emojiConvertImgHtml(this.messageText, serverUrl);
		this.messagePhoto = weChatUserMsg.getImgUrl();
		this.updateTime = weChatUserMsg.getUpdateTime();
		this.status = weChatUserMsg.getStatus();
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

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getMessagePhoto() {
		return messagePhoto;
	}

	public void setMessagePhoto(String messagePhoto) {
		this.messagePhoto = messagePhoto;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
