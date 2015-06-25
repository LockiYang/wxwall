package com.wxwall.modules.activity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wxwall.modules.activity.entity.ActivityUserRemind;

public interface ActivityUserRemindDao extends PagingAndSortingRepository<ActivityUserRemind, Long> {
	@Query("from ActivityUserRemind a " 
			+ "where a.weChatActivity.activityMid = :activityMid")
	List<ActivityUserRemind> findByActivityMID(@Param("activityMid")String activityMid);
	
	//删除活动所有已配对用户
	@Modifying
	@Query("delete from ActivityUserRemind a " 
			+ "where a.weChatActivity.activityMid = :activityMid")
	int deleteByActivityMID(@Param("activityMid")String activityMid);
	//删除活动指定配对用户
	@Modifying
	@Query("delete from ActivityUserRemind a " 
			+ "where a.id = :userPairID")
	int deleteByUserRemindID(@Param("userPairID")long userRemindID);
}
