package com.wxwall.common.scheduler;

import org.apache.log4j.Logger;

public abstract class RunnerContext {
	protected Logger LOG = Logger.getLogger(getClass());
	
	protected String contextId;
	protected boolean runningStatus = true;
	protected String errorMsg;
	
	public abstract String getContextId();
	public abstract void run(WeChatMsgRunner weChatMsgRunner);
	public abstract void cleanUp(); 
}
