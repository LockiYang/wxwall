package com.wxwall.modules.activity.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wxwall.common.entity.IdEntity;
import com.wxwall.modules.wechat.entity.WeChatActivity;
/**
 * 
 * <p>
 * 描述:微信新郎新娘日程提醒
 * </p>
 *
 * @see
 * @author ganjun
 *
 */
@Entity
@Table(name = "t_activity_user_remind")
@DynamicInsert
@DynamicUpdate
// 只操作修改过的字段
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ActivityUserRemind extends IdEntity {
	private static final long serialVersionUID = 1L;

	private WeChatActivity weChatActivity;
	private Date remindTime;
	private String content;
	// 功能开关
	@Column(nullable = false, columnDefinition = "bit(1) default 0")
	private boolean status;
	
	public ActivityUserRemind() {
		
	}

	public ActivityUserRemind(WeChatActivity weChatActivity, Date remindTime, String content, boolean status) {
		this.weChatActivity = weChatActivity;
		this.remindTime = remindTime;
		this.content = content;
		this.status = status;
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "wechat_activity_id")
	public WeChatActivity getWeChatActivity() {
		return weChatActivity;
	}

	public void setWeChatActivity(WeChatActivity weChatActivity) {
		this.weChatActivity = weChatActivity;
	}

	public Date getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
