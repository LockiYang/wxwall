package com.wxwall.common.listener;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.wxwall.common.scheduler.SchedulerManager;
import com.wxwall.modules.activity.service.ActivityService;

public class InitListener {
	protected Logger LOG = Logger.getLogger(getClass());
	
	@Autowired 
	private ActivityService activityService;
	
	@PostConstruct
	public void init() {
		// TODO Auto-generated method stub
		LOG.info("系统初始化开始！");
		initData();
		LOG.info("系统初始化成功！");
	}
	
	@PreDestroy
	public void destory() {
		LOG.info("系统销毁！");
		SchedulerManager.getInstance().shutdown();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e);
		}
		LOG.info("系统成功！");
	}
	
	private void initData() {
		try {
			SchedulerManager.getInstance().start();
//			//初始化还未开始活动的摇一摇值
//			activityService.updateShakeWeChatActivity();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
