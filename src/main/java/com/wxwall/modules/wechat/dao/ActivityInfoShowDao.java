package com.wxwall.modules.wechat.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wxwall.modules.wechat.entity.ActivityInfoShow;

public interface ActivityInfoShowDao extends PagingAndSortingRepository<ActivityInfoShow, Long> {
	@Query("from ActivityInfoShow a " 
			+ "where a.weChatActivity.activityMid = :activityMid")
	List<ActivityInfoShow> findByWeChatActivityMID(@Param("activityMid")String activityMid);
}
