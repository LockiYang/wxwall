package com.wxwall.modules.wechat.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wxwall.modules.wechat.entity.WeChatAppMenu;

public interface WeChatAppMenuDao extends PagingAndSortingRepository<WeChatAppMenu, Long> {
	@Query("from WeChatAppMenu a " 
			+ "where a.weChatApp.id = :weChatID and a.id = :menuId")
	WeChatAppMenu findByMenuIDAndWeChatID(@Param("weChatID")long weChatID, @Param("menuId")long menuId);
}
