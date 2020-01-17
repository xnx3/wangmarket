package com.xnx3.wangmarket.superadmin.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.Resource;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.MappingException;
import org.hibernate.metamodel.internal.MetamodelImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.xnx3.BaseVO;
import com.xnx3.DateUtil;
import com.xnx3.FileUtil;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.pluginManage.PluginManage;
import com.xnx3.j2ee.pluginManage.PluginRegister;
import com.xnx3.j2ee.pluginManage.controller.BasePluginController;
import com.xnx3.j2ee.util.AttachmentMode.LocalServerMode;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.SafetyUtil;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.util.VersionUtil;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.wangmarket.Authorization;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.superadmin.bean.Application;
import com.xnx3.wangmarket.superadmin.bean.PluginRegisterBean;
import com.xnx3.wangmarket.superadmin.cache.YunPluginMessageCache;
import com.xnx3.wangmarket.superadmin.util.pluginManage.ComponentUtils;
import com.xnx3.wangmarket.superadmin.util.pluginManage.ScanClassesUtil;
import com.xnx3.wangmarket.superadmin.util.pluginManage.TomcatUtil;
import com.xnx3.wangmarket.superadmin.util.pluginManage.ZipUtils;
import com.xnx3.wangmarket.superadmin.vo.InstallApplicationVO;
import net.sf.json.JSONObject;

/**
 * 插件管理中心
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/plugin/pluginManage/")
public class PluginManageController extends BasePluginController {
	@Resource
	SqlService sqlService;
	@Autowired
	private ApplicationContext applicationContext;
	
	/**
	 * 筛选出与插件相关的文件夹
	 * @author 李鑫
	 * <br/>date: 2019-07-20 16:05
	 * <br/>company: www.leimingyun.com
	 */
	class PluginFileFilter implements FilenameFilter{
		// 需要筛选插件的id
		private String pluginId;
		
		public PluginFileFilter(String pluginId) {
			this.pluginId = pluginId;
		}
		@Override
		public boolean accept(File dir, String name) {
			// 根据插件文件夹的指定命名规则进行文件夹的查找
			if(name.startsWith(pluginId) || name.startsWith(pluginId + "_")) {
				return true;
			}
			return false;
		}
	}
	
	/**
	 * 升级网市场插件
	 * @author 李鑫
	 * @param pluginId 进行升级的网市场插件id
	 * @param version 目前安装的网市场插件的版本
	 * @return {@link com.xnx3.BaseVO} result = 1:成功；0：失败；info:失败原因
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/upgradePlugin${url.suffix}")
	public BaseVO upgradePlugin(HttpServletRequest request, 
			@RequestParam(value = "plugin_id", required = false, defaultValue = "") String pluginId, 
			@RequestParam(value = "version", required = false, defaultValue = "") String version) 
					throws ClassNotFoundException, IOException {
		// 判断用户身份
		if(haveSuperAdminAuth() == false) {
			return error("您没有该功能操作权限");
		}
		
		// 参数安全过滤
		pluginId = SafetyUtil.xssFilter(pluginId);
		version = SafetyUtil.xssFilter(version);
		// 校验参数
		if(pluginId == null || pluginId.trim().equals("")) {
			return error("插件ID错误");
		}
		if(version == null || version.trim().equals("")) {
			return error("插件升级版本错误");
		}
		/*
		 * 判断当前网市场版本是否满足插件所支持的网市场最小版本
		 */
		int minVersionInt = YunPluginMessageCache.applicationMap.get(pluginId).getWangmarketVersionMin();
		//当前网市场的版本
		int currentVersionInt = VersionUtil.strToInt(G.VERSION);
		// 版本过低不能安装
		if(currentVersionInt < minVersionInt) {
			return error("当前网市场版较低，该插件支持网市场v"+VersionUtil.intToStr(minVersionInt)+" 及更高版本");
		}
		
		/*
		 * 判断安装的插件是否为未经授权用户可以使用插件
		 */
		// 如果没有授权并且该插件未经授权用户不可用，向客户提示信息
		Application application = YunPluginMessageCache.applicationMap.get(pluginId);
		if(Authorization.copyright) {
			if(application.getSupportFreeVersion() - 1 != 0) {
				return error("该插件未经授权用户不可用");
			}
		}
		// 如果授权并且该插件授权用户不可用，向客户提示信息
//		if(!Authorization.copyright) {
//			if(application.getSupportAuthorizeVersion() - 1 != 0) {
//				return error("该插件经授权用户不可用");
//			}
//		}
//		
		/*
		 *  比较当前安装和云插件库的插件版本
		 */
		// 版本过低不能安装
		if(currentVersionInt - minVersionInt == 0) {
			return error("您目前安装已是最新版本，无需更新");
		}
		
		// 卸载插件
		BaseVO unIstallBaseVO = unIstallPlugin(request, pluginId);
		if(unIstallBaseVO.getResult() == 0) {
			return error("插件卸载已安装版本时出错");
		}
		// 安装最新版本的插件
		BaseVO istallBaseVO = installYunPlugin(request, pluginId);
		if(istallBaseVO.getResult() == 0) {
			return error("插件安装最新版本时出错");
		}
		/*
		 * 在新升级的class上获取插件版本注解上的最新消息更新到缓存中
		 */
		String className = "com.xnx3.wangmarket.plugin." + pluginId + ".Plugin";
		Class<?> forName = Class.forName(className);
		//添加动作日志
		ActionLogUtil.insert(request, "总管理后台-插件管理，升级插件", "升级ID为" + pluginId + "的插件");
		
		return success();
	}
	
	/**
	 * 卸载网市场插件
	 * @author 李鑫 
	 * @param pluginId 插件的id标识，如 kefu
	 * @return {@link com.xnx3.j2ee.vo.BaseVO} result：1：成功、0：失败，info：失败信息
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/unIstallPlugin${url.suffix}")
	public BaseVO unIstallPlugin(HttpServletRequest request, 
			@RequestParam(value = "plugin_id", required = false, defaultValue = "") String pluginId) throws ClassNotFoundException, IOException {
		
		// 判断用户身份
		if(haveSuperAdminAuth() == false) {
			return error("您没有该功能操作权限");
		}		
		// 参数安全过滤
		pluginId = SafetyUtil.xssFilter(pluginId);
		//校验插件id
		if(pluginId == null || pluginId.trim().equals("")) {
			return error("插件信息错误");
		}
		/**
		 *  扫描到所有与插件有关的组件，并且将其咋IOC容器中移除
		 */
		// 扫面与插件有关的IOC组件
		List<Class<?>> classList = getPluginComponent(pluginId);

		//循环遍历所有组件并且移除组件
		Iterator<Class<?>> iterator = classList.iterator();
		String compomentName = null;
		while (iterator.hasNext()) {
			Class<?> compomentClass = iterator.next();
			//得到类名首字母小写的类名称，删除相应的IOC组件
			compomentName = compomentClass.getSimpleName();
			compomentName = compomentName.substring(0, 1).toLowerCase() + compomentName.substring(1, compomentName.length());
			// 将组件添加到IOC容器中
			try {
				// 移除组件
				if(compomentClass.getAnnotation(Entity.class) == null) {
					ComponentUtils.removeBean(compomentClass.getName(), compomentName, applicationContext);
				}
				// 如果是controller，移除requestMapping
				if(compomentClass.getAnnotation(Controller.class) != null) {
					// 移除组件中的RequestMapping映射
					ComponentUtils.unregisterMapping4Class(compomentClass, applicationContext, "\\$\\{url.suffix}", ".do");					
				}
			} catch (Exception e) {
				System.out.println("在容器中已没有该组件");
				e.printStackTrace();
			}
		}
		/*
		 * 删除本地相关的插件文件
		 */
		//获取操作的路径
		Map<String, String> pathMap = getPluginPath(request, pluginId);
		String classPath = pathMap.get("classPath");
		String jspPath = pathMap.get("jspPath");
		String staticPath = pathMap.get("staticPath");
		String metaJspPath = pathMap.get("metaJspPath");
		
		// 将要检索的目录存于list中
		List<String> filePathList = new LinkedList<String>();
		filePathList.add(classPath);
		filePathList.add(jspPath);
		filePathList.add(staticPath);
		if(metaJspPath != null) {
			filePathList.add(metaJspPath);
		}
		
		// 得到所有与插件相关，需要删除的文件
		List<File> deleteFileList = getPluginFile(pluginId, filePathList);
		
		// 循环遍历文件夹进行删除
		Iterator<File> deleIterator = deleteFileList.iterator();
		while (deleIterator.hasNext()) {
			File file = deleIterator.next();
			// 删除文件夹
			deleteDirectory(file, false);
		}
		/*
		 *  在已安装插件的缓存中去除掉次插件,并且在页面功能插件菜单中删除
		 */
		
		// 删除功能插件菜单栏中的菜单
		PluginManage.removePluginCache(pluginId);
		//添加动作日志
		ActionLogUtil.insert(request, "总管理后台-插件管理-卸载插件", "卸载ID为" + pluginId + "的插件");
		return success();
	}
	
	/**
	 * 检索多个文件夹中与插件相关的文件夹
	 * @author 李鑫
	 * @param pluginId 检索的插件id
	 * @param filePathList 需要检索的文件目录列表
	 * @return 检索的与插件相关的文件夹列表
	 */
	private List<File> getPluginFile(String pluginId, List<String> filePathList) {
		// 删除与插件有关的文件夹
		List<File> deleteFileList = new LinkedList<File>();
		// 创建文件名称过滤器
		PluginFileFilter pluginFileFilter = new PluginFileFilter(pluginId);
		File[] listFiles = null;
		Iterator<String> iterator = filePathList.iterator();
		while (iterator.hasNext()) {
			String filePath = iterator.next();
			if(new File(filePath).exists()) {
				// 筛选与插件相关的文件
				listFiles = new File(filePath)
							.listFiles(pluginFileFilter);
				// 将文件整合为list
				deleteFileList.addAll(Arrays.asList(listFiles));
			}
		}
		return deleteFileList;
	}
	
	/**
	 * 删除文件夹或者清空文件夹
	 * @author 李鑫
	 * @param file 需要清空的文件夹 例："/Users/a/directoryName"
	 * @param keepFile 是否当前文件夹，true：保留,false：不保留
	 */
	private void deleteDirectory(File file, boolean keepFile) {
		if(!file.exists()){
			return;
		}
		// 删除子文件夹
		File[] childFile = file.listFiles();
		if(childFile == null){
			return;
		}
		File nowfile;
		for (int i = 0; i < childFile.length; i++) {
			nowfile = childFile[i];
			//如果是文件夹进行递归删除
			if(nowfile.getName().endsWith(File.separator) || nowfile.isDirectory()) {
				deleteDirectory(nowfile, false);
			}else {
				// 是文件直接删除
				nowfile.delete();
			}
		}
		// 是否删除当前的文件夹
		if(!keepFile) {
			file.delete();
		}
	}
	
	/**
	 * 安装本地插件
	 * @author 李鑫
	 * @param pluginName 安装插件的id以及版本号 例如：kefu-10
	 * @return {@link com.xnx3.BaseVO } result：1：成功,info:restart:安装成功，但是重新启动;0：失败，info：失败信息
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@ResponseBody
	@RequestMapping("/installPlugin${url.suffix}")
	public BaseVO installPlugin(HttpServletRequest request, 
			@RequestParam(value = "plugin_id", required = false, defaultValue = "") String pluginId) 
					throws IOException, ClassNotFoundException {
		
		// 判断用户身份
		if(haveSuperAdminAuth() == false) {
			return error("您没有该功能操作权限");
		}		
		// 参数安全过滤
		pluginId = SafetyUtil.xssFilter(pluginId);
		// 校验信息
		if(pluginId == null || pluginId.trim().equals("")) {
			return error("插件ID错误");
		}
		
		/*
		 * 判断插件是否已经安装
		 */
		if(PluginManage.getAllInstallPlugin().get(pluginId) != null) {
			return error("该插件您已安装或者与本地插件ID发生冲突。");
		}
		
		// 插件的压缩文件名称
		String fileName = pluginId + ".zip";
		//获取当前项目的真实路径
		String realPath = request.getServletContext().getRealPath("/");
		Map<String, String> pluginPath = getPluginPath(request, pluginId);
		
		/*
		 * 本地插件直接进行解压安装
		 */
		try {
			// 创建临时文件夹
			if(!new File(realPath + "installPlugin" + File.separator).exists()) {
				new File(realPath + "installPlugin" + File.separator).mkdirs();
			}
			// 对插件文件进行解压到ROOT目录下
			unZip(new File(realPath + "myPlugin" + File.separator + fileName),realPath + "installPlugin" + File.separator);

			// 复制插件文件到项目中
			copyPluginFile(realPath, pluginPath, pluginId);
		} catch (Exception e) {
			e.printStackTrace();
			return error("插件的压缩文件不存在");
		}
		
		/*
		 *  将新添加的class（即与插件相关的组件，带有指定注解的类）文件加入SpringIoc容器中
		 */
		// 扫描与插件相关的组件
		List<Class<?>> classList = getPluginComponent(pluginId);
		Iterator<Class<?>> iterator = classList.iterator();
		String compomentName = null;
		// 是否重新
		boolean restartApplication = false;
		//循环遍历所有的带有Controller的类
		while (iterator.hasNext()) {
			Class<?> compomentClass = iterator.next();
			//得到类名首字母小写的类名称作为在IOC容器中的id
			compomentName = compomentClass.getSimpleName();
			compomentName = compomentName.substring(0, 1).toLowerCase() + compomentName.substring(1, compomentName.length());
			// 将组件添加到IOC容器中
			ComponentUtils.addBean(compomentClass.getName(), compomentName, new HashMap<Object, Object>(), applicationContext);
			// 如果是controller类，进行requestMapping映射。
			if(compomentClass.getAnnotation(Controller.class) != null ) {
				// 注册组件中的Mapping映射
				ComponentUtils.registerMapping4Class(compomentClass, applicationContext, "\\$\\{url.suffix}", ".do");
			}
			// 判断是否hibernate的实体类
			if(compomentClass.getAnnotation(Entity.class) != null) {
				// 判断是否有新加入的实体类
				EntityManager entityManager = applicationContext.getBean(EntityManager.class);
				MetamodelImpl metamodel = (MetamodelImpl) entityManager.getMetamodel();
				// 有新加入实体类的需要重启容器
				try {
					metamodel.entityPersister(compomentClass);
				} catch (MappingException e) {
					// 设置为需要重启
					restartApplication = true;
					e.printStackTrace();
				}
			}
		}
		/*
		 * 将新添加的class文件加入已安装的插件缓存中容器中，并且在首页功能插件中显示
		 */
		String className = "com.xnx3.wangmarket.plugin." + pluginId + ".Plugin";
		Class<?> forName = Class.forName(className);
		try {
			PluginManage.registerPlugin(forName);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		//添加动作日志
		ActionLogUtil.insert(request, "总管理后台-插件管理-安装插件", "安装ID为" + pluginId + "的插件");
		// 重启容器
		if(restartApplication) {
			return success("restart");
		}
		return success();
	}
	
	/**
	 * 安装云插件库插件
	 * @author 李鑫
	 * @param pluginName 安装插件的id以及版本号 例如：kefu-10
	 * @return {@link com.xnx3.BaseVO } result：1：成功,info:restart:安装成功，但是重新启动;0：失败，info：失败信息
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@ResponseBody
	@RequestMapping("/installYunPlugin${url.suffix}")
	public BaseVO installYunPlugin(HttpServletRequest request, 
			@RequestParam(value = "plugin_id", required = false, defaultValue = "") String pluginId) 
					throws IOException, ClassNotFoundException {
		
		// 判断用户身份
		if(haveSuperAdminAuth() == false) {
			return error("您没有该功能操作权限");
		}		
		// 参数安全过滤
		pluginId = SafetyUtil.xssFilter(pluginId);
		
		// 校验信息
		if(pluginId == null || pluginId.trim().equals("")) {
			return error("插件ID错误");
		}
		
		/*
		 * 判断当前使用的数据库是否满足插件要求
		 */
		String dataBaseType = applicationContext.getEnvironment().getProperty("spring.datasource.url");
		if(dataBaseType.indexOf("mysql") > -1) {
			if(YunPluginMessageCache.applicationMap.get(pluginId).getSupportMysql() == 0) {
				return error("该插件不支持MySQL数据库");
			}
		}
		if(dataBaseType.indexOf("sqlite") > -1) {
			if(YunPluginMessageCache.applicationMap.get(pluginId).getSupportSqlite() == 0) {
				return error("该插件不支持Sqlite数据库");
			}
		}
		
		/*
		 * 判断当前网市场版本是否满足插件所支持的网市场最小版本
		 */
		int minVersionInt = YunPluginMessageCache.applicationMap.get(pluginId).getWangmarketVersionMin();
		//当前网市场的版本
		int currentVersionInt = VersionUtil.strToInt(G.VERSION);
		// 版本过低不能安装
		if(currentVersionInt < minVersionInt) {
			return error("当前网市场版较低，该插件支持网市场v"+VersionUtil.intToStr(minVersionInt)+" 及更高版本");
		}
		
		/*
		 * 判断插件是否已经安装
		 */
		if(PluginManage.getAllInstallPlugin().get(pluginId) != null) {
			return error("该插件您已安装或者与本地插件ID发生冲突。");
		}
		
		/*
		 * 判断安装的插件是否为未经授权用户可以使用插件
		 */
		// 如果没有授权并且该插件未经授权用户不可用，向客户提示信息
		Application application = YunPluginMessageCache.applicationMap.get(pluginId);
		if(Authorization.copyright) {
			if(application.getSupportFreeVersion() - 1 != 0) {
				return error("该插件未经授权用户不可用");
			}
		}
		// 如果授权并且该插件授权用户不可用，向客户提示信息
//		if(!Authorization.copyright) {
//			if(application.getSupportAuthorizeVersion() - 1 != 0) {
//				return error("该插件经授权用户不可用");
//			}
//		}
		
		// 下载文件名称
		String fileName = pluginId + ".zip";
		// 获取插件压缩包的下载url
		HttpUtil httpUtil = new HttpUtil();
		// 验证授权身份获取下载地址
		HttpResponse httpResponse = httpUtil.get(com.xnx3.wangmarket.superadmin.Global.APPLICATION_API + "?action=down&plugin_id=" + pluginId + "&auth_id=" + Authorization.auth_id + "&domain=" + SystemUtil.get("AUTO_ASSIGN_DOMAIN"));
		// 请求异常
		if(httpResponse.getCode() - 200 != 0) {
			return error("云端插件库异常，轻稍后重试");
		}
		JSONObject contentJson = JSONObject.fromObject(httpResponse.getContent());
		// 请求结果异常
		if(contentJson.getInt("result") == 0) {
			return error(contentJson.getString("info"));
		}
		// 请求成功，获取下载地址
		String downUrl = contentJson.getString("url");
		//获取当前项目的真实路径
		String realPath = request.getServletContext().getRealPath("/");
		Map<String, String> pluginPath = getPluginPath(request, pluginId);
		
		/*
		 * 创建临时文件
		 */
		if(!new File(realPath + "yunPlugin" + File.separator).exists()) {
			new File(realPath + "yunPlugin" + File.separator).mkdirs();
		}
		if(!new File(realPath + "yunPlugin" + File.separator + fileName).exists()) {
			new File(realPath + "yunPlugin" + File.separator + fileName).createNewFile();
		}
		/*
		 * 云端插件进行下载，并且解压
		 */
		FileUtil.downFile(downUrl, realPath + "yunPlugin" + File.separator + fileName);
		
		try {
			// 创建临时文件夹
			if(!new File(realPath + "installPlugin").exists()) {
				new File(realPath + "installPlugin").mkdirs();
			}
			// 对插件文件进行解压到ROOT目录下
			unZip(new File(realPath + "yunPlugin" + File.separator + fileName),realPath + "installPlugin" + File.separator);
			// 复制插件文件到项目中
			copyPluginFile(realPath, pluginPath, pluginId);
			// 删除下载的云插件
			deleteDirectory(new File(realPath + "yunPlugin" + File.separator), false);
		} catch (Exception e) {
			e.printStackTrace();
			return error("插件的压缩文件不存在");
		}
		/*
		 *  将新添加的class（即与插件相关的组件，带有指定注解的类）文件加入SpringIoc容器中
		 */
		// 扫描与插件相关的组件
		List<Class<?>> classList = getPluginComponent(pluginId);
		Iterator<Class<?>> iterator = classList.iterator();
		String compomentName = null;
		// 是否重新
		boolean restartApplication = false;
		//循环遍历所有的带有Controller的类
		while (iterator.hasNext()) {
			Class<?> compomentClass = iterator.next();
			//得到类名首字母小写的类名称作为在IOC容器中的id
			compomentName = compomentClass.getSimpleName();
			compomentName = compomentName.substring(0, 1).toLowerCase() + compomentName.substring(1, compomentName.length());
			// 将组件添加到IOC容器中
			ComponentUtils.addBean(compomentClass.getName(), compomentName, new HashMap<Object, Object>(), applicationContext);
			// 如果是controller类，进行requestMapping映射。
			if(compomentClass.getAnnotation(Controller.class) != null ) {
				// 注册组件中的Mapping映射
				ComponentUtils.registerMapping4Class(compomentClass, applicationContext, "\\$\\{url.suffix}", ".do");
			}
			// 判断是否hibernate的实体类
			if(compomentClass.getAnnotation(Entity.class) != null) {
				// 判断是否有新加入的实体类
				EntityManager entityManager = applicationContext.getBean(EntityManager.class);
				MetamodelImpl metamodel = (MetamodelImpl) entityManager.getMetamodel();
				// 有新加入实体类的需要重启容器
				try {
					metamodel.entityPersister(compomentClass);
				} catch (MappingException e) {
					// 设置为需要重启
					restartApplication = true;
					//e.printStackTrace();
				}
			}
		}
		/*
		 * 将新添加的class文件加入已安装的插件缓存中容器中，并且在首页功能插件中显示
		 */
		String className = "com.xnx3.wangmarket.plugin." + pluginId + ".Plugin";
		Class<?> forName = Class.forName(className);
//		SitePluginBean sitePluginBean = new SitePluginBean(forName);
		//删除原本的插件
		PluginManage.removePluginCache(pluginId);
		
		//注册安装最新的插件
		try {
			if(!PluginManage.registerPlugin(forName)){
				return error("插件注册失败！");
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
			return error(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return error(e.getMessage());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return error(e.getMessage());
		} catch (SecurityException e) {
			e.printStackTrace();
			return error(e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return error(e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		//添加动作日志
		ActionLogUtil.insert(request, "总管理后台-插件管理，安装插件", "安装ID为" + pluginId + "的插件");
		// 重启容器
		if(restartApplication) {
			return success("restart");
		}
		return success();
	}
	
	/**
	 * 重新加载容器
	 * @author 李鑫
	 */
	@ResponseBody
	@RequestMapping("/restart${url.suffix}")
	public BaseVO restartApplication(HttpServletRequest request) {
		// 判断用户身份
		if(haveSuperAdminAuth() == false) {
			return error("您没有该功能操作权限");
		}
		
		//添加动作日志
		ActionLogUtil.insert(request, "总管理后台-插件管理-重启Tomcat", "因为安装新插件二重启服务器");
		// 检查当前的运行的环境决定重启的方式
		if(getPluginPath(request, "").get("environment").equals("tomcat")) {
			// 创建存放数据信息的Map
			Map<String, String> environmentMap = new HashMap<String, String>();
			// 将当前的运行环境存放map中
			environmentMap.put("runtimeEnvironment", applicationContext.getEnvironment().getProperty("os.name"));
			// 得到当前tomcat的bin文件的路径
			String realPath = request.getServletContext().getRealPath("/");
			environmentMap.put("realPath", realPath);
			// 使用的获取的信息进行tomcat的重启 
			new TomcatUtil(environmentMap).run();
		}else {
			com.Application.restart();
		}
		
		return success();
	}
	
	/**
	 * 将与插件相关的文件复制到对应的文件位置
	 * @author 李鑫
	 * @param realPath 项目的真是路径 例："/mnt/tomcat/webapp/ROOT/"
	 * @param pluginPath 插件的路径Map 详见 getPluginPath() 方法
	 * @param pluginId 操作的插件id
	 * @throws IOException
	 */
	private void copyPluginFile(String realPath, Map<String, String> pluginPath, String pluginId) throws IOException {
		
		String classPath = realPath + "installPlugin" + File.separator + "ROOT" + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "com" + File.separator + "xnx3" + File.separator + "wangmarket" + File.separator + "plugin" +File.separator;
		String jspPath =  realPath + "installPlugin" + File.separator + "ROOT" + File.separator + "WEB-INF" + File.separator + "view" + File.separator + "plugin" + File.separator;
		String staticPath =  realPath + "installPlugin" + File.separator + "ROOT" + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "static" + File.separator + "plugin" + File.separator;
		// 复制class文件
		copyDir(classPath, pluginPath.get("classPath"));
		// 复制jsp文件
		copyDir(jspPath, pluginPath.get("jspPath"));
		
		if(pluginPath.get("metaJspPath") != null) {
			copyDir(jspPath, pluginPath.get("metaJspPath"));
		}
		// 如果有静态文件的话进行复制
		if(new File(staticPath).exists()) {
			// 复制静态文件
			copyDir(staticPath, pluginPath.get("staticPath"));
		}
		
		/*
		 * 删除用于安装插件的临时文件
		 */
		deleteDirectory(new File(realPath + "installPlugin" + File.separator), false);
		/*
		 * eclipse下特有的文件夹
		 */
//		if(new File(installedPluginMap.get("rootPath") + "META-INF" + File.separator + "resources" + File.separator + "installPlugin" + File.separator).exists()) {
//			deleteDirectory(new File(installedPluginMap.get("rootPath") + "META-INF" + File.separator + "resources" + File.separator + "installPlugin" + File.separator), false);
//		}
		
	}
	
	
	/**
	 * 扫描与插件有关的IOC组件
	 * @author 李鑫
	 * @param pluginId 插件的id
	 * @return 与插件有关的IOC组件的Class列表
	 * @throws IOException
	 */
	private List<Class<?>> getPluginComponent(String pluginId) throws IOException {
		//需要扫面的包路径
		String rootPackage = "com.xnx3.wangmarket.plugin";
		//得到包的名字，转换为路径
		String packageDirName = rootPackage.replace('.', '/');
		// 获取包的Url
		Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
		// 用于保存与插件有关的文件夹的集合
		List<File> fileList =  null;
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			//过滤得到与该插件有关的文件
			File[] files = new File(url.getPath())
					.listFiles(new PluginFileFilter(pluginId));
			// 将符合要求的文件夹转换为list
			fileList = Arrays.asList(files);
		}
		// 获取文件夹下的所有class类
		List<Class<?>> pluginClassList = ScanClassesUtil.getPluginClassList(fileList,rootPackage);
		// 设置要扫描的注解
		String[] annotations = {"Controller", "Component", "Service", "Mapper", "Repository","Entity"};
		// 对class列表中有指定注解的class类
		List<Class<?>> pluClasses = ScanClassesUtil.getClassSearchAnnotationsName(pluginClassList, Arrays.asList(annotations));
		return pluClasses;
	}



	/**
	 * 更新插件压缩文件
	 * @author 李鑫
	 * @param file 需要上传的文件
	 * @param pluginId 上传的插件文件的id
	 * @return {@link com.xnx3.BaseVO} result：1：成功；2：失败、info：失败原因
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/uploadZip${url.suffix}")
	public BaseVO uploadZip(HttpServletRequest request, 
			@RequestParam(value = "plugin_id", required = false, defaultValue = "") String pluginId, 
			@RequestParam(value = "file", required = false) MultipartFile file) 
					throws IllegalStateException, IOException {
		
		// 判断用户身份
		if(haveSuperAdminAuth() == false) {
			return error("您没有该功能操作权限");
		}		
		// 参数安全过滤
		pluginId = SafetyUtil.xssFilter(pluginId);
		// 校验插件id是否合法
		if(pluginId == null || pluginId.trim().equals("")) {
			return error("插件信息错误");
		}
		// 校验上传的文件是否正常
		if(file == null) {
			return error("文件上传异常");
		}
		/*
		 * 本地上传
		 */
		// 在数据中取得插件的信息
		Application application = sqlService.findAloneByProperty(Application.class, "id", pluginId);
		// 得到当前的真是路径
		String realPath = request.getServletContext().getRealPath("/");
		// 创建文件夹
		new File(realPath + File.separator + "myPlugin").mkdirs();
		// 创建上传的文件
		File uploadFile = new File(realPath + "myPlugin" + File.separator + pluginId + ".zip");
		if(!uploadFile.exists()) {
			uploadFile.createNewFile();
		}
		// 将数据写入到上传的目标文件
		file.transferTo(uploadFile);
		
		//更新插件信息
		sqlService.save(application);
		//添加动作日志
		ActionLogUtil.insert(request, "总管理后台-功能插件-上传插件", "上传ID为" + pluginId + "的插件压缩包");
		return success();
	}
	
	/**
	 * 通过上传压缩包上传插件
	 * @author 李鑫
	 * @param file 上传的插件的压缩包
	 * @return {@link com.xnx3.BaseVO} result: 1： 成功；0：失败；
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	@ResponseBody
	@RequestMapping("addByZip${url.suffix}")
	public BaseVO uploadPluginZip(MultipartFile file, HttpServletRequest request) throws IOException {
		
		// 判断用户身份
		if(haveSuperAdminAuth() == false) {
			return error("您没有该功能操作权限");
		}		
		// 得到当前的项目真实路径
		String realPath = request.getServletContext().getRealPath("/");
		File shortTimeFile = new File(realPath + "shortTimeFile.zip");
		if(!shortTimeFile.exists()) {
			shortTimeFile.createNewFile();
		}
		/*
		 *  将数据写入到上传的目标文件,并读取配置文件
		 */
		// 上传到临时文件
		file.transferTo(shortTimeFile);
		ZipFile zipFile = new ZipFile(shortTimeFile, Charset.forName("UTF-8"));
		// 得到配置文件的输入流
		ZipEntry entry = zipFile.getEntry("ROOT/system.txt");
		if(entry == null) {
			return error("压缩包内缺少system.txt配置文件，请检查后重新上传");
		}
		InputStream inputStream = zipFile.getInputStream(entry);
		/*
		 *  读取配置文件获取插件id和名称
		 */
		byte[] buff = new byte[1024];
		StringBuffer sBuffer = new StringBuffer("");
		while (inputStream.read(buff) != -1) {
			sBuffer.append(new String(buff, 0, buff.length));
		}
		inputStream.close();
		zipFile.close();
		// 将文件内的UTF-8编码进行常规字符串
		String content = sBuffer.toString();
		content = StringUtil.utf8ToString(content);
		// 转为json格式获取配置信息
		JSONObject systemObject = JSONObject.fromObject(content);
		String pluginId = systemObject.getString("pluginId");
		String menuTitle = systemObject.getString("menuTitle");
		// 获取id和名称
		if(YunPluginMessageCache.applicationMap.get(pluginId) != null) {
			return error("该插件ID与云插件库id重复，请更换ID后上传");
		}
		/*
		 * 本地上传
		 */
		// 在数据中取得插件的信息
		Application application = sqlService.findAloneByProperty(Application.class, "id", pluginId);
		
		if(application != null) {
			return error("该ID插件已在数据库中存在。");
		}
		
		if(!new File(realPath + File.separator + "myPlugin").exists()) {
			// 创建文件夹
			new File(realPath + File.separator + "myPlugin").mkdirs();
		}
		
		// 创建上传的文件
		File uploadFile = new File(realPath + "myPlugin" + File.separator + pluginId + ".zip");
		if(!uploadFile.exists()) {
			uploadFile.createNewFile();
		}
		// 将数据写入到上传的目标文件
		FileUtil.copyFile(shortTimeFile.getAbsolutePath(), uploadFile.getAbsolutePath());
		
		//删除临时文件
		boolean delete = shortTimeFile.delete();
		if(!delete) {
			shortTimeFile.delete();
		}
		// 设置插件信息
		application = new Application();
		application.setId(pluginId);
		application.setMenuTitle(new String(menuTitle.getBytes(), "utf-8"));
		//更新插件信息
		sqlService.save(application);
		
		//添加动作日志
		ActionLogUtil.insert(request, "总管理后台-插件管理-添加插件", "添加ID为" + pluginId + "的插件");
		return success();
	}
	
	/**
	 * 插件管理首页入口
	 * @author 李鑫
	 */
	@RequestMapping("/index${url.suffix}")
	public String index(HttpServletRequest request ,Model model){
		// 判断用户身份
		if(haveSuperAdminAuth() == false) {
			return error(model, "您没有该功能操作权限");
		}
		ActionLogUtil.insert(request, "进入总管理后台-功能插件 首页");
		return "/superadmin/pluginManage/index";
	}
	
	/**
	 * 用户自己二次开发的插件
	 * @author 李鑫
	 */
	@RequestMapping("/myList${url.suffix}")
	public String myList(HttpServletRequest request ,Model model){
		// 判断用户身份
		if(haveSuperAdminAuth() == false) {
			return error(model, "您没有该功能操作权限");
		}
				
//		Sql sql = new Sql(request);
//		//搜索的列
//		String[] searchColumnArray = new String[]{"menu_title"};
//		sql.setSearchColumn(searchColumnArray);
//		
//		int count = sqlService.count("application", sql.getWhere());
//		Page page = new Page(count, 1000, request);
//		sql.setSelectFromAndPage("SELECT * FROM application", page);
//		List<Application> list = sqlService.findBySql(sql, Application.class);
		
		//获取当前所安装的插件列表
		Map<String,PluginRegister> installedPluginMap = PluginManage.getAllInstallPlugin();
		
		//自己本地的插件，自己开发的，或者自己本地方式安装的插件，这里不包含云端插件。
		Map<String,PluginRegisterBean> localPluginMap = new HashMap<String, PluginRegisterBean>();
		
		//云端的插件列表
		Map<String, Application> yunPluginMap = YunPluginMessageCache.applicationMap;
		
		//遍历本地已安装的插件
		for(Map.Entry<String, PluginRegister> entry : installedPluginMap.entrySet()){
			if(yunPluginMap.get(entry.getKey()) == null){
				//将本地有的，云端没有的，记录下来，这些就是自己可以导出的插件
				localPluginMap.put(entry.getKey(), new PluginRegisterBean(entry.getValue()));
			}
		}
		
		//传递到view界面的
		List<PluginRegisterBean> localList = new ArrayList<PluginRegisterBean>();
		for(Map.Entry<String, PluginRegisterBean> entry : localPluginMap.entrySet()){
			localList.add(entry.getValue());
		}
		
		//添加阿里云日志服务
		ActionLogUtil.insert(request, "总管理后台-插件管理，查看自己二次开发的插件", "查看插件列表");
		model.addAttribute("list", localList);
		return "/superadmin/pluginManage/myList/list";
	}
	
	
	/**
	 * 当前网市场已经安装的插件列表
	 * @author 李鑫
	 */
	@RequestMapping("/installList${url.suffix}")
	public String installList(HttpServletRequest request, Model model, 
			@RequestParam(value = "menu_title", required = false, defaultValue = "") String menuTitle){
		// 判断用户身份
		if(haveSuperAdminAuth() == false) {
			return error(model, "您没有该功能操作权限");
		}
		
		//传到jsp页面的list
		List<InstallApplicationVO> pluginList = new ArrayList<InstallApplicationVO>();
		// 循环遍历安装的map
		Map<String, PluginRegister> installPluginMap = PluginManage.getAllInstallPlugin();
		for (Map.Entry<String, PluginRegister> entry : installPluginMap.entrySet()) {
			// 根据插件名称搜索插件
			if(entry.getValue() != null && entry.getValue().menuTitle() != null && entry.getValue().menuTitle().indexOf(menuTitle) != -1) {
				Application app = YunPluginMessageCache.applicationMap.get(entry.getKey());
				InstallApplicationVO vo = new InstallApplicationVO();
				
				//判断一下这个插件是否有最新版本，也就是判断一下云端跟本地的是否有差别，云端版本是否高了
				if(app != null && app.getVersion() > VersionUtil.strToInt(entry.getValue().version())){
					vo.setFindNewVersion(true);
				}else{
					vo.setFindNewVersion(false);
				}
				vo.setPluginRegisterBean(new PluginRegisterBean(entry.getValue()));
				
				pluginList.add(vo);
			}
		}
		
		ActionLogUtil.insert(request, "总管理后台-插件管理，查看当前网市场已经安装的插件列表");
		model.addAttribute("pluginList", pluginList);
		return "/superadmin/pluginManage/installList/list";
	}
	
	/**
	 * 查看网市场云端插件列表
	 * @author 李鑫
	 * @param menuTitle 用户查询插件的筛选条件
	 */
	@RequestMapping("/yunList${url.suffix}")
	public String yunList(HttpServletRequest request ,Model model,
			@RequestParam(value = "menu_title", required = false, defaultValue = "") String menuTitle){
		// 判断用户身份
		if(haveSuperAdminAuth() == false) {
			return error(model, "您没有该功能操作权限");
		}
		
		List<Application> list = new LinkedList<Application>();
		// 将云端插件保存
		list.addAll(YunPluginMessageCache.applicationList);
		/*
		 * 筛选符合条件的id，不符合条件的插件去除掉
		 */
		// 如果搜索条件不为空
		if(menuTitle != null && !menuTitle.trim().equals("")) {
			// 循环遍历当前列表
			Iterator<Application> iterator = YunPluginMessageCache.applicationList.iterator();
			while (iterator.hasNext()) {
				Application application = (Application) iterator.next();
				// 如果不满足当前查询条件，移除该插件信息
				if(!application.getMenuTitle().contains(menuTitle)) {
					list.remove(application);
				}
			}
		}
		/*
		 * 判断当前使用的数据库是否满足插件要求
		 */
		String dataBaseType = applicationContext.getEnvironment().getProperty("spring.datasource.url");
		// 保存当前使用的数据库类型不支持的插件Id
		List<String> noSupportPluginIdList = new ArrayList<String>();
		// 如果是MySql环境，则把不支持MySQL的插件进行保存
		if(dataBaseType.indexOf("mysql") > -1) {
			Iterator<Application> iterator = YunPluginMessageCache.applicationList.iterator();
			while (iterator.hasNext()) {
				Application application = (Application) iterator.next();
				if(application.getSupportMysql() == 0) {
					noSupportPluginIdList.add(application.getId());
				}
			}
		}
		// 如果是Sqlite环境，则把不支持Sqlite的插件进行保存
		if(dataBaseType.indexOf("sqlite") > -1) {
			Iterator<Application> iterator = YunPluginMessageCache.applicationList.iterator();
			while (iterator.hasNext()) {
				Application application = (Application) iterator.next();
				if(application.getSupportSqlite() == 0) {
					noSupportPluginIdList.add(application.getId());
				}
			}
		}
		
		ActionLogUtil.insert(request, "总管理后台-插件管理，查看网市场云端插件列表");
		
		// 将已经安装的插件id放入缓存中
		model.addAttribute("list", list);
		model.addAttribute("page", YunPluginMessageCache.page);
		// 将授权信息转到前端
		model.addAttribute("isUnAuth", Authorization.copyright);
		// 初始化安装的插件信息
//		if(installedPluginMap == null) {
//			installedPluginMap = pluginService.getCurrentPluginMap();
//		}
		model.addAttribute("pluginIds", PluginManage.getAllInstallPlugin().toString());
		// 将当前环境不支持的插件Id转到前端页面啊
		model.addAttribute("unSupportPluginId", noSupportPluginIdList);
		return "/superadmin/pluginManage/yunList/list";
	}
	
	/**
	 * 查询云插件插件详情
	 * @author 李鑫
	 * @param pluginId 查询的插件id
	 */
	@RequestMapping("queryYunPluginById${url.suffix}")
	public String queryYunPluginById(Model model, HttpServletRequest request,
			@RequestParam(value = "plugin_id", required = false, defaultValue = "") String pluginId) {
		// 判断用户身份
		if(haveSuperAdminAuth() == false) {
			return error(model, "您没有该功能操作权限");
		}
		
		ActionLogUtil.insert(request, "总管理后台-插件管理，查询云插件插件详情");
		
		// 参数安全过滤
		pluginId = SafetyUtil.xssFilter(pluginId.trim());
		// 传递插件信息到页面
		model.addAttribute("plugin", YunPluginMessageCache.applicationMap.get(pluginId));
		return "/superadmin/pluginManage/yunList/view";
	}
	
	/**
	 * 删除插件
	 * @author 李鑫
	 * @param pluginId 需要删除插件的id信息
	 * @return {@link com.xnx3.BaseVO} result：1：成功；2：失败、info：失败原因
	 */
	@ResponseBody
	@RequestMapping("/deletePlugin${url.suffix}")
	public BaseVO deletePlugin(HttpServletRequest request, 
			@RequestParam(value = "plugin_id" , required = false, defaultValue = "")
			String pluginId) {
		// 判断用户身份
		if(haveSuperAdminAuth() == false) {
			return error("您没有该功能操作权限");
		}		
		// 参数安全过滤
		pluginId = SafetyUtil.xssFilter(pluginId);		
		//校验插件id是否合法
		if(pluginId == null || pluginId.trim().equals("")) {
			return error("插ID错误");
		}
		//通过id查询出需要删除的插件信息
		Application plugin = sqlService.findAloneByProperty(Application.class, "id", pluginId);
		// 删除插件信息
		sqlService.delete(plugin);
		//添加动作日志
		ActionLogUtil.insert(request, "总管理后台-插件管理，删除插件", "删除ID为" + plugin.getMenuTitle() + "的插件");
		return success();
	}
	
	/**
	 * 判断插件列表中的插件是否已经被当前系统安装
	 * @author 李鑫
	 * @param list 需要被插件的差价列表
	 * @return 进行判断之后的插件列表 修改类属性 installState：1：已安装 0：未安装
	 */
	private List<Application> setPluginInstallState(List<Application> list) {
		//如果已安装插件缓存为空 ，进行初始化处理
//		if(installedPluginMap == null ) {
//			installedPluginMap = pluginService.getCurrentPluginMap();
//		}
		//已安装插件 
		Map<String,PluginRegister> installedPluginMap = PluginManage.getAllInstallPlugin();
		
		Application application = null;
		//循环遍历需要检验的插件列表
		Iterator<Application> iterator = list.iterator();
		while (iterator.hasNext()) {
			application = iterator.next();
			//如果当前校验的插件在安装插件缓存中存在设置为安装状态。否则反之
			if(installedPluginMap.get(application.getId()) != null) {
				application.setInstallState((short) 1); 
			}else {
				application.setInstallState((short) 0); 
			}
		}
		return list;
	}
	
	/**
	 * 跳转上传插件压缩包页面
	 * @author 李鑫
	 * @param pluginId 上传文件的插件id
	 */
	@RequestMapping("/upload${url.suffix}")
	public String uploadZipFile(Model model, HttpServletRequest request,
			@RequestParam(value = "plugin_id", required = false, defaultValue = "") String pluginId) {
		// 判断用户身份
		if(haveSuperAdminAuth() == false) {
			return error(model, "您没有该功能操作权限");
		}
		
		ActionLogUtil.insert(request, "总管理后台-插件管理，跳转上传插件压缩包页面");
		// 参数安全过滤
		pluginId = SafetyUtil.xssFilter(pluginId.trim());
		model.addAttribute("plugin_id", pluginId);
		return "/superadmin/pluginManage/myList/upload";
	}
	
	/**
	 * 导出插件信息
	 * @author 李鑫
	 * @param pluginId 需要导出的插件id
	 * @return {@link com.xnx3.BaseVO} result: 1：成功，info：上床文件下载的url地址；0：失败，info：失败原因
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@ResponseBody
	@RequestMapping("/exportPlugin${url.suffix}")
	public BaseVO exportPlugin(HttpServletRequest request, 
			@RequestParam(value = "plugin_id", required = false, defaultValue = "") String pluginId) throws IOException, ClassNotFoundException {
		
		// 判断用户身份
		if(haveSuperAdminAuth() == false) {
			return error("您没有该功能操作权限");
		}		
		// 参数安全过滤
		if(pluginId == null || pluginId.trim().equals("")) {
			return error("插件id格式错误");
		}
		/*
		 * 判断要导出的插件是否为用户自己开发的本地插件
		 */
		Application application = YunPluginMessageCache.applicationMap.get(pluginId);
		if(application != null) {
			return error("该插件不是您的本地插件，不支持导出。");
		}
		
		//获取操作的路径
		Map<String, String> pathMap = getPluginPath(request, pluginId);
		String classPath = pathMap.get("classPath") + pluginId + File.separator;
		String jspPath = pathMap.get("jspPath") + pluginId + File.separator;
		String staticPath = pathMap.get("staticPath") + pluginId + File.separator;
		//class文件夹
		File classFile = new File(classPath);
		/* 管雷鸣增加 domain 项目的路径，如 kefu_domain */
		String classPath_domain = pathMap.get("classPath") + pluginId+"_domain" + File.separator;
		String jspPath_domain = pathMap.get("jspPath") + pluginId+"_domain" + File.separator;
		
		//没有文件的话返回错误结果
		if(!classFile.exists()) {
			return error("您未使用该插件或者插件信息错误");
		}

		//导出的目录，相对路径。生成的格式如： plugin/pluginManage/export/kefu_1576068279/
		String currentPath = new Global().getClass().getResource("/").getPath();
		String exportRelativePath = "";
		if(currentPath.indexOf("/target/classes/") > -1){
			//开发模式中，在eclipse中运行的
			exportRelativePath = "static"+ File.separator +"plugin"+File.separator+"exportPlugin"+File.separator;
		}else{
			exportRelativePath = "plugin"+File.separator+"exportPlugin"+File.separator;
		}
		String exportAbsolutePath = SystemUtil.getCurrentDir() +File.separator+ exportRelativePath;	//组合绝对路径
		//初始化创建文件夹
		LocalServerMode.directoryInit(exportRelativePath+DateUtil.timeForUnix10()+File.separator);
		
		String newClassPath = exportAbsolutePath + "ROOT" + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "com" + File.separator + "xnx3" + File.separator + "wangmarket" + File.separator + "plugin" + File.separator + pluginId;
		String newStaticPath = exportAbsolutePath + "ROOT" + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "static" + File.separator + "plugin" + File.separator + pluginId;
		String newJspPath = exportAbsolutePath + "ROOT" + File.separator + "WEB-INF" + File.separator + "view" + File.separator + "plugin" + File.separator + pluginId;
		/*管雷鸣增加 domain 项目的路径*/
		String newClassPath_domain = exportAbsolutePath + "ROOT" + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "com" + File.separator + "xnx3" + File.separator + "wangmarket" + File.separator + "plugin" + File.separator + pluginId+"_domain";
		String newJspPath_domain = exportAbsolutePath + "ROOT" + File.separator + "WEB-INF" + File.separator + "view" + File.separator + "plugin" + File.separator + pluginId+"_domain";
		
		//进行class文件的复制
		if(new File(classPath).exists()){
			copyDir(classPath, newClassPath);
		}
		//进行jsp文件的复制
		if(new File(jspPath).exists()){
			copyDir(jspPath, newJspPath);
		}
		//如果插件有静态文件的话进行复制
		if(new File(staticPath).exists()) {
			copyDir(staticPath, newStaticPath);
		}
		/**管雷鸣增加， domain 项目的复制**/
		if(new File(classPath_domain).exists()){
			copyDir(classPath_domain, newClassPath_domain);
		}
		if(new File(jspPath_domain).exists()){
			copyDir(jspPath_domain, newJspPath_domain);
		}
		
		/*
		 * 添加插件信息配置文件
		 */
		// 封装插件配置信息
		String className = "com.xnx3.wangmarket.plugin." + pluginId + ".Plugin";
		PluginRegister pluginRegister = PluginManage.getAllInstallPlugin().get(pluginId);
		if(pluginRegister == null){
			return error("未发现这个类: "+className);
		}
		
		// 获取文件
		File systemFile = new File(exportAbsolutePath + "ROOT" + File.separator + "system.txt");
		if(!systemFile.exists()) {
			systemFile.createNewFile();
		}
		FileOutputStream outputStream = new FileOutputStream(systemFile);
		// 将插件信息封装为json配置文件
		JSONObject json = new JSONObject();
		json.put("pluginId", pluginId);
		json.put("menuTitle", pluginRegister.menuTitle());
		String content = json.toString();
		// 将配置信息转为UTF8编码进行保存 防止中文。读取是在进行转回
		content = StringUtil.StringToUtf8(content);
		byte[] bytes = content.getBytes();
		outputStream.write(bytes, 0, bytes.length);
		outputStream.close();
		//对文件进行压缩,该文件每次进入首页时进行删除
		final String exportZipRelativePath = exportRelativePath + pluginId + ".zip";	//生成的zip文件相对路径
		ZipUtils.dozip(exportAbsolutePath + "ROOT", SystemUtil.getProjectPath() + exportZipRelativePath);
		// 删除临时创建的文件
		deleteDirectory(new File(exportAbsolutePath), false);
		//添加动作日志
		ActionLogUtil.insert(request, "导出插件", "导出ID为" + pluginId + "的插件");
		// 开启线程删除导出的文件
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(5 * 60 * 1000);
					// 删除导出临时文件夹
					deleteDirectory(new File(SystemUtil.getProjectPath() + exportZipRelativePath), false);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		ActionLogUtil.insert(request, "总管理后台-插件管理，导出插件");
		
		if(exportRelativePath.indexOf("static/") == 0){
			//eclipse开发调试状态时，要将 static/去掉
			exportRelativePath = exportRelativePath.replace("static/plugin", "plugin");
		}
		// 返回需要访问的路径
		return success("/"+exportRelativePath + pluginId + ".zip");
	}
	
	
	/**
	 * 将源文件夹下的文件复制到目标文件夹下，不包括当前源文件夹
	 * @author 李鑫
	 * @param oldPath 需要复制的文件路径 列如："/User/a/"
	 * @param newPath 复制的目标文件路径 列如："/User/b/"
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	private void copyDir(String oldPath, String newPath) throws IOException {
		// 如果新文件夹不存在，创建新的文件夹
		File newFile = new File(newPath);
		if(!newFile.exists()) {
//			newFile.mkdirs();
			System.out.println(newFile.mkdirs()+"  "+newPath);
		}
		//得到需要复制的文件
		File file = new File(oldPath);
		//文件名称列表
		String[] filePath = file.list();
		//如果新文件不存在的创建文件夹
		if (!(new File(newPath)).exists()) {
			(new File(newPath)).mkdirs();
		}
		//循环遍历文件夹下的文件列表
		for (int i = 0; i < filePath.length; i++) {
			//如果是文件夹
			if ((new File(oldPath + file.separator + filePath[i])).isDirectory()) {
				copyDir(oldPath  + file.separator  + filePath[i], newPath  + file.separator + filePath[i]);
			}
			//如果是文件
			if (new File(oldPath  + file.separator + filePath[i]).isFile()) {
				FileUtil.copyFile(oldPath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
			}
		}
	}
	
	/**
	 * 解压文件到指定的文件夹
	 * @author 李鑫
	 * @param srcFile 需要解压的文件
	 * @param destDirPath 解压到的目标 例如："/User/a/"
	 * @throws RuntimeException
	 */
	private void unZip(File srcFile, String destDirPath) throws RuntimeException {
		// 判断源文件是否存在
		if (!srcFile.exists()) {
			throw new RuntimeException(srcFile.getPath() + ",所指文件不存在");
		}
		// 开始解压
		ZipFile zipFile = null;
		try {
			// 对文件进行压缩封装
			zipFile = new ZipFile(srcFile);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				// 如果是文件夹，就创建个文件夹
				if (entry.isDirectory()) {
					String dirPath = destDirPath + File.separator + entry.getName();
					File dir = new File(dirPath);
					dir.mkdirs();
				}else {
					// 如果是文件，就先创建一个文件，然后用io流把内容copy过去
					File targetFile = new File(destDirPath + File.separator + entry.getName());
					// 保证这个文件的父文件夹必须要存在
					if(!targetFile.getParentFile().exists()){
						targetFile.getParentFile().mkdirs();
					}
					try {
						targetFile.createNewFile();
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 将压缩文件内容写入到这个文件中
					InputStream is = zipFile.getInputStream(entry);
					FileOutputStream fos = new FileOutputStream(targetFile);
					int len;
					byte[] buf = new byte[1024];
					while ((len = is.read(buf)) != -1) {
						fos.write(buf, 0, len);
					}
					// 关流顺序，先打开的后关闭
					fos.close();
					is.close();
				}
			}
		} catch (Exception e) {
		    throw new RuntimeException("unzip error from ZipUtils", e);
	    } finally {
	    	if(zipFile != null){
	    		try {
	    			zipFile.close();
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    	}
	    }
	}
	
	/**
	 * 获取对插件操作使用的路径
	 * @author 李鑫
	 * @param pluginId 进行操作的插件id
	 * @return 路径名称 
	 * 		<ul>
	 * 			<li> key:rootPath value:当前项目的根目录 </li>
	 * 			<li> key:classPath value:当前插件class文件所在的文件夹路径 
	 * 				例：/mnt/tomcat8/webapp/ROOT/WEB-INF/classes/com/xnx3/wangmarket/plugin/ 
	 * 			</li>
	 * 			<li> key:jspPath value:当前插件jsp文件所在的文件夹路径  
	 * 				例：/mnt/tomcat8/webapp/ROOT/WEB-INF/view/plugin/ 
	 * 			</li>
	 * 			<li> key:staticPath value:当前插件static静态文件所在的文件夹路径 
	 * 				例：/mnt/tomcat8/webapp/ROOT/static/plugin/ 
	 * 			</li>
	 * 			<li> key:metaJspPath value:tomcat环境下META-INF下的jsp文件路径
	 * 				例：/mnt/tomcat8/webapp/ROOT/WEB-INF/classes/META-INF/resources/WEB-INF/view/plugin/
	 * 			</li>
	 * 			<li> key:environment value:当前运行的环境
	 * 				例：tomcat，eclipse
	 * 			</li>
	 * 		</ul>
	 */
	private Map<String, String> getPluginPath(HttpServletRequest request, String pluginId) {
		
		//项目的真实路径
		String realPath = request.getServletContext().getRealPath("/");
		Map<String, String> pathMap = new HashMap<String, String>();
		//class文件夹
		String classPath = null;
		//jsp文件夹
		String jspPath = null;
		//js文件夹
		String staticPath = null;
		// 项目根目录
		String rootPath = null;
		
		//tomcat下Jsp第二个目录
		String metaJspPath = null;
		// 当前的运行环境
		String environment = null;
		/*
		 * eclipse环境下路径
		 */
		if(realPath.indexOf("main" + File.separator + "webapp" + File.separator) > -1) {
			rootPath = realPath.substring(0, realPath.length() - 16) + "target" + File.separator + "classes" + File.separator;
			classPath = rootPath + "com" + File.separator + "xnx3" + File.separator + "wangmarket" + File.separator + "plugin" + File.separator;
			jspPath = rootPath + "META-INF" + File.separator + "resources" + File.separator + "WEB-INF" + File.separator + "view" + File.separator + "plugin" + File.separator;
			staticPath = rootPath + "static" + File.separator + "plugin" + File.separator;
			environment = "eclipse";
		}else {
			/*
			 * toomcat war包路径
			 */
			rootPath = realPath;
			classPath = realPath + "WEB-INF" + File.separator + "classes" + File.separator + "com" + File.separator + "xnx3" + File.separator + "wangmarket" + File.separator + "plugin" + File.separator;
			jspPath = realPath + "WEB-INF" + File.separator + "view" + File.separator + "plugin" + File.separator;
			metaJspPath = realPath + "WEB-INF" + File.separator + "classes" + File.separator + "META-INF" + File.separator + "resources" + File.separator + "WEB-INF" + File.separator + "view" + File.separator;
			staticPath = realPath + "static" + File.separator + "plugin" + File.separator;
			environment = "tomcat";
		}

		pathMap.put("rootPath", rootPath);
		pathMap.put("classPath", classPath);
		pathMap.put("jspPath", jspPath);
		pathMap.put("metaJspPath", metaJspPath);
		pathMap.put("staticPath", staticPath);
		pathMap.put("environment", environment);
		return pathMap;
	}
}
