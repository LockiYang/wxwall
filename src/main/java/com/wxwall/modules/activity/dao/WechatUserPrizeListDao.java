package com.wxwall.modules.activity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wxwall.modules.activity.entity.WechatUserPrizeList;

public interface WechatUserPrizeListDao extends PagingAndSortingRepository<WechatUserPrizeList, Long> {
/*	@Query("from WechatUserPrizeList a " 
			+ "where a.activityPrize.id = :activityPrizeID")
	List<WechatUserPrizeList> findByActivityPrizeID(@Param("activityPrizeID")long activityPrizeID);
*/	
	
	@Query("from WechatUserPrizeList a " 
			+ "where a.rActivityWeChatUser.id = :rActivityWeChatUserId and a.activityPrize.id = :activityPrizeID")
	WechatUserPrizeList findByRActivityUserIDAndActivityPrizeID(@Param("activityPrizeID")long activityPrizeID, @Param("rActivityWeChatUserId")long rActivityWeChatUserId);
	
	@Query("from WechatUserPrizeList a " 
			+ "where a.activityPrize.weChatActivity.activityMid = :activityMid")
	List<WechatUserPrizeList> findByActivityMID(@Param("activityMid")String activityMid);
	
/*	@Query("from WechatUserPrizeList a " 
			+ "where a.activityPrize.weChatActivity.id = :activityID and a.activityPrize.id = :activityPrizeID")
	List<WechatUserPrizeList> findByActivityIDAndActivityPrizeID(@Param("activityID")long activityID, @Param("activityPrizeID")long activityPrizeID);*/
	
	//删除活动所有已中奖用户
	@Modifying
	@Query("delete from WechatUserPrizeList a " 
			+ "where a.activityPrize.weChatActivity.activityMid = :activityMid")
	int deleteByActivityMID(@Param("activityMid")String activityMid);
	//删除活动某奖项所有已中奖用户
	@Modifying
	@Query("delete from WechatUserPrizeList a " 
			+ "where a.activityPrize.id = :activityPrizeID")
	int deleteByActivityPrizeID(@Param("activityPrizeID")long activityPrizeID);
	
	//删除某个已中奖用户
	@Modifying
	@Query("delete from WechatUserPrizeList a " 
			+ "where a.rActivityWeChatUser.id = :rActivityWeChatUserId and a.activityPrize.id = :activityPrizeID")
	int deleteByRActivityUserIDAndActivityPrizeID(@Param("activityPrizeID")long activityPrizeID, @Param("rActivityWeChatUserId")long rActivityWeChatUserId);
}
