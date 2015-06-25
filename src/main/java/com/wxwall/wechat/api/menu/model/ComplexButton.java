package com.wxwall.wechat.api.menu.model;
/**  
*   
* 项目名称：wechatapi  
* 类名称：ComplexButton  
* 类描述：复杂按钮（父按钮）  
* 创建人：Myna Wang  
* 创建时间：2014-3-11 下午12:40:19  
* @version       
*/
public class ComplexButton extends Button{
	// 二级菜单数组，个数应为1~5个 
	private Button[] sub_button;   
	// 菜单的响应动作类型，目前有click、view两种类型 
	private String type;
	// 菜单KEY值，用于消息接口推送，不超过128字节 
    private String key;
    
    
	public Button[] getSub_button() {  
        return sub_button;  
    }  
  
    public void setSub_button(Button[] sub_button) {  
        this.sub_button = sub_button;  
    }  
    
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
}
