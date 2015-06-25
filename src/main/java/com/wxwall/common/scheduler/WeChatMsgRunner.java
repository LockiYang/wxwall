package com.wxwall.common.scheduler;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import com.wxwall.common.scheduler.RunningRunner.Progress;
import com.wxwall.common.scheduler.RunningRunner.Running;

public class WeChatMsgRunner implements Job, Running, InterruptableJob{
	protected Logger LOG = Logger.getLogger(getClass());

	private JobExecutionContext context;
	//当执行完发送消息后，清楚相应的数据到数据库
	private RunnerContext runnerContext;
	
	private Progress status = Progress.CONTINUE;
	
	private boolean interrupt = false;

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			this.context = context;
			if (init()) {
				SchedulerManager.getInstance().monitor(this);
				run();
				finish();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	protected boolean init() {
		JobDataMap map = context.getJobDetail().getJobDataMap();
		runnerContext = (RunnerContext) map.get("runnerContext");
		interrupt = false;
		return true;
	}

	public void run(){
		runnerContext.run(this);
	}


	private void finish() {
		runnerContext.cleanUp();
	}

	public Progress running() {
		return status;
	}

	public boolean isInterrupt() {
		return interrupt;
	}

	public void setInterrupt(boolean interrupt) {
		this.interrupt = interrupt;
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		// TODO Auto-generated method stub
		this.interrupt = true;
	}
}
