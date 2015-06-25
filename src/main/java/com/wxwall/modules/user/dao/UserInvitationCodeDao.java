package com.wxwall.modules.user.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.wxwall.modules.user.entity.UserInvitationCode;

public interface UserInvitationCodeDao extends PagingAndSortingRepository<UserInvitationCode, Long> {

	UserInvitationCode findByCode(String code);
}
