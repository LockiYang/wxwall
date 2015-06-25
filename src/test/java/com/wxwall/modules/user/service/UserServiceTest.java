 package com.wxwall.modules.user.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wxwall.common.test.SpringTransactionalTestCase;
import com.wxwall.modules.user.dao.UserDao;
import com.wxwall.modules.user.entity.User;
import com.wxwall.modules.user.utils.UserUtils;
import com.wxwall.modules.wechat.dao.WeChatActivityDao;
import com.wxwall.modules.wechat.entity.WeChatActivity;
import com.wxwall.modules.wechat.entity.WeChatApp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-wxwall.xml" })
public class UserServiceTest extends SpringTransactionalTestCase {
	
	@Autowired private UserService userService;
	@Autowired private UserDao userDao;
	@Autowired private WeChatActivityDao activityDao;
	
	@Before
	public void before() {
//		UserUtils.initTest();
	}

	@Test
	public void testRegister() throws Exception {
		User newUser = new User("\\xF0\\x9F\\x98\\x84","test");
//		userService.register(newUser, null);
	}
	
	@Test
	public void testUserUtils() {
		User user = UserUtils.getUser();
		System.out.println(user.getLoginEmail());
	}
	
	@Test
	public void testUpdate() throws Exception {
		User user = new User("lol@lol.com", "");
		user.setId(2L);
		user.setUserName("啦啦啦");
		userDao.save(user);
	}
	
	@Test
	public void testGetUser() throws Exception {
		WeChatActivity f1 = activityDao.findByActivityMid("f7b0687e1dfb795e67f36595564a261c");
		System.out.println("------------原始查询:" + f1.getEndShakeNum());
		WeChatActivity f2 = activityDao.findByActivityMid("f7b0687e1dfb795e67f36595564a261c");
		System.out.println("------------缓存查询1:" + f2.getEndShakeNum());
		f2.setEndShakeNum(200);
		WeChatActivity f3 = activityDao.findByActivityMid("f7b0687e1dfb795e67f36595564a261c");
		System.out.println("------------缓存查询2(未更新数据库):" + f3.getEndShakeNum());
		activityDao.save(f2);
		WeChatActivity f4 = activityDao.findByActivityMid("f7b0687e1dfb795e67f36595564a261c");
		System.out.println("------------缓存查询3(已更新数据库):" + f4.getEndShakeNum());
	}
	
	@Test
	public void testGetUserByLoginName() throws Exception {
		User user = userService.get("admin");
	}
	
	
}
