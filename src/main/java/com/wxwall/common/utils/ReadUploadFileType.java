package com.wxwall.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.MimetypesFileTypeMap;

public class ReadUploadFileType {
	private static Properties properties;
	static{
		PropertiesTool propertiesTool = new PropertiesTool();
		propertiesTool.loadFile("allow_upload_file_type.properties", "UTF-8");
		properties = propertiesTool.getTool();
	}
	/**
	 * 判断该文件是否为上传的文件类型
	 * @param uploadfile
	 * @return
	 */
	public static Boolean readUploadFileType(File uploadfile){
		if(uploadfile!=null&&uploadfile.length()>0){
			String ext = uploadfile.getName().substring(uploadfile.getName().lastIndexOf(".")+1).toLowerCase();
			List<String> allowfiletype = new ArrayList<String>();
			for(Object key : properties.keySet()){
				String value = (String)properties.get(key);
				String[] values = value.split(",");
				for(String v:values){
					allowfiletype.add(v.trim());
				}
			}
			// "Mime Type of gumby.gif is image/gif" 
			return allowfiletype.contains( new MimetypesFileTypeMap().getContentType(uploadfile).toLowerCase())&&properties.keySet().contains(ext);
		}
		return true;
	}
}
