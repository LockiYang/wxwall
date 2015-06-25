package com.wxwall.common.scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.wxwall.common.utils.ShakeManager;
import com.wxwall.modules.wechat.entity.WeChatActivity;

public class ActivityShake extends RunnerContext {
	private WeChatActivity weChatActivity;
	public static final String CONTEXT_PREFIX = "shake_";
	
	
	public ActivityShake(WeChatActivity weChatActivity) {
		this.contextId = ActivityShake.CONTEXT_PREFIX + weChatActivity.getActivityMid();
		this.weChatActivity = weChatActivity;
	}

	@Override
	public String getContextId() {
		// TODO Auto-generated method stub
		return contextId;
	}

	@Override
	public void run(WeChatMsgRunner weChatMsgRunner) {
		// TODO Auto-generated method stub
		try {
			Date startTime = Calendar.getInstance(TimeZone.getDefault()).getTime();
			while(true) {
				if (weChatMsgRunner.isInterrupt()) {
					break;
				}
				Date endTime = Calendar.getInstance(TimeZone.getDefault()).getTime();
				int shakeTime = (int)((endTime.getTime() - startTime.getTime())/1000);
				if (shakeTime >= (weChatActivity.getEndShakeTime() * 60)) {
					break;
				} else {
//					weChatActivity.setShakeTime(weChatActivity.getEndShakeTime() * 60 - shakeTime);
//					weChatActivityDao.save(weChatActivity);
					Thread.sleep(1000);
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			this.runningStatus = false;
		}
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
//		weChatActivity.setfShakeStart(false);
//		weChatActivity.setShakeTime(weChatActivity.getEndShakeTime() * 60);
//		weChatActivityDao.save(weChatActivity);
		ShakeManager.getInstance().stopShake(weChatActivity.getActivityMid());
	}

}
