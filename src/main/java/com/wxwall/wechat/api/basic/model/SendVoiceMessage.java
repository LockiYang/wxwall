package com.wxwall.wechat.api.basic.model;

/**  
*   
* 项目名称：wechatapi  
* 类名称：SendVoiceMessage  
* 类描述：回复语音消息  
* 创建人：Myna Wang  
* 创建时间：2014-1-26 下午7:20:53  
* @version       
*/
public class SendVoiceMessage extends SendBaseMessage{
	// 语音
	private SendVoice voice;

	public SendVoice getVoice() {
		return voice;
	}

	public void setVoice(SendVoice voice) {
		this.voice = voice;
	}
}
