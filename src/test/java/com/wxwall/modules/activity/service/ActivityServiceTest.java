package com.wxwall.modules.activity.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wxwall.modules.wechat.entity.WeChatActivity;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-wxwall.xml" })
public class ActivityServiceTest {
	
	@Autowired 
	private ActivityService activityService;
	
	/*@Before
	public void before() {
		UserUtils.initTest();
	}*/

	@Test
	public void testUpdateStatusRActWeUserByOpenID() throws Exception {
		//activityService.updateStatusRActWeUserByOpenID("o5soTuKxiVTbe3McqXCv4Vp7C5KU", (byte)1, (byte)3);
		//activityService.updateStatusRActWeUserByOpenID(2, (byte)1, (byte)3);
		//RActivityWeChatUser rActivityWeChatUser = activityService.getRActWeUserByWeActIDAndOpenID(1, "o5soTuFMlKyknorUzhiy-WXBMKdM");
//		RActivityWeChatUser rActivityWeChatUser = activityService.findRActWeUserByWeAppIDAndOpenIDAndEndDate(1, "o5soTuFMlKyknorUzhiy-WXBMKdM", new Date());
//		System.out.println(rActivityWeChatUser);

	}
	
	@Test
	public void testName() throws Exception {
		//activityService.listAllUserForPrize(1);
		//activityService.initUserInfoForShake(1);
	}
	
	@Test
	public void testGetMyActivitys() throws Exception {
		Page<WeChatActivity> myActivitys = activityService.getMyActivitys(0, 1, "aa");
		System.out.println(myActivitys.getNumber());
	}
}
