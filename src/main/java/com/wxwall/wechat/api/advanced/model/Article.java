package com.wxwall.wechat.api.advanced.model;


/**  
*   
* 项目名称：wechat 
* 类名称：SendArticle  
* 类描述：回复图文Articles  
* 创建人：Myna Wang  
* 创建时间：2013-10-3 下午4:10:51  
* @version       
*/
public class Article {
	// 图文消息名称  
    private String title;  
    // 图文消息描述  
    private String description;  
    // 点击图文消息跳转链接  
    private String url;  
    // 图片链接，支持JPG、PNG格式，较好的效果为大图600*300，小图80*80，限制图片链接的域名需要与开发者填写的基本资料中的Url一致  
    private String picUrl;
    
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
}
