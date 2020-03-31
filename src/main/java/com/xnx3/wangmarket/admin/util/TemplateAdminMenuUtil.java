package com.xnx3.wangmarket.admin.util;

import java.util.HashMap;
import java.util.Map;

import com.xnx3.j2ee.pluginManage.PluginManage;
import com.xnx3.j2ee.pluginManage.PluginRegister;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.util.TemplateAdminMenu.FirstMenu;
import com.xnx3.wangmarket.admin.util.TemplateAdminMenu.MenuBean;
import com.xnx3.wangmarket.admin.util.TemplateAdminMenu.TemplateMenuEnum;

/**
 * 网站管理后台，左侧菜单相关的工具类，也可以理解为权限相关，控制左侧菜单的显示、隐藏，控制某个用户哪个菜单可见，哪个不可见
 * @author 管雷鸣
 *
 */
public class TemplateAdminMenuUtil {
	/**
	 * 将 TemplateMenuEnum 枚举中定义的菜单拿出来，等级层次分清，以便随时使用
	 * key：TemplateMenuEnum.id
	 * value:MenuBean
	 */
	public static Map<String, MenuBean> menuMap = new HashMap<String, MenuBean>();
	
	static{
		menuMap.clear();
		
		//取出所有的权限菜单-一级菜单
		for (TemplateMenuEnum e : TemplateMenuEnum.values()) {
			MenuBean menuBean = new MenuBean(e);
			if(menuBean.getParentid().length() < 2){
				//是一级菜单
				menuMap.put(menuBean.getId(), menuBean);
			}
		}
		//再取出所有的权限菜单的二级菜单
		for (TemplateMenuEnum e : TemplateMenuEnum.values()) {
			MenuBean menuBean = new MenuBean(e);
			if(menuBean.getParentid().length() > 2){
				//是二级菜单
				menuMap.get(menuBean.getParentid()).getSubList().add(menuBean);
			}
		}
		
	}
	
	/**
	 * 获取网站管理后台登陆成功后，显示的菜单列表的html。这个是根据当前用户所拥有哪些权限来显示哪些内容的
	 */
	public static String getLeftMenuHtml(){
		Map<String, String> map = SessionUtil.getSiteMenuRole();
		StringBuffer sb = new StringBuffer();
		
		//取出所有的权限菜单-一级菜单
		for (TemplateMenuEnum e : TemplateMenuEnum.values()) {
			if(map.get(e.id) != null){
				//有这一项
			}
		}
		
		//系统管理
		if(map.get(TemplateMenuEnum.SYSTEM.id) != null){
			MenuBean mb = menuMap.get(TemplateMenuEnum.SYSTEM.id);
			
			FirstMenu Menu = new FirstMenu("li_"+TemplateMenuEnum.SYSTEM.id, TemplateMenuEnum.SYSTEM.id, TemplateMenuEnum.SYSTEM.href, TemplateMenuEnum.SYSTEM.icon, TemplateMenuEnum.SYSTEM.name);
			
			for (int i = 0; i < mb.getSubList().size(); i++) {
				MenuBean submb = mb.getSubList().get(i);
				//二级菜单
				if(map.get(submb.id) != null){
					Menu.addTwoMenu("dd_"+submb.id, submb.id, submb.href, submb.name);
				}
			}
//			
//			//判断其内是否有二级菜单可显示
//			//基本信息
//			
//			//网站设置
//			if(map.get(TemplateMenuEnum.SYSTEM_WangZhanSheZhi.id) != null){
//				Menu.addTwoMenu("dd_"+TemplateMenuEnum.SYSTEM_WangZhanSheZhi.id, TemplateMenuEnum.SYSTEM_WangZhanSheZhi.id, TemplateMenuEnum.SYSTEM_WangZhanSheZhi.href, TemplateMenuEnum.SYSTEM_WangZhanSheZhi.name);
//			}
//			//修改密码
//			if(map.get(TemplateMenuEnum.SYSTEM_XiuGaiMiMa.id) != null){
//				Menu.addTwoMenu("dd_"+TemplateMenuEnum.SYSTEM_XiuGaiMiMa.id, TemplateMenuEnum.SYSTEM_XiuGaiMiMa.id, TemplateMenuEnum.SYSTEM_XiuGaiMiMa.href, TemplateMenuEnum.SYSTEM_XiuGaiMiMa.name);
//			}
//			//预览网站
//			if(map.get(TemplateMenuEnum.SYSTEM_YuLanWangZhan.id) != null){
//				Menu.addTwoMenu("dd_"+TemplateMenuEnum.SYSTEM_YuLanWangZhan.id, TemplateMenuEnum.SYSTEM_YuLanWangZhan.id, TemplateMenuEnum.SYSTEM_YuLanWangZhan.href, TemplateMenuEnum.SYSTEM_YuLanWangZhan.name);
//			}
			
			//将生成的html，加入进来
			sb.append(Menu.gainMenuHTML());
		}
		
		//模版管理
		if(map.get(TemplateMenuEnum.TEMPLATE.id) != null){
			FirstMenu Menu = new FirstMenu("li_"+TemplateMenuEnum.TEMPLATE.id, TemplateMenuEnum.TEMPLATE.id, TemplateMenuEnum.TEMPLATE.href, TemplateMenuEnum.TEMPLATE.icon, TemplateMenuEnum.TEMPLATE.name);
			
			//全局变量
			if(map.get(TemplateMenuEnum.TEMPLATE_QuanJuBianLiang.id) != null){
				Menu.addTwoMenu("dd_"+TemplateMenuEnum.TEMPLATE_QuanJuBianLiang.id, TemplateMenuEnum.TEMPLATE_QuanJuBianLiang.id, TemplateMenuEnum.TEMPLATE_QuanJuBianLiang.href, TemplateMenuEnum.TEMPLATE_QuanJuBianLiang.name);
			}
			//模版变量
			if(map.get(TemplateMenuEnum.TEMPLATE_MoBanBianLiang.id) != null){
				Menu.addTwoMenu("dd_"+TemplateMenuEnum.TEMPLATE_MoBanBianLiang.id, TemplateMenuEnum.TEMPLATE_MoBanBianLiang.id, TemplateMenuEnum.TEMPLATE_MoBanBianLiang.href, TemplateMenuEnum.TEMPLATE_MoBanBianLiang.name);
			}
			//模版页面
			if(map.get(TemplateMenuEnum.TEMPLATE_MoBanYeMian.id) != null){
				Menu.addTwoMenu("dd_"+TemplateMenuEnum.TEMPLATE_MoBanYeMian.id, TemplateMenuEnum.TEMPLATE_MoBanYeMian.id, TemplateMenuEnum.TEMPLATE_MoBanYeMian.href, TemplateMenuEnum.TEMPLATE_MoBanYeMian.name);
			}
			//输入模型
			if(map.get(TemplateMenuEnum.TEMPLATE_ShuRuMoXing.id) != null){
				Menu.addTwoMenu("dd_"+TemplateMenuEnum.TEMPLATE_ShuRuMoXing.id, TemplateMenuEnum.TEMPLATE_ShuRuMoXing.id, TemplateMenuEnum.TEMPLATE_ShuRuMoXing.href, TemplateMenuEnum.TEMPLATE_ShuRuMoXing.name);
			}
			//导出备份
			if(map.get(TemplateMenuEnum.TEMPLATE_DaoChuBeiFen.id) != null){
				Menu.addTwoMenu("dd_"+TemplateMenuEnum.TEMPLATE_DaoChuBeiFen.id, TemplateMenuEnum.TEMPLATE_DaoChuBeiFen.id, TemplateMenuEnum.TEMPLATE_DaoChuBeiFen.href, TemplateMenuEnum.TEMPLATE_DaoChuBeiFen.name);
			}
			//导入还原
			if(map.get(TemplateMenuEnum.TEMPLATE_DaoRuHuanYuan.id) != null){
				Menu.addTwoMenu("dd_"+TemplateMenuEnum.TEMPLATE_DaoRuHuanYuan.id, TemplateMenuEnum.TEMPLATE_DaoRuHuanYuan.id, TemplateMenuEnum.TEMPLATE_DaoRuHuanYuan.href, TemplateMenuEnum.TEMPLATE_DaoRuHuanYuan.name);
			}
			//模版插件
			if(map.get(TemplateMenuEnum.TEMPLATE_MoBanChaJian.id) != null){
				Menu.addTwoMenu("dd_"+TemplateMenuEnum.TEMPLATE_MoBanChaJian.id, TemplateMenuEnum.TEMPLATE_MoBanChaJian.id, TemplateMenuEnum.TEMPLATE_MoBanChaJian.href, TemplateMenuEnum.TEMPLATE_MoBanChaJian.name);
			}
			
			//将生成的html，加入进来
			sb.append(Menu.gainMenuHTML());
		}
		
		//帮助说明
		if(map.get(TemplateMenuEnum.HELP.id) != null){
			FirstMenu Menu = new FirstMenu("li_"+TemplateMenuEnum.HELP.id, TemplateMenuEnum.HELP.id, TemplateMenuEnum.HELP.href, TemplateMenuEnum.HELP.icon, TemplateMenuEnum.HELP.name);
			
			//使用入门
			if(map.get(TemplateMenuEnum.HELP_ShiYongRuMen.id) != null){
				Menu.addTwoMenu("dd_"+TemplateMenuEnum.HELP_ShiYongRuMen.id, TemplateMenuEnum.HELP_ShiYongRuMen.id, TemplateMenuEnum.HELP_ShiYongRuMen.href, TemplateMenuEnum.HELP_ShiYongRuMen.name);
			}
			//模版开发
			if(map.get(TemplateMenuEnum.HELP_MoBanKaiFa.id) != null){
				Menu.addTwoMenu("dd_"+TemplateMenuEnum.HELP_MoBanKaiFa.id, TemplateMenuEnum.HELP_MoBanKaiFa.id, TemplateMenuEnum.HELP_MoBanKaiFa.href, TemplateMenuEnum.HELP_MoBanKaiFa.name);
			}
			
			//将生成的html，加入进来
			sb.append(Menu.gainMenuHTML());
		}
		
		//功能插件
		if(map.get(TemplateMenuEnum.PLUGIN.id) != null){
			FirstMenu Menu = new FirstMenu("li_"+TemplateMenuEnum.PLUGIN.id, TemplateMenuEnum.PLUGIN.id, TemplateMenuEnum.PLUGIN.href, TemplateMenuEnum.PLUGIN.icon, TemplateMenuEnum.PLUGIN.name);
			
			//将加载的插件拿出来
			if(PluginManage.cmsSiteClassManage.size() > 0){
				for (Map.Entry<String, PluginRegister> entry : PluginManage.cmsSiteClassManage.entrySet()) {
					PluginRegister plugin = entry.getValue();
					Menu.addTwoMenu("dd_"+entry.getKey(), entry.getKey(), "javascript:loadIframeByUrl('"+plugin.menuHref()+"'), notUseTopTools();", plugin.menuTitle());
				}
			}
			
			//将生成的html，加入进来
			sb.append(Menu.gainMenuHTML());
		}
		
		//栏目管理
		if(map.get(TemplateMenuEnum.COLUMN.id) != null){
			FirstMenu Menu = new FirstMenu("li_"+TemplateMenuEnum.COLUMN.id, TemplateMenuEnum.COLUMN.id, TemplateMenuEnum.COLUMN.href, TemplateMenuEnum.COLUMN.icon, TemplateMenuEnum.COLUMN.name);
			//将生成的html，加入进来
			sb.append(Menu.gainMenuHTML());
		}
				
		//内容管理
		if(map.get(TemplateMenuEnum.NEWS.id) != null){
			FirstMenu Menu = new FirstMenu("li_"+TemplateMenuEnum.NEWS.id, TemplateMenuEnum.NEWS.id, TemplateMenuEnum.NEWS.href, TemplateMenuEnum.NEWS.icon, TemplateMenuEnum.NEWS.name);
			//将生成的html，加入进来
			sb.append(Menu.gainMenuHTML());
		}
				
		//技术支持
		if(map.get(TemplateMenuEnum.SUPPORT.id) != null){
			FirstMenu Menu = new FirstMenu("li_"+TemplateMenuEnum.SUPPORT.id, TemplateMenuEnum.SUPPORT.id, TemplateMenuEnum.SUPPORT.href, TemplateMenuEnum.SUPPORT.icon, TemplateMenuEnum.SUPPORT.name);
			//将生成的html，加入进来
			sb.append(Menu.gainMenuHTML());
		}
				
		//生成整站
		if(map.get(TemplateMenuEnum.SHENGCHENGZHENGZHAN.id) != null){
			FirstMenu Menu = new FirstMenu("li_"+TemplateMenuEnum.SHENGCHENGZHENGZHAN.id, TemplateMenuEnum.SHENGCHENGZHENGZHAN.id, TemplateMenuEnum.SHENGCHENGZHENGZHAN.href, TemplateMenuEnum.SHENGCHENGZHENGZHAN.icon, TemplateMenuEnum.SHENGCHENGZHENGZHAN.name);
			//将生成的html，加入进来
			sb.append(Menu.gainMenuHTML());
		}
		
		return sb.toString();
	}
	
	/**
	 * 判断当前登陆的用户，对于某个 id 是否有管理操作的权力。当然，这个只是明面上能看到的权力，并不是修改的
	 * @param id
	 */
	public static boolean haveRole(String id){
		Map<String, String> map = SessionUtil.getSiteMenuRole();
		if(map.get(id) != null){
			return true;
		}else{
			return false;
		}
	}
}
