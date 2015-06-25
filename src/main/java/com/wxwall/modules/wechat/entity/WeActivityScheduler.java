package com.wxwall.modules.wechat.entity;

import java.util.Date;

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
@Table(name = "t_wechat_activity_scheduler")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WeActivityScheduler extends BaseEntity {
	private static final long serialVersionUID = 1L;

	public static enum STATUS {
		CHECKING("checking", (byte)1), SUCCESSED("successed", (byte)2),
		FAILED("failed", (byte)3);

		private byte index;
		private String name;

		public String getName() {
			return name;
		}

		private STATUS(String name, byte index) {
			this.name = name;
			this.index = index;
		}

		public byte getIndex() {
			return index;
		}
	};

	private byte status;// 回复类型 1：文本 2：图文
	private Date noticeTime;// 标题
	private String noticeContent;// 图片地址
	private String failedMsg;// 发送错误消息

	private WeChatActivity weChatActivity;

	public WeActivityScheduler() {

	}

	public WeActivityScheduler(Date noticeTime, String noticeContent,
			byte status, String failedMsg, WeChatActivity weChatActivity) {
		this.noticeTime = noticeTime;
		this.noticeContent = noticeContent;
		this.status = status;
		this.failedMsg = failedMsg;
		this.weChatActivity = weChatActivity;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "wechat_activity_id")
	public WeChatActivity getWeChatActivity() {
		return weChatActivity;
	}

	public void setWeChatActivity(WeChatActivity weChatActivity) {
		this.weChatActivity = weChatActivity;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Date getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(Date noticeTime) {
		this.noticeTime = noticeTime;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public String getFailedMsg() {
		return failedMsg;
	}

	public void setFailedMsg(String failedMsg) {
		this.failedMsg = failedMsg;
	}
	
}
