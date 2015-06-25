package com.wxwall.wechat.api.advanced.util;

import net.sf.json.JSONObject;

import com.alibaba.druid.util.StringUtils;
import com.wxwall.wechat.api.util.CommonUtil;

/**
*
* 项目名称：wechatapi
* 类名称：AddTemplate
* 类描述：设置公众号模板
* 创建人：ganjun
* 创建时间：2015-2-28 下午3:35:29
* @version
*/
public class AddTemplate extends CommonUtil{
	/**
	 * 设置公众号模板
	 * @param accessToken 接口访问凭证
	 * @param industryId1 行业ID1
	 * @param industryId2 行业ID2
	 * @return
	 */
	public static String addTemplate(String accessToken, String templateIdShort) {
		String requestUrl=TEMPLATE_ADD_URL.replace("ACCESS_TOKEN", accessToken);
		// 需要提交的json数据
		String jsonMsg="{\"template_id_short\": \"%s\"}";
		// 创建临时带参数二维码
		JSONObject jsonObject = httpRequest(requestUrl, "POST",
				String.format(jsonMsg, templateIdShort));
		if (null!=jsonObject) {
			try {
				if (StringUtils.equalsIgnoreCase("0", jsonObject.getString("errcode"))) {
					return jsonObject.getString("template_id");
				} else {
					log.error("设置公众号模板失败 errCode:{} errormsg:{} ",jsonObject.getString("errcode"),jsonObject.getString("errmsg"));
					return null;
				}
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				return null;
			}
		}
		return null;
	}

	// String accessToken=getAccessToken("wx13c0a227486f7e64", "864e16284d38c05c62cddc1be000351e").getAccesstoken();
	public static void main(String[] args) {
		// 获取接口访问凭证
		String accessToken=getAccessToken("wxa15b5a3c0cc56dd3", "abe1c14cd5bf04719ace3c3197f06068").getAccesstoken();
		// 创建临时二维码
		// gQHN7zoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL0pFTUxKVDdsS1Q1UWFOeGtvbTJ3AAIEIn4ZUwMECAcAAA==
		String templateId = addTemplate(accessToken, "TM00015");
		System.err.println(templateId);
		// 创建永久二维码
		// gQGx8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL1JrTlIyajNsZ2o3NzlyNXFfRzJ3AAIEGKUZUwMEPAAAAA==
		//String Permanentqrcode=createPermanentQRCode(accessToken, "2");
		//System.err.println(Permanentqrcode);
	}
}
