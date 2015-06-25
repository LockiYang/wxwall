package com.wxwall.wechat.api.advanced.model;

/**  
*   
* 项目名称：wechatapi  
* 类名称：WeixinQRCode  
* 类描述：临时二维码信息 
* 创建人：Myna Wang  
* 创建时间：2014-3-7 下午2:05:14  
* @version       
*/
public class WeixinQRCode {
	// 获取的二维码ticket
	private String ticket;
	// 二维码有效时间，以秒为单位， 最大不超过1800秒
	private int expireSeconds;
	
	public String getTicket() {
		return ticket;
	}
	
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	public int getExpireSeconds() {
		return expireSeconds;
	}
	
	public void setExpireSeconds(int expireSeconds) {
		this.expireSeconds = expireSeconds;
	}
}
