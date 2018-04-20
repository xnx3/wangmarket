package com.xnx3.j2ee.generateCache;
import java.util.List;
import com.xnx3.j2ee.service.SqlService;

/**
 * 权限相关数据缓存生成
 * @author 管雷鸣
 *
 */
public class Role extends BaseGenerate {
	
	/**
	 * 项目启动时，在{@link InitServlet}中进行初始化
	 */
	public void role(SqlService sqlService){
		List<com.xnx3.j2ee.entity.Role> list = sqlService.findAll(com.xnx3.j2ee.entity.Role.class);
		createCacheObject("role");
		for (int i = 0; i < list.size(); i++) {
    		com.xnx3.j2ee.entity.Role role = list.get(i);
    		cacheAdd(role.getId(), role.getName());
		}
		appendContent("/* 将 2,3,4 的权限字段转换为会员,超级管理员显示在html */ function writeName(authority){var roleArray=authority.split(',');var s='';for(var i=0;i<roleArray.length;i++){if(s!=''){s=s+','}s=s+role[roleArray[i]]}document.write(s)} ");
		
		generateCacheFile();
	}
}
