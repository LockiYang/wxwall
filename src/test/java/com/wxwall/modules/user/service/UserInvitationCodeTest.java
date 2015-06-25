package com.wxwall.modules.user.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wxwall.common.utils.IdUtils;
import com.wxwall.modules.user.dao.UserInvitationCodeDao;
import com.wxwall.modules.user.entity.UserInvitationCode;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-wxwall.xml" })
public class UserInvitationCodeTest {

	@Autowired private UserInvitationCodeService service;
	@Autowired private UserInvitationCodeDao dao;
	
	/**
	 * 生成新的邀请码
	 * @throws Exception
	 */
	@Test
	public void testGenerate() throws Exception {
		service.generate(10);
	}
	
	@Test
	public void testDao() throws Exception {
		UserInvitationCode userInvitationCode = new UserInvitationCode(IdUtils.uuid2());
		dao.save(userInvitationCode);
	}
}
