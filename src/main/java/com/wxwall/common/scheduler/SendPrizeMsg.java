package com.wxwall.common.scheduler;

import com.wxwall.wechat.api.advanced.util.MakeCustomMessage;
import com.wxwall.wechat.api.advanced.util.SendCustomMessage;

public class SendPrizeMsg extends RunnerContext {
	private String message;
	private String accessToken;
	private String openID;
	
	public SendPrizeMsg(String accessToken, String openID, String message, long contextId) {
		this.accessToken = accessToken;
		this.openID = openID;
		this.message = message;
		this.contextId = "send_prize_msg_" + contextId;
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
			String jsonTextMsg=MakeCustomMessage.makeTextCustomMessage(openID, message);
			SendCustomMessage.sendCustomMessage(accessToken, jsonTextMsg);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			this.runningStatus = false;
		}
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub

	}

}
