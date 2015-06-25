package com.wxwall.common.scheduler;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

public class RunningRunner implements Runnable {
	protected Logger LOG = Logger.getLogger(getClass());

	public static enum Progress {
		CONTINUE, EXIT
	};

	interface Running {
		Progress running();
	}

	private LinkedBlockingQueue<Running> runnings;

	private Thread t;

	public RunningRunner() {
		runnings = new LinkedBlockingQueue<Running>();
	}

	public void start() {
		t = new Thread(this);
		t.start();
	}

	public void run() {
		LOG.info("RunningRunner started.");

		while (true) {
			try {
				// block if no more running
				Running running = runnings.take();

				Progress progress = running.running();
				switch (progress) {
				case CONTINUE:
					runnings.put(running);
					break;
				case EXIT:
					break;
				}

				LOG.debug("go to sleep...");
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return;
			}
		}

	}

	public boolean add(Running running) {
		return runnings.offer(running);
	}

	public void shutdown() {
		LOG.info("RunningRunner shutting down.");
		try {
			t.interrupt();
			t.join(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LOG.info("RunningRunner shutdown complete.");
	}
}