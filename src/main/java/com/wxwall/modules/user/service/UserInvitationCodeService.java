package com.wxwall.modules.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wxwall.common.utils.IdUtils;
import com.wxwall.modules.user.dao.UserInvitationCodeDao;
import com.wxwall.modules.user.entity.UserInvitationCode;

@Service
@Transactional
public class UserInvitationCodeService {

	@Autowired
	private UserInvitationCodeDao userInvitationCodeDao;

	/**
	 * 生成新的邀请码
	 * 
	 * @param num
	 */
	public void generate(int num) {
		int sum = (num < 1) ? 1 : num;
		List<UserInvitationCode> codeList = new ArrayList<UserInvitationCode>();
		for (int i = 0; i < sum; i++) {
			UserInvitationCode userInvitationCode = new UserInvitationCode(IdUtils.uuid2().toUpperCase());
			codeList.add(userInvitationCode);
		}
		
		userInvitationCodeDao.save(codeList);
	}
	
	public UserInvitationCode getUnUsedCode(String code) {
		return userInvitationCodeDao.findByCode(code);
	}

	// 统计邀请码使用情况
	public void statistics() {

	}
}
