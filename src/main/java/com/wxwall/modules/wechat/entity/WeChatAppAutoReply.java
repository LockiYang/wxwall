package com.wxwall.modules.wechat.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wxwall.common.entity.BaseEntity;

@Entity
@Table(name = "t_wechat_app_auto_reply")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WeChatAppAutoReply extends BaseEntity {
	private static final long serialVersionUID = 1L;
	public static final String DEF_SIGN_IMG = "statics/images/qr-code.jpg";
	public static final String DEF_SIGN_CONTENT = "签到成功，欢迎参加我们的活动";//默认签到成功返回消息
	public static final String DEF_UPBANG_CONTENT = "发送消息上墙成功！";//默认上墙成功返回消息
	
	private AutoReply subscribeAutoReply;
	private AutoReply autoReply;
	private List<WeChatAppKeyWordAutoReply> keyWordAutoReplys;
	
	private WeChatApp weChatApp;
	
	public WeChatAppAutoReply() {
	}
	
	public WeChatAppAutoReply(AutoReply subscribeAutoReply, AutoReply autoReply, WeChatApp weChatApp) {
		this.subscribeAutoReply = subscribeAutoReply;
		this.autoReply = autoReply;
		this.weChatApp = weChatApp;
	}
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "subscribe_auto_reply_id")
	public AutoReply getSubscribeAutoReply() {
		return subscribeAutoReply;
	}

	public void setSubscribeAutoReply(AutoReply subscribeAutoReply) {
		this.subscribeAutoReply = subscribeAutoReply;
	}
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "auto_reply_id")
	public AutoReply getAutoReply() {
		return autoReply;
	}

	public void setAutoReply(AutoReply autoReply) {
		this.autoReply = autoReply;
	}
	
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="weChatAppAutoReply")
	public List<WeChatAppKeyWordAutoReply> getKeyWordAutoReplys() {
		return keyWordAutoReplys;
	}

	public void setKeyWordAutoReplys(
			List<WeChatAppKeyWordAutoReply> keyWordAutoReplys) {
		this.keyWordAutoReplys = keyWordAutoReplys;
	}
	
	@OneToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "wechat_app_id")
	public WeChatApp getWeChatApp() {
		return weChatApp;
	}

	public void setWeChatApp(WeChatApp weChatApp) {
		this.weChatApp = weChatApp;
	}
}
