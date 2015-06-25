package com.wxwall.modules.wechat.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wxwall.modules.wechat.entity.WeChatActivity;
import com.wxwall.modules.wechat.entity.WeChatApp;

public interface WeChatActivityDao extends PagingAndSortingRepository<WeChatActivity, Long> {
	WeChatActivity findByWeChatAppAndActivityMid(WeChatApp weChatApp, String activityMid);
	@Query("select max(a.sceneID) from WeChatActivity a " 
			+ "where a.weChatApp.id = :weChatId")
	int findByMaxSceneId(@Param("weChatId")long weChatId);
	@Query("from WeChatActivity a " 
			+ "where a.weChatApp.id = :weChatID and a.sceneID = :sceneID")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	WeChatActivity findBySceneIDAndWeChatID(@Param("weChatID")long weChatID, @Param("sceneID")int sceneID);
	 
//	@Query("from WeChatActivity a " 
//			+ "where a.weChatApp.id = :weChatID and a.signKeyWord = :signKeyWord and :now < a.endDate")
//	WeChatActivity findBySignKeyWordAndWeChatIDAndNow(@Param("weChatID")long weChatID, @Param("now")Date now, @Param("signKeyWord")String signKeyWord);
	
	
	@Query("from WeChatActivity a " + "where a.activityMid = :activityMid")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	WeChatActivity findByActivityMid(@Param("activityMid")String activityMid);
	
	/*
	 * 某个用户正在进行的活动
	 */
	@Query("from WeChatActivity a " 
			+ "where a.weChatApp.id = :weChatAppId and :now > a.startDate and :now < a.endDate")
	Page<WeChatActivity> findByWeChatAppAndInProgress(@Param("weChatAppId")long weChatAppId, @Param("now")Date now, Pageable pageable);
	
	/*
	 * 某个用户筹备中的活动
	 */
	@Query("from WeChatActivity a " 
			+ "where a.weChatApp.id = :weChatAppId and :now < a.startDate")
	Page<WeChatActivity> findByWeChatAppAndBeforeProgress(@Param("weChatAppId")long weChatAppId, @Param("now")Date now, Pageable pageable);
	
	/*
	 * 某个用户已结束的活动
	 */
	@Query("from WeChatActivity a " 
			+ "where a.weChatApp.id = :weChatAppId and :now > a.endDate")
	Page<WeChatActivity> findByWeChatAppAndAfterProgress(@Param("weChatAppId")long weChatAppId, @Param("now")Date now, Pageable pageable);
	/**
	 * 寻找未结束的活动列表
	 * @param weChatAppId
	 * @param now
	 * @param pageable
	 * @return
	 */
	@Query("from WeChatActivity a " 
			+ "where a.weChatApp.id = :weChatAppId and :now < a.endDate")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	List<WeChatActivity> findByWeChatAppAndBeforeEndProgress(@Param("weChatAppId")long weChatAppId, @Param("now")Date now);
	

}
