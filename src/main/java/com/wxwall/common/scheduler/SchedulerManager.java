package com.wxwall.common.scheduler;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.StdSchedulerFactory;

import com.wxwall.common.scheduler.RunningRunner.Running;
import com.wxwall.common.utils.CommonUtils;

public class SchedulerManager {
	protected Logger LOG = Logger.getLogger(getClass());

	private static final String DEFAULT_THREAD_SIZE = "15";
	private static final String DEFAULT_SCHEDULER_GROUP = "scheduler";
	private static SchedulerManager instance;
	private Scheduler scheduler;
	private RunningRunner runner;

	private SchedulerManager() {

	}

	public static SchedulerManager getInstance() {
		if (instance == null) {
			synchronized (SchedulerManager.class) {
				if (instance == null) {
					instance = new SchedulerManager();
				}
			}
		}
		return instance;
	}

	public void start() {
		try {
			LOG.info("SchedulerManager starting.");
			startScheduler();
			// loadCrontabs();
			startRunner();
			LOG.info("SchedulerManager started.");
		} catch (SchedulerException e) {
			LOG.error("SchedulerManager failed to start.", e);
		}
	}

	protected void startScheduler() throws SchedulerException {
		Properties props = new Properties();
		String threadPool = CommonUtils.getPropertiesTool().getString(
				"org.quartz.threadPool.threadCount", DEFAULT_THREAD_SIZE);
		props.setProperty("org.quartz.threadPool.threadCount", threadPool);

		StdSchedulerFactory ssf = new StdSchedulerFactory(props);
		scheduler = ssf.getScheduler();
		scheduler.start();
	}

	protected void loadCrontabs() {
		LOG.info("Crontabs loaded.");
	}

	protected void startRunner() {
		runner = new RunningRunner();
		runner.start();
	}

	public boolean submit(RunnerContext runnerContext) {
		if (scheduler == null)
			return false;

		if (runnerContext.getContextId() == null)
			return false;

		JobDetail job = JobBuilder
				.newJob(WeChatMsgRunner.class)
				.withIdentity(runnerContext.getContextId(),
						DEFAULT_SCHEDULER_GROUP).build();

		JobDataMap map = job.getJobDataMap();
		map.put("runnerContext", runnerContext);

		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(runnerContext.getContextId(),
						DEFAULT_SCHEDULER_GROUP).startNow().build();

		try {
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
			LOG.error("SchedulerManager failed to schedule.");
			return false;
		}

		return true;
	}

	public boolean killJob(String contextId) {
		if (scheduler == null)
			return false;

		if (contextId == null)
			return false;

		try {
			scheduler.interrupt(new JobKey(contextId,
					DEFAULT_SCHEDULER_GROUP));
		} catch (UnableToInterruptJobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public boolean monitor(Running running) {
		if (runner == null)
			return false;

		runner.add(running);
		return true;
	}

	public void shutdown() {
		LOG.info("SchedulerManager shutting down.");

		if (scheduler != null) {
			try {
				scheduler.clear();
				scheduler.shutdown();
			} catch (SchedulerException e) {
				e.printStackTrace();
				LOG.error("scheduler failed to shutdown.");
			}
		}

		if (runner != null) {
			runner.shutdown();
		}
		LOG.info("SchedulerManager shutdown complete.");
	}

}
