package com.wxwall.modules.wechat.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wxwall.common.entity.BaseEntity;

@Entity
@Table(name = "t_auto_reply")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AutoReply extends BaseEntity {
	private static final long serialVersionUID = 1L;
	public static enum REPLY_TYPE {
		TEXT("text", (byte)1), IMG_TEXT("image_text", (byte)2);

		private byte index;
		private String name;

		public String getName() {
			return name;
		}

		private REPLY_TYPE(String name, byte index) {
			this.name = name;
			this.index = index;
		}

		public byte getIndex() {
			return index;
		}
	};
	
	private byte replyType;// 回复类型 1：文本 2：图文
	private WeChatImageText weChatImageText;
	private WeChatText weChatText;
	
	public AutoReply() {
	}

	public AutoReply(byte replyType, WeChatImageText weChatImageText, WeChatText weChatText) {
		this.replyType = replyType;
		this.weChatImageText = weChatImageText;
		this.weChatText = weChatText;
	}
	
	public byte getReplyType() {
		return replyType;
	}

	public void setReplyType(byte replyType) {
		this.replyType = replyType;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "image_text_id", nullable = true)
	public WeChatImageText getWeChatImageText() {
		return weChatImageText;
	}

	public void setWeChatImageText(WeChatImageText weChatImageText) {
		this.weChatImageText = weChatImageText;
	}
	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "text_id", nullable = true)
	public WeChatText getWeChatText() {
		return weChatText;
	}

	public void setWeChatText(WeChatText weChatText) {
		this.weChatText = weChatText;
	}
}
