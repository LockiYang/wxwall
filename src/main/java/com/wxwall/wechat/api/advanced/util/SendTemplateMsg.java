package com.wxwall.wechat.api.advanced.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.alibaba.druid.util.StringUtils;
import com.wxwall.wechat.api.advanced.model.TemplateData;
import com.wxwall.wechat.api.advanced.model.WxTemplate;
import com.wxwall.wechat.api.util.CommonUtil;

/**
*
* 项目名称：wechatapi
* 类名称：SendTemplateMsg
* 类描述：发送模板消息
* 创建人：ganjun
* 创建时间：2015-2-28 下午3:35:29
* @version
*/
public class SendTemplateMsg extends CommonUtil{
	/**
	 * 设置公众号模板
	 * @param accessToken 接口访问凭证
	 * @param industryId1 行业ID1
	 * @param industryId2 行业ID2
	 * @return
	 */
	public static String sendTemplateMsg(String accessToken, WxTemplate wxTemplate) {
		String requestUrl=TEMPLATE_SEND_MSG_URL.replace("ACCESS_TOKEN", accessToken);
		// 需要提交的json数据
		String jsonMsg = JSONObject.fromObject(wxTemplate).toString(); 
		// 创建临时带参数二维码
		JSONObject jsonObject = httpRequest(requestUrl, "POST", jsonMsg);
		if (null!=jsonObject) {
			try {
				if (StringUtils.equalsIgnoreCase("0", jsonObject.getString("errcode"))) {
					return jsonObject.getString("msgid");
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
		WxTemplate t = new WxTemplate();
		String open_id = "o5soTuFMlKyknorUzhiy-WXBMKdM";
		String templateId = "TdvThXE5A9PLU6ISdwulSrJUrUawd56QViUVixWJvJI";
		t.setUrl("http://www.baidu.com");
		t.setTouser(open_id);
		t.setTopcolor("#000000");
		t.setTemplate_id(templateId);
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		TemplateData user = new TemplateData();
		user.setColor("#000000");
		user.setValue("杨明");
		m.put("user", user);
		TemplateData pruduct = new TemplateData();
		pruduct.setColor("#000000");
		pruduct.setValue("人民币");
		m.put("pruduct", pruduct);
		TemplateData price = new TemplateData();
		pruduct.setColor("#000000");
		pruduct.setValue("132元");
		m.put("price", price);
		TemplateData remark = new TemplateData();
		remark.setColor("blue");
		remark.setValue("备注说明");
		m.put("remark", remark);
		t.setData(m);
		String msgId = sendTemplateMsg(accessToken, t);
		System.err.println(msgId);
	}
}
