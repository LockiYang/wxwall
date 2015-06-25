package com.wxwall.modules.wechat.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wxwall.modules.wechat.entity.RActivityWeChatUser;

public interface RActivityWeChatUserDao extends PagingAndSortingRepository<RActivityWeChatUser, Long> {
	/**
	 * 通过uuid找到相应用户
	 * @param uuid
	 * @return
	 */
	@Query("from RActivityWeChatUser a " 
			+ "where a.uuid = :uuid")
	RActivityWeChatUser findByUuid(@Param("uuid")String uuid);
	//获取活动已签到所有用户
	@Query("from RActivityWeChatUser a " 
			+ "where a.weChatActivity.activityMid = :activityMid and a.status = 1")
	List<RActivityWeChatUser> findSignUsersByWeChatActivityMIDAndStatus(@Param("activityMid")String activityMid);
	
	@Query("from RActivityWeChatUser a " 
			+ "where a.weChatActivity.activityMid = :activityMid and a.status = :status")
	Page<RActivityWeChatUser> findUsersByWeChatActivityMIDAndStatus(@Param("activityMid")String activityMid, @Param("status")byte status, Pageable pageable);
	
	@Query("from RActivityWeChatUser a " 
			+ "where a.weChatActivity.activityMid = :activityMid and a.weChatUser.sex = :sex and a.status = 1")
	List<RActivityWeChatUser> findByWeChatActivityMIDAndSex(@Param("activityMid")String activityMid, @Param("sex")byte sex);
	
	/**
	 * 获取活动用户
	 * @param wechatActivityID
	 * @param openID
	 * @return
	 */
	@Query("from RActivityWeChatUser a " 
			+ "where a.weChatActivity.activityMid = :activityMid and a.weChatUser.openID = :openID")
	RActivityWeChatUser findRActWeUserByWeActMIDAndOpenID(@Param("activityMid")String activityMid, @Param("openID")String openID);
	
	/**
	 * 获取活动签到用户
	 * @param wechatActivityID
	 * @param openID
	 * @return
	 */
	@Query("from RActivityWeChatUser a " 
			+ "where a.weChatActivity.activityMid = :activityMid and a.weChatUser.openID = :openID and a.status = 1")
	RActivityWeChatUser findSignRActWeUserByWeActMIDAndOpenID(@Param("activityMid")String activityMid, @Param("openID")String openID);
	
	/**
	 * 获取所有签到用户
	 * @param wechatActivityID
	 * @param updateTime
	 * @param pageable
	 * @return
	 */
	@Query("from RActivityWeChatUser a " 
			+ "where a.weChatActivity.activityMid = :activityMid and a.status = 1 and a.updateTime > :updateTime")
	Page<RActivityWeChatUser> findSignUsersByActivityMIDUpUpdateTime(@Param("activityMid")String activityMid, @Param("updateTime")Date updateTime, Pageable pageable);
	
	//按顺序获取单个签到用户，序号从0开始
	@Query(nativeQuery=true, value="select * from r_activity_wechat_user where wechat_activity_id=:activityId ORDER BY signin_date ASC limit :serial,1")
	RActivityWeChatUser findSingleByActivityId(@Param("activityId")long activityId, @Param("serial")int serial);
	//获取活动未结束的签到用户
	@Query("from RActivityWeChatUser a " 
			+ "where a.weChatUser.weChatApp.id = :wechatAppID and a.weChatUser.openID = :openID "
			+ "and a.status = 1 and a.weChatActivity.endDate >= :endDate")
	RActivityWeChatUser findSignRActWeUserByWeAppIDAndOpenIDAndEndDate(@Param("wechatAppID")long wechatAppID, 
			@Param("openID")String openID, @Param("endDate")Date endDate);
	
	/**
	 * 更新所有的用户|活动关系状态
	 * @param openID
	 * @param orginStatus
	 * @param status
	 * @return
	 */
	@Modifying
	@Query(nativeQuery=true, value="update r_activity_wechat_user a join t_wechat_user b on a.wechat_user_id = b.id set a.status"
			+ " = :status where b.openid = :openID and a.status= :orginStatus")
	int updateStatusRActWeUserByOpenID(@Param("openID")String openID, 
			@Param("orginStatus")byte orginStatus, @Param("status")byte status);
	
	/**
	 * 更新活动未结束的用户|活动关系状态
	 * @param openID
	 * @param orginStatus
	 * @param status
	 * @param endDate
	 * @return
	 */
/*	@Modifying
	@Query(nativeQuery=true, value="update r_activity_wechat_user a join t_wechat_user b on a.wechat_user_id = b.id join "
			+ "t_wechat_activity c on a.wechat_activity_id = c.id set a.status"
			+ " = :status where b.openid = :openID and a.status= :orginStatus and c.end_date < :endDate")
	int updateStatusRActWeUserByOpenIDAndEndDate(@Param("openID")String openID, 
			@Param("orginStatus")byte orginStatus, @Param("status")byte status, @Param("endDate")Date endDate);*/
	
	@Modifying
	@Query(nativeQuery=true, value="update r_activity_wechat_user a join t_wechat_activity b on a.wechat_activity_id=b.id "
			+ "set a.shake_num=0 where b.activity_mid = :activityMid")
	int updateShakeNumByActivityMID(@Param("activityMid")String activityMid);
	
//	@Query("from RActivityWeChatUser a " 
//			+ "where a.weChatActivity.activityMid = :activityMid and a.status = 1 and a.shakeNum > 0")
//	Page<RActivityWeChatUser> findShakeUsersByActivityMID(@Param("activityMid")String activityMid, Pageable pageable);
//	
//	@Query("select count(*) from RActivityWeChatUser a "
//			+ "where a.weChatActivity.activityMid = :activityMid and a.status = 1 and a.shakeNum > 0")
//	int getTotalShakeUsersByActivityMID(@Param("activityMid")String activityMid);
}
