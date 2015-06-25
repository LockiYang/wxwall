package com.wxwall.wechat.api.advanced.model;

/**  
*   
* 项目名称：wechatapi  
* 类名称：WeixinGroup  
* 类描述：公众账号分组信息  
* 创建人：Myna Wang  
* 创建时间：2014-3-8 下午1:15:31  
* @version       
*/
public class WeixinGroup {
	// 分组id
	private int id;
	// 分组名称
	private String name;
	// 分组内用户数
	private int count;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
}
