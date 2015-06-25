package com.wxwall.modules.wechat.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wxwall.common.entity.BaseEntity;
import com.wxwall.common.utils.FaceUtils;

/**
 * 微信文本回复内容
 * 
 * @author locki
 * @date 2015年2月2日
 *
 */
@Entity
@Table(name = "t_wechat_text")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WeChatText extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String content; // 文本内容
	private Date updateTime;
	
	private List<AutoReply> autoReplys;

	public WeChatText() {

	}

	public WeChatText(String content) {
		this.content = content;
		this.updateTime = Calendar.getInstance(TimeZone.getDefault()).getTime();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = FaceUtils.filterEmoji(content);
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="weChatText")
	public List<AutoReply> getAutoReplys() {
		return autoReplys;
	}

	public void setAutoReplys(List<AutoReply> autoReplys) {
		this.autoReplys = autoReplys;
	}
}
