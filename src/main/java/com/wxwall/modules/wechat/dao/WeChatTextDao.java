package com.wxwall.modules.wechat.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.wxwall.modules.wechat.entity.WeChatText;

public interface WeChatTextDao extends PagingAndSortingRepository<WeChatText, Long> {

}
