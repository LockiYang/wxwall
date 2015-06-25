package com.wxwall.modules.wechat.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.CascadeType;
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

import com.wxwall.common.entity.BaseEntity;

/**
 * 微信图文文本回复内容
 * 
 * @author locki
 * @date 2015年2月2日
 *
 */
@Entity
@Table(name = "t_wechat_image_text")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WeChatImageText extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String title;// 标题
	private String img;// 图片地址
	private String imgUrl;// 图片连接地址
	private String description;// 描述信息
	private Date updateTime;
	private WeChatImageText weChatImageText;//一级图文消息
	private List<WeChatImageText> weChatImageTexts;//二级图文消息
	private List<AutoReply> autoReplys;

	public WeChatImageText() {

	}

	public WeChatImageText(String title, String img, String imgUrl, String description, WeChatImageText weChatImageText) {
		this.title = title;
		this.img = img;
		this.imgUrl = imgUrl;
		this.description = description;
		this.weChatImageText = weChatImageText;
		this.updateTime = Calendar.getInstance(TimeZone.getDefault()).getTime();
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "wechat_image_text_id")
	public WeChatImageText getWeChatImageText() {
		return weChatImageText;
	}

	public void setWeChatImageText(WeChatImageText weChatImageText) {
		this.weChatImageText = weChatImageText;
	}
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="weChatImageText")
	public List<WeChatImageText> getWeChatImageTexts() {
		return weChatImageTexts;
	}

	public void setWeChatImageTexts(List<WeChatImageText> weChatImageTexts) {
		this.weChatImageTexts = weChatImageTexts;
	}
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="weChatImageText")
	public List<AutoReply> getAutoReplys() {
		return autoReplys;
	}

	public void setAutoReplys(List<AutoReply> autoReplys) {
		this.autoReplys = autoReplys;
	}
	
}
