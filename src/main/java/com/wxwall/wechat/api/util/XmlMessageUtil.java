package com.wxwall.wechat.api.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.wxwall.wechat.api.basic.model.SendArticle;
import com.wxwall.wechat.api.basic.model.SendImageMessage;
import com.wxwall.wechat.api.basic.model.SendMusicMessage;
import com.wxwall.wechat.api.basic.model.SendNewsMessage;
import com.wxwall.wechat.api.basic.model.SendTextMessage;
import com.wxwall.wechat.api.basic.model.SendVideoMessage;
import com.wxwall.wechat.api.basic.model.SendVoiceMessage;

/**
 * 
 * 项目名称：wechat-api 类名称：XmlMessUtil 类描述：xml形式消息处理工具类 创建人：Myna Wang 创建时间：2014-1-21
 * 下午3:23:59
 * 
 * @version
 */
public class XmlMessageUtil {

	/**
	 * 
	 * <p>
	 * 描述:拆分微信服务器请求信息，返回原始XML信息
	 * </p>
	 * 
	 * @param request
	 * @return xml
	 * @throws
	 * @see
	 * @since %I%
	 */
	public static String parseReqMessage(HttpServletRequest request)
			throws IOException {
		// 从request中取得输入流
		BufferedReader br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		// 获取微信服务端xml消息实体

		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		// 释放资源
		br.close();
		br = null;
		return sb.toString();
	}

	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(String decryptXml)
			throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();
		Document document = null;
		// 拆分xml字符串
		document = DocumentHelper.parseText(decryptXml);

		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		return map;
	}

	/**
	 * 文本消息对象转换成xml
	 * 
	 * @param textMessage
	 *            文本消息对象
	 * @return xml
	 */
	public static String textMessageToXml(SendTextMessage textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 图片消息对象转换成xml
	 * 
	 * @param imageMessage
	 *            图片消息对象
	 * @return xml
	 */
	public static String imageMessageToXml(SendImageMessage imageMessage) {
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}

	/**
	 * 语音消息对象转换成xml
	 * 
	 * @param voiceMessage
	 *            语音消息对象
	 * @return xml
	 */
	public static String voiceMessageToXml(SendVoiceMessage voiceMessage) {
		xstream.alias("xml", voiceMessage.getClass());
		return xstream.toXML(voiceMessage);
	}

	/**
	 * 视频消息对象转换成xml
	 * 
	 * @param videoMessage
	 *            视频消息对象
	 * @return xml
	 */
	public static String videoMessageToXml(SendVideoMessage videoMessage) {
		xstream.alias("xml", videoMessage.getClass());
		return xstream.toXML(videoMessage);
	}

	/**
	 * 音乐消息对象转换成xml
	 * 
	 * @param musicMessage
	 *            音乐消息对象
	 * @return xml
	 */
	public static String musicMessageToXml(SendMusicMessage musicMessage) {
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}

	/**
	 * 图文消息对象转换成xml
	 * 
	 * @param newsMessage
	 *            图文消息对象
	 * @return xml
	 */
	public static String newsMessageToXml(SendNewsMessage newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new SendArticle().getClass());
		return xstream.toXML(newsMessage);
	}

	/**
	 * 扩展xstream，使其支持CDATA块
	 * 
	 * @date 2013-05-19
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
}
