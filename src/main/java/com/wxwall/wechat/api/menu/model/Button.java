package com.wxwall.wechat.api.menu.model;
/**  
*   
* 项目名称：wechatapi  
* 类名称：Button  
* 类描述：一级菜单数组(父按钮)，个数应为1~3个  
* 创建人：Myna Wang  
* 创建时间：2014-3-11 下午12:39:41  
* @version       
*/
public class Button {
	//菜单标题，不超过16个字节，子菜单不超过40个字节 
	private String name;  
	  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
}
