package com.wxwall.wechat.api.basic.model;

/**  
*   
* 项目名称：wechatapi  
* 类名称：SendVideoMessage  
* 类描述：回复视频消息  
* 创建人：Myna Wang  
* 创建时间：2014-1-26 下午7:22:50  
* @version       
*/
public class SendVideoMessage extends SendBaseMessage{
	// 视频
	private SendVideo video;

	public SendVideo getVideo() {
		return video;
	}

	public void setVideo(SendVideo video) {
		this.video = video;
	}
	
}
