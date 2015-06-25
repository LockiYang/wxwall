package com.wxwall.wechat.api.menu.model;
/**  
*   
* 项目名称：wechatapi  
* 类名称：CommonButton  
* 类描述：普通按钮（子按钮）  
* 创建人：Myna Wang  
* 创建时间：2014-3-11 下午12:40:03  
* @version       
*/
public class CommonButton extends Button{
	// 菜单的响应动作类型，目前有click、view两种类型 
	private String type;
	// 菜单KEY值，用于消息接口推送，不超过128字节
    private String key; 
	// 网页链接，用户点击菜单可打开链接，不超过256字节 
    private String url;
	public String getType() {  
        return type;  
    }  
  
	public void setType(String type) {  
        this.type = type;  
    }  
    
	
    public String getKey() {  
        return key;  
    }  
  
    public void setKey(String key) {  
        this.key = key;  
    }  
    

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}  
