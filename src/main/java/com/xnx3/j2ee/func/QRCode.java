package com.xnx3.j2ee.func;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.xnx3.QRCodeUtil;
import com.xnx3.media.ImageUtil;

/**
 * 二维码相关
 * @author 管雷鸣
 */
public class QRCode {
	

	/**
	 * 在当前页面上输出二维码。使用如：
	 * <pre>
	 * public void test(HttpServletResponse response) throws IOException{
	 *		qRCodeService.showQRCodeForPage("http://www.xnx3.com", response);
	 * }
	 * </pre>
	 * @param content 二维码扫描后的内容，若网址，要写上http://
	 * @param response
	 */
	public static void showQRCodeForPage(String content, HttpServletResponse response) {
		BufferedImage bi = QRCodeUtil.createQRCoder(content);
		
		try {
			response.getOutputStream().write(ImageUtil.bufferedImageToByte(bi, "jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
