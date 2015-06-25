package com.wxwall.modules.user.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wxwall.common.entity.BaseEntity;
import com.wxwall.common.utils.DateUtils;

/**
 * 用户邀请码
 * 
 * @author locki
 * @date 2015年2月3日
 *
 */

@Entity
@Table(name = "t_user_invitation_code")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserInvitationCode extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	public enum Status {
		UNUSED, INVALID,USED;
	}

	private String code; // 邀请码
	private Date cutOffTime; // 截止时间
	private String status; // 未使用，已失效，已注册
	private User user; // 注册用户
	
	public UserInvitationCode() {}
	
	public UserInvitationCode(String code) {
		this.code = code;
		this.cutOffTime = DateUtils.addDays(DateUtils.getDateEnd(DateUtils.now()), 7);
		this.status = Status.UNUSED.toString();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCutOffTime() {
		return cutOffTime;
	}

	public void setCutOffTime(Date cutOffTime) {
		this.cutOffTime = cutOffTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Transient
	public boolean isValid() {
		if (StringUtils.equalsIgnoreCase(status, Status.UNUSED.toString())) {
			return true;
		}
		return false;
	}

	@OneToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
