package com.wxwall.modules.wechat.aes;

/**
 * 
 * <p>
 * 描述:加密微信传输消息
 * </p>
 *
 * @see
 * @author ganjun
 *
 */
public class AuthProcess {
	private String token;//公众平台上面自己填写的Token
	private String encodingAESKey;//公众平台上面自己填写的43位EncodingAESKey
	private String appID;//应用的appid（微信生成的）

	public AuthProcess(String token, String encodingAESKey, String appID) {
		this.token = token;
		this.encodingAESKey = encodingAESKey;
		this.appID = appID;
	}
	/**
	* 将加密后的原文进行解密重新封装
	* @param msgSignature 加密签名
	* @param timestamp 时间戳
	* @param nonce 随机数
	* @param originalXml 原xml
	* 
	* @return    重新解密后的xml
	 * @throws AesException 
	 */
	public String  decryptMsg(String msgSignature, String timestamp, String nonce, String originalXml) throws AesException {
		WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAESKey, appID);
		return pc.decryptMsg(msgSignature, timestamp, nonce, originalXml);
	}
	
	/**
	* 对需要回复的原文进行加密重新封装
	* @param request
	* @param replyXml 需要回复的xml
	* @return    重新加密后的xml
	 * @throws AesException 
	 */
	public String encryptMsg(String timestamp, String nonce, String replyXml) throws AesException {
		WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAESKey, appID);
		return pc.encryptMsg(replyXml, timestamp, nonce);
	}
}
