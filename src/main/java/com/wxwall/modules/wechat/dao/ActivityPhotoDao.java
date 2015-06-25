package com.wxwall.modules.wechat.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wxwall.modules.wechat.entity.ActivityPhoto;

public interface ActivityPhotoDao extends PagingAndSortingRepository<ActivityPhoto, Long> {
	@Query("from ActivityPhoto a " 
			+ "where a.weChatActivity.activityMid = :activityMid")
	List<ActivityPhoto> findByWeChatActivityMID(@Param("activityMid")String activityMid);
	
	@Query(nativeQuery=true, value="select * from t_activity_photo " 
			+ "where wechat_activity_id=:activityId limit :serial, 1")
	ActivityPhoto findByWeChatActivityIDAndSerialD(@Param("activityId")long activityId, @Param("serial")int serial);
}
