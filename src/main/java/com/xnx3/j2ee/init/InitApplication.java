package com.xnx3.j2ee.init;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.xnx3.ConfigManagerUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.generateCache.Message;
import com.xnx3.j2ee.generateCache.PayLog;
import com.xnx3.j2ee.generateCache.Role;
import com.xnx3.j2ee.generateCache.SmsLog;
import com.xnx3.j2ee.generateCache.User;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.net.OSSUtil;

/**
 * 系统启动初始化
 * @author 管雷鸣
 */
@Component
public class InitApplication implements CommandLineRunner{
	@Resource
	private SqlService sqlService;
	
	@Value("${spring.datasource.driver-class-name}")
	private String databaseSourceDriverClassName;
	
	public InitApplication() {
		ConsoleUtil.debug("项目启动后开启自动初始化缓存数据加载");
	}

	public void run(String... args) throws Exception {
		//加在systemConfig.xml中自动检测的项
		
		boolean checkDb = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("startAutoCheck.db").equals("true");
		try {
			//数据库自动检测
			if(checkDb){
				checkDb();
			}
		} catch (org.springframework.beans.factory.NoSuchBeanDefinitionException e) {
			//未使用数据库，此项忽略
			if(checkDb){
				ConsoleUtil.info("检测到spring中没有sqlService这个bean，也就是当前项目未使用数据库！数据库自动检测略过");
			}
		}
		
		//设置 附件存储 的位置，是阿里云，还是本地。参数来源于数据库
		AttachmentUtil.mode = SystemUtil.get("ATTACHMENT_FILE_MODE");
		if(AttachmentUtil.mode == null){
			AttachmentUtil.mode = AttachmentUtil.MODE_LOCAL_FILE;
			ConsoleUtil.info("AttachmentUtil.mode = "+AttachmentUtil.mode);
		}
		
		//如果使用的是阿里云OSS，进行OSS初始化赋值。
		if(AttachmentUtil.isMode(AttachmentUtil.MODE_ALIYUN_OSS)){
			initOssConfig();
		}
		
		//附件、文件的请求网址(CDN会先查找数据库配置的此项，若此项没有配置，才会使用xnx3Config.xml中配置的oss的cdn)，本地服务器作为存储磁盘，必须使用数据库配置的此附件地址
		if(AttachmentUtil.netUrl() == null){
			ConsoleUtil.debug("未发现当前上传图片、附件所使用的域名。");
			ConsoleUtil.debug("    设置方式：");
			ConsoleUtil.debug("    1. 本项目在开启后，取第一次访问时使用的url，作为当前的 ATTACHMENT_FILE_URL");
			ConsoleUtil.debug("    2. 进入总管理后台－系统管理－系统变量，设置ATTACHMENT_FILE_URL变量，加上图片等附件的访问域名，格式如： http://res.weiunity.com/");
			ConsoleUtil.debug("    3. 您在程序中自行进行设置AttachmentUtil.setNetUrl(url);");
		}
		ConsoleUtil.debug("AttachmentUtil.url : "+AttachmentUtil.netUrl());
		

		/*以下为生成相关数据缓存*/
		try {
			new Message();
		} catch (Throwable e) {}
		try {
			new PayLog();
		} catch (Throwable e) {}
		try {
			new SmsLog();
		} catch (Throwable e) {}
		try {
			new User();
		} catch (Throwable e) {}
	}
	
	

	/**
	 * 项目自动运行后，检测数据库是否导入 iw.sql
	 */
	public void checkDb(){
		//判断一下，当 system 表中有数据时，才会加载postClass、role、system等数据库信息。反之，如果system表没有数据，也就是认为开发者刚吧iw框架假设起来，还没有往里填充数据，既然没有数据，便不需要加载这几个数据表的数据了
		boolean useDB = false;
		
		if(databaseSourceDriverClassName.equals("org.sqlite.JDBC")){
			//使用sqlite
			ConsoleUtil.info("Using the database : Sqlite");
			useDB = true;
		}else{
			//使用Mysql
			ConsoleUtil.info("Using the database : Mysql");
			List<Map<String,Object>> map = sqlService.findMapBySqlQuery("SHOW TABLES LIKE '%system%'");
			if(map.size() > 0){
				useDB = true;
			}
		}
		
		//如果使用数据库，则加载初始化的一些数据
		if(useDB){
//			generateCache_postClass();
			readSystemTable();
			try {
				new Role().role(sqlService);
			} catch (Throwable e) {
				ConsoleUtil.debug("权限系统异常:"+e.getMessage()+"，如果您当前项目使用不到权限编辑操作，此项忽略即可");
			}
		}else{
			Global.databaseCreateFinish = false;
			Global.databaseCreateFinish_explain = "数据库异常：请将数据库中的初始数据导入，数据文件地址  https://github.com/xnx3/iw/blob/master/iw.sql";
			ConsoleUtil.debug(Global.databaseCreateFinish_explain);
		}
	}
	
	/**
	 * 读system表数据
	 */
	public void readSystemTable(){
		Global.system.clear();
		ConsoleUtil.debug("开始装载System数据表信息");
		
		List<Map<String,Object>> list = sqlService.findMapBySqlQuery("SELECT name,value FROM system");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String name = map.get("name").toString();
			String value = map.get("value") != null? map.get("value").toString():"";
			Global.system.put(name, value);
			ConsoleUtil.debug(name+"="+value);
		}
		
		ConsoleUtil.info("system 表数据载入内存完毕，共"+list.size()+"条数据");
	}
	
	/**
	 * 检测OSS配置信息，服务于 {@link OSS}，保证其能正常使用的配置是否正常
	 */
	public void initOssConfig(){
		OSSUtil.accessKeyId = SystemUtil.get("ALIYUN_ACCESSKEYID");
		OSSUtil.accessKeySecret = SystemUtil.get("ALIYUN_ACCESSKEYSECRET");
		OSSUtil.bucketName = SystemUtil.get("ALIYUN_OSS_BUCKETNAME");
		OSSUtil.endpoint = SystemUtil.get("ALIYUN_OSS_ENDPOINT");
		OSSUtil.url = AttachmentUtil.netUrl();
		
		boolean checkOss = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("startAutoCheck.oss").equals("true");
		if(!checkOss){
			//若不启用OSS自检，直接退出此方法即可
			return;
		}
		
		if(OSSUtil.accessKeyId == null || OSSUtil.accessKeySecret.length() < 10){
			ConsoleUtil.info("OSS对象存储初始化时，accessKeyId 或 accessKeyId 无有效值（字符小于10）");
			return;
		}
	}
}
