package com.xnx3.wangmarket.plugin.pluginManage.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
import org.springframework.beans.BeanUtils;
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
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.IpUtil;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.UploadFileVO;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.wangmarket.admin.pluginManage.PluginManage;
import com.xnx3.wangmarket.admin.pluginManage.SitePluginBean;
import com.xnx3.wangmarket.admin.service.PluginService;
import com.xnx3.wangmarket.plugin.base.controller.BasePluginController;
import com.xnx3.wangmarket.plugin.pluginManage.entity.Application;
import com.xnx3.wangmarket.plugin.pluginManage.util.ComponentUtils;
import com.xnx3.wangmarket.plugin.pluginManage.util.ScanClassesUtil;
import com.xnx3.wangmarket.plugin.pluginManage.util.TomcatUtil;
import com.xnx3.wangmarket.plugin.pluginManage.util.ZipUtils;

import net.sf.json.JSONObject;

/**
 * 插件管理中心
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/plugin/pluginManage/")
public class PluginManageController extends BasePluginController {
	
	@Resource
	private PluginService pluginService;
	
	@Resource
	SqlService sqlService;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	//当前已经安装的插件
	private Map<String, SitePluginBean> pluginMap;
	
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
	public BaseVO upgradePlugin(@RequestParam(value = "plugin_id", required = false, defaultValue = "") 
			String pluginId, @RequestParam(value = "version", required = false, defaultValue = "") 
			String version,HttpServletRequest request) throws ClassNotFoundException, IOException {
		// 校验参数
		if(pluginId == null || pluginId.equals("")) {
			return error("插件ID错误");
		}
		if(version == null || version.equals("")) {
			return error("插件升级版本错误");
		}
		// 获取插件最新版本的下载地址
		HttpUtil httpUtil = new HttpUtil();
		/*
		 * 获取升级插件的最新版本号和插件文件的下载url
		 */
		HttpResponse httpResponse = 
				httpUtil.get("http://39.107.137.250/application/getPluginDownUrl.do?plugin_id=" + pluginId);
		String content = httpResponse.getContent();
		// 对返回结果进行Json处理
		JSONObject messageJson = JSONObject.fromObject(content);
		if(messageJson.getString("result").equals("0")) {
			return error(messageJson.getString("info"));
		}
		// 比较两个插件版本是否相同，不相同即可升级。 因为不存在安装版本比最新版本高的情况
		String newVersion = messageJson.getString("version");
		newVersion = newVersion.replaceAll("0", "");
		if((newVersion.equals(version.replaceAll(".", "")))) {
			return error("您目前安装已是最新版本，无需更新");
		}
		// 卸载插件
		BaseVO unIstallBaseVO = unIstallPlugin(pluginId, request);
		if(unIstallBaseVO.getResult() == 0) {
			return error("插件升级失败");
		}
		String downUrl = messageJson.getString("url");
		// 安装最新版本的插件
		BaseVO istallBaseVO = installPlugin(pluginId, downUrl, request);
		if(istallBaseVO.getResult() == 0) {
			return error("插件升级失败");
		}
		/*
		 * 在新升级的class上获取插件版本注解上的最新消息更新到缓存中
		 */
		String className = "com.xnx3.wangmarket.plugin." + pluginId + ".Plugin";
		Class<?> forName = Class.forName(className);
		SitePluginBean sitePluginBean = new SitePluginBean(forName);
		pluginMap.put(pluginId, sitePluginBean);
		
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
	public BaseVO unIstallPlugin(@RequestParam(value = "plugin_id", required = false, defaultValue = "")
			String pluginId, HttpServletRequest request) throws ClassNotFoundException, IOException {
		
		//校验插件id
		if(pluginId == null || pluginId.equals("")) {
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
			//得到类名首字母小写的类名称，删除相应的ioc组件
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
		/**
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
		String className = "com.xnx3.wangmarket.plugin." + pluginId + ".Plugin";
		Class<?> forName = Class.forName(className);
		SitePluginBean sitePluginBean = new SitePluginBean(forName);
		setPagePluginMenu(pluginId, sitePluginBean, 0);
		// 在缓存插件中移除
		pluginMap.remove(pluginId);
		return success();
	}
	
	/**
	 * 检索多个文件夹中与插件相关的文件夹
	 * @author 李鑫
	 * @param pluginId 检索的插件id
	 * @param filePathList 徐亚检索的文件目录列表
	 * @return 检索的与插件相关的文件夹
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
		// 删除子文件夹
		File[] childFile = file.listFiles();
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
	 * 安装插件
	 * @author 李鑫
	 * @param pluginName 安装插件的id以及版本号 例如：kefu-10
	 * @param downUrl 下载插件压缩包的的Url
	 * @return {@link com.xnx3.BaseVO } result：1：成功,info:restart:安装成功，但是重新启动;0：失败，info：失败信息
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@ResponseBody
	@RequestMapping("/installPlugin${url.suffix}")
	public BaseVO installPlugin(@RequestParam(value = "plugin_id", required = false, defaultValue = "")
			String pluginId, @RequestParam(value = "down_url", required = false, defaultValue = "") 
			String downUrl, HttpServletRequest request) throws IOException, ClassNotFoundException {
			
		// 校验信息
		if(pluginId == null || pluginId.equals("")) {
			return error("插件ID错误");
		}
		if(downUrl == null || downUrl.equals("")) {
			return error("请检查插件压缩文件是否上传");
		}
		/*
		 * 判断插件是否已经安装
		 */
		if(!(pluginMap.get(pluginId) == null)) {
			return error("该插件您已安装或者与本地插件ID发生冲突。");
		}
		
		// 插件的压缩文件名称
		String fileName = pluginId + ".zip";
		//获取当前项目的真实路径
		String realPath = request.getServletContext().getRealPath("/");
		Map<String, String> pluginPath = getPluginPath(request, pluginId);
		
		/*
		 * 如果是本地插件直接进行解压安装
		 */
		if(downUrl.indexOf("127.0.0.1") > -1) {
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
		}
		/*
		 * 云端插件安装
		 */
		if(downUrl.indexOf("127.0.0.1") == -1) {
			/*
			 * 创建临时文件
			 */
			if(!new File(realPath + "yunPlugin" + File.separator).exists()) {
				new File(realPath + "yunPlugin" + File.separator).mkdirs();
			}
			if(!new File(realPath + "yunPlugin" + File.separator + fileName).exists()) {
				new File(realPath + "yunPlugin" + File.separator + fileName).createNewFile();
			}
			//云端插件进行下载，并且解压
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
		}
		/*
		 *  将新添加的class（即与插件相关的组件）文件加入SpringIoc容器中
		 */
		// 扫面与插件相关的组件
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
		SitePluginBean sitePluginBean = new SitePluginBean(forName);
		pluginMap.put(pluginId, sitePluginBean);
		// 添加功能插件菜单
		setPagePluginMenu(pluginId, sitePluginBean, 1);
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
	public String restartApplication(HttpServletRequest request) {
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
		return "";
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
		if(new File(pluginMap.get("rootPath") + "META-INF" + File.separator + "resources" + File.separator + "installPlugin" + File.separator).exists()) {
			deleteDirectory(new File(pluginMap.get("rootPath") + "META-INF" + File.separator + "resources" + File.separator + "installPlugin" + File.separator), false);
		}
		
	}
	
		
	/**
	 * 设置首页功能插件的列表信息
	 * @author 李鑫
	 * @param pluginId 操作插件的id
	 * @param sitePluginBean 插件的信息
	 * @param action 进行的操作 0：删除操作 1：添加操作
	 * @return 该返回参数无意义，热河情况下都为true。
	 */
	private boolean setPagePluginMenu(String pluginId, SitePluginBean sitePluginBean, int action) {
		// 添加插件菜单操作
		if(action == 1){
			if(sitePluginBean.isApplyToCMS()) {
				PluginManage.cmsSiteClassManage.put(pluginId, sitePluginBean);
			}
			if(sitePluginBean.isApplyToAgency()) {
				PluginManage.agencyClassManage.put(pluginId, sitePluginBean);
			}
			if(sitePluginBean.isApplyToSuperAdmin()) {
				PluginManage.superAdminClassManage.put(pluginId, sitePluginBean);
			}
			return true;
		}
		// 删除插件菜单操作
		if(sitePluginBean.isApplyToCMS()) {
			PluginManage.cmsSiteClassManage.remove(pluginId);
		}
		if(sitePluginBean.isApplyToAgency()) {
			PluginManage.agencyClassManage.remove(pluginId);
		}
		if(sitePluginBean.isApplyToSuperAdmin()) {
			PluginManage.superAdminClassManage.remove(pluginId);
		}
		return true;
	}
		
	/**
	 * 扫描与插件有关的IOC组件
	 * @author 李鑫
	 * @param pluginId插件的id
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
	 * 上传插件压缩文件
	 * @author 李鑫
	 * @param file 需要上传的文件
	 * @param pluginId 上传的插件问价的id
	 * @return {@link com.xnx3.BaseVO} result：1：成功；2：失败、info：失败原因
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/uploadZip${url.suffix}")
	public BaseVO uploadZip(@RequestParam(value = "file", required = false)
			MultipartFile file, HttpServletRequest request, @RequestParam(value = "plugin_id", required = false, defaultValue = "") String pluginId) 
			throws IllegalStateException, IOException {
		// 校验插件id是否合法
		if(pluginId == null || pluginId.equals("")) {
			return error("插件信息错误");
		}
		// 校验上传的文件是否正常
		if(file == null) {
			return error("文件上传异常");
		}
		/*
		 * 本地上传
		 */
		if(Global.get("ATTACHMENT_FILE_MODE").equals("localFile")) {
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
			// 设置插件的下载地址
			application.setDownUrl("http://" + IpUtil.getIpAddress(request) + ":" + request.getLocalPort() + File.separator + "myPlugin" + File.separator + pluginId + ".zip");
			//更新插件信息
			sqlService.save(application);
		}else {
			/*
			 *  上传插件到oss
			 */
			UploadFileVO uploadFileVO = AttachmentFile.uploadFileByMultipartFile("plugin" + File.separator + pluginId + File.separator, file);
			if(uploadFileVO.getResult() == 0) {
				return error("上传失败，请联系管理员");
			}
			Application application = sqlService.findAloneByProperty(Application.class, "id", pluginId);
			// 设置插件的下载地址
			application.setDownUrl("http:" + uploadFileVO.getUrl());
			//更新插件信息
			sqlService.save(application);
		}
		return success();
	}
	
	/**
	 * 入口页面
	 */
	@RequestMapping("/index${url.suffix}")
	public String index(HttpServletRequest request ,Model model){
		// 将之前导出插件临时的文件夹进行删除
		if(new File(request.getServletContext().getRealPath("/") + "pluginZip" + File.separator).exists()) {
			deleteDirectory(new File(request.getServletContext().getRealPath("/") + "pluginZip" + File.separator), false);
		}
		return "/plugin/pluginManage/index";
	}
	
	/**
	 * 用户自己二次开发的插件
	 * @author 李鑫
	 */
	@RequestMapping("/myList${url.suffix}")
	public String myList(HttpServletRequest request ,Model model){
		Sql sql = new Sql(request);
		//搜索的列
		String[] searchColumnArray = new String[]{
			"menu_title",
			"apply_to_cms=",
			"apply_to_pc=",
			"apply_to_wap=",
			"apply_to_agency=",
			"apply_to_superadmin=",
			"support_oss_storage=",
			"support_sqlite=",
			"support_local_storage=",
			"support_mysql=",
			"support_sls=",
			"wangmarket_version_min<"
		};
		sql.setSearchColumn(searchColumnArray);
		
		int count = sqlService.count("application", sql.getWhere());
		Page page = new Page(count, 20, request);
		sql.setSelectFromAndPage("SELECT * FROM application", page);
		List<Application> list = sqlService.findBySql(sql, Application.class);
		
		//检验插件是否已经被安装
		list = setPluginInstallState(list);
		
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		
		//添加阿里云日志服务
		ActionLogCache.insert(request, "查看插件列表", "查看插件列表");
		return "/plugin/pluginManage/myList/list";
	}
	
	/**
	 * 当前网市场已经安装的插件列表
	 * @author 李鑫
	 */
	@RequestMapping("/installList${url.suffix}")
	public String installList(HttpServletRequest request, Model model, 
			@RequestParam(value = "menu_title", required = false, defaultValue = "") String menuTitle){
		List<SitePluginBean> pluginList = new ArrayList<SitePluginBean>();
		
		//获取当前已经安装的所有的插件
		if(pluginMap == null) {
			pluginMap = pluginService.getCurrentPluginMap();
		}
		// 循环遍历安装的map
		for (Map.Entry<String, SitePluginBean> entry : pluginMap.entrySet()) {
			// 根据插件名称搜索插件
			if(entry.getValue().getMenuTitle().indexOf(menuTitle) != -1) {
				pluginList.add(entry.getValue());
			}
		}
		// 获取本地插件的id列表
		List<String> idList = new LinkedList<String>();
		// sql语句
		String sql = "SELECT id FROM application;";
		// 执行sql语句 返回执行结果
		List<Map<String, Object>> list = sqlService.findMapBySqlQuery(sql);
		// 遍历结果
		Iterator<Map<String, Object>> iterator = list.iterator();
		while (iterator.hasNext()) {
			Map<String, Object> map = iterator.next();
			// 将遍历结果放于list中
			idList.add((String) map.get("id"));
		}
		
		model.addAttribute("ids", idList.toString());
		model.addAttribute("pluginList", pluginList);
		return "/plugin/pluginManage/installList/list";
	}
	
	/**
	 * 查看网市场插件列表
	 * @author 李鑫
	 */
	@RequestMapping("/yunList${url.suffix}")
	public String yunList(HttpServletRequest request ,Model model){
		// 将已经安装的插件id放入缓存中
		model.addAttribute("pluginIds", pluginMap.toString());
		return "/plugin/pluginManage/yunList/list";
	}
	
	/**
	 * 跳转到添加插件和修改插件的页面
	 * @author 李鑫
	 * @param pluginId 插件id
	 * 		<ul>
	 * 			<li>如果是添加，此处不需要传递参数</li>
	 * 			<li>如果是修改，这里传入要修改的插件的id，如 kefu </li>
	 * 		</ul>
	 */
	@RequestMapping("/add${url.suffix}")
	public String toAddPage(@RequestParam(value = "plugin_id",required = false,defaultValue = "")
			String pluginId,Model model, HttpServletRequest request) {
		//如果id不为空的话进行修改操作，在数据库中取出需要修改的插件信息传递到页面中
		if(pluginId != null && !(pluginId.equals(""))) {
			Application plugin = sqlService.findAloneByProperty(Application.class, "id", pluginId);
			model.addAttribute("plugin", plugin);
		}
		//添加动作日志
		ActionLogCache.insert(request, "跳转页面", "跳转到添加插件页面");
		return "/plugin/pluginManage/myList/add";	
	}
	
	/**
	 * 提交插件信息进行修改或者添加
	 * @author 李鑫
	 * @param application 接收插件信息的实体类信息
	 * @return {@link com.xnx3.BaseVO} result: 1： 成功
	 */
	@ResponseBody
	@RequestMapping("/addSubmit${url.suffix}")
	public BaseVO addPluginSubmit(Application application, HttpServletRequest request) {
		/*
		 * 检查id是否已被网市场云端插件库占用
		 */
		HttpUtil httpUtil = new HttpUtil();
		HttpResponse httpResponse = httpUtil.get("http://39.107.137.250/application/pluginIdList.do");
		String content = httpResponse.getContent();
		if(content.indexOf(application.getId()) > -1) {
			return error("该插件ID已被占用，请更改后重新提交");
		}
		/**
		 *  如果数据库中有id为传入id的值则进行修改操作
		 */
		//在数据库中找到需要修改的插件信息
		Application plugin = sqlService.findAloneByProperty(Application.class, "id", application.getId());
		
		if(plugin == null) {
			//添加操作
			//对添加的信息进行Xss过滤
			application.setMenuTitle(application.getMenuTitle());
			application.setIntro(application.getIntro());
			application.setAuthorName(application.getAuthorName());
			application.setAddtime(DateUtil.timeForUnix10());
			application.setUpdatetime(DateUtil.timeForUnix10());
			//新增插件
			sqlService.save(application);
			//添加动作日志
			ActionLogCache.insert(request, "添加插件", "添加" + application.getMenuTitle() + "插件");
		}else {
			//修改操作
			//对添加的信息进行Xss过滤
			application.setId(application.getId());
			application.setMenuTitle(application.getMenuTitle());
			application.setIntro(application.getIntro());
			application.setAuthorName(application.getAuthorName());
			application.setAddtime(DateUtil.timeForUnix10());
			application.setUpdatetime(DateUtil.timeForUnix10());
			//将用户新修改的信息赋值给持久态的实体类
			BeanUtils.copyProperties(application, plugin);
			//更新插件修改的最后修改时间
			plugin.setUpdatetime(DateUtil.timeForUnix10());
			//更新插件信息
			sqlService.save(plugin);
			//添加动作日志
			ActionLogCache.insert(request, "修改插件", "修改" + plugin.getMenuTitle() + "插件");
		}
		return success();
	}
	
	/**
	 * 通过id查看插件的详细信息
	 * @author 李鑫
	 * @param pluginId 查看详情插件的id
	 */
	@RequestMapping("/queryViewById${url.suffix}")
	public String queryById(HttpServletRequest request,@RequestParam(value = "plugin_id" ,required = false,defaultValue = "")
			String pluginId,Model model) {
		//对查询的插件id进行校验
		if(pluginId == null || pluginId.equals("") ) {
			return error(model, "插件ID错误，请重新尝试");
		}
		
		//在数据库中查询插件信息，传递到前端页面中
		Application plugin = sqlService.findAloneByProperty(Application.class, "id", pluginId);
		model.addAttribute("plugin", plugin);
		//添加动作日志
		ActionLogCache.insert(request,"查看插件详情" ,"通过id查看" + plugin.getMenuTitle() + "插件");
		
		return "/plugin/pluginManage/myList/view";	
	}
	

	/**
	 * 删除插件
	 * @author 李鑫
	 * @param pluginId 需要删除插件的id信息
	 * @return {@link com.xnx3.BaseVO} result：1：成功；2：失败、info：失败原因
	 */
	@ResponseBody
	@RequestMapping("/deletePlugin${url.suffix}")
	public BaseVO deletePlugin(@RequestParam(value = "plugin_id" , required = false, defaultValue = "")
			String pluginId,HttpServletRequest request) {
		//校验插件id是否合法
		if(pluginId == null || pluginId.equals("")) {
			return error("插ID错误");
		}
		//通过id查询出需要删除的插件信息
		Application plugin = sqlService.findAloneByProperty(Application.class, "id", pluginId);
		//将插件的状态设置为删除
		
		//更新-插件状态
		sqlService.delete(plugin);
		
		//添加动作日志
		ActionLogCache.insert(request, "删除插件", "删除ID为" + plugin.getMenuTitle() + "的插件");
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
		if(pluginMap == null ) {
			pluginMap = pluginService.getCurrentPluginMap();
		}
		Application application = null;
		//循环遍历需要检验的插件列表
		Iterator<Application> iterator = list.iterator();
		while (iterator.hasNext()) {
			application = iterator.next();
			//如果当前校验的插件在安装插件缓存中存在设置为安装状态。否则反之
			if(pluginMap.get(application.getId()) != null) {
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
	public String uploadZipFile(@RequestParam(value = "plugin_id", required = false, defaultValue = "")
			String pluginId, Model model) {
		model.addAttribute("plugin_id", pluginId);
		return "/plugin/pluginManage/myList/upload";
	}
	
	/**
	 * 导出插件信息
	 * @author 李鑫
	 * @param pluginId 需要导出的插件id
	 * @return {@link com.xnx3.BaseVO} result: 1：成功，info：上床文件下载的url地址；0：失败，info：失败原因
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/exportPlugin${url.suffix}")
	public BaseVO exportPlugin(@RequestParam(value = "plugin_id", required = false, defaultValue = "")
			String pluginId, HttpServletRequest request) throws IOException {
		
		/*
		 * 判断要导出的插件是否为用户自己开发的本地插件
		 */
		Application application = sqlService.findById(Application.class, pluginId);
		if(application == null) {
			return error("该插件不是您的本地插件，不支持导出。");
		}
		
		//获取操作的路径
		Map<String, String> pathMap = getPluginPath(request, pluginId);
		String classPath = pathMap.get("classPath") + pluginId + File.separator;
		String jspPath = pathMap.get("jspPath") + pluginId + File.separator;
		String staticPath = pathMap.get("staticPath") + pluginId + File.separator;
		//class文件夹
		File classFile = new File(classPath);
		
		//没有文件的话返回错误结果
		if(!classFile.exists()) {
			return error("您未使用该插件或者插件信息错误");
		}
		String realPath = request.getServletContext().getRealPath("/");
		String newClassPath = realPath + "export" + File.separator + "ROOT" + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "com" + File.separator + "xnx3" + File.separator + "wangmarket" + File.separator + "plugin" + File.separator + pluginId;
		String newStaticPath = realPath + "export" + File.separator + "ROOT" + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "static" + File.separator + "plugin" + File.separator + pluginId;
		String newJspPath = realPath + "export" + File.separator + "ROOT" + File.separator + "WEB-INF" + File.separator + "view" + File.separator + "plugin" + File.separator + pluginId;
		
		//进行class文件的复制
		copyDir(classPath, newClassPath);
		//进行jsp文件的复制
		copyDir(jspPath, newJspPath);
		//如果插件有静态文件的话进行复制
		if(new File(staticPath).exists()) {
			copyDir(staticPath, newStaticPath);
		}
		//对文件进行压缩,该文件每次进入首页时进行删除
		ZipUtils.dozip(realPath + "export" + File.separator + "ROOT", realPath + "pluginZip" + File.separator + pluginId + ".zip");
		// 删除临时创建的文件
		deleteDirectory(new File(realPath + "export" + File.separator), false);
		// 返回需要访问的路径
		return success(File.separator + "pluginZip" + File.separator + pluginId + ".zip");
		
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
		if(!new File(newPath).exists()) {
			new File(newPath).mkdirs();
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
	public static void unZip(File srcFile, String destDirPath) throws RuntimeException {
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
                } else {
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
