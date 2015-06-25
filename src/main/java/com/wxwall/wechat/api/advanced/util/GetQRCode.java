package com.wxwall.wechat.api.advanced.util;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import com.wxwall.wechat.api.util.CommonUtil;

/**
*
* 项目名称：wechatapi
* 类名称：CreateQRCode
* 类描述：获取二维码工具
* 创建人：Myna Wang
* 创建时间：2014-3-7 下午3:35:29
* @version
*/
public class GetQRCode extends CommonUtil{
	// LOGO宽度
	private static final int WIDTH = 60;
	// LOGO高度
	private static final int HEIGHT = 60;
	// 二维码尺寸
	private static final int QRCODE_SIZE = 430;
	private static final String FORMAT_NAME = "JPEG";
	/**
	 * 通过ticket换取二维码
	 *
	 * @param ticket 获取的二维码ticket
	 * @param savePath 保存路径
	 * @return String
	 */
	public static String getQRCode(String ticket,String savePath, String fileName) {
		String filePath = null;
		String requestUrl = GET_QRCODE_URL.replace("TICKET",
				urlEncodeUTF8(ticket));
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			if (!savePath.endsWith("/")) {
				savePath += "/";
			}
			// 将ticket作为文件名
			filePath = savePath + fileName;
			// 将微信服务器返回的输入流写入文件
			BufferedInputStream bis = new BufferedInputStream(
					conn.getInputStream());
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);
			fos.close();
			bis.close();
			conn.disconnect();
			log.info("根据ticket换取二维码成功，filePath=" + filePath);
		} catch (Exception e) {
			filePath = null;
			log.error("根据ticket换取二维码失败:{}", e);
		}
		return filePath;
	}
	
	public static String getLogoQRCode(String ticket,String savePath, String fileName, String imgPath) {
		String filePath=null;
		String tmpFilePath=null;
		String requestUrl=GET_QRCODE_URL.replace("TICKET", urlEncodeUTF8(ticket));
		try {
			URL url=new URL(requestUrl);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			if (!savePath.endsWith("/")) {
				savePath+="/";
			}
			// 将ticket作为文件名
			filePath=savePath+fileName;
			
			tmpFilePath = savePath+fileName + "_tmp";
			// 将微信服务器返回的输入流写入文件
			BufferedInputStream bis=new BufferedInputStream(conn.getInputStream());
			FileOutputStream fos=new FileOutputStream(new File(tmpFilePath));
			byte[] buf=new byte[8096];
			int size=0;
			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);
			fos.close();
			bis.close();
			conn.disconnect();
			insertImage(tmpFilePath, imgPath, filePath, true);
			File tmpFile = new File(tmpFilePath);
			if(tmpFile.exists()) {
				FileUtils.forceDelete(tmpFile);
			}
			log.info("根据ticket换取二维码成功，filePath="+filePath);
		} catch (Exception e) {
			filePath=null;
			log.error("根据ticket换取二维码失败:{}",e);
		}
		return filePath;
	}
	
	private static void insertImage(String sourcePath, String imgPath, String destPath,
			boolean needCompress) throws Exception {
		BufferedImage source = null;
		File file = new File(imgPath);
		if (!file.exists()) {
			System.err.println(""+imgPath+"   该文件不存在！");
			return;
		}
		
		File sourceFile = new File(sourcePath);
		if (!file.exists()) {
			System.err.println(""+imgPath+"   该文件不存在！");
			return;
		}
		source = ImageIO.read(sourceFile);
		Image src = ImageIO.read(new File(imgPath));
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		if (needCompress) { // 压缩LOGO
			if (width > WIDTH) {
				width = WIDTH;
			}
			if (height > HEIGHT) {
				height = HEIGHT;
			}
			Image image = src.getScaledInstance(width, height,
					Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			src = image;
		}
		// 插入LOGO
		Graphics2D graph = source.createGraphics();
		int x = (QRCODE_SIZE - width) / 2;
		int y = (QRCODE_SIZE - height) / 2;
		graph.drawImage(src, x, y, width, height, null);
		Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
		ImageIO.write(source, FORMAT_NAME, new File(destPath));
	}

	public static void main(String[] args) {
		/*String ticket="gQFw8ToAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2YwUEJOazNsNFpTWUliMnZJRzhpAAIElynLVAMEAAAAAA==";
		String savePath="/tmp";
		// 根据ticket换取二维码
		getQRCode(ticket, savePath, "1.jpg");*/
		try {
			insertImage("E:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/wxwall/store/file/2015_3_2/images/1_1426043479241_alipay.jpg", 
					"E:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/wxwall/statics/images/icon-heart.png", "E:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/wxwall/store/file/2015_3_2/images/test1.jpg", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
