package com.wxwall.modules.wechat.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
@Table(name = "t_wechat_activity_tips")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WeActivityTips extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	private String tipContent;// 文本内容
	private Date createTime;

	private WeChatActivity weChatActivity;

	public WeActivityTips() {
		
	}

	public WeActivityTips(String tipContent, WeChatActivity weChatActivity) {
		this.tipContent = tipContent;
		this.createTime = Calendar.getInstance(TimeZone.getDefault()).getTime();
		this.weChatActivity = weChatActivity;
	}
	
	public String getTipContent() {
		return tipContent;
	}

	public void setTipContent(String tipContent) {
		this.tipContent = tipContent;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "wechat_activity_id")
	public WeChatActivity getWeChatActivity() {
		return weChatActivity;
	}

	public void setWeChatActivity(WeChatActivity weChatActivity) {
		this.weChatActivity = weChatActivity;
	}
	
}
