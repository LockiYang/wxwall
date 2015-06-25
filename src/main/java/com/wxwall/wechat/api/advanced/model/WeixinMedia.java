package com.wxwall.wechat.api.advanced.model;

/**  
*   
* 项目名称：wechatapi  
* 类名称：WeixinMedia  
* 类描述：媒体文件信息  
* 创建人：Myna Wang  
* 创建时间：2014-3-8 下午6:19:39  
* @version       
*/
public class WeixinMedia {
	// 媒体类型
	private String type;
	// 媒体文件标识或缩略图的媒体文件标识
	private String mediaId;
	// 媒体文件上传的时间
	private int createdAt;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getMediaId() {
		return mediaId;
	}
	
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	public int getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(int createdAt) {
		this.createdAt = createdAt;
	}
}
