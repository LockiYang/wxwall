package com.wxwall.wechat.api.basic.model;

/**  
*   
* 项目名称：wechatapi  
* 类名称：SendImageMessage  
* 类描述：回复图片消息  
* 创建人：Myna Wang  
* 创建时间：2014-3-11 上午10:49:42  
* @version       
*/
public class SendImageMessage extends SendBaseMessage{
	// 图片
	private SendImage image;

	public SendImage getImage() {
		return image;
	}

	public void setImage(SendImage image) {
		this.image = image;
	}
}
