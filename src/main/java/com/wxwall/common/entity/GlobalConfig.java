package com.wxwall.common.entity;

import java.util.Map;

import com.google.common.collect.Maps;
import com.wxwall.common.utils.PropertiesLoader;

/**
 * app常量类
 * @author Locki
 * @date 2014年8月19日
 *
 */
public class GlobalConfig {

	/**
	 * 系统角色
	 */
//	public final static String SYSTEM_ROLE_ADMIN = "admin";
//	public final static String SYSTEM_ROLE_USER = "user";
	
	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> properties = Maps.newHashMap();
	
	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader propertiesLoader = new PropertiesLoader("jeesite.properties");
	
	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		String value = properties.get(key);
		if (value == null){
			value = propertiesLoader.getProperty(key);
			properties.put(key, value);
		}
		return value;
	}
}
