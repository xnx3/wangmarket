package com.xnx3.j2ee.util.AttachmentMode;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.xnx3.j2ee.util.AttachmentMode.bean.SubFileBean;
import com.xnx3.j2ee.vo.UploadFileVO;
import com.xnx3.net.ossbean.PutResult;

/**
 * 存储模块接口。比如阿里云、华为云、服务器本地存储，都要实现这个接口
 * @author 管雷鸣
 *
 */
public interface StorageModeInterface {

	/**
	 * 给出文本内容，写出文件。写出UTF－8编码
	 * @param path 写出的路径,上传后的文件所在的目录＋文件名，如 "jar/file/xnx3.html"
	 * @param text 文本内容
	 * @param encode 编码格式，可传入 {@link FileUtil#GBK}、{@link FileUtil#UTF8}
	 */
	public void putStringFile(String path, String text, String encode);
	
	/**
	 * 上传本地文件。上传的文件名会被自动重命名为uuid+后缀
	 * @param filePath 上传后的文件所在的目录、路径，如 "jar/file/"
	 * @param localFile 本地要上传的文件
	 * @return {@link PutResult} 若失败，返回null
	 */
	public UploadFileVO put(String filePath, File localFile);
	
	
	/**
	 * 上传文件。上传后的文件名固定
	 * @param path 上传到哪里，包含上传后的文件名，如"image/head/123.jpg"
	 * @param inputStream 文件
	 * @return {@link UploadFileVO}
	 */
	public UploadFileVO put(String path,InputStream inputStream);
	
	/**
	 * 传入一个路径，得到其源代码(文本)
	 * @param path 要获取的文本内容的路径，如  site/123/index.html
	 * @return 返回其文本内容。若找不到，或出错，则返回 null
	 */
	public String getTextByPath(String path);
	
	/**
	 * 删除文件
	 * @param filePath 文件所在的路径，如 "jar/file/xnx3.jpg"
	 */
	public void deleteObject(String filePath);
	
	/**
	 * 上传文件。UEditor使用
	 * @param filePath 上传到哪里，包含上传后的文件名，如"image/head/123.jpg"
	 * @param content 文件
	 * @param meta {@link com.aliyun.oss.model.ObjectMetadata}其他属性、说明
	 */
	public void putForUEditor(String filePath, InputStream input, ObjectMetadata meta);
	
	/**
	 * 获取某个目录（文件夹）占用空间的大小
	 * @param path 要计算的目录(文件夹)，如 jar/file/
	 * @return 计算出来的大小。单位：字节，B。  千分之一KB
	 */
	public long getDirectorySize(String path);
	
	/**
	 * 复制文件
	 * @param originalFilePath 原本文件所在的路径(相对路径，非绝对路径，操作的是当前附件文件目录下)
	 * @param newFilePath 复制的文件所在的路径，所放的路径。(相对路径，非绝对路径，操作的是当前附件文件目录下)
	 */
	public void copyObject(String originalFilePath, String newFilePath);
	
	/**
	 * 获取某个目录下的子文件列表。获取的只是目录下第一级的子文件，并非是在这个目录下无论目录深度是多少都列出来
	 * @param path 要获取的是哪个目录的子文件。传入如 site/219/
	 * @return 该目录下一级子文件（如果有文件夹，也包含文件夹）列表。如果size为0，则是没有子文件或文件夹。无论什么情况不会反null
	 */
	public List<SubFileBean> getSubFileList(String path);
	
	/**
	 * 获取某个文件的大小，这个是文件，如果传入文件夹，是不起作用的，会返回-1，文件未发现
	 * @param path 要获取的是哪个文件。传入如 site/219/1.html
	 * @return 单位是 B， * 1000 = KB 。 如果返回-1，则是文件未发现，文件不存在
	 */
	public long getFileSize(String path);
	
	/**
	 * 创建文件夹
	 * @param path 要创建的文件路径，传入如 site/219/test/ 则是创建 test 文件夹
	 */
	public void createFolder(String path);
}
