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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wxwall.common.entity.BaseEntity;
import com.wxwall.common.utils.FaceUtils;
import com.wxwall.wechat.api.advanced.model.PersonalInf;

/**
 * 微信活动签到用户
 * 
 * @author locki
 * @date 2015年2月2日
 *
 */

@Entity
@Table(name = "t_wechat_user")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WeChatUser extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    public static enum USER_SEX {
		BOY("boy", (byte)1), GIRL("girl", (byte)2);

		private byte index;
		private String name;

		public String getName() {
			return name;
		}

		private USER_SEX(String name, byte index) {
			this.name = name;
			this.index = index;
		}

		public byte getIndex() {
			return index;
		}
	};
    
    private String openID; // 参加微信用户唯一标记
    private String nickName; // 微信用户昵称
    private Byte sex; // 微信用户性别
    private String headImgUrl;// 微信用户头像
    private String language;// 微信用户语言
    private String city;// 微信用户城市
    private String province;// 微信用户省份
    private String country;// 微信用户国家
    
    private Date subscribeTime;// 微信用户订阅时间
    private Date unSubscribeTime; // 取消订阅时间，默认时间为null
    private WeChatApp weChatApp;
    
    
    private List<RActivityWeChatUser> rActivityWeChatUsers;


    public WeChatUser() {

    }

    public WeChatUser(PersonalInf personalInfo, WeChatApp weChatApp) {
        this.weChatApp = weChatApp;
        this.openID = personalInfo.getOpenID();
        this.nickName = FaceUtils.filterEmoji(personalInfo.getNickname());
        this.sex = (byte) personalInfo.getSex();
        this.language = personalInfo.getLanguage();
        this.city = personalInfo.getCity();
        this.province = personalInfo.getProvince();
        this.country = personalInfo.getCountry();
        this.headImgUrl = personalInfo.getHeadImgUrl();
        this.subscribeTime = personalInfo.getSubScribeTime();
    }
    
    public WeChatUser(String openID, String nickName, byte sex, String language, String city, String province, String country
    		, String headImgUrl, Date subscribeTime, WeChatApp weChatApp) {
        this.weChatApp = weChatApp;
        this.openID = openID;
        this.nickName = FaceUtils.filterEmoji(nickName);
        this.sex = sex;
        this.language = language;
        this.city = city;
        this.province = province;
        this.country = country;
        this.headImgUrl = headImgUrl;
        this.subscribeTime = subscribeTime;
    }
    
    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = FaceUtils.filterEmoji(nickName);
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    @Column(columnDefinition = "TIMESTAMP NULL")
    public Date getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(Date subscribeTime) {
        this.subscribeTime = subscribeTime;
    }
    @Column(columnDefinition = "TIMESTAMP NULL")
    public Date getUnSubscribeTime() {
        return unSubscribeTime;
    }

    public void setUnSubscribeTime(Date unSubscribeTime) {
        this.unSubscribeTime = unSubscribeTime;
    }

    @JsonIgnore
    @ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wechat_app_id")
    public WeChatApp getWeChatApp() {
        return weChatApp;
    }

    public void setWeChatApp(WeChatApp weChatApp) {
        this.weChatApp = weChatApp;
    }

    @OneToMany(cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY,mappedBy="weChatUser")
    public List<RActivityWeChatUser> getrActivityWeChatUsers() {
		return rActivityWeChatUsers;
	}
    
	public void setrActivityWeChatUsers(
			List<RActivityWeChatUser> rActivityWeChatUsers) {
		this.rActivityWeChatUsers = rActivityWeChatUsers;
	}
}
