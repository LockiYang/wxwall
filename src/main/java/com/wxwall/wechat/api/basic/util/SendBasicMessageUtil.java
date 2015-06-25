package com.wxwall.wechat.api.basic.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.wxwall.wechat.api.basic.model.SendArticle;
import com.wxwall.wechat.api.basic.model.SendImage;
import com.wxwall.wechat.api.basic.model.SendImageMessage;
import com.wxwall.wechat.api.basic.model.SendMusic;
import com.wxwall.wechat.api.basic.model.SendMusicMessage;
import com.wxwall.wechat.api.basic.model.SendNewsMessage;
import com.wxwall.wechat.api.basic.model.SendTextMessage;
import com.wxwall.wechat.api.basic.model.SendVideo;
import com.wxwall.wechat.api.basic.model.SendVideoMessage;
import com.wxwall.wechat.api.basic.model.SendVoice;
import com.wxwall.wechat.api.basic.model.SendVoiceMessage;
import com.wxwall.wechat.api.util.WeixinUtil;
import com.wxwall.wechat.api.util.XmlMessageUtil;

/**
 * 
 * 项目名称：wechatapi 类名称：SendBasicService 类描述：回复文本消息类 创建人：Myna Wang 创建时间：2014-2-19
 * 下午7:36:53
 * 
 * @version
 */
public class SendBasicMessageUtil {

	/**
	 * 1.回复文本消息
	 * 
	 * @param fromUserName
	 *            粉丝openid
	 * @param toUserName
	 *            微信公众号
	 * @param respContent
	 *            回复信息
	 * @return String
	 */
	public static String sendTextmessage(String fromUserName,
			String toUserName, String respContent) {
		// 初始化回复信息
		String respmessage;
		// 回复文本消息
		SendTextMessage textMessage = new SendTextMessage();
		// 发送方帐号（一个OpenID）
		textMessage.setToUserName(fromUserName);
		// 开发者微信号
		textMessage.setFromUserName(toUserName);
		// 消息创建时间 （整型）
		textMessage.setCreateTime(new Date().getTime());
		// 消息类型text
		textMessage.setMsgType(WeixinUtil.REQUEST_TEXT);
		// 回复的消息内容,长度不超过2048字节
		textMessage.setContent(respContent);
		// 转为xml格式
		respmessage = XmlMessageUtil.textMessageToXml(textMessage);
		// 返回回复信息
		return respmessage;
	}

	/**
	 * 2.回复图片消息
	 * 
	 * @param fromUserName
	 *            粉丝openid
	 * @param toUserName
	 *            微信公众号
	 * @param mediaId
	 *            通过上传多媒体文件，得到的id
	 * @return
	 */
	public static String sendImagemessage(String fromUserName,
			String toUserName, String mediaId) {
		// 初始化回复消息
		String respmessage;
		// 回复图片消息
		SendImageMessage imageMessage = new SendImageMessage();
		// 发送放账号（一个OpenID）
		imageMessage.setToUserName(fromUserName);
		// 开发者微信号
		imageMessage.setFromUserName(toUserName);
		// 消息创建时间（整型）
		imageMessage.setCreateTime(new Date().getTime());
		// 消息类型image
		imageMessage.setMsgType(WeixinUtil.REQUEST_IMAGE);
		// 图片model
		SendImage sendImage = new SendImage();
		// 图片通过上传多媒体文件，得到的id
		sendImage.setMediaId(mediaId);
		imageMessage.setImage(sendImage);
		// 转换为xml格式
		respmessage = XmlMessageUtil.imageMessageToXml(imageMessage);
		// 返回回复消息
		return respmessage;
	}

	/**
	 * 3.回复语音消息
	 * 
	 * @param fromUserName
	 *            粉丝openid
	 * @param toUserName
	 *            微信公众号
	 * @param mediaId
	 *            通过上传多媒体文件，得到的id
	 * @return
	 */
	public static String sendVoicemessage(String fromUserName,
			String toUserName, String mediaId) {
		// 初始化回复消息
		String respmessage;
		// 回复语言消息
		SendVoiceMessage voiceMessage = new SendVoiceMessage();
		// 发送放账号（一个OpenID）
		voiceMessage.setToUserName(fromUserName);
		// 开发者微信号
		voiceMessage.setFromUserName(toUserName);
		// 消息创建时间（整型）
		voiceMessage.setCreateTime(new Date().getTime());
		// 消息类型image
		voiceMessage.setMsgType(WeixinUtil.REQUEST_VOICE);
		SendVoice sendVoice = new SendVoice();
		// 图片通过上传多媒体文件，得到的id
		sendVoice.setMediaId(mediaId);
		voiceMessage.setVoice(sendVoice);
		// 转换为xml格式
		respmessage = XmlMessageUtil.voiceMessageToXml(voiceMessage);
		// 返回回复消息
		return respmessage;
	}

	/**
	 * 4.回复视频消息
	 * 
	 * @param fromUserName
	 *            粉丝openid
	 * @param toUserName
	 *            微信公众号
	 * @param mediaId
	 *            通过上传多媒体文件，得到的id
	 * @param title
	 *            视频标题
	 * @param description
	 *            视频描述
	 * @return String
	 */
	public static String sendVideoMessage(String fromUserName,
			String toUserName, String mediaId, String title, String description) {
		// 初始化回复信息
		String respmessage;
		// 创建视频消息
		SendVideoMessage videoMessage = new SendVideoMessage();
		// 发送方帐号（一个OpenID）
		videoMessage.setToUserName(fromUserName);
		// 开发者微信号
		videoMessage.setFromUserName(toUserName);
		// 消息创建时间 （整型）
		videoMessage.setCreateTime(new Date().getTime());
		// 消息类型video
		videoMessage.setMsgType(WeixinUtil.REQUEST_VIDEO);
		// 创建视频
		SendVideo sendVideo = new SendVideo();
		// 通过上传多媒体文件，得到的id
		sendVideo.setMediaId(mediaId);
		// 视频消息的标题
		sendVideo.setTitle(title);
		// 视频消息的描述
		sendVideo.setDescription(description);
		videoMessage.setVideo(sendVideo);
		// 转换为xml格式
		respmessage = XmlMessageUtil.videoMessageToXml(videoMessage);
		// 返回回复消息
		return respmessage;
	}

	/**
	 * 5.回复音乐消息
	 * 
	 * @param fromUserName
	 *            粉丝openid
	 * @param toUserName
	 *            微信公众号
	 * @param title
	 *            音乐标题
	 * @param description
	 *            音乐描述
	 * @param musicurl
	 *            音乐链接
	 * @param hqmusicurl
	 *            高质量音乐链接，WIFI环境优先使用该链接播放音乐
	 * @param thumbmediaid
	 *            缩略图的媒体id，通过上传多媒体文件，得到的id
	 * @return String
	 */
	public static String sendMusicMessage(String fromUserName,
			String toUserName, String title, String description,
			String musicurl, String hqmusicurl, String thumbmediaid) {
		// 初始化回复信息
		String respmessage;
		// 创建音乐消息
		SendMusicMessage musicMessage = new SendMusicMessage();
		// 发送方帐号（一个OpenID）
		musicMessage.setFromUserName(fromUserName);
		// 开发者微信号
		musicMessage.setToUserName(toUserName);
		// 消息创建时间 （整型）
		musicMessage.setCreateTime(new Date().getTime());
		// 消息类型music
		musicMessage.setMsgType(WeixinUtil.REQUEST_MUSIC);
		// 创建音乐
		SendMusic sendMusic = new SendMusic();
		// 音乐标题
		sendMusic.setTitle(title);
		// 音乐描述
		sendMusic.setDescription(description);
		// 音乐链接
		sendMusic.setMusicurl(musicurl);
		// 高质量音乐链接，WIFI环境优先使用该链接播放音乐
		sendMusic.setHqmusicurl(hqmusicurl);
		// 缩略图的媒体id，通过上传多媒体文件，得到的id
		sendMusic.setThumb_media_id(thumbmediaid);
		musicMessage.setMusic(sendMusic);
		// 转换为xml格式
		respmessage = XmlMessageUtil.musicMessageToXml(musicMessage);
		// 返回回复消息
		return respmessage;
	}

	/**
	 * 6.图文消息设置
	 * 
	 * @param fromUserName
	 *            粉丝openid
	 * @param toUserName
	 *            开发者微信公众账号
	 * @param newslist
	 *            图文消息list
	 * @return String
	 */
	public static String sendNewsmessage(String fromUserName,
			String toUserName, List<SendArticle> newslist) {
		// 初始化回复信息
		String respmessage;
		// 创建图文消息
		SendNewsMessage newsMessage = new SendNewsMessage();
		// 发送方帐号（一个OpenID）
		newsMessage.setToUserName(fromUserName);
		// 开发者微信号
		newsMessage.setFromUserName(toUserName);
		// 消息创建时间 （整型）
		newsMessage.setCreateTime(new Date().getTime());
		// 消息类型news
		newsMessage.setMsgType(WeixinUtil.REQUEST_NEWS);
		// 图文消息个数，限制为10条以内
		newsMessage.setArticleCount(newslist.size());
		// 多条图文消息信息，默认第一个item为大图
		newsMessage.setArticles(newslist);
		// 转成xml形式
		respmessage = XmlMessageUtil.newsMessageToXml(newsMessage);
		// 回复信息
		return respmessage;
	}

	public static void main(String args[]) {
		List<SendArticle> sendArticleList = new ArrayList<SendArticle>();
		SendArticle sendArticle = new SendArticle();
		String title = "123";
		if (StringUtils.isNotBlank(title)) {
			sendArticle.setTitle(title);
		}
		
		sendArticleList.add(sendArticle);
		System.out.println(SendBasicMessageUtil.sendNewsmessage("xxxx", "1233", sendArticleList));
	}
}