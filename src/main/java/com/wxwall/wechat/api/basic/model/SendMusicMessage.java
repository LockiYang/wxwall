package com.wxwall.wechat.api.basic.model;

/**  
*   
* 项目名称：wechatapi  
* 类名称：SendMusicMessage  
* 类描述：回复音乐消息   
* 创建人：Myna Wang  
* 创建时间：2014-1-26 下午7:28:07  
* @version       
*/
public class SendMusicMessage extends SendBaseMessage{
	// 音乐  
    private SendMusic Music;  
  
    public SendMusic getMusic() {  
        return Music;  
    }  
  
    public void setMusic(SendMusic music) {  
        Music = music;  
    }  
}
