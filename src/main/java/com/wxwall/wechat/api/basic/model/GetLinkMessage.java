package com.wxwall.wechat.api.basic.model;


/**  
*   
* 项目名称：wechatapi  
* 类名称：GetLinkMessage  
* 类描述：接收链接消息    
* 创建人：Myna Wang  
* 创建时间：2014-1-21 下午3:26:22  
* @version       
*/
public class GetLinkMessage {
	// 消息标题  
    private String Title;  
    // 消息描述  
    private String Description;  
    // 消息链接  
    private String Url;  
  
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
  
    public String getUrl() {  
        return Url;  
    }  
  
    public void setUrl(String url) {  
        Url = url;  
    }  
}
