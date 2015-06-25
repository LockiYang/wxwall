package com.wxwall.modules.wechat.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
import com.wxwall.modules.user.entity.User;

/**
 * 微信公众号
 * 
 * @author locki
 * @date 2015年2月2日
 *
 */
@Entity
@Table(name = "t_wechat_app")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WeChatApp extends IdEntity {
	private static final long serialVersionUID = 1L;
	public static enum MENU_TYPE {
		CLICK("checking", (byte)1), VIEW("successed", (byte)2);

		private byte index;
		private String name;

		public String getName() {
			return name;
		}

		private MENU_TYPE(String name, byte index) {
			this.name = name;
			this.index = index;
		}

		public byte getIndex() {
			return index;
		}
	};
	
	public static enum ACCOUNT_TYPE {
		UNAUTH("Unauth", (byte)1), SUBSCRIBER("subscriber", (byte)2),
		SERVICER("servicer", (byte)3);

		private byte index;
		private String name;

		public String getName() {
			return name;
		}

		private ACCOUNT_TYPE(String name, byte index) {
			this.name = name;
			this.index = index;
		}

		public byte getIndex() {
			return index;
		}
	};
	
	public static long IN_EXPIRE = 7200 * 1000;

	private byte accountType;
	private String weChatName;// 公众号名称
	private String weChatOriginId; // 公众号原始ID

	private String appId; // 微信应用Id 取消
	private String appSecret; // 微信应用秘钥
	private String serverUrl; // 服务器地址
	private String token; // 令牌
	private String encodingAESKey; // 消息加解密密钥

	private String accessToken;// 全局凭证
	private Date expire; // 全局凭证过期时间
	private String status; //绑定状态，成功；失败

	private String weChatImage;// 微信二维码
	private String mid;
	private User user; // 所属用户
	private List<WeChatActivity> weChatActivitys;
	private List<WeChatUser> weChatUsers;
	private List<WeChatAppMenu> weChatAppMenus;
	private WeChatAppAutoReply weChatAppAutoReply;

	public WeChatApp() {

	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	public String getWeChatName() {
		return weChatName;
	}

	public void setWeChatName(String weChatName) {
		this.weChatName = weChatName;
	}

	public String getWeChatImage() {
		return weChatImage;
	}

	public void setWeChatImage(String weChatImage) {
		this.weChatImage = weChatImage;
	}

	public String getWeChatOriginId() {
		return weChatOriginId;
	}

	public void setWeChatOriginId(String weChatOriginId) {
		this.weChatOriginId = weChatOriginId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	@Transient
	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getEncodingAESKey() {
		return encodingAESKey;
	}

	public void setEncodingAESKey(String encodingAESKey) {
		this.encodingAESKey = encodingAESKey;
	}
	
	public byte getAccountType() {
		return accountType;
	}

	public void setAccountType(byte accountType) {
		this.accountType = accountType;
	}

	/**
	 * cascade 级联
	 * fetch 是否立即加载
	 * optional 外键是否允许为空
	 * @JoinColumn 指定外键的列名
	 */
	@OneToOne(cascade={CascadeType.REFRESH,CascadeType.MERGE},fetch=FetchType.LAZY,optional=true)
    @JoinColumn(name="user_id", unique=true)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="weChatApp")
	@OrderBy("createDate desc")
	public List<WeChatActivity> getWeChatActivitys() {
		return weChatActivitys;
	}

	public void setWeChatActivitys(List<WeChatActivity> weChatActivitys) {
		this.weChatActivitys = weChatActivitys;
	}
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="weChatApp")
	public List<WeChatUser> getWeChatUsers() {
		return weChatUsers;
	}

	public void setWeChatUsers(List<WeChatUser> weChatUsers) {
		this.weChatUsers = weChatUsers;
	}
	
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="weChatApp")
	public List<WeChatAppMenu> getWeChatAppMenus() {
		return weChatAppMenus;
	}

	public void setWeChatAppMenus(List<WeChatAppMenu> weChatAppMenus) {
		this.weChatAppMenus = weChatAppMenus;
	}
	
	@OneToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE,
			CascadeType.REMOVE, CascadeType.PERSIST }, fetch = FetchType.LAZY, mappedBy = "weChatApp")
	public WeChatAppAutoReply getWeChatAppAutoReply() {
		return weChatAppAutoReply;
	}

	public void setWeChatAppAutoReply(WeChatAppAutoReply weChatAppAutoReply) {
		this.weChatAppAutoReply = weChatAppAutoReply;
	}

	@Transient
	public String getStatus() {
		if (expire == null) {
			this.status = "FAILD";
		} else if (expire.compareTo(DateUtils.now()) > 0){
			this.status = "SUCCESS";
		} else {
			this.status = "FAILD";
		}
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public boolean needComplete() {
		if (StringUtils.isBlank(this.getAccessToken())
				|| StringUtils.isBlank(this.getAppId())
				|| StringUtils.isBlank(this.getAppSecret())
				|| StringUtils.isBlank(this.getWeChatOriginId())
				|| StringUtils.isBlank(this.getWeChatName())) {
			return true;
		} else {
			return false;
		}
	}
}
