package com.wxwall.common.web;

/**
 * Rest请求状态信息
 * @author Locki
 * @date 2014年9月23日
 *
 */
public class RestResponseBody {

	private int re_code = -1; //返回状态码
	private String re_msg; //返回信息
	private String redirect_url; //重定向地址
	private Object data; //数据
	
	public int getRe_code() {
		return re_code;
	}
	public void setRe_code(int re_code) {
		this.re_code = re_code;
	}
	public String getRe_msg() {
		return re_msg;
	}
	public void setRe_msg(String re_msg) {
		this.re_msg = re_msg;
	}
	public String getRedirect_url() {
		return redirect_url;
	}
	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
