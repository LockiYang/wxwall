package com.wxwall.modules.activity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wxwall.modules.activity.entity.ActivityPrize;

public interface ActivityPrizeDao extends PagingAndSortingRepository<ActivityPrize, Long> {
	@Query("from ActivityPrize a "  
			+ "where a.weChatActivity.activityMid = :activityMid")
	List<ActivityPrize> findByActivityMID(@Param("activityMid")String activityMid);
	
	@Query("from ActivityPrize a "  
			+ "where a.weChatActivity.activityMid = :activityMid and a.id = :prizeId")
	ActivityPrize findByWechatActivityMIDAndPrizeId(@Param("activityMid")String activityMid, @Param("prizeId")long prizeId);
}
