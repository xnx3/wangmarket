package com.xnx3.j2ee.shiro;

import java.util.ArrayList;
import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import com.xnx3.j2ee.entity.Permission;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.bean.PermissionMark;
import com.xnx3.j2ee.bean.PermissionTree;

/**
 * shiro权限相关用到的函数
 * @author 管雷鸣
 *
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
				if(mp.getId()==ap.getId()){
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
			if(pm.getPermission().getParentId()==0){
				PermissionTree permissionTree = new PermissionTree();
				permissionTree.setPermissionMark(pm);
				List<PermissionMark> markTreeList = new ArrayList<PermissionMark>();
				for (int j = 0; j < listMark.size(); j++) {
					PermissionMark permissionMarkSub = listMark.get(j);
					if(permissionMarkSub.getPermission().getParentId()==pm.getPermission().getId()){
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
	 */
	public static ActiveUser getCurrentActiveUser(){
		//从shiro的session中取activeUser
		if(SecurityUtils.getSubject() == null){
			return null;
		}else{
			Subject subject = SecurityUtils.getSubject();
			//取身份信息
			ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
			if(activeUser != null){
				return activeUser;
			}else{
				return null;
			}
		}
	}
	
	/**
	 * Session中获取当前登录用户的信息
	 * @return 	<li>登陆了，则返回User对象
	 * 			<li>未登陆，返回null
	 */
	public static User getUser(){
		if(getCurrentActiveUser() == null){
			return null;
		}else{
			ActiveUser activeUser = getCurrentActiveUser();
			if(activeUser!=null){
				return activeUser.getUser();
			}else{
				return null;
			}
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
	 * @return true:设置成功，false:设置失败
	 */
	public static boolean setUEditorAllowUpload(boolean allow){
		//从shiro的session中取activeUser
		if(SecurityUtils.getSubject() == null){
			//肯定是设施失败了
			return false;
		}else{
			Subject subject = SecurityUtils.getSubject();
			//取身份信息
			ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
			if(activeUser != null){
				activeUser.setAllowUploadForUEditor(allow);
				return true;
			}else{
				return false;
			}
		}
		
	}
	
	/**
	 * 获取当前用户是否能使用UEditor编辑器进行图片、文件、视频等上传。若为false，则不能用其进行上传
	 * <br/>
	 * @param allow 若用户没有登陆，同样返回false
	 */
	public static boolean getUEditorAllowUpload(){
		ActiveUser au = getCurrentActiveUser();
		if(au == null){
			return false;
		}else{
			return au.isAllowUploadForUEditor();
		}
	}
	
}
