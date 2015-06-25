package com.wxwall.modules.wechat.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wxwall.modules.wechat.entity.ActivityImage;

public interface ActivityImageDao extends PagingAndSortingRepository<ActivityImage, Long> {
	@Query("from ActivityImage a " 
			+ "where a.weChatActivity.activityMid = :activityMid and a.type = :type")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<ActivityImage> findByWeChatActivityMID(@Param("activityMid")String activityMid, @Param("type")String type);
}
