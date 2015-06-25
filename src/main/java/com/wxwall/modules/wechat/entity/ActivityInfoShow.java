package com.wxwall.modules.wechat.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wxwall.common.entity.BaseEntity;

/**
 * 活动页面头部展示信息
 * 
 * @author locki
 * @date 2015年2月5日
 *
 */
@Entity
@Table(name = "r_activity_info_show")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ActivityInfoShow extends BaseEntity {

	private static final long serialVersionUID = 1L;


	private WeChatActivity weChatActivity;
	private String info; 
	
	public ActivityInfoShow() {
		
	}
	
	public ActivityInfoShow(WeChatActivity weChatActivity, String info) {
		this.weChatActivity = weChatActivity;
		this.info = info;
	}

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "wechat_activity_id")
	public WeChatActivity getWeChatActivity() {
		return weChatActivity;
	}

	public void setWeChatActivity(WeChatActivity weChatActivity) {
		this.weChatActivity = weChatActivity;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
