package com.wxwall.wechat.api.advanced.util;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.wxwall.wechat.api.advanced.model.WeixinGroup;
import com.wxwall.wechat.api.util.CommonUtil;


/**  
*   
* 项目名称：wechatapi  
* 类名称：GroupUtil  
* 类描述：用户组操作工具  
* 创建人：Myna Wang  
* 创建时间：2014-3-8 下午2:17:48  
* @version       
*/
public class GroupUtil extends CommonUtil{
	/**
	 * 查询所有分组
	 * 
	 * @param accessToken 调用接口凭证
	 * @return List<WeixinGroup>
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static List<WeixinGroup> getGroups(String accessToken) {
		List<WeixinGroup> weixinGroupList=null;
		String requestUrl=GET_GROUPS_URL.replace("ACCESS_TOKEN", accessToken);
		// 查询分组
		JSONObject jsonObject=httpRequest(requestUrl, "GET", null);
		
		if (null!=jsonObject) {
			try {
				weixinGroupList = JSONArray.toList
						(jsonObject.getJSONArray("groups"),WeixinGroup.class);
			} catch (JSONException e) {
				weixinGroupList=null;
				int errorCode=jsonObject.getInt("errcode");
				String errorMsg=jsonObject.getString("errmsg");
				log.error("获取分组失败 errcode:{} errmsg:{} ", errorCode, errorMsg);
			}
		}
		return weixinGroupList;
	}
	
	/**
	 * 创建分组
	 * 
	 * @param accessToken 调用接口凭证
	 * @param groupName 组名
	 * @return WeixinGroup
	 */
	public static WeixinGroup createGroup(String accessToken,String groupName) {
		WeixinGroup weixinGroup=null;
		String requestUrl=CREATE_GROUPS_URL.replace("ACCESS_TOKEN", accessToken);
		// 需要提交的json数据
		String jsonData="{\"group\":{\"name\":\"%s\"}}";
		// 创建分组
		JSONObject jsonObject=httpRequest(requestUrl, "POST", 
				String.format(jsonData, groupName));
		if (null!=jsonObject) {
			try {
				weixinGroup=new WeixinGroup();
				weixinGroup.setId(jsonObject.getJSONObject("group").getInt("id"));
				weixinGroup.setName(jsonObject.getJSONObject("group").getString("name"));
			} catch (JSONException e) {
				weixinGroup=null;
				int errorCode=jsonObject.getInt("errcode");
				String errorMsg=jsonObject.getString("errmsg");
				log.error("创建分组失败 errcode:{} errmsg:{} ", errorCode, errorMsg);
			}
		}
		return weixinGroup;
	}
	
	/**
	 * 修改分组名
	 * 
	 * @param accessToken 调用接口凭证
	 * @param groupId 分组id
	 * @param groupName 修改后的组名
	 * @return true|false
	 */
	public static boolean updateGroup(String accessToken,int groupId,String groupName) {
		boolean result=false;
		String requestUrl=UPDATE_GROUPS_URL.replace("ACCESS_TOKEN", accessToken);
		// 需要提交的JSON数据
		String jsonData="{\"group\": {\"id\": %d, \"name\": \"%s\"}}";
		// 修改分组名
		JSONObject jsonObject=httpRequest(requestUrl, "POST", 
				String.format(jsonData, groupId,groupName));
		if (null!=jsonObject) {
			int errorCode=jsonObject.getInt("errcode");
			String errorMsg=jsonObject.getString("errmsg");
			if (0==errorCode) {
				result=true;
				log.error("修改分组成功 errcode:{} errmsg:{} ", errorCode, errorMsg);
			}else {
				result=false;
				log.error("修改分组失败 errcode:{} errmsg:{} ", errorCode, errorMsg);
			}
		}
		return result;
	}
	
	/**
	 * 移动用户分组
	 * 
	 * @param accessToken 调用接口凭证
	 * @param openId 用户唯一标识符
	 * @param groupId 分组id
	 * @return true|false
	 */
	public static boolean removeMemberGroups(String accessToken,String openId,int groupId) {
		boolean result=false;
		String requestUrl=REMOVE_MEMBER_URL.replace("ACCESS_TOKEN", accessToken);
		// 需要提交json数据
		String jsonData="{\"openid\":\"%s\",\"to_groupid\":%d}";
		// 移动用户分组
		JSONObject jsonObject=httpRequest(requestUrl, "POST", 
				String.format(jsonData, openId,groupId));
		if (null!=jsonObject) {
			int errorCode=jsonObject.getInt("errcode");
			String errorMsg=jsonObject.getString("errmsg");
			if (0==errorCode) {
				result=true;
				log.error("移动用户分组成功 errcode:{} errmsg:{} ", errorCode, errorMsg);
			}else {
				result=false;
				log.error("移动用户分组失败 errcode:{} errmsg:{} ", errorCode, errorMsg);
			}
		}
		return result;
	}
	
	
	
	
	public static void main(String[] args) {
		// 获取接口访问凭证
		String accessToken=getAccessToken("wx13c0a227486f7e64", "864e16284d38c05c62cddc1be000351e").getAccesstoken();
		
		// 获取分组列表
		List<WeixinGroup> groupList=getGroups(accessToken);
		// 循环输出各分组信息
		for(WeixinGroup group:groupList){
			System.err.println(String.format("ID: %d 名称: %s 用户数: %d", group.getId(),
					group.getName(),group.getCount()));
		}
		
		
		/*// 创建分组
		WeixinGroup group=createGroup(accessToken, "学生");
		System.err.println(String.format("创建学生组成功: %s id: %d", group.getName(),group.getId()));
		
		// 修改分组
		boolean result=updateGroup(accessToken, 100, "傻逼");
		
		// 移动用户分组
		boolean result2=removeMemberGroups(accessToken, "odIK5uJFzt2cg1zZTEpTVdx8sJVo", 100);
		*/
	}
}
