package com.wxwall.wechat.api.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;


/**  
*   
* 项目名称：wechat-api  
* 类名称：MyX509TrustManager  
* 类描述： 证书信任管理器（用于https请求）编写证书过滤器   
* 创建人：Myna Wang  
* 创建时间：2014-1-26 下午7:33:07  
* @version       
*/
public class MyX509TrustManager implements X509TrustManager{	
	/**
	 * 该方法体为空时信任所有客户端证书(检查客户端证书)
	 */
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {	
	}
	
	/**
	 * 该方法体为空时信任所有服务器证书 (检查服务器证书)
	 */
	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {	
	}
	
	/**返回信任的证书
	 * @return   
	 */
	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}
