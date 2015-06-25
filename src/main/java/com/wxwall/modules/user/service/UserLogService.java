package com.wxwall.modules.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wxwall.common.service.BaseService;
import com.wxwall.modules.user.dao.UserLogDao;
import com.wxwall.modules.user.entity.UserLog;

@Service
@Transactional
public class UserLogService extends BaseService {

	@Autowired
	private UserLogDao userLogDao;

	public void saveUserLog(UserLog userLog) {
		userLogDao.save(userLog);
	}
	
}
