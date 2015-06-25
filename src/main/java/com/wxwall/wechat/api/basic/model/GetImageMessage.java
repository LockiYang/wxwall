package com.wxwall.wechat.api.basic.model;


/**  
*   
* 项目名称：wechatapi 
* 类名称：GetImageMessage  
* 类描述：接收图片消息  
* 创建人：Myna Wang  
* 创建时间：2014-1-21 下午3:26:09  
* @version       
*/
public class GetImageMessage extends GetBaseMessage{
	// 图片链接  
    private String PicUrl; 
 	// 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。 
    private String MediaId;
    
    public String getPicUrl() {  
        return PicUrl;  
    }  
  
    public void setPicUrl(String picUrl) {  
        PicUrl = picUrl;  
    }

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}  
}
