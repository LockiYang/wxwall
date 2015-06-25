package com.wxwall.modules.user.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.wxwall.modules.user.entity.UserLog;

public interface UserLogDao extends PagingAndSortingRepository<UserLog, Long> {

}
