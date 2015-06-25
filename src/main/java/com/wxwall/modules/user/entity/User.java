package com.wxwall.modules.user.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wxwall.common.entity.BaseEntity;
import com.wxwall.common.web.validator.VlidatorGroup.CreateGroup;
import com.wxwall.modules.wechat.entity.WeChatApp;

@Entity
@Table(name = "t_user")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends BaseEntity {
	private static final long serialVersionUID = 1L;

	public static final String FREE_USER = "free";
	public static final String VIP_USER = "vip";
	public static final String ADMIN_USER = "admin";

	private String userName; // 个体名称 （公司名或个人用户名）
	private String loginEmail; // 登录邮箱
	private String linkman; // 联系人
	private String mobilePhone; // 手机号
	private String userType; // 用户类型（免费会员（free），付费会员（vip），管理员（admin））
	private Date vipStartDate; // 续费开始时间
	private Date vipEndDate; // 续费到期时间
	private int vipTimes; // 剩余使用次数

	private String password; // 加密密码
	private String plainPassword; // 原始密码
	private String salt; // 加密串
	private String randomCode; // 随机码 （激活帐户与生成重设密码链接时使用）
	private String validFlag; // 是否可用

	private Date createDate; // 创建时间
	private Date updateDate; // 更新时间
	private WeChatApp weChatApp; // 微信公众号
	private String textPassword;
	public User() {
	}

	public User(String loginEmail, String plainPassword) {
		this.loginEmail = loginEmail;
		this.plainPassword = plainPassword;
	}

	@Transient
	@JsonIgnore
	@NotEmpty(message = "{user.plainpassword.not.empty}", groups = CreateGroup.class)
	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@NotEmpty(message = "{user.email.not.empty}", groups = CreateGroup.class)
	@Email(message = "{user.email.not.correct}", groups = CreateGroup.class)
	public String getLoginEmail() {
		return loginEmail;
	}

	public void setLoginEmail(String loginEmail) {
		this.loginEmail = loginEmail;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Date getVipStartDate() {
		return vipStartDate;
	}

	public void setVipStartDate(Date vipStartDate) {
		this.vipStartDate = vipStartDate;
	}

	public Date getVipEndDate() {
		return vipEndDate;
	}

	public void setVipEndDate(Date vipEndDate) {
		this.vipEndDate = vipEndDate;
	}

	public String getRandomCode() {
		return randomCode;
	}

	public void setRandomCode(String randomCode) {
		this.randomCode = randomCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public String getTextPassword() {
		return textPassword;
	}

	public void setTextPassword(String textPassword) {
		this.textPassword = textPassword;
	}

	/**
	 * cascade 更新用户的时候，级联更新WeChatApp 
	 * mappedBy 指定关系的维护方位多的那一方
	 * optional=false来告诉hibernate，映射的属性一定存在，Lazy加载才会成功
	 */
	@OneToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE,
			CascadeType.REMOVE, CascadeType.PERSIST }, fetch = FetchType.LAZY, mappedBy = "user")
	public WeChatApp getWeChatApp() {
		return weChatApp;
	}

	public void setWeChatApp(WeChatApp weChatApp) {
		this.weChatApp = weChatApp;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public boolean needComplete() {
		if (StringUtils.isBlank(this.getLoginEmail())
				|| StringUtils.isBlank(this.getUserName())
				|| StringUtils.isBlank(this.getLinkman())
				|| StringUtils.isBlank(this.getMobilePhone())) {
			return true;
		} else {
			return false;
		}
	}

	@Column(nullable = false, columnDefinition = "INT default 0")
	public int getVipTimes() {
		return vipTimes;
	}

	public void setVipTimes(int vipTimes) {
		this.vipTimes = vipTimes;
	}

}
