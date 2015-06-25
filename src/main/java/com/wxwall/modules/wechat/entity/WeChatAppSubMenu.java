package com.wxwall.modules.wechat.entity;

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

/**
 * 微信公众号
 * 
 * @author locki
 * @date 2015年2月2日
 *
 */
@Entity
@Table(name = "t_wechat_app_sub_menu")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WeChatAppSubMenu extends IdEntity {
	private static final long serialVersionUID = 1L;
	
	private byte type;
	private String name;
	private String keyEvent;
	private String url;
	//回复类型
	private byte replyType;
	//图文回复
	private String title;// 标题
	private String imgUrl;// 图片地址
	private String imgLinkUrl;// 图片连接地址
	private String imgDescription;// 描述信息
	//文本回复
	private String textContent;// 文本内容
	
	private WeChatAppMenu weChatAppMenu;

	public WeChatAppSubMenu(String name, WeChatAppMenu weChatAppMenu) {
		this.name = name;
		this.weChatAppMenu = weChatAppMenu;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "wechatapp_menu_id")
	public WeChatAppMenu getWeChatAppMenu() {
		return weChatAppMenu;
	}

	public void setWeChatAppMenu(WeChatAppMenu weChatAppMenu) {
		this.weChatAppMenu = weChatAppMenu;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getImgLinkUrl() {
		return imgLinkUrl;
	}

	public void setImgLinkUrl(String imgLinkUrl) {
		this.imgLinkUrl = imgLinkUrl;
	}

	public String getImgDescription() {
		return imgDescription;
	}

	public void setImgDescription(String imgDescription) {
		this.imgDescription = imgDescription;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	
	public void setReplyInfo(String keyEvent, String url, byte type, String title, String imgUrl, String imgLinkUrl, String imgDescription, String textContent, byte replyType) {
		this.keyEvent = keyEvent;
		this.url = url;
		this.type = type;
		this.title = title;
		this.imgUrl = imgUrl;
		this.imgLinkUrl = imgLinkUrl;
		this.imgDescription = imgDescription;
		this.textContent = textContent;
		this.replyType = replyType;
	}

	public byte getReplyType() {
		return replyType;
	}

	public void setReplyType(byte replyType) {
		this.replyType = replyType;
	}

	public String getKeyEvent() {
		return keyEvent;
	}

	public void setKeyEvent(String keyEvent) {
		this.keyEvent = keyEvent;
	}
	
}
