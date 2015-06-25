package com.wxwall.wechat.api.event.model;

/**  
*   
* 项目名称：wechatapi  
* 类名称：LocationEvent  
* 类描述：上报地理位置事件  
* 创建人：Myna Wang  
* 创建时间：2014-3-11 上午11:39:15  
* @version       
*/
public class LocationEvent extends BasicEvent{
	// 地理位置纬度 
	private String Latitude;
	// 地理位置经度 
	private String Longitude;
	// 地理位置精度 
	private String Precision;
	
	public String getLatitude() {
		return Latitude;
	}
	
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	
	public String getLongitude() {
		return Longitude;
	}
	
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	
	public String getPrecision() {
		return Precision;
	}
	
	public void setPrecision(String precision) {
		Precision = precision;
	}
}
