package com.wxwall.modules.wechat.dao;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wxwall.modules.wechat.entity.WeChatUser;

public interface WeChatUserDao extends PagingAndSortingRepository<WeChatUser, Long> {
	@Query("from WeChatUser a " + "where a.weChatApp.id = :wechatAppID and a.openID = :openID")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	WeChatUser findByWeChatAppIDAndOpenID(@Param("wechatAppID")long wechatAppID, @Param("openID")String openID);
}
