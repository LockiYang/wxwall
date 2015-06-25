package com.wxwall.modules.activity.entity;

import java.util.List;

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

import com.wxwall.common.entity.IdEntity;
import com.wxwall.modules.wechat.entity.WeChatActivity;
/**
 * 
 * <p>
 * 描述:奖项设置
 * </p>
 *
 * @see
 * @author ganjun
 *
 */
@Entity
@Table(name = "t_activity_prize")
@DynamicInsert
@DynamicUpdate
// 只操作修改过的字段
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ActivityPrize extends IdEntity {
	private static final long serialVersionUID = 1L;

	private String prizeName; // 奖项名
	private String description; //奖项描述
	private String img; //奖项预览图片
	private int winNum;//中奖人数设置
	
	private WeChatActivity weChatActivity;
	private List<WechatUserPrizeList> wechatUserPrizeLists;
	
	public ActivityPrize() {
		
	}

	public ActivityPrize(String prizeName, String description, String img, int winNum, 
			WeChatActivity weChatActivity) {
		this.prizeName = prizeName;
		this.description = description;
		this.img = img;
		this.winNum = winNum;
		this.weChatActivity = weChatActivity;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getWinNum() {
		return winNum;
	}

	public void setWinNum(int winNum) {
		this.winNum = winNum;
	}
	
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="activityPrize")
	public List<WechatUserPrizeList> getWechatUserPrizeLists() {
		return wechatUserPrizeLists;
	}

	public void setWechatUserPrizeLists(
			List<WechatUserPrizeList> wechatUserPrizeLists) {
		this.wechatUserPrizeLists = wechatUserPrizeLists;
	}

	@ManyToOne(cascade={CascadeType.REFRESH,CascadeType.MERGE},fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="wechat_activity_id")
	public WeChatActivity getWeChatActivity() {
		return weChatActivity;
	}

	public void setWeChatActivity(WeChatActivity weChatActivity) {
		this.weChatActivity = weChatActivity;
	}
}
