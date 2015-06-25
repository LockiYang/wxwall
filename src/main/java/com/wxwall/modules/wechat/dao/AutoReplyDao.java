package com.wxwall.modules.wechat.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.wxwall.modules.wechat.entity.AutoReply;

public interface AutoReplyDao extends PagingAndSortingRepository<AutoReply, Long> {

}
