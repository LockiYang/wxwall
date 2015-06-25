package com.wxwall.wechat.api.basic.model;

/**  
*   
* 项目名称：wechatapi  
* 类名称：SendVideo  
* 类描述：视频model  
* 创建人：Myna Wang  
* 创建时间：2014-1-26 下午7:29:52  
* @version       
*/
public class SendVideo {
	// 通过上传多媒体文件，得到的id 
	private String MediaId;
	// 视频消息的标题 
	private String Title;
	// 视频消息的描述 
	private String Description;
	
	public String getMediaId() {
		return MediaId;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
}
