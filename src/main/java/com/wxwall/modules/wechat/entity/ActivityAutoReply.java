package com.wxwall.modules.wechat.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wxwall.common.entity.BaseEntity;

@Entity
@Table(name = "t_activity_auto_reply")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ActivityAutoReply extends BaseEntity {
	private static final long serialVersionUID = 1L;
	public static final String DEF_SIGN_IMG = "statics/images/qr-code.jpg";
	public static final String DEF_SIGN_CONTENT = "签到成功，欢迎参加我们的活动";//默认签到成功返回消息
	public static final String DEF_UPBANG_CONTENT = "发送消息上墙成功！";//默认上墙成功返回消息
	
	private AutoReply siginAutoReply;
	private AutoReply upbangAutoReply;
	
	private WeChatActivity weChatActivity;
	
	public ActivityAutoReply() {
	}
	
	public ActivityAutoReply(AutoReply siginAutoReply, AutoReply upbangAutoReply, WeChatActivity weChatActivity) {
		this.siginAutoReply = siginAutoReply;
		this.upbangAutoReply = upbangAutoReply;
		this.weChatActivity = weChatActivity;
	}
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "sigin_auto_reply_id")
	public AutoReply getSiginAutoReply() {
		return siginAutoReply;
	}

	public void setSiginAutoReply(AutoReply siginAutoReply) {
		this.siginAutoReply = siginAutoReply;
	}
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "upbang_auto_reply_id")
	public AutoReply getUpbangAutoReply() {
		return upbangAutoReply;
	}

	public void setUpbangAutoReply(AutoReply upbangAutoReply) {
		this.upbangAutoReply = upbangAutoReply;
	}
	
	@OneToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "wechat_activity_id")
	public WeChatActivity getWeChatActivity() {
		return weChatActivity;
	}

	public void setWeChatActivity(WeChatActivity weChatActivity) {
		this.weChatActivity = weChatActivity;
	}
}
