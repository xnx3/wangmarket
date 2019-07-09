package com.xnx3.j2ee;

import java.util.HashMap;
import java.util.Map;

import com.xnx3.ConfigManagerUtil;
import com.xnx3.Lang;
import com.xnx3.j2ee.func.Log;

/**
 * 基础配置、集中管理
 * @author 管雷鸣
 */
public class Global {
	public static boolean isJarRun = true;	//是否是以jar包的形式运行。默认为是jar包方式运行。若为false，则为war方式，放到自行放tomcat中运行
	

	/****站内信****/
	public static boolean MESSAGE_USED = true;			//是否使用站内信息功能，若开启，则访问任何页面都会提前读数据库判断是否有新的未读信息 	systemConfig.xml
	public static int MESSAGE_CONTENT_MINLENGTH = 2;		//发送站内信时短信内容允许的最小字符	systemConfig.xml
	public static int MESSAGE_CONTENT_MAXLENGTH = 100;		//发送站内信时短信内容允许的最大字符 systemConfig.xml
	
	/********文件目录相关，会在当前项目的根目录下的文件夹*********/
	public final static String CACHE_FILE="cache/js/";
	
	/********文件目录相关，用户头像存在于当前项目的根目录下的文件夹*********/
	public final static String USER_HEAD_FILE="upload/userHead/";
	
	/*
	 * 当前项目再硬盘的路径，绝对路径。动态参数，会在项目启动时加载。取此参数，可以使用 {@link #getProjectPath()} 取
	 */
	private static String projectPath=null;
	
	/***** system表的参数,name-value ******/
	public static Map<String, String> system = new HashMap<String, String>();	//value：String字符串，此数据会在应用启动起来后，自动从数据库中将system表的全部数据取出来放到这里。
	/**
	 * 同上system，
	 * 只不过这里的value是Integer
	 * 取这里的数据时，会先判断这个map中是否有数据
	 * 	<ul>
	 * 		<li>若有，直接取出</li>
	 * 		<li>若没有，再找上面的system的map中是否有这个值
	 * 			<ul>
	 * 				<li>若有，则将其转换位int，并缓存到这个map里，下次再取这个值的时候就直接从这里就能取</li>
	 * 				<li>若没有，返回 null （不返回0，因为如果返回null，开发程序的时候就会报错，就能直接定位到问题所在）</li>
	 * 			</ul>
	 * 		</li>
	 * 	</ul>
	 */
	public static Map<String, Integer> systemForInteger = new HashMap<String, Integer>();	//同上system，只不过这里的value是Integer，取这里的数据时，会先判断这map中是否有数据，若没有，再找system中是否有，若有，则将其转换位int，并缓存到这个map里，下次取的时候就直接从这里取

	
	/**********固定参数**********/
	public final static int USER_PASSWORD_SALT_NUMBER=2;	//密码散列几次，2即可,需要跟配置文件的散列次数一致
	public final static int PROMPT_STATE_SUCCESS=1;			//中专提示页面prompt.jsp的成功提示状态
	public final static int PROMPT_STATE_ERROR=0;			//中专提示页面prompt.jsp的失败（错误）提示状态
	
	/*****论坛相关******/
	public static int bbs_titleMinLength;	//发帖标题允许的最小长度（英文长度）
	public static int bbs_titleMaxLength;	//发帖标题允许的最大长度（英文长度），最大值同时取决于数据库字段的最大值限制
	public static int bbs_textMinLength;	//内容所允许的最小长度（英文长度)
	public static boolean bbs_readPost_addLog;	//是否将阅读帖子写日志进行记录
	public static int bbs_commentTextMinLength;	//回帖，内容所允许的最小长度（英文长度)
	public static int bbs_commentTextMaxLength;	//回帖，内容所允许的最大长度（英文长度)
	public static int DEFAULT_BBS_CREATEPOST_CLASSID=1;	//发帖时，如果没有选择发帖到哪，这里默认选中的那个分类id systemConfig.xml
	public static int POST_INFO_AUTOCAT_MAX=60;		//发帖时，自动截取内容前多少个字节作为简介。这里是截取的开头的最大字符 systemConfig.xml
	
	/**** 文件上传 *****/
	public static String ossFileUploadImageSuffixList = ""; //图片文件，允许上传的图片的后缀名，在 systemConfig.xml 中的 attachmentFile.allowUploadSuffix 中配置
	
	/***** 权限相关 *****/
	public static int roleId_admin = 9;	//超级管理员的角色id
	public static int roleId_user = 1;		//普通用户的角色id
	
	/****** iw框架down下来，搭建开发环境时使用到的参数 ******/
	public static boolean databaseCreateFinish = true;		//数据库是否完成创建导入。默认是数据库正常已创建。有initServlet初始化时进行判断，给其赋值
	public static String databaseCreateFinish_explain = "";	//其上出错的说明
	public static boolean xnx3Config_oss = true;			//OSS文件上传是否完成配置
	public static String xnx3Config_oss_explain = "";		//其上出错的说明
	
	static{
		/*****论坛相关******/
		bbs_titleMinLength = Lang.stringToInt(ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("bbs.titleMinLength"), 0);
		bbs_titleMaxLength = Lang.stringToInt(ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("bbs.titleMaxLength"), 0);
		bbs_textMinLength = Lang.stringToInt(ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("bbs.textMinLength"), 0);
		bbs_readPost_addLog = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("bbs.readPost_addLog").equals("true");
		bbs_commentTextMinLength = Lang.stringToInt(ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("bbs.commentTextMinLength"), 0);
		bbs_commentTextMaxLength = Lang.stringToInt(ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("bbs.commentTextMaxLength"), 0);
		DEFAULT_BBS_CREATEPOST_CLASSID = Lang.stringToInt(ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("bbs.default_createPost_classId"), 0);
		POST_INFO_AUTOCAT_MAX = Lang.stringToInt(ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("bbs.post_info_autoCat_max"), 0);
		
		
		/****图片文件上传 ****/
		ossFileUploadImageSuffixList = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("attachmentFile.allowUploadSuffix");
	}
	
	
	/**
	 * 返回 system 表的值
	 * @param systemName
	 * @return
	 */
	public static String get(String systemName){
		return system.get(systemName);
	}
	
	/**
	 * 返回 system 表的值（int型的，此取的数据源来源于 {@link #get(String)}，只不过针对Integer进行了二次缓存 ）
	 * @param systemName 要获取的值的变量名
	 * @return 变量的值。注意，若没有，会返回0
	 */
	public static int getInt(String systemName){
		Integer i = systemForInteger.get(systemName);
		if(i == null){
			//没有这个值，那么从system这个原始map中找找
			String s = system.get(systemName);
			if(s != null){
				i = Integer.parseInt(s);
				systemForInteger.put(systemName, i);
			}
		}
		return i==null? 0:i;
	}
	
	/**
	 * 当前项目再硬盘的路径，绝对路径 返回格式如 /aaa/bb/ccc/WEB-INF/classes/  最后会加上 /
	 */
	public static String getProjectPath(){
		if(projectPath == null){
			String path = new Global().getClass().getResource("/").getPath();
			projectPath = path.replace("WEB-INF/classes/", "");
			Log.info("projectPath : "+projectPath);
		}
		return projectPath;
	}
	
	public static void main(String[] args) {
		System.out.println("v2.0.20180113");
	}
}
