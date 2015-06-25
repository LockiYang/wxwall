package com.wxwall.wechat.api.advanced.model;

import java.util.List;

/**  
*   
* 项目名称：wechatapi  
* 类名称：WeixinUserList  
* 类描述：获取关注者列表  
* 创建人：Myna Wang  
* 创建时间：2014-3-7 下午7:53:00  
* @version       
*/
public class WeixinUserList {
	// 公众账号的总关注用户数
	private int total;
	// 获取的openID个数
	private int count;
	// OpenID列表
	private List<String> openIdList;
	// 拉取列表的后一个用户的OpenID
	private String nextOpenId;
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public List<String> getOpenIdList() {
		return openIdList;
	}
	
	public void setOpenIdList(List<String> openIdList) {
		this.openIdList = openIdList;
	}
	
	public String getNextOpenId() {
		return nextOpenId;
	}
	
	public void setNextOpenId(String nextOpenId) {
		this.nextOpenId = nextOpenId;
	}
}
