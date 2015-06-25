package com.wxwall.wechat.api.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.wxwall.wechat.api.advanced.model.AccessToken;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * 
 * 项目名称：wechatsapi 类名称：CommonUtil 类描述：http请求和接收 创建人：Myna Wang 创建时间：2014-1-26
 * 下午7:34:54
 * 
 * @version
 */
public class CommonUtil extends WeixinUtil {
	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject (通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		boolean disConn = false;
		// 设置重试机制
		for (int retry = 0; retry < 3; retry++) {
			if (disConn) {
				break;
			}
			try {
				if (retry > 0) {
					Thread.sleep(1000);
				}
				// 创建SSLContext对象，并使用我们指定的信任管理器初始化(证书过滤)
				TrustManager[] tm = { new MyX509TrustManager() };
				// 取得SSL的SSLContext实例
				SSLContext sslContext = SSLContext
						.getInstance("SSL", "SunJSSE");
				// 初始化SSLContext
				sslContext.init(null, tm, new java.security.SecureRandom());
				// 从上述SSLContext对象中得到SSLSocketFactory对象
				SSLSocketFactory ssf = sslContext.getSocketFactory();

				URL url = new URL(requestUrl);
				HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
						.openConnection();
				httpUrlConn.setSSLSocketFactory(ssf);

				httpUrlConn.setDoOutput(true);
				httpUrlConn.setDoInput(true);
				httpUrlConn.setUseCaches(false);
				// 设置请求方式（GET/POST）
				httpUrlConn.setRequestMethod(requestMethod);

				/*
				 * if ("GET".equalsIgnoreCase(requestMethod))
				 * httpUrlConn.connect();
				 */
				// 当有数据需要提交时(当outputStr不为null时，向输出流写数据)
				if (null != outputStr) {
					OutputStream outputStream = httpUrlConn.getOutputStream();
					// 注意编码格式，防止中文乱码
					outputStream.write(outputStr.getBytes("UTF-8"));
					outputStream.close();
				}

				// 将返回的输入流转换成字符串
				InputStream inputStream = httpUrlConn.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream, "utf-8");
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String str = null;
				StringBuffer buffer = new StringBuffer();
				while ((str = bufferedReader.readLine()) != null) {
					buffer.append(str);
				}

				// 释放资源
				bufferedReader.close();
				inputStreamReader.close();
				inputStream.close();
				inputStream = null;
				httpUrlConn.disconnect();
				jsonObject = JSONObject.fromObject(buffer.toString());
				disConn = true;
			} catch (ConnectException ce) {
				log.error("http连接超时: {}", ce);
			} catch (Exception e) {
				log.error("http请求异常: {}", e);
			}
		}
		return jsonObject;
	}

	/**
	 * 获取access_token
	 * 
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return AccessToken 返回接口凭证
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;
		String requestUrl = GET_ACCESS_TOKEN_URL.replace("APPID", appid)
				.replace("APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken
						.setAccesstoken(jsonObject.getString("access_token"));
				accessToken.setExpiresin(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				// 获取token失败
				accessToken = null;
				log.error("获取token失败 errcode:{} errmsg:{}",
						jsonObject.getInt("errcode"),
						jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}

	/**
	 * URL编码(utf-8)
	 * 
	 * @param source
	 * @return String
	 */
	public static String urlEncodeUTF8(String source) {
		String result = source;
		try {
			result = URLEncoder.encode(source, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据类型判断文件扩展名
	 * 
	 * @param contentType
	 *            内容类型
	 * @return String
	 */
	public static String getFileExt(String contentType) {
		String fileExt = "";
		if ("image/jpeg".equals(contentType)) {
			fileExt = ".jpg";
		} else if ("audio/mpeg".equals(contentType)) {
			fileExt = ".mp3";
		} else if ("audio/amr".equals(contentType)) {
			fileExt = ".amr";
		} else if ("video/mp4".equals(contentType)) {
			fileExt = ".mp4";
		} else if ("video/mpeg4".equals(contentType)) {
			fileExt = ".mp4";
		}
		return fileExt;
	}

	public static void main(String[] args) {
		/*
		 * String url=
		 * "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx13c0a227486f7e64&redirect_uri=http%3A%2F%2F122.228.158.111%2FTest%2Fservlet%2FOAuthServlet&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"
		 * ;
		 */

		String url = "http://122.228.158.111/Test/servlet/OAuthServlet";
		System.err.println(urlEncodeUTF8(url));

		// 获取接口访问凭证
		AccessToken accessToken = getAccessToken("wx13c0a227486f7e64",
				"864e16284d38c05c62cddc1be000351e");
		if (accessToken.getAccesstoken() == null) {
			System.out.println("空");
		} else {
			System.out.println(accessToken.getAccesstoken());
		}
	}
}
