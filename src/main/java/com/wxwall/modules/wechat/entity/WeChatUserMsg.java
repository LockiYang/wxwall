package com.wxwall.modules.wechat.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wxwall.common.emoji.EmojiUtils;
import com.wxwall.common.entity.BaseEntity;
import com.wxwall.common.utils.DateUtils;
import com.wxwall.common.utils.FaceUtils;

/**
 * 微信用户消息
 * 
 * @author locki
 * @date 2015年2月2日
 *
 */
@Entity
@Table(name = "t_wechat_user_msg")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WeChatUserMsg extends BaseEntity {
	private static final long serialVersionUID = 1L;

	public static enum MSG_STATUS {
		CHECKING("checking", (byte)1), SUCCESS("success", (byte)2), FAILED("failed", (byte)3);

		private byte index;
		private String name;

		public String getName() {
			return name;
		}

		private MSG_STATUS(String name, byte index) {
			this.name = name;
			this.index = index;
		}

		public byte getIndex() {
			return index;
		}
	};
	
	private String content; // 文本内容
	private String imgUrl; // 图片内容
	private Byte status; // 状态
	private Date createTime;
	private Date updateTime;

	private RActivityWeChatUser rActivityWeChatUser;

	public WeChatUserMsg() {

	}

	public WeChatUserMsg(String content, String imgUrl, Byte status,
			RActivityWeChatUser rActivityWeChatUser) {
		this.content = EmojiUtils.getInstance().emojiConvertUnified(content);
		this.content = FaceUtils.filterEmoji(this.content);
		//this.content = FaceUtils.filterEmoji(content);
		this.imgUrl = imgUrl;
		this.status = status;
		this.rActivityWeChatUser = rActivityWeChatUser;
		this.createTime = Calendar.getInstance(TimeZone.getDefault()).getTime();
		this.updateTime = createTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = FaceUtils.filterEmoji(content);
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Column(columnDefinition = "TIMESTAMP NULL")
	public Date getCreateTime() {
		return createTime;
	}
	public static void main(String[] args) {
		Date date = DateUtils.parseDate("1970-01-01");
		System.out.println(date.getTime());
	}

	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(columnDefinition = "TIMESTAMP NULL")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@ManyToOne(cascade={CascadeType.REFRESH,CascadeType.MERGE},fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name = "r_activity_wechat_user_id")
	public RActivityWeChatUser getrActivityWeChatUser() {
		return rActivityWeChatUser;
	}

	public void setrActivityWeChatUser(RActivityWeChatUser rActivityWeChatUser) {
		this.rActivityWeChatUser = rActivityWeChatUser;
	}
}
