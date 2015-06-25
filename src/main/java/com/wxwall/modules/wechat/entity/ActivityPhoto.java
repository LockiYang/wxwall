
package com.wxwall.modules.wechat.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.wxwall.common.entity.BaseEntity;
/**
 * 精彩瞬间图片
 * @author ganjun
 *
 */
@Entity
@Table(name = "t_activity_photo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ActivityPhoto extends BaseEntity {

	private static final long serialVersionUID = 1L;
	public static final String SYSTEM_UPLOAD = "system";
	public static final String ADMIN_UPLOAD = "admin";
	public static final String SIGN_USER_UPLOAD = "sign";

	private String url; // 图片地址
	private String type; // 系统图片/管理员微信上传/签到用户微信上传
	private String relativePath; // 相对路径
	
	private WeChatActivity weChatActivity;
	
	public ActivityPhoto(String url, String relativePath, String type, WeChatActivity weChatActivity) {
		this.url = url;
		this.relativePath = relativePath;
		this.type = type;
		this.weChatActivity = weChatActivity;
	}

	public ActivityPhoto() { }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "wechat_activity_id")
	public WeChatActivity getWeChatActivity() {
		return weChatActivity;
	}

	public void setWeChatActivity(WeChatActivity weChatActivity) {
		this.weChatActivity = weChatActivity;
	}

}
