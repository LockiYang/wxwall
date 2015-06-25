package com.wxwall.modules.wechat.entity;

import java.util.Date;
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

import com.wxwall.common.entity.BaseEntity;
import com.wxwall.common.utils.DateUtils;
import com.wxwall.modules.activity.entity.WechatUserPrizeList;

/**
 * 微信用户和活动关联表
 * 签到表
 * 
 * @author locki
 * @date 2015年2月5日
 *
 */
@Entity
@Table(name = "r_activity_wechat_user")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RActivityWeChatUser extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public static enum USER_STATUS {
		SIGN("sign", (byte)1), CANCEL_SIGN("cancel sign", (byte)2);

		private byte index;
		private String name;

		public String getName() {
			return name;
		}

		private USER_STATUS(String name, byte index) {
			this.name = name;
			this.index = index;
		}

		public byte getIndex() {
			return index;
		}
	};

	private WeChatUser weChatUser;
	private WeChatActivity weChatActivity;
	private byte status; // 用户状态 1.签到2.取消签到
	private String uuid; //?
	//private int shakeNum;
	private Date signInDate; // 签到时间
	private Date updateTime; 
	private List<WeChatUserMsg> weChatUserMsgs;
	private List<WechatUserPrizeList> userPrizes;
	private boolean fActivityPhotoForAdmin;//精彩瞬间图片上传
	
	public RActivityWeChatUser() {
		
	}
	
	public RActivityWeChatUser(WeChatUser weChatUser, WeChatActivity weChatActivity, byte status, String uuid) {
		this.weChatActivity = weChatActivity;
		this.weChatUser = weChatUser;
		this.status = status;
		this.updateTime = DateUtils.now();
		this.uuid = uuid;
		//this.shakeNum = shakeNum;
		this.signInDate = updateTime;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "wechat_user_id")
	public WeChatUser getWeChatUser() {
		return weChatUser;
	}

	public void setWeChatUser(WeChatUser weChatUser) {
		this.weChatUser = weChatUser;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "wechat_activity_id")
	public WeChatActivity getWeChatActivity() {
		return weChatActivity;
	}

	public void setWeChatActivity(WeChatActivity weChatActivity) {
		this.weChatActivity = weChatActivity;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	@Column(name="update_time", columnDefinition = "TIMESTAMP NULL")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="rActivityWeChatUser")
	public List<WeChatUserMsg> getWeChatUserMsgs() {
		return weChatUserMsgs;
	}

	public void setWeChatUserMsgs(List<WeChatUserMsg> weChatUserMsgs) {
		this.weChatUserMsgs = weChatUserMsgs;
	}
	
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="rActivityWeChatUser")
    public List<WechatUserPrizeList> getUserPrizes() {
        return userPrizes;
    }
	
	public void setUserPrizes(List<WechatUserPrizeList> userPrizes) {
        this.userPrizes = userPrizes;
    }

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name="signin_date", columnDefinition = "TIMESTAMP NULL")
	public Date getSignInDate() {
		return signInDate;
	}

	public void setSignInDate(Date signInDate) {
		this.signInDate = signInDate;
	}
	
	@Column(nullable = false, columnDefinition = "bit(1) default 0")
	public boolean isfActivityPhotoForAdmin() {
		return fActivityPhotoForAdmin;
	}

	public void setfActivityPhotoForAdmin(boolean fActivityPhotoForAdmin) {
		this.fActivityPhotoForAdmin = fActivityPhotoForAdmin;
	}
}
