package com.wxwall.wechat.api.basic.model;

/**  
*   
* 项目名称：wechatapi 
* 类名称：GetVoiceMessage  
* 类描述：接收音频消息   
* 创建人：Myna Wang  
* 创建时间：2014-1-21 下午3:27:55  
* @version       
*/
public class GetVoiceMessage {
	// 媒体ID  
    private String MediaId;  
    // 语音格式  
    private String Format;  
    // 语音识别结果,UTF8编码
    private String Recognition;  
    
    public String getMediaId() {  
        return MediaId;  
    }  
  
    public void setMediaId(String mediaId) {  
        MediaId = mediaId;  
    }  
  
    public String getFormat() {  
        return Format;  
    }  
  
    public void setFormat(String format) {  
        Format = format;  
    }

	public String getRecognition() {
		return Recognition;
	}

	public void setRecognition(String recognition) {
		Recognition = recognition;
	}  
}
