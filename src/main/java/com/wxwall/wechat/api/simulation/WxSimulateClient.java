package com.wxwall.wechat.api.simulation;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;

public class WxSimulateClient {

	private final static Log LOG = LogFactory.getLog(WxSimulateClient.class);


	public final static String LOGIN_URL = "https://mp.weixin.qq.com/cgi-bin/login?lang=zh_CN";
	public final static String INDEX_URL = "http://mp.weixin.qq.com/cgi-bin/indexpage?t=wxm-index&lang=zh_CN";
	public final static String SENDMSG_URL = "https://mp.weixin.qq.com/cgi-bin/singlesend";
	public final static String FANS_URL = "http://mp.weixin.qq.com/cgi-bin/contactmanagepage?t=wxm-friend&lang=zh_CN&pagesize=10&pageidx=0&type=0&groupid=0";
	public final static String LOGOUT_URL = "http://mp.weixin.qq.com/cgi-bin/logout?t=wxm-logout&lang=zh_CN";
	public final static String DOWNLOAD_URL = "http://mp.weixin.qq.com/cgi-bin/downloadfile?";
	public final static String VERIFY_CODE = "http://mp.weixin.qq.com/cgi-bin/verifycode?";
	public final static String POST_MSG = "https://mp.weixin.qq.com/cgi-bin/masssend?t=ajax-response";
	public final static String VIEW_HEAD_IMG = "http://mp.weixin.qq.com/cgi-bin/viewheadimg";
	public final static String GET_IMG_DATA = "http://mp.weixin.qq.com/cgi-bin/getimgdata";
	public final static String GET_REGIONS = "http://mp.weixin.qq.com/cgi-bin/getregions";
	public final static String GET_MESSAGE = "http://mp.weixin.qq.com/cgi-bin/getmessage";
	public final static String OPER_ADVANCED_FUNC = "http://mp.weixin.qq.com/cgi-bin/operadvancedfunc";
	public final static String MASSSEND_PAGE = "http://mp.weixin.qq.com/cgi-bin/masssendpage";
	public final static String FILE_MANAGE_PAGE = "http://mp.weixin.qq.com/cgi-bin/filemanagepage";
	public final static String OPERATE_APPMSG = "https://mp.weixin.qq.com/cgi-bin/operate_appmsg?token=416919388&lang=zh_CN&sub=edit&t=wxm-appmsgs-edit-new&type=10&subtype=3&ismul=1";
	public final static String FMS_TRANSPORT = "http://mp.weixin.qq.com/cgi-bin/fmstransport";
	public final static String GET_CONTACT_INFO = "https://mp.weixin.qq.com/cgi-bin/getcontactinfo";
	public final static String CONTACT_MANAGE_PAGE = "http://mp.weixin.qq.com/cgi-bin/contactmanage";
	public final static String MESSAGE_MANAGE_PAGE = "http://mp.weixin.qq.com/cgi-bin/message";
	public final static String OPER_SELF_MENU = "http://mp.weixin.qq.com/cgi-bin/operselfmenu";
	public final static String REPLY_RULE_PAGE = "http://mp.weixin.qq.com/cgi-bin/replyrulepage";
	public final static String SINGLE_MSG_PAGE = "http://mp.weixin.qq.com/cgi-bin/singlemsgpage";
	public final static String USER_INFO_PAGE = "http://mp.weixin.qq.com/cgi-bin/userinfopage";
	public final static String DEV_APPLY = "http://mp.weixin.qq.com/cgi-bin/devapply";
	public final static String UPLOAD_MATERIAL = "https://mp.weixin.qq.com/cgi-bin/uploadmaterial?cgi=uploadmaterial&type=2&token=416919388&t=iframe-uploadfile&lang=zh_CN&formId=1";

	public final static String HOST = "http://mp.weixin.qq.com";
	public final static String CONNECTION_H = "Connection";
	public final static String CONNECTION = "keep-alive";
	public final static String HOST_H = "Host";
	public final static String COOKIE_H = "Cookie";
	public final static String CONTENT_TYPE_H = "Content-Type";
	public final static String CONTENT_TYPE = "application/x-www-form-urlencoded; charset=UTF-8";
	public final static String XMLHTTP_REQUEST_H = "X-Requested-With";
	public final static String XMLHTTP_REQUEST = "XMLHttpRequest";
	public final static String USER_AGENT_H = "User-Agent";
	public final static String REFERER_H = "Referer";
	public final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22";
	public final static String UTF_8 = "UTF-8";

	private HttpClient client = new HttpClient();

	private Cookie[] cookies;
	private String cookiestr;

	private String token;

	private String loginUser;
	private String loginPwd;

	public WxSimulateClient(String user, String pwd) {
		this.loginUser = user;
		this.loginPwd = pwd;
	}

	/**
	 * 发送登录信息,记录cookie，登录状态，token等信息
	 * 登录失败，则重试一次
	 * 
	 * @return
	 */
	private boolean login() {
		try {
			PostMethod post = new PostMethod(LOGIN_URL);
			
			post.setRequestHeader(REFERER_H, HOST);
			post.setRequestHeader(USER_AGENT_H, USER_AGENT);
			
			NameValuePair[] params = new NameValuePair[] {
					new NameValuePair("username", this.loginUser),
					new NameValuePair("pwd", DigestUtils.md5Hex(this.loginPwd.getBytes())), new NameValuePair("f", "json"),
					new NameValuePair("imagecode", "") };
			post.setQueryString(params);
			System.setProperty("jsse.enableSNIExtension", "false");
			int status = client.executeMethod(post);

			if (status == HttpStatus.SC_OK) {// 请求成功
				String ret = post.getResponseBodyAsString();
				LoginJson retcode = JSON.parseObject(ret, LoginJson.class);

				LOG.info("Response Code: " + retcode.getBase_resp().getRet());
				if ((retcode.getBase_resp().getRet() == 302 || retcode.getBase_resp().getRet() == 0)) {// 成功登录
					this.cookies = client.getState().getCookies();
					StringBuffer cookie = new StringBuffer();
					for (Cookie c : client.getState().getCookies()) {
						cookie.append(c.getName()).append("=").append(c.getValue()).append(";");
					}
					this.cookiestr = cookie.toString();
					this.token = getToken(retcode.getRedirect_url());
					return true;
				}
			} 
		} catch (Exception e) {
			String err = "【登录失败】【发生异常：" + e.getMessage() + "】";
			LOG.error(err, e);
			return false;
		}
		return false;
	}
	
	public FanInfo getFanInfo(String fakeId) {
		FanInfo parseArray = null;
		try {
			PostMethod post = new PostMethod(GET_CONTACT_INFO);
			NameValuePair[] parameters = new NameValuePair[] {
					new NameValuePair("ajax", "1"),
					new NameValuePair("f", "json"),
					new NameValuePair("fakeid", fakeId),
					new NameValuePair("lang", "zh_CN"),
					new NameValuePair("random", "0.9637480949493703"),
					new NameValuePair("token", token),
					new NameValuePair("t", "ajax-getcontactinfo") };
			post.setQueryString(parameters);

			// 请求头的设定
			post.setRequestHeader(REFERER_H, INDEX_URL);
			post.setRequestHeader("Cookie", this.cookiestr);
			post.setRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			post.setRequestHeader("Accept-Encoding", "gzip, deflate");
			post.setRequestHeader("Accept-Language", "zh-cn");
			post.setRequestHeader("Cache-Control", "no-cache");
			post.setRequestHeader("Connection", "Keep-Alive");
			post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			post.setRequestHeader(HOST_H, HOST);
			post.setRequestHeader(USER_AGENT_H, USER_AGENT);
			post.setRequestHeader("x-requested-with", "XMLHttpRequest");
			int status = client.executeMethod(post);
			if (status == HttpStatus.SC_OK) {
				
				String responseStr = post.getResponseBodyAsString();
				System.out.println("aaa" + responseStr);
				int contactStart = responseStr.indexOf("contact_info") + 14;
				int contactEnd = responseStr.indexOf("},", contactStart) + 1;
				String contactJsonStr = responseStr.substring(contactStart, contactEnd);
				
				parseArray = JSON.parseObject(contactJsonStr, FanInfo.class);
			}
		} catch (Exception e) {
			String err = "【获取粉丝数失败】可能登录过期";
			LOG.error(err, e);
		}
		
		return parseArray;
	}
	
	/**
	 * 获取消息列表
	 */
	public List<WxMessage> getMessages() {
		String paramStr = "?t=message/list&count=50&day=7&token=" + this.token + "&lang=zh_CN";
		GetMethod get = new GetMethod(MESSAGE_MANAGE_PAGE + paramStr);
		get.setRequestHeader(REFERER_H, INDEX_URL);
		get.setRequestHeader("Cookie", this.cookiestr);
		List<WxMessage> parseMessages = null;
		try {
			int status = client.executeMethod(get);
			if (status == HttpStatus.SC_OK) {
				String responseStr = get.getResponseBodyAsString();
				GetMethod get2 = null;
				if (StringUtils.containsIgnoreCase(responseStr, "登录超时")) {
					if (this.login()) {
						String paramStr2 = "?t=message/list&count=50&day=7&token=" + this.token + "&lang=zh_CN";
						get2 = new GetMethod(MESSAGE_MANAGE_PAGE + paramStr2);
						get2.setRequestHeader(REFERER_H, INDEX_URL);
						get2.setRequestHeader("Cookie", this.cookiestr);
						status = client.executeMethod(get2);
					}
				}
				
				if (status == HttpStatus.SC_OK) {
					String responseStr2 = get2.getResponseBodyAsString();
					parseMessages = parseMessages(responseStr2);
				}
			}
			
		} catch (Exception e) {
			LOG.error("获取消息列表失败", e);
		}
		return parseMessages;
	}

	private List<WxMessage> parseMessages(String text) {
		try {
			int dataStart = text.indexOf("wx.cgiData") + 11;
			text = text.substring(dataStart);
			int msgListStart = text.indexOf("msg_item") + 10;
			int msgListEnd = text.indexOf(".msg_item", msgListStart) - 2;
			String msgListJson = text.substring(msgListStart, msgListEnd);

			List<WxMessage> list = JSON.parseArray(msgListJson, WxMessage.class);
			return list;
		} catch (Exception e) {
			LOG.error("消息解析失败", e);
			return null;
		}
	}

	/**
	 * 从登录成功的信息中分离出token信息
	 * 
	 * @param s
	 * @return
	 */
	private static String getToken(String s) {
		try {
			if (StringUtils.isBlank(s))
				return null;
			String[] ss = StringUtils.split(s, "?");
			String[] params = null;
			if (ss.length == 2) {
				if (!StringUtils.isBlank(ss[1]))
					params = StringUtils.split(ss[1], "&");
			} else if (ss.length == 1) {
				if (!StringUtils.isBlank(ss[0]) && ss[0].indexOf("&") != -1)
					params = StringUtils.split(ss[0], "&");
			} else {
				return null;
			}
			for (String param : params) {
				if (StringUtils.isBlank(param))
					continue;
				String[] p = StringUtils.split(param, "=");
				if (null != p && p.length == 2
						&& StringUtils.equalsIgnoreCase(p[0], "token"))
					return p[1];

			}
		} catch (Exception e) {
			String err = "【解析Token失败】发生异常" + e.getMessage();
			LOG.error(err, e);
			return null;
		}
		return null;
	}

	/**
	 * 说明:<br>
	 * new Weixin()对象，先登录再取粉丝数和者发消息。<br>
	 * 发消息需要设置post参数中的content<br>
	 * 内容中的超链接可以直接发送不用使用<a>标签 经过我（trprebel）修改之后，此份代码可在2013年11月之后使用
	 * 我只做了获取粉丝列表和发送消息，其他部分未做 理论上可以获取到粉丝的地址，签名等一切你登陆可以得到的信息
	 * 另外可能需要你在本机先登陆过至少一次微信公众平台获取SSL证书 此份代码作者较多，函数前面都有作者名，我只修改了登陆，获取粉丝列表和发送消息
	 * 其他代码可能已经不能用了但我并没有删除，方便大家扩展，我做的也比较粗糙，没整理
	 * 原文地址：http://50vip.com/blog.php?i=268 使用到的库： commons-codec-1.3.jar
	 * commons-httpclient-3.1.jar commons-lang.jar commons-logging-1.0.4.jar
	 * fastjson-1.1.15.jar gson-2.2.4.jar httpclient-4.1.3.jar
	 * httpcore-4.1.4.jar jsoup-1.5.2.jar 环境：JDK1.6
	 * 
	 * @param args
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public static void main(String[] args) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		String LOGIN_USER = "lockiyang@foxmail.com";
		String LOGIN_PWD = "y378591874";
		WxSimulateClient wx = new WxSimulateClient(LOGIN_USER, LOGIN_PWD);
//		wx.login();
		// ImgFileForm form = new ImgFileForm();
		// form.setUploadfile(new File("D:\\Data\\image\\4.jpg"));
		// wx.updateImg(form);
		// List<WeiXinFans> fans2 = wx.getFans(null);
//		 System.out.println("粉丝数：" + wx.getFans());
		List<WxMessage> messages = wx.getMessages();

		for (WxMessage wxMessage : messages) {
			System.out.println(wxMessage.getFakeid());
			FanInfo fanInfo = wx.getFanInfo(wxMessage.getFakeid() + "");
			System.out.println(BeanUtils.describe(fanInfo));
		}
//		wx.getMessages();
		// System.out.println("粉丝数："+ fans2.size());
		// wx.sendMsg(0);// 像好友列表中的第几个好友发消息，

	}
	

}
