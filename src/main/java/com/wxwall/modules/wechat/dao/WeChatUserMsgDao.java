package com.wxwall.modules.wechat.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wxwall.modules.wechat.entity.WeChatUserMsg;

public interface WeChatUserMsgDao extends PagingAndSortingRepository<WeChatUserMsg, Long> {
	@Query("from WeChatUserMsg a " 
			+ "where a.status = :status and a.rActivityWeChatUser.weChatActivity.activityMid = :activityMid and a.updateTime > :updateTime")
	Page<WeChatUserMsg> findByActivityMIDStatusUpUpdateTime(@Param("activityMid")String activityMid, @Param("status")byte status, @Param("updateTime")Date updateTime, Pageable pageable);
	
	@Query("from WeChatUserMsg a " 
			+ "where a.status = :status and a.rActivityWeChatUser.weChatActivity.activityMid = :activityMid order by rand()")
	Page<WeChatUserMsg> findRandomMsgByActivityMIDStatus(@Param("activityMid")String activityMid, @Param("status")byte status, Pageable pageable);
	
	@Query("from WeChatUserMsg a " 
			+ "where a.status = :status and a.rActivityWeChatUser.weChatActivity.activityMid = :activityMid and a.updateTime <= :updateTime")
	Page<WeChatUserMsg> findByActivityMIDStatusDownUpdateTime(@Param("activityMid")String activityMid, @Param("status")byte status, @Param("updateTime")Date updateTime, Pageable pageable);
	@Query("from WeChatUserMsg a " 
			+ "where a.status = :status and a.rActivityWeChatUser.weChatActivity.activityMid = :activityMid")
	Page<WeChatUserMsg> findByActivityMIdStatus(@Param("activityMid")String activityMid, @Param("status")byte status, Pageable pageable);
	@Query("select count(*) from WeChatUserMsg a " 
			+ "where a.status = :status and a.rActivityWeChatUser.weChatActivity.activityMid = :activityMid and a.updateTime <= :updateTime")
	int findTotalByActivityMIDStatusDownUpdateTime(@Param("activityMid")String activityMid, @Param("status")byte status, @Param("updateTime")Date updateTime);
	@Query("select count(*) from WeChatUserMsg a " 
			+ "where a.rActivityWeChatUser.weChatActivity.activityMid = :activityMid")
	int findTotalUserMsgByActivityMID(@Param("activityMid")String activityMid);
	
	@Query("select count(*) from WeChatUserMsg a " 
			+ "where a.rActivityWeChatUser.weChatActivity.activityMid = :activityMid and a.rActivityWeChatUser.weChatUser.openID = :openID " 
			+ "and a.createTime > a.rActivityWeChatUser.signInDate")
	int findTotalUserMsgByActivityMIDAndOpendID(@Param("activityMid")String activityMid, @Param("openID")String openID);
}
