package com.wxwall.modules.user.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.wxwall.modules.user.entity.User;

public interface UserDao extends PagingAndSortingRepository<User, Long> {

	User findByUserName(String userName);
	User findByLoginEmail(String loginEmail);
	
//	@Query("from User user where user.id=?1")
//	User findByUserId(Long id);
}
