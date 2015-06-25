package com.wxwall.modules.wechat.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.wxwall.common.entity.BaseEntity;

@Entity
@Table(name = "t_activity_image")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ActivityImage extends BaseEntity {

	private static final long serialVersionUID = 1L;
	public static final Map<String, String> defActivityImgs = new HashMap<String, String>();
	
	static {
		defActivityImgs.put("怦然心动", "statics/images/bg_prxd.jpg");
		defActivityImgs.put("夕夕相映", "statics/images/bg_xxxy.jpg");
		defActivityImgs.put("天蓝", "statics/images/bg.jpg");
		defActivityImgs.put("喜庆大红", "statics/images/xqdh.jpg");
		defActivityImgs.put("草绿", "statics/images/cn.jpg");
		defActivityImgs.put("梦幻紫", "statics/images/mhz.jpg");
		defActivityImgs.put("默认大红", "statics/images/defaultdx.png");
	}

	public static enum IMAGE_TYPE {
		BACKGROUND("background", 1), ALBUM("album", 2);

		private int index;
		private String name;

		public String getName() {
			return name;
		}

		private IMAGE_TYPE(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public int getIndex() {
			return index;
		}
	};

	private String url; // 图片地址
	private String name; // 图片名称
	private String type; // 背景图
	private String relativePath; // 相对路径
	
	private WeChatActivity weChatActivity;
	
	public ActivityImage(String relativePath, String type, WeChatActivity weChatActivity) {
		this.relativePath = relativePath;
		this.type = type;
		this.weChatActivity = weChatActivity;
	}

	public ActivityImage() { }

	@Transient
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
