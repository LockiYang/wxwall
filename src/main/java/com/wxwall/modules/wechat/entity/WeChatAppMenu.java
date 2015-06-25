package com.wxwall.modules.wechat.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "t_wechat_app_menu")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WeChatAppMenu extends IdEntity {
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
	
	
	private List<WeChatAppSubMenu> weChatAppSubMenus;
	
	private WeChatApp weChatApp;

	public WeChatAppMenu(String name, WeChatApp weChatApp) {
		this.name = name;
		this.weChatApp = weChatApp;
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

	public String getKeyEvent() {
		return keyEvent;
	}

	public void setKeyEvent(String keyEvent) {
		this.keyEvent = keyEvent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "wechatapp_id")
	public WeChatApp getWeChatApp() {
		return weChatApp;
	}

	public void setWeChatApp(WeChatApp weChatApp) {
		this.weChatApp = weChatApp;
	}
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="weChatAppMenu")
	public List<WeChatAppSubMenu> getWeChatAppSubMenus() {
		return weChatAppSubMenus;
	}

	public void setWeChatAppSubMenus(List<WeChatAppSubMenu> weChatAppSubMenus) {
		this.weChatAppSubMenus = weChatAppSubMenus;
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
		this.title = title;
		this.imgUrl = imgUrl;
		this.imgLinkUrl = imgLinkUrl;
		this.imgDescription = imgDescription;
		this.textContent = textContent;
		this.replyType = replyType;
		this.keyEvent = keyEvent;
		this.url = url;
		this.type = type;
	}
	
	public void cleanReplyInfo() {
		this.title = null;
		this.imgUrl = null;
		this.imgLinkUrl = null;
		this.imgDescription = null;
		this.textContent = null;
		this.keyEvent = null;
		this.replyType = AutoReply.REPLY_TYPE.TEXT.getIndex();
	}
	
	@Column(nullable = false, columnDefinition = "bit(1) default 1")
	public byte getReplyType() {
		return replyType;
	}

	public void setReplyType(byte replyType) {
		this.replyType = replyType;
	}
}
