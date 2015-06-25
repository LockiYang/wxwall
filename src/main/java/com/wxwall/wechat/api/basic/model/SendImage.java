package com.wxwall.wechat.api.basic.model;

/**  
*   
* 项目名称：wechatapi 
* 类名称：SendImage 
* 类描述：图片model
* 创建人：Myna Wang  
* 创建时间：2014-1-26 下午7:19:53  
* @version       
*/
public class SendImage{
	// 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。 
    private String MediaId;

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
    
}
