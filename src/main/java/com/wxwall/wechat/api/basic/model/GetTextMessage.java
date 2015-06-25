package com.wxwall.wechat.api.basic.model;

/**  
*   
* 项目名称：wechatapi  
* 类名称：GetTextMessage  
* 类描述：接收文本消息  
* 创建人：Myna Wang  
* 创建时间：2014-1-21 下午3:27:39  
* @version       
*/
public class GetTextMessage extends GetBaseMessage{
	// 消息内容  
    private String Content;  
  
    public String getContent() {  
        return Content;  
    }  
  
    public void setContent(String content) {  
        Content = content;  
    }  
} 
