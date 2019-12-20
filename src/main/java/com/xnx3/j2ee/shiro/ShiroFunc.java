package com.xnx3.j2ee.shiro;

import java.util.ArrayList;
import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import com.xnx3.j2ee.entity.Permission;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.util.SessionUtil;
import com.xnx3.j2ee.bean.ActiveUser;
import com.xnx3.j2ee.bean.PermissionMark;
import com.xnx3.j2ee.bean.PermissionTree;

/**
 * shiro权限相关用到的函数
 * @author 管雷鸣
 */
public class ShiroFunc {
	
	/**
	 * 将数据库查询的Permission的list转换为tree树的形式，分级。
	 * @param myList 要标记的已选择的
	 * @param allList 所有要显示出来的
	 * @return
	 */
	public List<PermissionTree> PermissionToTree(List<Permission> myList,List<Permission> allList){
		List<PermissionMark> listMark = new ArrayList<PermissionMark>();
		
		//整理所有要显示的资源列表，那些标记选中，那些标记不选中
		for (int i = 0; i < allList.size(); i++) {
			Permission ap = allList.get(i);
			PermissionMark pm = new PermissionMark();
			pm.setPermission(ap);
			
			for (int j = 0; j < myList.size(); j++) {
				Permission mp = myList.get(j);
				if(mp.getId() != null && ap.getId() != null && mp.getId() - ap.getId() == 0){
					pm.setSelected(true);
					break;
				}
			}
			listMark.add(pm);
		}
		
		
		//转换为树状数据输出
		List<PermissionTree> permissionTreeList = new ArrayList<PermissionTree>();
		for (int i = 0; i < listMark.size(); i++) {
			PermissionMark pm = listMark.get(i);
			if(pm.getPermission().getParentId() != null && pm.getPermission().getParentId() - 0 == 0){
				PermissionTree permissionTree = new PermissionTree();
				permissionTree.setPermissionMark(pm);
				List<PermissionMark> markTreeList = new ArrayList<PermissionMark>();
				for (int j = 0; j < listMark.size(); j++) {
					PermissionMark permissionMarkSub = listMark.get(j);
					if(permissionMarkSub.getPermission().getParentId() != null && pm.getPermission().getId() != null && permissionMarkSub.getPermission().getParentId() - pm.getPermission().getId() == 0){
						markTreeList.add(permissionMarkSub);
					}
				}
				permissionTree.setList(markTreeList);
				permissionTreeList.add(permissionTree);
			}
		}
		
		return permissionTreeList;
	}
	

	/**
	 * Session中获取用户登录之后的用户相关信息
	 * @return	<li>登陆了，则返回ActiveUser对象
	 * 			<li>未登陆，返回null
	 * @deprecated 请使用 {@link SessionUtil#getActionUser()}
	 */
	public static com.xnx3.j2ee.shiro.ActiveUser getCurrentActiveUser(){
		com.xnx3.j2ee.bean.ActiveUser activeUserBean = SessionUtil.getActiveUser();
		if(activeUserBean == null){
			return null;
		}
		com.xnx3.j2ee.shiro.ActiveUser activeUser = new com.xnx3.j2ee.shiro.ActiveUser();
		activeUser.setAllowUploadForUEditor(SessionUtil.isAllowUploadForUEditor());
		activeUser.setLanguagePackageName(SessionUtil.getLanguagePackageName());
//		activeUser.setObj(obj);
		activeUser.setPermissions(activeUserBean.getPermissions());
		activeUser.setPermissionTreeList(activeUserBean.getPermissionTreeList());
		activeUser.setUeUploadParam1(SessionUtil.getUeUploadParam1());
		activeUser.setUser(getUser());
		return activeUser;
	}
	
	/**
	 * Session中获取当前登录用户的信息
	 * @return 	<li>登陆了，则返回User对象
	 * 			<li>未登陆，返回null
	 */
	public static User getUser(){
		ActiveUser activeUser = SessionUtil.getActiveUser();
		if(activeUser != null){
			return activeUser.getUser();
		}else{
			return null;
		}
	}
	
	/**
	 * Session中获取当前登录用户的id
	 * @return 	<li>登陆了，则返回其id
	 * 			<li>未登陆，返回 0
	 */
	public static int getUserId(){
		User user = getUser();
		if(user == null){
			return 0;
		}else{
			return user.getId();
		}
	}
	
	/**
	 * 设置当前用户是否能使用UEditor编辑器进行图片、文件、视频等上传。若为false，则不能用其进行上传
	 * @param allow 当前用户是否能使用UEditor编辑器进行图片、文件、视频等上传。若为false，则不能用其进行上传
	 * @return 这里全是true
	 * @deprecated 请使用 {@link SessionUtil#setAllowUploadForUEditor(boolean)}
	 */
	public static boolean setUEditorAllowUpload(boolean allow){
		SessionUtil.setAllowUploadForUEditor(allow);
		return true;
	}
	
	/**
	 * 获取当前用户是否能使用UEditor编辑器进行图片、文件、视频等上传。若为false，则不能用其进行上传
	 * <br/>
	 * @param allow 若用户没有登陆，同样返回false
	 * @deprecated 请使用 {@link SessionUtil#isAllowUploadForUEditor()}
	 */
	public static boolean getUEditorAllowUpload(){
		return SessionUtil.isAllowUploadForUEditor();
	}
	

	/**
	 * 从Shrio的Session中获取当前用户的代理相关信息、站点信息、以及当前用户的上级的代理相关信息
	 * @deprecated 使用 {@link SessionUtil#getUserBeanForSession()}
	 */
//	public static UserBean getUserBeanForShiroSession(){
//		ActiveUser au = ShiroFunc.getCurrentActiveUser();
//		if(au == null){
//			return null;
//		}
//		UserBean userBean = (UserBean) au.getObj();
//		if(userBean == null){
//			return null;
//		}else{
//			return userBean;
//		}
//	}
	

	
	/**
	 * 获取插件信息，从session中。这里取到的是一个对象
	 * 可以从session中获取该用户某个插件的缓存信息，避免频繁查数据库。
	 * 前提是已经将该用户的插件的信息缓存进去了
	 * @param pluginId 插件id，如 kefu 、 cnzz 等
	 * @return 如果获取到，返回插件的Object对象，自行进行类型转换。如果获取不到，如用户未登录、插件信息不存在，则返回null
	 */
//	public static Object getPluginDataObjectBySession(String pluginId){
//		UserBean userBean = ShiroFunc.getUserBeanForShiroSession();
//		if(userBean == null){
//			//未登录，没有userBean
//			return null;
//		}
//		Object obj = userBean.getPluginDataMap().get(pluginId);
//		return obj;
//	}
//	
//	/**
//	 * 获取插件信息，从session中。这里取到的直接就是具体的类，不再需要object强制类型转换
//	 * 可以从session中获取该用户某个插件的缓存信息，避免频繁查数据库。
//	 * 前提是已经将该用户的插件的信息缓存进去了
//	 * @param pluginId 插件id，如 kefu 、 cnzz 等
//	 * @return 如果获取到，返回。如果获取不到，如用户未登录、插件信息不存在，则返回null
//	 */
//	public static <T> T getPluginDataBySession(String pluginId){
//		Object obj = getPluginDataObjectBySession(pluginId);
//		if(obj == null){
//			return null;
//		}
//		return (T)obj;
//	}
//	
//	/**
//	 * 设置插件信息，加入到session中。也就是将某个用户的某个插件的信息加入到session中缓存
//	 * @param pluginId 插件id，如 kefu 、 cnzz 等
//	 * @param obj 插件要缓存的信息
//	 * @return 成功：true；  若用户未登录导致缓存失败，返回false
//	 */
//	public static boolean setPluginDataBySession(String pluginId, Object obj){
//		UserBean userBean = ShiroFunc.getUserBeanForShiroSession();
//		if(userBean == null){
//			//未登录，没有userBean
//			return false;
//		}
//		Map<String, Object> map = userBean.getPluginDataMap();
//		map.put(pluginId, obj);
//		userBean.setPluginDataMap(map);
//		return true;
//	}
	
}
