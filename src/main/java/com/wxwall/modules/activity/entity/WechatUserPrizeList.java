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
/**
 * 
 * <p>
 * 描述:微信用户抽奖名单
 * </p>
 *
 * @see
 * @author ganjun
 *
 */
@Entity
@Table(name = "t_wechat_user_prize_list")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WechatUserPrizeList extends IdEntity {
	private static final long serialVersionUID = 1L;

	private ActivityPrize activityPrize;
	private RActivityWeChatUser rActivityWeChatUser;
	
	public WechatUserPrizeList() {
		
	}

	public WechatUserPrizeList(ActivityPrize activityPrize, RActivityWeChatUser rActivityWeChatUser) {
		this.activityPrize = activityPrize;
		this.rActivityWeChatUser = rActivityWeChatUser;
	}

	@ManyToOne(cascade={CascadeType.REFRESH,CascadeType.MERGE},fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="activity_prize_id")
	public ActivityPrize getActivityPrize() {
		return activityPrize;
	}

	public void setActivityPrize(ActivityPrize activityPrize) {
		this.activityPrize = activityPrize;
	}
	
	@ManyToOne(cascade={CascadeType.REFRESH,CascadeType.MERGE},fetch=FetchType.EAGER,optional=false)
	public RActivityWeChatUser getrActivityWeChatUser() {
		return rActivityWeChatUser;
	}

	public void setrActivityWeChatUser(RActivityWeChatUser rActivityWeChatUser) {
		this.rActivityWeChatUser = rActivityWeChatUser;
	}
}
