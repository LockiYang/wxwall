/*
 * <p>
 *  版权所有 : ©2014 80通讯科技有限公司. 
 * Copyright © PING AN INSURANCE (GROUP) COMPANY OF CHINA ,LTD. All Rights Reserved
 * </p>
 */
package com.wxwall.common.web;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wxwall.common.entity.Result;
import com.wxwall.common.utils.CommonUtils;
import com.wxwall.common.utils.FileUpload;
import com.wxwall.modules.user.entity.User;
import com.wxwall.modules.user.utils.UserUtils;

/**
 * 
 * <p>
 * 描述:通用请求访问接口controller
 * </p>
 * 
 * @see
 * @author ganjun
 * 
 */
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {

	/**
	 * 
	 * <p>
	 * 描述:上传文件到临时目录
	 * </p>
	 * 
	 * @param
	 * @return
	 * @throws
	 * @see
	 * @since %I%
	 */
	@ResponseBody
	@RequestMapping(value = "/uploadImgToTmp")
	public Result uploadToTmp(HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		try {
			// TEST中
			User loginUser = UserUtils.getUser();
			//
			String rootPath = request.getSession().getServletContext().getRealPath("/"); // 项目根目录的实际路径
			String relativePath = CommonUtils.getTmpDir(loginUser.getId());
			String tempPath = rootPath + File.separator +relativePath; // 实际路径
			String showUrl = CommonUtils.getServerUrl(request) + File.separator + relativePath; // 网络路径
			if (tempPath != null) {
				FileUpload fileUpload = new FileUpload();
				result = fileUpload.upload(request, tempPath, showUrl, FileUpload.maxImgSize, true);
			}
			return result;
		} catch (Exception e) {
			LOG.error(e);
			result.setSuccess(false);
			result.setMessage("系统内部错误");
			return result;
		}
	}
}
