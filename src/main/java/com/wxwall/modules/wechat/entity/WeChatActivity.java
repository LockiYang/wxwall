package com.wxwall.modules.wechat.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wxwall.common.entity.IdEntity;
import com.wxwall.common.utils.DateUtils;
import com.wxwall.modules.activity.entity.ActivityPrize;
import com.wxwall.modules.activity.entity.WechatUserFatePair;

/**
 * 微信活动实体
 * 
 * @author locki
 * @date 2015年2月2日
 * 
 */

@Entity
@Table(name = "t_wechat_activity")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WeChatActivity extends IdEntity {
	private static final long serialVersionUID = 1L;
	public static final String DEF_SIGN_KEY_WORD = "1314";
	public static final int DEF_USER_MSG_NUM_BEFORE_PROGRESS = 20;//活动未进行时默认上墙消息数量
	
	public static final String FREE_TYPE = "free";
	public static final String VIP_TYPE = "vip";
	public static final String openActivityPhotoForAdminToKeyWord = "kqjcsj";
	public static final String closeActivityPhotoForAdminToKeyWord = "kqjcsj";

	public static enum ACTIVITY_STATUS {
		CHECKING("checking", 1), SUCESSED("successed", 2), FAILED("failed", 3), CANCEL(
				"cancel", 4);

		private int index;
		private String name;

		public String getName() {
			return name;
		}

		private ACTIVITY_STATUS(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public int getIndex() {
			return index;
		}
	};

	public enum ACTIVITY_CATEGORY {
		MEETIING, WEDDING;
	}
	
	public enum ActivityStatus {
		PREP,INPROGRESS,DONE;
	}
	
	private String activityMid;//UUID
	private String subject;// 活动主题
	private String description;// 活动描述
	private String category; // 活动类别
	private String organisers; // 活动主办方
	private Date startDate;// 活动开始时间
	private Date endDate;// 活动结束时间
	private String status;// 活动状态 1:未审核 2：已审核
	private String type; // free, vip;
	private String salutatory; // 签到欢迎词
	private String upWallReply; // 上墙成功回复
	private String filterContent; // 上墙信息过滤关键词

	private String ticket;// 微信二维码图片
	private int sceneID;// 微信二维码场景值，最大值不超过1-100000
	private Date msgTimeTag;
	
	private String signKeyWord;

	// 功能开关
	private boolean fDraw;// 是否抽奖
	private boolean fLogo; // 是否自定义logo
	private boolean fBackground; // 允许自定义背景
	private boolean fAutoUpWall; // 自动上墙
	private boolean fAutoFiter; // 自动上墙过滤
	private boolean fVote; // 投票
	private boolean fShake; // 摇一摇
	private boolean fPair; // 对对碰
	private boolean fSms; // 短信
	private boolean fSignIn; // 签到
	private boolean fScheduler;//日程安排
	private boolean fAlbum; //自定义相册开关
	private boolean fActivityPhoto;//活动图片
	
	//精彩瞬间图片展示设置
	private boolean fActivityPhotoForAdmin;//管理员微信上传
	private boolean fActivityPhotoForSignUser;//签到用户微信上传
	private boolean fActivityPhotoForSystem;//系统后台上传
	
	private List<ActivityPhoto> photoPiles; // 精彩瞬间图
	

	private String logo;// logo图片
	private List<ActivityImage> backgroudImgs; // 背景图
	
	private String albumSubject;
	
//	private boolean fShakeStart;//摇一摇是否开始
//	private int shakeTime;//摇一摇进行时间毫秒
	
	private int endShakeTime;//摇一摇截止时间分钟,默认1分钟
	private int endShakeNum;//摇一摇截止次数,默认400次
	

	private WeChatApp weChatApp;
	private List<ActivityPrize> activityPrizes;
	private List<WeActivityScheduler> weActivitySchedulers;
	private List<WeActivityTips> tips;
	private List<RActivityWeChatUser> rActivityWeChatUsers;
	private List<WechatUserFatePair> wechatUserFatePairs;
	private ActivityAutoReply activityAutoReply;
	

	public WeChatActivity() {
		//初始化摇一摇参数
		this.endShakeTime = 1;
		this.endShakeNum = 400;
//		this.shakeTime = this.endShakeTime * 60;
	}
	
	public WeChatActivity (long activityId, String subject, String organisers, String description) {
		this.id = activityId;
		this.subject = subject;
		this.organisers = organisers;
		this.description = description;
		//初始化摇一摇参数
		this.endShakeTime = 1;
		this.endShakeNum = 400;
//		this.shakeTime = this.endShakeTime * 60;
	}

	public WeChatActivity(String activityMid, String subject, String actDesc, Date actStart,
			Date actEnd, String status, String ticket, int sceneID, 
			WeChatApp weChatApp) {
		this.activityMid = activityMid;
		this.subject = subject;
		this.description = actDesc;
		this.startDate = actStart;
		this.endDate = actEnd;
		this.status = status;
		this.weChatApp = weChatApp;
		this.ticket = ticket;
		this.sceneID = sceneID;
		this.upWallReply = "上墙成功！";
		this.salutatory = "签到成功！";
		this.logo = null;
		//初始化摇一摇参数
		this.endShakeTime = 1;
		this.endShakeNum = 400;
	//	this.shakeTime = this.endShakeTime * 60;
	}

	public WeChatActivity(String activityMid, String subject, String actDesc, Date actStart,
			Date actEnd, String status, String ticket, int sceneID,
			String salutatory, String upWallReply, String logo,
			WeChatApp weChatApp) {
		this.activityMid = activityMid;
		this.subject = subject;
		this.description = actDesc;
		this.startDate = actStart;
		this.endDate = actEnd;
		this.status = status;
		this.weChatApp = weChatApp;
		this.ticket = ticket;
		this.sceneID = sceneID;
		this.upWallReply = upWallReply;
		this.salutatory = salutatory;
		this.logo = logo;
		//初始化摇一摇参数
		this.endShakeTime = 1;
		this.endShakeNum = 400;
	//	this.shakeTime = this.endShakeTime * 60;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Transient
	public String getStatus() {
		if (StringUtils.isNotBlank(this.status)) {
			return this.status;
		} else {
			if (this.inProgress()) {
				return ActivityStatus.INPROGRESS.toString();
			} else if (this.afterProgress()) {
				return ActivityStatus.DONE.toString();
			} else {
				return ActivityStatus.PREP.toString();
			}
		}
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "wechatapp_id")
	public WeChatApp getWeChatApp() {
		return weChatApp;
	}

	public void setWeChatApp(WeChatApp weChatApp) {
		this.weChatApp = weChatApp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOrganisers() {
		return organisers;
	}

	public void setOrganisers(String organisers) {
		this.organisers = organisers;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	@Column(nullable = false, columnDefinition = "INT default 0")
	public int getSceneID() {
		return sceneID;
	}

	public void setSceneID(int sceneID) {
		this.sceneID = sceneID;
	}

	public String getSalutatory() {
		return salutatory;
	}

	public void setSalutatory(String salutatory) {
		this.salutatory = salutatory;
	}

	public String getUpWallReply() {
		return upWallReply;
	}

	public void setUpWallReply(String upWallReply) {
		this.upWallReply = upWallReply;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	@Column(nullable = false, columnDefinition = "bit(1) default 0")
	public boolean isfDraw() {
		return fDraw;
	}

	public void setfDraw(boolean fDraw) {
		this.fDraw = fDraw;
	}
	@Column(nullable = false, columnDefinition = "bit(1) default 0")
	public boolean isfLogo() {
		return fLogo;
	}

	public void setfLogo(boolean fLogo) {
		this.fLogo = fLogo;
	}
	@Column(nullable = false, columnDefinition = "bit(1) default 0")
	public boolean isfBackground() {
		return fBackground;
	}

	public void setfBackground(boolean fBackground) {
		this.fBackground = fBackground;
	}
	@Column(nullable = false, columnDefinition = "bit(1) default 0")
	public boolean isfAutoUpWall() {
		return fAutoUpWall;
	}

	public void setfAutoUpWall(boolean fAutoUpWall) {
		this.fAutoUpWall = fAutoUpWall;
	}
	@Column(nullable = false, columnDefinition = "bit(1) default 0")
	public boolean isfVote() {
		return fVote;
	}

	public void setfVote(boolean fVote) {
		this.fVote = fVote;
	}
	@Column(nullable = false, columnDefinition = "bit(1) default 0")
	public boolean isfShake() {
		return fShake;
	}

	public void setfShake(boolean fShake) {
		this.fShake = fShake;
	}
	@Column(nullable = false, columnDefinition = "bit(1) default 0")
	public boolean isfPair() {
		return fPair;
	}

	public void setfPair(boolean fPair) {
		this.fPair = fPair;
	}
	@Column(nullable = false, columnDefinition = "bit(1) default 0")
	public boolean isfSms() {
		return fSms;
	}

	public void setfSms(boolean fSms) {
		this.fSms = fSms;
	}
	@Column(nullable = false, columnDefinition = "bit(1) default 0")
	public boolean isfSignIn() {
		return fSignIn;
	}

	public void setfSignIn(boolean fSignIn) {
		this.fSignIn = fSignIn;
	}
	@Column(nullable = false, columnDefinition = "bit(1) default 0")
	public boolean isfAutoFiter() {
		return fAutoFiter;
	}

	public void setfAutoFiter(boolean fAutoFiter) {
		this.fAutoFiter = fAutoFiter;
	}
	
	@Column(nullable = false, columnDefinition = "bit(1) default 0")
	public boolean isfScheduler() {
		return fScheduler;
	}

	public void setfScheduler(boolean fScheduler) {
		this.fScheduler = fScheduler;
	}
	
	
//	@Column(nullable = false, columnDefinition = "bit(1) default 0")
//	public boolean isfShakeStart() {
//		return fShakeStart;
//	}
//
//	public void setfShakeStart(boolean fShakeStart) {
//		this.fShakeStart = fShakeStart;
//	}
//	
//	@Column(nullable = false, columnDefinition = "INT default 61")
//	public int getShakeTime() {
//		return shakeTime;
//	}
	@Column(nullable = false, columnDefinition = "bit(1) default 0")
	public boolean isfActivityPhoto() {
		return fActivityPhoto;
	}

	public void setfActivityPhoto(boolean fActivityPhoto) {
		this.fActivityPhoto = fActivityPhoto;
	}
	@Column(nullable = false, columnDefinition = "bit(1) default 1")
	public boolean isfActivityPhotoForAdmin() {
		return fActivityPhotoForAdmin;
	}

	public void setfActivityPhotoForAdmin(boolean fActivityPhotoForAdmin) {
		this.fActivityPhotoForAdmin = fActivityPhotoForAdmin;
	}

	public boolean isfActivityPhotoForSignUser() {
		return fActivityPhotoForSignUser;
	}

	public void setfActivityPhotoForSignUser(boolean fActivityPhotoForSignUser) {
		this.fActivityPhotoForSignUser = fActivityPhotoForSignUser;
	}
	
	@Column(nullable = false, columnDefinition = "bit(1) default 1")
	public boolean isfActivityPhotoForSystem() {
		return fActivityPhotoForSystem;
	}

	public void setfActivityPhotoForSystem(boolean fActivityPhotoForSystem) {
		this.fActivityPhotoForSystem = fActivityPhotoForSystem;
	}

	@Column(nullable = false, columnDefinition = "INT default 1")
	public int getEndShakeTime() {
		return endShakeTime;
	}

	public void setEndShakeTime(int endShakeTime) {
		this.endShakeTime = endShakeTime;
	}
	
	@Column(nullable = false, columnDefinition = "INT default 400")
	public int getEndShakeNum() {
		return endShakeNum;
	}

	public void setEndShakeNum(int endShakeNum) {
		this.endShakeNum = endShakeNum;
	}
	
	@Column(nullable = false, columnDefinition = "bit(1) default 0")
	public boolean isfAlbum() {
		return fAlbum;
	}

	public void setfAlbum(boolean fAlbum) {
		this.fAlbum = fAlbum;
	}

	public String getAlbumSubject() {
		return albumSubject;
	}

	public void setAlbumSubject(String albumSubject) {
		this.albumSubject = albumSubject;
	}

//	public void setShakeTime(int shakeTime) {
//		this.shakeTime = shakeTime;
//	}

	public String getFilterContent() {
		return filterContent;
	}

	public void setFilterContent(String filterContent) {
		this.filterContent = filterContent;
	}
	
	
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="weChatActivity")
	public List<ActivityPrize> getActivityPrizes() {
		return activityPrizes;
	}

	public void setActivityPrizes(List<ActivityPrize> activityPrizes) {
		this.activityPrizes = activityPrizes;
	}

	public String getActivityMid() {
		return activityMid;
	}

	public void setActivityMid(String activityMid) {
		this.activityMid = activityMid;
	}

	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.MERGE,
			CascadeType.REMOVE, CascadeType.PERSIST }, fetch = FetchType.LAZY, mappedBy="weChatActivity")
	//@JoinColumn(name="activity_id")
	public List<ActivityImage> getBackgroudImgs() {
		return backgroudImgs;
	}

	public void setBackgroudImgs(List<ActivityImage> backgroudImgs) {
		this.backgroudImgs = backgroudImgs;
	}
	
	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.MERGE,
			CascadeType.REMOVE, CascadeType.PERSIST }, fetch = FetchType.LAZY, mappedBy="weChatActivity")
	public List<ActivityPhoto> getPhotoPiles() {
		return photoPiles;
	}

	public void setPhotoPiles(List<ActivityPhoto> photoPiles) {
		this.photoPiles = photoPiles;
	}

	public void addBackgroudImg(ActivityImage image) {
		if (this.backgroudImgs == null) {
			this.backgroudImgs = new ArrayList<ActivityImage>();
		}
		
		backgroudImgs.add(image);
	}
	
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="weChatActivity")
	@OrderBy("noticeTime asc")
	public List<WeActivityScheduler> getWeActivitySchedulers() {
		return weActivitySchedulers;
	}

	public void setWeActivitySchedulers(
			List<WeActivityScheduler> weActivitySchedulers) {
		this.weActivitySchedulers = weActivitySchedulers;
	}
	
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="weChatActivity")
	public List<WeActivityTips> getTips() {
		return tips;
	}

	public void setTips(List<WeActivityTips> tips) {
		this.tips = tips;
	}
	
	@Column(nullable = true, columnDefinition = "TIMESTAMP NULL")
	public Date getMsgTimeTag() {
		return msgTimeTag;
	}

	public void setMsgTimeTag(Date msgTimeTag) {
		this.msgTimeTag = msgTimeTag;
	}
	
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="weChatActivity")
	public List<RActivityWeChatUser> getrActivityWeChatUsers() {
		return rActivityWeChatUsers;
	}

	public void setrActivityWeChatUsers(
			List<RActivityWeChatUser> rActivityWeChatUsers) {
		this.rActivityWeChatUsers = rActivityWeChatUsers;
	}
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="weChatActivity")
	public List<WechatUserFatePair> getWechatUserFatePairs() {
		return wechatUserFatePairs;
	}

	public void setWechatUserFatePairs(List<WechatUserFatePair> wechatUserFatePairs) {
		this.wechatUserFatePairs = wechatUserFatePairs;
	}

	public String getSignKeyWord() {
		return signKeyWord;
	}

	public void setSignKeyWord(String signKeyWord) {
		this.signKeyWord = signKeyWord;
	}
	
	@OneToOne(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="weChatActivity")
	public ActivityAutoReply getActivityAutoReply() {
		return activityAutoReply;
	}

	public void setActivityAutoReply(ActivityAutoReply activityAutoReply) {
		this.activityAutoReply = activityAutoReply;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 活动是否进行中
	 * @return
	 */
	public boolean inProgress() {
		return DateUtils.biggerThan(DateUtils.now(), this.startDate)
				&& DateUtils.biggerThan(this.endDate, DateUtils.now());
	}
	
	/**
	 * 活动是否准备中
	 * @return
	 */
	public boolean beforeProgress() {
		return DateUtils.biggerThan(this.startDate, DateUtils.now());
	}
	
	/**
	 * 活动是否已结束
	 */
	public boolean afterProgress() {
		return DateUtils.biggerThan(DateUtils.now(), this.endDate);
	}
	
	/**
	 * 活动结束前
	 * @return
	 */
	public boolean beforeEnd() {
		return !this.afterProgress();
	}
}
