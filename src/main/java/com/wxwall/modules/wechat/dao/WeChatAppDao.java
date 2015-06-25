package com.wxwall.modules.wechat.dao;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wxwall.modules.wechat.entity.WeChatApp;

public interface WeChatAppDao extends PagingAndSortingRepository<WeChatApp, Long> {
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	WeChatApp findByMid(String mid);
	@Query("from WeChatApp a " + "where a.user.id = :userId")
	WeChatApp findByUserId(@Param("userId")long userId);
}
