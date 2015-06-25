package com.wxwall.modules.activity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wxwall.modules.activity.entity.WechatUserFatePair;

public interface WechatUserFatePairDao extends PagingAndSortingRepository<WechatUserFatePair, Long> {
	@Query("from WechatUserFatePair a " 
			+ "where a.weChatActivity.activityMid = :activityMid")
	List<WechatUserFatePair> findByActivityMID(@Param("activityMid")String activityMid);
	
	@Modifying
	@Query(value="delete t_wechat_user_fate_pair from t_wechat_user_fate_pair join "
			+ "r_activity_wechat_user  on (t_wechat_user_fate_pair.r_activity_user_man_id = r_activity_wechat_user.id "
			+ "or t_wechat_user_fate_pair.r_activity_user_woman_id=r_activity_wechat_user.id) join t_wechat_user "
			+ "on r_activity_wechat_user.wechat_user_id = t_wechat_user.id where t_wechat_user.id=:rActivityUserID",nativeQuery=true)
	int deleteByRActivityUserID(@Param("rActivityUserID")long rActivityUserID);
	
	//删除活动所有已配对用户
	@Modifying
	@Query("delete from WechatUserFatePair a " 
			+ "where a.weChatActivity.activityMid = :activityMid")
	int deleteByActivityMID(@Param("activityMid")String activityMid);
	
	//删除活动指定配对用户
	@Modifying
	@Query("delete from WechatUserFatePair a " 
			+ "where a.id = :userPairID")
	int deleteByUserPairID(@Param("userPairID")long userPairID);
}
