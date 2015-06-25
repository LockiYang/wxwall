package com.wxwall.wechat.api.advanced.model;

/**  
*   
* 项目名称：wechat  
* 类名称：AccessToken  
* 类描述：获取accesstoken(公众号的全局唯一票据)  
* 创建人：Myna Wang  
* 创建时间：2014-2-17 下午1:26:58  
* @version       
*/
public class AccessToken {
 	// 获取到的凭证
	private String accesstoken;
	// 凭证有效时间，单位：秒  有效期为7200秒
	private int expiresin;
	public String getAccesstoken() {
		return accesstoken;
	}
	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
	public int getExpiresin() {
		return expiresin;
	}
	public void setExpiresin(int expiresin) {
		this.expiresin = expiresin;
	}
}
