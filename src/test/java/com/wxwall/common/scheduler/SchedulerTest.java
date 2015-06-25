package com.wxwall.common.scheduler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-wxwall.xml" })
public class SchedulerTest {
	private SchedulerManager schedulerManager;
	
	@Before
	public void before() {
		schedulerManager = SchedulerManager.getInstance();
		schedulerManager.start();
	}
	
	@Test
	public void testScheduler() throws Exception {
		int count = 0;
		while(true) {
			if (count <=3) {
				SendPrizeMsg sendPrizeMsg = new SendPrizeMsg("27NxodxBCJEUDgKSOIeyuSb0z3WUOwHDbyNnOtOrYvCvNn0mXq5ySlYTzFo"
						+ "WBoVLAoA3SJNa7RMw88BM5CwFXFn363-orkKiyiyrxmzyfm0", "o5soTuFMlKyknorUzhiy-WXBMKdM", "恭喜你中奖", 1);
				schedulerManager.submit(sendPrizeMsg);
				count ++;
			}
			Thread.sleep(1000);
		}
	}
}
