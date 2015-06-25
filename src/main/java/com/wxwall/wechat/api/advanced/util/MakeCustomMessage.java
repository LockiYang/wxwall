package com.wxwall.wechat.api.advanced.util;

import java.util.List;

import com.wxwall.wechat.api.advanced.model.Article;
import com.wxwall.wechat.api.advanced.model.Music;

import net.sf.json.JSONObject;

/**  
*   
* 项目名称：wechatapi  
* 类名称：MakeCustomMessage  
* 类描述：生成客服消息工具  
* 创建人：Myna Wang  
* 创建时间：2014-3-7 上午10:24:43  
* @version       
*/
public class MakeCustomMessage {	
	/**
	 * 组装文本客服消息
	 * 
	 * @param openId 普通用户openid
	 * @param content 文本消息内容
	 * @return String
	 */
	public static String makeTextCustomMessage(String openId,String content) {
		// 对消息内容中的双引号进行转义
		content=content.replace("\"", "\\\"");
		String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"text\",\"text\":" +
				"{\"content\":\"%s\"}}";
		return String.format(jsonMsg, openId,content);
	}
	
	/**
	 * 组装图片客服消息
	 * 
	 * @param openId 普通用户openid
	 * @param mediaId 发送的图片的媒体ID
	 * @return String
	 */
	public static String makeImageCustomMessage(String openId,String mediaId) {
		String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"image\",\"image\":" +
				"{\"media_id\":\"%s\"}}";
		return String.format(jsonMsg, openId,mediaId);
	}
	
	
	/**
	 * 组装语音客服消息
	 * 
	 * @param openId 普通用户openid
	 * @param mediaId 发送的语音的媒体ID
	 * @return String
	 */
	public static String makeVoiceCustomMessage(String openId,String mediaId) {
		String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"voice\",\"voice\":" +
				"{\"media_id\":\"%s\"}}";
		return String.format(jsonMsg, openId,mediaId);
	}
	
	/** 
	 * 组装视频客服消息
	 * 
	 * @param openId 普通用户openid
	 * @param mediaId 发送的语音的媒体ID
	 * @param title 视频消息的标题
	 * @param description 视频消息的描述
	 * @return String
	 */
	public static String makeVideoCustomMessage(String openId,String mediaId,String title,String description) {
		String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"video\",\"video\":" +
				"{\"media_id\":\"%s\",\"title\":\"%s\",\"description\":\"%s\"}}";
		return String.format(jsonMsg, openId,mediaId,title,description);
	} 
	
	/**
	 * 组装音乐客服消息
	 * 
	 * @param openId 普通用户openid
	 * @param music 音乐对象
	 * @return String
	 */
	public static String makeMusicCustomMessage(String openId,Music music) {
		String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"music\",\"music\":%s}";
		jsonMsg=String.format(jsonMsg, openId,JSONObject.fromObject(music).toString());
		return jsonMsg;
	} 
	
	/**
	 * 组装图文客服消息
	 * 
	 * @param openId 普通用户openid
	 * @param articleList 图文消息列表
	 * @return String
	 */
	public static String makeNewsCustomMessage(String openId,List<Article> articleList) {
		String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"news\",\"news\":{\"articles\":%s}}";
		jsonMsg=String.format(jsonMsg, openId,JSONObject.fromObject(articleList).toString().replace("\"", "\\\""));
		return jsonMsg;
	} 
}
