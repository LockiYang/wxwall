package com.wxwall.common.utils;

public class PropertiesUtils {
	private static PropertiesTool propertiesTool = new PropertiesTool();
	
	static {
		propertiesTool.loadFile("wxwall.properties", "UTF-8");
	}
	
	public static String getString(String key) {
		return propertiesTool.getString(key);
	}
}
