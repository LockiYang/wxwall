package com.wxwall.wechat.api.basic.model;

/**  
*   
* 项目名称：wechatapi  
* 类名称：GetVideoMessage  
* 类描述：接收视频消息  
* 创建人：Myna Wang  
* 创建时间：2014-1-26 下午7:19:02  
* @version       
*/
public class GetVideoMessage {
	// 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。 
	private String MediaId;
	// 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
	private String ThumbMediaId;
	
	public String getMediaId() {
		return MediaId;
	}
	
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	
	public String getThumbMediaId() {
		return ThumbMediaId;
	}
	
	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}
}
