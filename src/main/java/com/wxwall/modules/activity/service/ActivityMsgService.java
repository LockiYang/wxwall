package com.wxwall.modules.activity.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wxwall.common.entity.Result;
import com.wxwall.common.entity.UpbangMsg;
import com.wxwall.common.service.BaseService;
import com.wxwall.common.utils.CommonUtils;
import com.wxwall.common.utils.DateUtils;
import com.wxwall.modules.wechat.dao.WeChatActivityDao;
import com.wxwall.modules.wechat.dao.WeChatUserMsgDao;
import com.wxwall.modules.wechat.entity.WeChatActivity;
import com.wxwall.modules.wechat.entity.WeChatUser;
import com.wxwall.modules.wechat.entity.WeChatUserMsg;

@Service
@Transactional
public class ActivityMsgService extends BaseService {

	@Autowired private WeChatUserMsgDao weChatUserMsgDao;
	@Autowired private WeChatActivityDao weChatActivityDao;
	
	/**
	 * 获取活动的消息列表
	 * 
	 * @param activityId
	 * @param page
	 * @param size
	 * @param status
	 * @return
	 */
	public Page<WeChatUserMsg> getActivityMsgList(String activityMid, int page, int size, byte status) {
		WeChatActivity weChatActivity = weChatActivityDao.findByActivityMid(activityMid);
		if (weChatActivity == null) {
			return null;
		}
		
		PageRequest pageRequest = new PageRequest(page, size, Direction.DESC,new String[] { "updateTime", "createTime"});
		return weChatUserMsgDao.findByActivityMIdStatus(activityMid, status, pageRequest);
	}
	
	/**
	 * 消息下墙
	 * @param activityMid
	 * @param msgId
	 */
	public void updateMsgStatus(long msgId, byte status) {
		WeChatUserMsg weChatUserMsg = weChatUserMsgDao.findOne(msgId);
		weChatUserMsg.setStatus(status);
		weChatUserMsg.setUpdateTime(DateUtils.now());
		weChatUserMsgDao.save(weChatUserMsg);
	}
	
	/**
	 * 返回最新的上墙信息
	 * 
	 * @param activityID
	 * @return
	 */
	public Result getUpbangMsg(String activityMid, Date queryDate, String serverUrl) {
		// TODO
		Result result = new Result();
		WeChatActivity weChatActivity = weChatActivityDao.findByActivityMid(activityMid);
		if (weChatActivity == null) {
			LOG.warn("活动ID[" + activityMid + "]在数据库中未存在!");
			result.setSuccess(false);
			result.setMessage("请求参数错误");
			return result;
		}
		PageRequest pageRequest = new PageRequest(0, 1, Direction.ASC,
				new String[] { "updateTime" });
		// 查询系统已经上墙的时间戳
		Date updateTime = null;
		Date msgTimeTag = weChatActivity.getMsgTimeTag();
		
		if (msgTimeTag == null) {// 判断是不是第一次获取上墙信息
			updateTime = CommonUtils.INIT_DATE_TIME;
		} else if (msgTimeTag != null) {// 判断是不是获取上墙最新信息
			updateTime = msgTimeTag;
		} 

		Page<WeChatUserMsg> weChatUserMsgs = weChatUserMsgDao
				.findByActivityMIDStatusUpUpdateTime(activityMid,
						WeChatUserMsg.MSG_STATUS.SUCCESS.getIndex(),
						updateTime, pageRequest);
		
		long totalUpbangMsgNum = weChatUserMsgDao.findTotalByActivityMIDStatusDownUpdateTime(activityMid, 
				(byte) WeChatUserMsg.MSG_STATUS.SUCCESS.getIndex(), msgTimeTag);
		result.setTotal(totalUpbangMsgNum);
		if (weChatUserMsgs.getContent().size() == 0) {
			// 随机取出消息
			PageRequest pageRequestRandom = new PageRequest(0, 1);
			weChatUserMsgs = weChatUserMsgDao.findRandomMsgByActivityMIDStatus(activityMid,
						WeChatUserMsg.MSG_STATUS.SUCCESS.getIndex(), pageRequestRandom);
			if (weChatUserMsgs.getContent().size() == 0) {
				result.setSuccess(true);
				return result;
			}
			WeChatUserMsg weChatUserMsg = weChatUserMsgs.getContent().get(0);
			WeChatUser weChatUser = weChatUserMsg.getrActivityWeChatUser().getWeChatUser();
			UpbangMsg upbangMsg = new UpbangMsg(weChatUserMsg, weChatUser, serverUrl);
			result.setSuccess(true);
			result.setData(upbangMsg);
			return result;
		} else {
			// 取出消息
			WeChatUserMsg weChatUserMsg = weChatUserMsgs.getContent().get(0);
			WeChatUser weChatUser = weChatUserMsg.getrActivityWeChatUser().getWeChatUser();
			UpbangMsg upbangMsg = new UpbangMsg(weChatUserMsg, weChatUser, serverUrl);
			
			// 取出数据并更新
			if (msgTimeTag == null) {// 新增上墙标记
				weChatActivity.setMsgTimeTag(weChatUserMsg.getUpdateTime());
				weChatActivityDao.save(weChatActivity);
			} else {// 更新上墙消息
				if (weChatUserMsg.getUpdateTime().compareTo(msgTimeTag) > 0) {
					weChatActivity.setMsgTimeTag(weChatUserMsg.getUpdateTime());
					weChatActivityDao.save(weChatActivity);
				}
			}

			result.setSuccess(true);
			result.setData(upbangMsg);
			return result;
		}
	}
}
