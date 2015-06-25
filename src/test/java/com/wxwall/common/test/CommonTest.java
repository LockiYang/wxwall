package com.wxwall.common.test;


import org.junit.Test;

import com.wxwall.common.utils.BeanUtils;
import com.wxwall.modules.user.entity.User;

public class CommonTest {
	
	@Test
	public void testName() throws Exception {
		User user1 = new User("test1", "passwd1");
		User user2 = new User("test2", "");
		System.out.println(user1);
		BeanUtils.copyNewPropertites(user1, user2);
		System.out.println(user1);
	}

}
