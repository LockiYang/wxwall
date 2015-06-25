package com.wxwall.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public class FaceUtils {
	public static String qqFaceResource = "https://res.wx.qq.com/mpres/htmledition/images/icon/emotion/";
	private static String[] qqfacesData = { "/::)", "/::~", "/::B", "/::|",
			"/:8-)", "/::<", "/::$", "/::X", "/::Z", "/::'(", "/::-|", "/::@",
			"/::P", "/::D", "/::O", "/::(", "/::+", "/:--b", "/::Q", "/::T",
			"/:,@P", "/:,@-D", "/::d", "/:,@o", "/::g", "/:|-)", "/::!",
			"/::L", "/::>", "/::,@", "/:,@f", "/::-S", "/:?", "/:,@x", "/:,@@",
			"/::8", "/:,@!", "/:!!!", "/:xx", "/:bye", "/:wipe", "/:dig",
			"/:handclap", "/:&-(", "/:B-)", "/:<@", "/:@>", "/::-O", "/:>-|",
			"/:P-(", "/::'|", "/:X-)", "/::*", "/:@x", "/:8*", "/:pd", "/:<W>",
			"/:beer", "/:basketb", "/:oo", "/:coffee", "/:eat", "/:pig",
			"/:rose", "/:fade", "/:showlove", "/:heart", "/:break", "/:cake",
			"/:li", "/:bome", "/:kn", "/:footb", "/:ladybug", "/:shit",
			"/:moon", "/:sun", "/:gift", "/:hug", "/:strong", "/:weak",
			"/:share", "/:v", "/:@)", "/:jj", "/:@@", "/:bad", "/:lvu", "/:no",
			"/:ok", "/:love", "/:<L>", "/:jump", "/:shake", "/:<O>",
			"/:circle", "/:kotow", "/:turn", "/:skip", "/:oY", "/:#-0",
			"/:hiphot", "/:kiss", "/:<&", "/:&>" };
	public static String qqfaceRegex = "(/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::$|/::X|/::Z|/::"
			+ "'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q|/::T|/:,@P|/:,"
			+ "@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@|/:,@f|/::-S|/:\\?|/:,@x|/:,"
			+ "@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe|/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>"
			+ "-\\||/:P-\\(|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo|/:coffee|/:eat|"
			+ "/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|"
			+ "/:sun|/:gift|/:hug|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok|/:love|/:<L>|/:jump|"
			+ "/:shake|/:<O>|/:circle|/:kotow|/:turn|/:skip|/:oY|/:#-0|/:hiphot|/:kiss|/:<&|/:&>)";
	public static List<String> qqfaces = null;
	static {
		qqfaces = Arrays.asList(qqfacesData);
	}

	/**
	 * 转换为QQ表情
	 * 
	 * @param content
	 * @return
	 */
	public static String converQqFace(String content) {
		if (StringUtils.isBlank(content)) {
			return content;
		}
		// 判断QQ表情的正则表达式
		content = StringEscapeUtils.unescapeHtml(content);
		Pattern p = Pattern.compile(qqfaceRegex);
		Matcher matcher = p.matcher(content);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "<img src=\"" + qqFaceResource
					+ qqfaces.indexOf(matcher.group(1)) + ".gif\" "
					+ "width=\"24\" height=\"24\">");
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 检测是否有emoji字符
	 * 
	 * @param source
	 * @return 一旦含有就抛出
	 */
	public static boolean containsEmoji(String source) {
		if (StringUtils.isBlank(source)) {
			return false;
		}
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (isNotEmojiCharacter(codePoint)) {
				// do nothing，判断到了这里表明，确认有表情字符
				return true;
			}
		}
		return false;
	}

	private static boolean isNotEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * 
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source) {
		if (source == null) {
			return source;
		}
		StringBuilder buf = new StringBuilder();
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (isNotEmojiCharacter(codePoint)) {
				buf.append(codePoint);
			} else {
				System.out.println(Integer.toHexString(codePoint));
			}
		}
		return buf.toString();
	}
}
