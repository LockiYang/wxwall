package com.wxwall.common.entity;

import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wxwall.modules.user.entity.User;
import com.wxwall.modules.user.utils.UserUtils;

/**
 * 统一定义id的entity基类.
 * 
 */
@MappedSuperclass
public abstract class IdEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public enum ValidFlag {
		NORMAL, DELETE;
	}

	protected User createBy; // 创建者
	protected Date createDate;// 创建日期
	protected User updateBy; // 更新者
	protected Date updateDate; // 更新日期
	protected String validFlag; // 有效标记（0：正常；1：删除；2：审核）

	@PrePersist
	public void prePersist() {
		User user = UserUtils.getUser();
		if (user != null) {
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
	}

	@PreUpdate
	public void preUpdate() {
		User user = UserUtils.getUser();
		if (user != null) {
			this.updateBy = user;
		}
		this.updateDate = new Date();
	}

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(User updateBy) {
		this.updateBy = updateBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}
