package com.wxwall.wechat.api.event.model;

/**  
*   
* 项目名称：wechatapi  
* 类名称：QRCodeEvent  
* 类描述：二维码扫描事件  
* 创建人：Myna Wang  
* 创建时间：2014-3-11 上午11:37:18  
* @version       
*/
public class QRCodeEvent extends BasicEvent{
	// 事件key值
	private String EventKey; 
	// 二维码的ticket，可用来换取二维码图片 
	private String Ticket;
	
	public String getEventKey() {
		return EventKey;
	}
	
	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}
	
	public String getTicket() {
		return Ticket;
	}
	
	public void setTicket(String ticket) {
		Ticket = ticket;
	}
}
