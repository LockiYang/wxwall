package com.wxwall.common.utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.wxwall.common.entity.Result;
import com.wxwall.modules.user.entity.User;
import com.wxwall.modules.user.utils.UserUtils;

public class FileUpload {
	protected Logger LOG = Logger.getLogger(getClass());
	private int sizeThreshold = 1024;
	private PropertiesTool propertiesTool;
	private HttpServletRequest request;
	public static final long maxImgSize = 2 * 1024 * 1024L;
	public static final long middleImgSize = 1 * 1024 * 1024L;

	// 初始化
	public FileUpload() {
		propertiesTool = new PropertiesTool();
		propertiesTool.loadFile("wxwall.properties", "UTF-8");

		sizeThreshold = Integer.parseInt(propertiesTool
				.getString("wxwall.template.description.sizeThreshold"));
	}

	public FileUpload(HttpServletRequest request) {
		this.request = request;
	}

	public File upload(String storePath, long maxSize, boolean checkImg) {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		File file = null;
		if (isMultipart) {

			try {
				File storePathFile = new File(storePath);
				if (!storePathFile.exists()) {
					storePathFile.mkdirs();
				}

				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(sizeThreshold); // 设置缓冲区大小，这里是4kb
				factory.setRepository(storePathFile);// 设置缓冲区目录

				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("UTF-8");// 解决文件乱码问题
				// upload.setSizeMax(sizeMax);// 设置最大文件尺寸

				List<FileItem> items = upload.parseRequest(request);

				// 检查是否符合上传的类型
				if (checkImg && !checkFileType(items))
					throw new RuntimeException("图片格式不正确，只支持gif，jpg，png，bmp");

				// 保存文件
				String storefileName = null;
				for (FileItem item : items) {
					if (!item.isFormField()) {// 如果是文件类型
						String name = item.getName();// 获得文件名 包括路径
						if (name != null) {
							if (maxSize != 0 && item.getSize() > maxSize)
								throw new RuntimeException("图片大小不能超过" + maxSize
										/ 1024 / 1024 + "M");

							File fullFile = new File(item.getName());

							// TODO 生成文件名
							User user = UserUtils.getUser();
							storefileName = CommonUtils.generateNewFileName(
									user.getId(), fullFile.getName());

							file = new File(storePathFile, storefileName);
							item.write(file);
						}
					}
				}
			} catch (RuntimeException re) {
				LOG.error(re.getMessage(), re);
				throw re;
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				throw new RuntimeException("系统异常");
			}
		} else {
			throw new RuntimeException("请求中不包含文件");
		}
		return file;
	}

	/**
	 * 上传单文件
	 * 
	 * @param request
	 * @param storePath
	 *            存储路径
	 * @param showUrl
	 *            url显示路径
	 * @param maxSize
	 *            最大上传size, 默认0表示不限制大小
	 * @param checkImg
	 *            是否检测图片格式
	 * @return true 上传成功 false上传失败
	 */
	public Result upload(HttpServletRequest request, String storePath,
			String showUrl, long maxSize, boolean checkImg) {
		Result result = new Result();
		// 检查输入请求是否为multipart表单数据。
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			/** 为该请求创建一个DiskFileItemFactory对象，通过它来解析请求。执行解析后，所有的表单项目都保存在一个List中。 **/
			Map<String, Object> resultData = new HashMap<String, Object>();

			try {
				File storePathFile = new File(storePath);
				if (!storePathFile.exists()) {
					storePathFile.mkdirs();
				}

				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(sizeThreshold); // 设置缓冲区大小，这里是4kb
				factory.setRepository(storePathFile);// 设置缓冲区目录

				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("UTF-8");// 解决文件乱码问题
				// upload.setSizeMax(sizeMax);// 设置最大文件尺寸

				List<FileItem> items = upload.parseRequest(request);

				// 检查是否符合上传的类型
				if (checkImg && !checkFileType(items)) {
					result.setSuccess(false);
					result.setMessage("图片格式不正确，只支持gif，jpg，png，bmp");
					return result;
				}

				// 保存文件
				String storefileName = null;
				for (FileItem item : items) {
					if (!item.isFormField()) {// 如果是文件类型
						String name = item.getName();// 获得文件名 包括路径
						if (name != null) {
							if (maxSize != 0 && item.getSize() > maxSize) {
								result.setSuccess(false);
								result.setMessage("图片大小不能超过" + maxSize / 1024
										/ 1024 + "K");
								return result;
							}

							File fullFile = new File(item.getName());

							// TODO 生成文件名
							User user = UserUtils.getUser();
							storefileName = CommonUtils.generateNewFileName(
									user.getId(), fullFile.getName());

							File savedFile = new File(storePathFile,
									storefileName);
							item.write(savedFile);

							resultData.put("name", fullFile.getName());
							resultData.put("size", item.getSize());
							resultData.put("pic", showUrl + File.separator
									+ storefileName);
							resultData.put("tmpPath", savedFile.getPath());
						}
					}
				}

				result.setSuccess(true);
				result.setData(resultData);
			} catch (Exception e) {
				result.setSuccess(false);
				result.setMessage(e.getMessage());
			}
		} else {
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 删除一组文件
	 * 
	 * @param filePath
	 *            文件路径数组
	 */
	public void deleteFile(String[] filePath, String uploadPath) {
		if (uploadPath != null && !"".equals(uploadPath)) {
			if (filePath != null && filePath.length > 0) {
				for (String path : filePath) {
					String realPath = uploadPath + path;
					File delfile = new File(realPath);
					if (delfile.exists()) {
						delfile.delete();
					}
				}
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param filePath
	 *            单个文件路径
	 */
	public void deleteFile(String filePath, String uploadPath) {
		if (uploadPath != null && !"".equals(uploadPath)) {
			if (filePath != null && !"".equals(filePath)) {
				String[] path = new String[] { filePath };
				deleteFile(path, uploadPath);
			}
		}
	}

	/**
	 * 判断上传文件类型
	 * 
	 * @param items
	 * @return
	 */
	private boolean checkFileType(List<FileItem> items) {
		for (FileItem item : items) {
			if (!item.isFormField()) {// 如果是文件类型
				String name = item.getName();// 获得文件名 包括路径啊
				if (name != null) {
					File fullFile = new File(item.getName());
					if (!ReadUploadFileType.readUploadFileType(fullFile))
						return false;
				}
			}
		}
		return true;
	}
}
