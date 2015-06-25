package com.wxwall.common.emoji;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class EmojiUtils {
	protected Logger LOG = Logger.getLogger(getClass());
	private String ENCODING = "UTF-8"; // 字符编码
	private static EmojiUtils emojiUtils;
//	private Map<String, String> names = new HashMap<String, String>();
//	private Map<String, String> kaomoji = new HashMap<String, String>();
//	private Map<String, String> unifiedToDocomo = new HashMap<String, String>();
//	private Map<String, String> unifiedToKddi = new HashMap<String, String>();
//	private Map<String, String> unifiedToSoftbank = new HashMap<String, String>();
//	private Map<String, String> unifiedToGoogle = new HashMap<String, String>();
	private Map<String, String> docomoToUnified = new HashMap<String, String>();
	private Map<String, String> kddiToUnified = new HashMap<String, String>();
	private Map<String, String> softbankToUnified = new HashMap<String, String>();
	private Map<String, String> googleToUnified = new HashMap<String, String>();
//	private Map<String, String> unifiedToHtml = new HashMap<String, String>();
	private Map<String, String> iosUtf16ToImg = new HashMap<String, String>();
	private Map<String, String> iosUtf8ToImg = new HashMap<String, String>();
	
//	private String[] docomoToUnifiedKey = null;
//	private String[] docomoToUnifiedValue = null;
//	private String[] kddiToUnifiedKey = null;
//	private String[] kddiToUnifiedValue = null;
//	private String[] softbankToUnifiedKey = null;
//	private String[] softbankToUnifiedValue = null;
//	private String[] googleToUnifiedKey = null;
//	private String[] googleToUnifiedValue = null;
	
	private String[] iosUtf16ToImgKey = null;
	private String[] iosUtf16ToImgValue = null;
	private String[] iosUtf8ToImgKey = null;
	private String[] iosUtf8ToImgValue = null;

	private EmojiUtils() {
//		initData(names, "/emoji/names.properties");
//		initData(kaomoji, "/emoji/kaomoji.properties");
//		initData(unifiedToDocomo, "/emoji/unified_to_docomo.properties");
//		initData(unifiedToKddi, "/emoji/unified_to_kddi.properties");
//		initData(unifiedToSoftbank, "/emoji/unified_to_softbank.properties");
//		initData(unifiedToGoogle, "/emoji/unified_to_google.properties");
//		initData(docomoToUnified, "/emoji/docomo_to_unified.properties");
//		initData(kddiToUnified, "/emoji/kddi_to_unified.properties");
//		initData(softbankToUnified, "/emoji/softbank_to_unified.properties");
//		initData(googleToUnified, "/emoji/google_to_unified.properties");
//		initData(unifiedToHtml, "/emoji/unified_to_html.properties");
		//initData(iosUtf16Toutf8, "/emoji/ios_utf16_to_utf8.properties");
		initData(iosUtf8ToImg, iosUtf16ToImg, "/emoji/ios_utf16_to_utf8_sbutf.properties");
		
//		initKeyValue(docomoToUnifiedKey, docomoToUnifiedValue, docomoToUnified);
//		initKeyValue(kddiToUnifiedKey, kddiToUnifiedValue, kddiToUnified);
//		initKeyValue(softbankToUnifiedKey, softbankToUnifiedValue, softbankToUnified);
//		initKeyValue(googleToUnifiedKey, googleToUnifiedValue, googleToUnified);
//		initKeyValue(iosUtf8ToImgKey, iosUtf8ToImgValue, iosUtf8ToImg);
//		initKeyValue(iosUtf16ToImgKey, iosUtf16ToImgValue, iosUtf16ToImg);
//		docomoToUnifiedKey = new String[docomoToUnified.size()];
//		docomoToUnifiedValue = new String[docomoToUnified.size()];
//		int i = 0;
//		for (String key : docomoToUnified.keySet()) {
//			docomoToUnifiedKey[i] = key;
//			docomoToUnifiedValue[i] = docomoToUnified.get(key);
//			++i;
//		}
//		kddiToUnifiedKey = new String[kddiToUnified.size()];
//		kddiToUnifiedValue = new String[kddiToUnified.size()];
//		i = 0;
//		for (String key : kddiToUnified.keySet()) {
//			kddiToUnifiedKey[i] = key;
//			kddiToUnifiedValue[i] = kddiToUnified.get(key);
//			++i;
//		}
//		softbankToUnifiedKey = new String[softbankToUnified.size()];
//		softbankToUnifiedValue = new String[softbankToUnified.size()];
//		i = 0;
//		for (String key : softbankToUnified.keySet()) {
//			softbankToUnifiedKey[i] = key;
//			softbankToUnifiedValue[i] = softbankToUnified.get(key);
//			++i;
//		}
//		googleToUnifiedKey = new String[googleToUnified.size()];
//		googleToUnifiedValue = new String[googleToUnified.size()];
//		i = 0;
//		for (String key : googleToUnified.keySet()) {
//			googleToUnifiedKey[i] = key;
//			googleToUnifiedValue[i] = googleToUnified.get(key);
//			++i;
//		}
		iosUtf8ToImgKey = new String[iosUtf8ToImg.size()];
		iosUtf8ToImgValue = new String[iosUtf8ToImg.size()];
		int i = 0;
		for (String key : iosUtf8ToImg.keySet()) {
			iosUtf8ToImgKey[i] = key;
			iosUtf8ToImgValue[i] = iosUtf8ToImg.get(key);
			++i;
		}
		iosUtf16ToImgKey = new String[iosUtf16ToImg.size()];
		iosUtf16ToImgValue = new String[iosUtf16ToImg.size()];
		i = 0;
		for (String key : iosUtf16ToImg.keySet()) {
			iosUtf16ToImgKey[i] = key;
			iosUtf16ToImgValue[i] = iosUtf16ToImg.get(key);
			++i;
		}
	}
	
	private void initKeyValue(String[] dataKey, String[] dataValue, Map<String, String> data, String name) {
		if ("docomoToUnified".equals(name)) {
			
		}
		dataKey = new String[data.size()];
		dataValue = new String[data.size()];
		int i = 0;
		for (String key : data.keySet()) {
			dataKey[i] = key;
			dataValue[i] = data.get(key);
			++i;
		}
	}

	public static EmojiUtils getInstance() {
		if (emojiUtils == null) {
			emojiUtils = new EmojiUtils();
		}
		return emojiUtils;
	}
	
	public Map<String, String> getNames() {
		return softbankToUnified;
	}
	
	private void initData(Map<String, String> data ,String fileName) {
		String absolutePath = EmojiUtils.class.getResource(fileName).getPath();
		File file = new File(absolutePath); // 读取文件
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		try {
			if (file.isFile() && file.exists()) { // 文件流是否存在
				read = new InputStreamReader(new FileInputStream(file),
						ENCODING);
				bufferedReader = new BufferedReader(read);
				String txt = null;
				while ((txt = bufferedReader.readLine()) != null) { // 读取文件，将文件内容放入到set中
					txt = StringUtils.trim(txt);
					String[] keyValue = null;
					if (StringUtils.isNotBlank(txt)) {
						int index = -1;
						index = txt.indexOf("=");
						if (index != -1) {
							keyValue = new String[2];
							keyValue[0] = txt.substring(0, index);
							keyValue[1] = txt.substring(index+1);
						}
					}
					
					if (keyValue != null && keyValue.length == 2) {
						String key = toStringHex1(StringUtils.trim(keyValue[0]));
						String value = null;
						if ("/emoji/ios_utf16_to_utf8.properties".equalsIgnoreCase(fileName)) {
							value = toStringHex2(StringUtils.trim(keyValue[1]));
							data.put(value ,key);
						} else if ("/emoji/ios_utf8_to_img.properties".equalsIgnoreCase(fileName)) {
							value = StringUtils.trim(keyValue[1]);
							data.put(key ,value);
						} else {
							value = toStringHex1(StringUtils.trim(keyValue[1]));
							data.put(key ,value);
						}
					}
				}
			} else { // 不存在抛出异常信息
				throw new Exception("文件不存在");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			try {
				if (read != null) {
					read.close();
					read = null;
				}

				if (bufferedReader != null) {
					bufferedReader.close();
					bufferedReader = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOG.error(e.getMessage(), e);
			}// 关闭文件流
		}
	}
	
	private void initData(Map<String, String> utf8Data, Map<String, String> utf16Data, String fileName) {
		String absolutePath = EmojiUtils.class.getResource(fileName).getPath();
		File file = new File(absolutePath); // 读取文件
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		try {
			if (file.isFile() && file.exists()) { // 文件流是否存在
				read = new InputStreamReader(new FileInputStream(file),
						ENCODING);
				bufferedReader = new BufferedReader(read);
				String txt = null;
				while ((txt = bufferedReader.readLine()) != null) { // 读取文件，将文件内容放入到set中
					txt = StringUtils.trim(txt);
					String[] keyValue = null;
					if (StringUtils.isNotBlank(txt)) {
						keyValue = txt.split("=");
					}
					
					if (keyValue != null && keyValue.length == 3) {
						String keyUtf8 = toStringHex1(StringUtils.trim(keyValue[0]));
						String keyUtf16 = toStringHex2(StringUtils.trim(keyValue[1]));
						String value = StringUtils.trim(keyValue[2]).toLowerCase();
						utf8Data.put(keyUtf8, "#" + value + "#");
						utf16Data.put(keyUtf16, "#" + value + "#");
					}
				}
			} else { // 不存在抛出异常信息
				throw new Exception("文件不存在");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			try {
				if (read != null) {
					read.close();
					read = null;
				}

				if (bufferedReader != null) {
					bufferedReader.close();
					bufferedReader = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOG.error(e.getMessage(), e);
			}// 关闭文件流
		}
	}
	
	/**
	 * utf-8
	 * @param s
	 * @return
	 */
	private String toStringHex1(String s) {
		int index = -1;
		index = s.indexOf("\\x");
		StringBuffer sb = new StringBuffer();
		while (index != -1) {
			sb.append(s.substring(0, index));
			String temp = null;
			if (s.length() > index + 4) {
				temp = s.substring(index+2, index+4);
			} else {
				temp = s.substring(index+2);
			}
			char key = (char)Integer.parseInt(temp, 16);
			sb.append(key);
			s = s.substring(index + 4);
			index = s.indexOf("\\x");
		}
		
		if (StringUtils.isNotBlank(s)) {
			sb.append(s);
		}

		return sb.toString();
	}
	/**
	 * utf-16
	 * @param s
	 * @return
	 */
	private String toStringHex2(String s) {
		int startIndex = -1;
		int endIndex = -1;
		startIndex = s.indexOf("\\x");
		StringBuffer sb = new StringBuffer();
		while (startIndex != -1) {
			endIndex = s.indexOf("\\x", startIndex + 1);
			sb.append(s.substring(0, startIndex));
			String temp = null;
			if (endIndex != -1) {
				temp = s.substring(startIndex+2, endIndex);
				s = s.substring(endIndex);
			} else {
				temp = s.substring(startIndex+2);
				s = "";
			}
			char key = (char)Integer.parseInt(temp, 16);
			sb.append(key);
			startIndex = s.indexOf("\\x");
		}
		
		if (StringUtils.isNotBlank(s)) {
			sb.append(s);
		}

		return sb.toString();
	}
	
//	private String emojiConvert(String text, Map<String, String> data) {
//		String patternString = "(" + StringUtils.join(data.keySet(), "|") + ")"; 
//	    Pattern pattern = Pattern.compile(patternString); 
//	    Matcher matcher = pattern.matcher(text); 
//
//	    //两个方法：appendReplacement, appendTail 
//	    StringBuffer sb = new StringBuffer();
//	    while(matcher.find()) {
//	        matcher.appendReplacement(sb, data.get(matcher.group(1))); 
//	    }
//	    matcher.appendTail(sb); 
//	    return sb.toString();
//	}
	
	private String emojiConvert(String text, String[] dataKey, String[] dataValue) {
	    text = StringUtils.replaceEach(text, dataKey, dataValue);
	    return text;
	}
	
	private String emojiConvertHtmlImg(String text, Map<String, String> data, String serverUrl) {
		String patternString = "(" + StringUtils.join(data.values(), "|") + ")"; 
	    Pattern pattern = Pattern.compile(patternString); 
	    Matcher matcher = pattern.matcher(text); 

	    //两个方法：appendReplacement, appendTail 
	    StringBuffer sb = new StringBuffer();
	    while(matcher.find()) {
	    	String strMat = matcher.group(1);
	        matcher.appendReplacement(sb, "<img src=\""
	    			+ serverUrl + "/statics/screen/images/emoji/emoji_" + strMat.substring(1, strMat.length()-1) + ".png"
	    			+ "\" width=\"24\" height=\"24\">"); 
	    }
	    matcher.appendTail(sb); 
	    return sb.toString();
	}
	//////////////////////
//	private String emojiDocomoToUnified(String text) {
//		return emojiConvert(text, docomoToUnifiedKey, docomoToUnifiedValue);
//	}
//	
//	private String emojiKddiToUnified(String text) {
//		return emojiConvert(text, kddiToUnifiedKey, kddiToUnifiedValue);
//	}
//	
//	private String emojiSoftbankToUnified(String text) {
//		return emojiConvert(text, softbankToUnifiedKey, softbankToUnifiedValue);
//	}
//
//	private String emojiGoogleToUnified(String text) {
//		return emojiConvert(text, googleToUnifiedKey, googleToUnifiedValue);
//	}
//	
//	/////////////////////////////////////////////
	private String emojiIosUnified16ToImg(String text) {
		return emojiConvert(text, iosUtf16ToImgKey, iosUtf16ToImgValue);
	}
	
	private String emojiIosUnified8ToImg(String text) {
		return emojiConvert(text, iosUtf8ToImgKey, iosUtf8ToImgValue);
	}
	
//	private String emojiUnifiedToDocomo(String text) {
//		return emojiConvert(text, unifiedToDocomo);
//	}
//	
//	private String emojiUnifiedToKddi(String text) {
//		return emojiConvert(text, unifiedToKddi);
//	}
//	
//	private String emojiUnifiedToSoftbank(String text) {
//		return emojiConvert(text, unifiedToSoftbank);
//	}
//	
//	private String emojiUnifiedToGoogle(String text) {
//		return emojiConvert(text, unifiedToGoogle);
//	}
//	
//	private String emojiUnifiedToHtml(String text) {
//		return emojiConvert(text, unifiedToHtml);
//	}
	
/*	public String emojiUnifiedToHtml(String text) {
		return emojiConvert(text, unifiedToHtml);
	}*/
	
	public String emojiConvertUnified(String text) {
		if (StringUtils.isBlank(text)) {
			return text;
		}
//		text = emojiGoogleToUnified(text);
//		text = emojiSoftbankToUnified(text);
//		text = emojiKddiToUnified(text);
//		text = emojiDocomoToUnified(text);
		text = emojiIosUnified16ToImg(text);
		text = emojiIosUnified8ToImg(text);
//		text = emojiUnifiedToDocomo(text);
//		text = emojiUnifiedToKddi(text);
//		text = emojiUnifiedToSoftbank(text);
//		text = emojiUnifiedToGoogle(text);
		return text;
	}
	
//	private String emojiConvertHtml(String text) {
//		text = emojiUnifiedToHtml(text);
//		return text;
//	}
	
	public String emojiConvertImgHtml(String text, String serverUrl) {
		if (StringUtils.isBlank(text)) {
			return text;
		}
		text = emojiConvertHtmlImg(text, iosUtf8ToImg, serverUrl);
		return text;
	}
	
/*	public String emojiToHtml(String text) {
		if (StringUtils.isBlank(text)) {
			return text;
		}
		text = emojiIosUnified16ToUnified8(text);
//		text = emojiConvertUnified(text);
		text = emojiConvertHtml(text);
		return text;
	}*/
	
	public static void main(String[] args) throws UnsupportedEncodingException {
//		Map<String, String> names = EmojiUtils.getInstance().getNames();
//		//Map<String, String> names = EmojiUtils.getInstance().getKaomoji();
//		for (String key : names.keySet()) {
//			System.out.println("key:" + key + "\tvalue:" + names.get(key));
//		}
//		System.out.println(EmojiUtils.getInstance().emojiConvertImgHtml("#e405##e405#", "http://localhost"));
//		char t1 = 0xd83d;
//		char t2 = 0xde04;
		char[] t3 = new char[]{0xd83d , 0xDC50};
		String t4 = new String(t3);
		String t = new String((t4).getBytes("UTF-16"), "UTF-16");
//		System.out.println(Integer.toHexString(t1));
//		System.out.println(Integer.toHexString(t2));
//		for (int i=0; i<t.length(); ++i) {
//			System.out.println(Integer.toHexString(EmojiUtils.getInstance().getInstance().convert(t.charAt(i))));
//		}
		System.out.println(EmojiUtils.getInstance().emojiConvertUnified(t));
	}
}
