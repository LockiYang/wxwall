package com.wxwall.wechat.api.advanced.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import com.wxwall.common.utils.CommonUtils;
import com.wxwall.wechat.api.advanced.model.SNSUserInfo;
import com.wxwall.wechat.api.advanced.model.WeixinOauth2Token;
import com.wxwall.wechat.api.util.CommonUtil;

/**  
*   
* 项目名称：wechatapi  
* 类名称：OAuthUtil  
* 类描述：OAuth2授权工具 
* 创建人：Myna Wang  
* 创建时间：2014-3-6 下午9:40:30  
* @version       
*/
public class OAuthUtil extends CommonUtil{
	/**
	 * 1.通过code换取网页授权access_token
	 * 
	 * @param appId  公众号的唯一标识
	 * @param appSecret 公众号的appsecret
	 * @param code 填写第一步获取的code参数
	 * @return WeixinOauth2Token
	 */
	public static WeixinOauth2Token getOauth2AccessToken(String appId,
			String appSecret,String code) {
		WeixinOauth2Token wat=null;
		String requestUrl=OAUTH2_ACCESSTOKEN_URL.replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);
		System.err.println(requestUrl);
		//获取网页授权凭证
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  
		if (null != jsonObject) {
			try {
				wat=new WeixinOauth2Token();
				wat.setAccessToken(jsonObject.getString("access_token"));
				wat.setExpiresIn(jsonObject.getInt("expires_in"));
				wat.setRefreshToken(jsonObject.getString("refresh_token"));
				wat.setOpenId(jsonObject.getString("openid"));
				wat.setScope(jsonObject.getString("scope"));
			} catch (Exception e) {
				wat=null;
				int errorCode=jsonObject.getInt("errcode");
				String errorMsg=jsonObject.getString("errmsg");
				log.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
			}
		}
		return wat;
	}
	
	
	/**
	 * 2.刷新access_token
	 * 
	 * @param appId 公众号的唯一标识 
	 * @param refreshToken 通过access_token获取到的refresh_token参数 
	 * @return
	 */
	public static WeixinOauth2Token refreshOauth2Token(String appId,
			String refreshToken) {
		WeixinOauth2Token wat=null;
		String requestUrl=REFRESH_ACCESSTOKEN_URL.replace("APPID", appId).replace("REFRESH_TOKEN", refreshToken);
		//获取网页授权凭证
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  
		if (null != jsonObject) {
			try {
				wat=new WeixinOauth2Token();
				wat.setAccessToken(jsonObject.getString("access_token"));
				wat.setExpiresIn(jsonObject.getInt("expires_in"));
				wat.setRefreshToken(jsonObject.getString("refresh_token"));
				wat.setOpenId(jsonObject.getString("openid"));
				wat.setScope(jsonObject.getString("scope"));
			} catch (Exception e) {
				wat=null;
				int errorCode=jsonObject.getInt("errcode");
				String errorMsg=jsonObject.getString("errmsg");
				log.error("刷新网页授权凭证失败 errcode:{} errmsg:{}",errorCode,errorMsg);
			}
		}
		return wat;
	}
	
	/**
	 * 3.拉取用户信息(需scope为 snsapi_userinfo)
	 * 
	 * @param accessToken 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	 * @param openId 用户的唯一标识
	 * @return SNSUserInfo
	 */
	public static SNSUserInfo getSNSUserInfo(String accessToken,String openId) {
		SNSUserInfo sui=null;
		String requestUrl=OAUTH2_USERINFO_URL.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
		// 通过网页授权获取用户信息
		JSONObject jsonObject=httpRequest(requestUrl, "GET", null);
		if (null!=jsonObject) {
			try {
				sui=new SNSUserInfo();
				if (jsonObject.has("errcode")) {
					String errorCode = jsonObject.getString("errcode");
					String errorMsg = jsonObject.getString("errmsg");
					log.error("获取基本个人信息失败 errcode:{} errmsg:{} ", errorCode,
							errorMsg);
					return null;
				}
				// 用户标识
				sui.setOpenId(jsonObject.getString("openid"));
				// 昵称
				sui.setNickname(jsonObject.getString("nickname"));
				// 性别(1是男性，2是女性，3是未知)
				sui.setSex(jsonObject.getInt("sex"));
				// 用户所在国家
				sui.setCountry(jsonObject.getString("country"));
				// 用户所在省份
				sui.setProvince(jsonObject.getString("province"));
				// 用户所在城市
				sui.setCity(jsonObject.getString("city"));
				// 用户头像
				sui.setHeadImgUrl(jsonObject.getString("headimgurl"));
				// 语言
				sui.setLanguage(jsonObject.getString("language"));
				//订阅时间
				if(jsonObject.has("subscribe_time")) {
					sui.setSubscribeTime(new Date(Long.parseLong(jsonObject.getString("subscribe_time")) * 1000));
				}
				// 用户特权信息
				//sui.setPrivilegeList(JSONArray.toList(jsonObject.getJSONArray("privilege"),List.class));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				sui=null;
				int errorCode=jsonObject.getInt("errcode");
				String errorMsg=jsonObject.getString("errmsg");
				log.error("获取用户信息失败 errcode:{} errmsg:{}",errorCode,errorMsg);
			}
		}
		return sui;
	}
	/**
	 * 生成第三方授权链接
	 * @param activityMid  活动Mid
	 * @param sourceOpenId  活动原始openID
	 * @param appID  第三方appID
	 * @param serverUrl 服务器域名
	 * @return
	 */
	public static String generateOathUrl(String activityMid, String sourceOpenId, String serverUrl) {
		String appId = CommonUtils.getSystemAppId();
		if (StringUtils.isBlank(appId)) {
			return null;
		}
		String requestUrl = OAUTH2_CHECK_OAUTH_URL.replace("APPID", appId).replace("STATE", activityMid);
		String callBackUrl = (serverUrl + OAUTH2_CALLBACK_OAUTH_URL).replace("SOURCE_OPENID", sourceOpenId);
		try {
			requestUrl = requestUrl.replace("REDIRECT_URI", URLEncoder.encode(callBackUrl, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.error("URL编码错误 ", e);
			return null;
		}
		return requestUrl;
	}
	/**
	 * 返回不用认证的链接
	 * @param activityMid
	 * @param sourceOpenId
	 * @param serverUrl
	 * @return
	 */
	public static String generateNoOathUrl(String activityMid, String sourceOpenId, String serverUrl) {
		String callBackUrl = (serverUrl + OAUTH2_CALLBACK_NO_OAUTH_URL).replace("SOURCE_OPENID", sourceOpenId).replace("ACTIVITY_MID", activityMid);
		return callBackUrl;
	}
	
	public static void main(String[] args) {
		WeixinOauth2Token weixinOauth2Token=getOauth2AccessToken("wx13c0a227486f7e64",
				"864e16284d38c05c62cddc1be000351e", "0011d88ec32c1ab93fcd8dfa2e138b4e");
		System.err.println("weixintoken是："+weixinOauth2Token);
		String accessToken=weixinOauth2Token.getAccessToken();
		System.err.println("accestoken是"+accessToken);
		String openId=weixinOauth2Token.getOpenId();
		System.err.println("openId是"+openId);
		SNSUserInfo snsUserInfo=getSNSUserInfo(accessToken, openId);
		System.err.println("snsUserInfo是"+snsUserInfo);
		
	}
}
