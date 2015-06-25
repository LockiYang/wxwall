package com.wxwall.modules.wechat.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wxwall.common.entity.BaseEntity;

@Entity
@Table(name = "t_wechat_app_key_word_auto_reply")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WeChatAppKeyWordAutoReply extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	private String keyWord;
	private AutoReply autoReply;
	
	private WeChatAppAutoReply weChatAppAutoReply;
	
	
	public WeChatAppKeyWordAutoReply() {
	}
	
	public WeChatAppKeyWordAutoReply(String keyWord, AutoReply autoReply, WeChatAppAutoReply weChatAppAutoReply) {
		this.keyWord = keyWord;
		this.autoReply = autoReply;
		this.weChatAppAutoReply = weChatAppAutoReply;
	}
	
	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "wechat_auto_reply_id")	
	public WeChatAppAutoReply getWeChatAppAutoReply() {
		return weChatAppAutoReply;
	}

	public void setWeChatAppAutoReply(WeChatAppAutoReply weChatAppAutoReply) {
		this.weChatAppAutoReply = weChatAppAutoReply;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "auto_reply_id")
	public AutoReply getAutoReply() {
		return autoReply;
	}

	public void setAutoReply(AutoReply autoReply) {
		this.autoReply = autoReply;
	}
	
}
