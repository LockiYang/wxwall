package com.wxwall.common.utils;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.wxwall.common.cache.OAuthCacheUserInfo;
import com.wxwall.common.sensitive.SensitivewordFilter;

public class CommonUtils {
	// 时间类型的处理以及格式化字符串
	public static String DATE_FORMAT = "yyyy-MM-dd";
	public static String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
	
	public static String DATE_TIME_NO_SS_FORMAT = "yyyy-MM-dd hh:mm";
	
	public static String STORE_PICTURE_DIR="images";
	
	public static Date INIT_DATE_TIME = null;
	
	private static PropertiesTool propertiesTool = new PropertiesTool();
	private static SensitivewordFilter filter = new SensitivewordFilter();
	private static LRUCache<String, Integer> msgIdLRuCache = new LRUCache<String, Integer>(10000);
	//private static LRUCache<String, OAuthCacheUserInfo> oathUserLRuCache = new LRUCache<String, OAuthCacheUserInfo>(10000);

	static {
		propertiesTool.loadFile("wxwall.properties", "UTF-8");
		Calendar calendar = Calendar.getInstance();
		calendar.set(2015, 1, 1, 0, 0, 0);
		INIT_DATE_TIME = calendar.getTime();
	}
	
	public static LRUCache<String, Integer> getMsgIdLRuCache() {
		return msgIdLRuCache;
	}
	
//	public static LRUCache<String, OAuthCacheUserInfo> getOathUserLRuCache() {
//		return oathUserLRuCache;
//	}
	
	public static String replaceSensitiveWord(String txt) {
		if (StringUtils.isBlank(txt)) {
			return txt;
		}
		return filter.replaceSensitiveWord(txt, 1, "*");
	}
	
	public static String getSystemAppId() {
		return propertiesTool.getString("system_appid");
	}
	
	public static String getSystemAppSecret() {
		return propertiesTool.getString("system_appSecret");
	}
	
	public static PropertiesTool getPropertiesTool() {
		return propertiesTool;
	}
	
	/**
	 * 通过二进制数组返回MD5值的字符串
	 * @param source
	 * @return
	 */
	private static String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };// 用来将字节转换成16进制表示的字符
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest();// MD5 的计算结果是一个 128 位的长整数，
									// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2];// 每个字节用 16 进制表示的话，使用两个字符， 所以表示成 16
											// 进制需要 32 个字符
			int k = 0;// 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) {// 从第一个字节开始，对 MD5 的每一个字节// 转换成 16
				// 进制字符的转换
				byte byte0 = tmp[i];// 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];// 取字节中高 4 位的数字转换,// >>>
				// 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf];// 取字节中低 4 位的数字转换

			}
			s = new String(str);// 换后的结果转换为字符串

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	/**
	 * 返回UUID的字符串类型
	 * @return
	 */
	public static String generateUUID() {
		UUID uuid = UUID.randomUUID();  
		String str = uuid.toString();
		return getMD5(str.getBytes());
	}
	
	public static String generateTokens(String uuid) {
		return getMD5(uuid.getBytes());
	}
	
	public static String getServerUrl(HttpServletRequest request) {
		String serverUrl = request.getScheme() + "://" + request.getServerName() + 
				((request.getServerPort() != 80) ? (":"+ 
					request.getServerPort()) : "") + request.getContextPath();
		return serverUrl;
	}
	
	/**
	 * 返回临时文件存储目录
	 * @param userID
	 * @return
	 */
	public static String getTmpDir(long userID) {
		String storeDir = propertiesTool.getString("wxwall.store.dir", "store");
		String prefixDir = propertiesTool.getString("wxwall.tmp.dir", "temp");
		String relativePath = storeDir + File.separator + prefixDir + File.separator + userID;
		return relativePath;
	}
	/**
	 * 返回实际文件存储目录
	 * @param userID
	 * @return
	 */
	public static String getFileDir(long userID) {
		String storeDir = propertiesTool.getString("wxwall.store.dir", "store");
		String prefixDir = propertiesTool.getString("wxwall.file.dir", "file");
		String relativePath = storeDir + File.separator + prefixDir + File.separator + userID;
		return relativePath;
	}
	/**
	 * 根据用户ID，时间戳生成新的文件名，存储到数据库中
	 * @param userID
	 * @param fileName
	 * @return
	 */
	public static String generateNewFileName(long userID, String fileName) {
		String newFileName = userID + "_" + System.currentTimeMillis() + "_"
				+ fileName;
		return newFileName;
	}
	/**
	 * 根据存储目录前缀、时间（年、月、日）、文件夹类型(images/text等生成目录)
	 * @param dir
	 * @return
	 */
	public static String generateNewDir(String dir) {
		String storeDir = propertiesTool.getString("wxwall.store.dir", "store");
		String prefixDir = propertiesTool.getString("wxwall.file.dir", "file");
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.WEEK_OF_MONTH);
		String newDir = storeDir + File.separator + prefixDir + File.separator + year + "_" + month + "_"
				+ day + File.separator + dir;
		return newDir;
	}

	/**
	 * 根据用户ID，时间戳生成新的文件名，存储到数据库中
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static String getOriginFileName(String filePath, String fileName) {
		if (!StringUtils.isBlank(fileName)) {
			File fullFile = new File(filePath + File.separator + fileName);
			if (fullFile.exists()) {
				String lastName = fullFile.getName();
				int pos = lastName.indexOf('_');
				lastName = lastName.substring(pos + 1);
				pos = lastName.indexOf('_');
				return lastName.substring(pos + 1);
			} else {
				return "";
			}
		} else {
			return "";
		}
	}
	/**
	 * 随机从数组里面取一定数量的数
	 * @param originArray
	 * @param num
	 * @return
	 */
	public static int[] shuffle(int[] originArray, int num) {
		int[] resultArray = new int[num];
		Random random = new Random();
		for(int i=0; i<num; ++i) {
			int index = (random.nextInt() % (originArray.length - i));
			resultArray[i] = originArray[index];
			originArray[index] = originArray[originArray.length - i -1];
			originArray[originArray.length - i -1] = resultArray[i];
		}
		
		return resultArray;
	}
}
