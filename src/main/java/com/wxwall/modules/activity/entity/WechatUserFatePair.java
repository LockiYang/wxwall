package com.wxwall.modules.activity.entity;

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

import com.wxwall.common.entity.IdEntity;
import com.wxwall.modules.wechat.entity.RActivityWeChatUser;
import com.wxwall.modules.wechat.entity.WeChatActivity;
/**
 * 
 * <p>
 * 描述:微信用户对对碰名单
 * </p>
 *
 * @see
 * @author ganjun
 *
 */
@Entity
@Table(name = "t_wechat_user_fate_pair")
@DynamicInsert
@DynamicUpdate
// 只操作修改过的字段
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WechatUserFatePair extends IdEntity {
	private static final long serialVersionUID = 1L;

	private WeChatActivity weChatActivity;
	private RActivityWeChatUser manRActivityWeChatUser;
	private RActivityWeChatUser womanRActivityWeChatUser;
	
	public WechatUserFatePair() {
		
	}

	public WechatUserFatePair(WeChatActivity weChatActivity, RActivityWeChatUser manRActivityWeChatUser, RActivityWeChatUser womanRActivityWeChatUser) {
		this.weChatActivity = weChatActivity;
		this.manRActivityWeChatUser = manRActivityWeChatUser;
		this.womanRActivityWeChatUser = womanRActivityWeChatUser;
	}
	
	@ManyToOne(cascade={CascadeType.REFRESH,CascadeType.MERGE},fetch=FetchType.LAZY,optional=false)
	@JoinColumn(name = "r_activity_user_man_id")
	public RActivityWeChatUser getManRActivityWeChatUser() {
		return manRActivityWeChatUser;
	}

	public void setManRActivityWeChatUser(RActivityWeChatUser manRActivityWeChatUser) {
		this.manRActivityWeChatUser = manRActivityWeChatUser;
	}
	
	@ManyToOne(cascade={CascadeType.REFRESH,CascadeType.MERGE},fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name = "r_activity_user_woman_id")
	public RActivityWeChatUser getWomanRActivityWeChatUser() {
		return womanRActivityWeChatUser;
	}

	public void setWomanRActivityWeChatUser(
			RActivityWeChatUser womanRActivityWeChatUser) {
		this.womanRActivityWeChatUser = womanRActivityWeChatUser;
	}

	@ManyToOne(cascade={CascadeType.REFRESH,CascadeType.MERGE},fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name = "wechat_activity_id")
	public WeChatActivity getWeChatActivity() {
		return weChatActivity;
	}

	public void setWeChatActivity(WeChatActivity weChatActivity) {
		this.weChatActivity = weChatActivity;
	}
}
