package com.wxwall.modules.wechat.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wxwall.modules.wechat.entity.WeActivityScheduler;

public interface WeActivitySchedulerDao extends PagingAndSortingRepository<WeActivityScheduler, Long> {
	@Query("from WeActivityScheduler a "  
			+ "where a.weChatActivity.activityMid = :activityMid and a.id = :schedulerId")
	WeActivityScheduler findByWechatActivityMIDAndSchedulerId(@Param("activityMid")String activityMid, @Param("schedulerId")long schedulerId);
}
