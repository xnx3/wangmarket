package com.xnx3.wangmarket.superadmin.util.pluginManage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * ZIP文件的压缩和解压操作工具类
 * @author 李鑫
 */
public class ZipUtils {
	public static void main(String[] args) throws IOException {
		String path = "/Users/a/GitHub/wangmarket/target/wangmarket/WEB-INF/view/plugin/formManage";
		dozip(path, "/Users/a/zip.zip");
	}
	
	/**
	 * 对文件进行压缩
	 * @author 李鑫
	 * @param srcfile 需要压缩的文件夹路径 例："/Users/a/test/"
	 * @param zipFileName 压缩的目标文件名 例："/Users/a/test.zip"
	 * @throws IOException
	 */
	public static void dozip(String srcfile, String zipFileName) throws IOException {
		String temp = "";
		File src = new File(srcfile);
		File zipFile = new File(zipFileName);
		// 判断要压缩的文件存不存在
		if (!src.exists()) {
			System.err.println("要压缩的文件不存在！");
			System.exit(1);
		}
		// 如果说压缩路径不存在，则创建
		if (!zipFile.getParentFile().exists()) {
			zipFile.getParentFile().mkdirs();
		}
		// 封装压缩的路径
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(zipFileName));
		// 这里可以加入校验
		// CheckedOutputStream cos = new CheckedOutputStream(bos,new CRC32());
		// 还可以设置压缩格式，默认UTF-8
		Charset charset = Charset.forName("UTF-8");
		ZipOutputStream zos = new ZipOutputStream(bos, charset);
		zip(src, zos, temp);
		// 关闭流
		zos.flush();
		zos.close();
	}

	/**
	 * 对文件夹进行zip压缩
	 * @author 李鑫
	 * @param file 需要压缩的文件
	 * @param zos 目标压缩文件的输出流 
	 * @param temp 在压缩文件中的目录
	 * @throws IOException
	 */
	private static void zip(File file, ZipOutputStream zos, String temp) throws IOException {
		// 如果不加"/"将会作为文件处理，空文件夹不需要读写操作
		if (file.isDirectory() || file.getName().endsWith(File.separator)) {
			String str = temp + file.getName() + File.separator;
			zos.putNextEntry(new ZipEntry(str));
			File[] files = file.listFiles();
			for (File file2 : files) {
				zip(file2, zos, str);
			}
		} else {
			ZipFile(file, zos, temp);
		}
	}

	/**
	 * 将文件加入到压缩文件中<
	 * @author 李鑫
	 * @param srcFile 需要压缩的文件
	 * @param zos 压缩文件的输出流
	 * @param temp 加入压缩文件中的文件夹目录 例："directoryName/"
	 * @throws IOException
	 */
	private static void ZipFile(File srcFile, ZipOutputStream zos, String temp) throws IOException {
		// 默认的等级压缩-1
		// zos.setLevel(xxx);
		// 封装待压缩文件
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
		zos.putNextEntry(new ZipEntry(temp + srcFile.getName()));

		//进行文件的写入
		byte buff[] = new byte[1024];
		int len;
		while ((len = bis.read(buff)) != -1) {
			zos.write(buff, 0, len);
		}
		// 按标准需要关闭当前条目，不写也行
		zos.closeEntry();
		bis.close();
	}
}
