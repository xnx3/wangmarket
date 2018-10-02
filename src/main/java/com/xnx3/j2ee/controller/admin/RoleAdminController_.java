package com.xnx3.j2ee.controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xnx3.j2ee.entity.Permission;
import com.xnx3.j2ee.entity.Role;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.service.RoleService;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.bean.PermissionTree;
import com.xnx3.j2ee.bean.RoleMark;
import com.xnx3.j2ee.controller.BaseController;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * 权限管理
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/admin/role")
public class RoleAdminController_ extends BaseController {
	@Resource
	private RoleService roleService;
	@Resource
	private UserService userService;
	@Resource
	private SqlService sqlService;
	
	/**
	 * 新增、编辑角色
	 * @param id 要编辑的角色的id, Role.id
	 */
	@RequestMapping("role${url.suffix}")
	@RequiresPermissions("adminRoleRole")
	public String role(@RequestParam(value = "id", required = true) int id,Model model, HttpServletRequest request){
		if(id>0){
			//修改
			Role role = sqlService.findById(Role.class, id);
			if(role!=null){
				ActionLogCache.insert(request, role.getId(), "进入编辑角色页面", role.getName()+"，"+role.getDescription());
				model.addAttribute("role", role);
			}
		}else{
			//新增
			ActionLogCache.insert(request, "进入添加角色页面");
		}
		return "iw/admin/role/role";
	}
	
	/**
	 * 添加／修改角色提交页
	 * @param role {@link Role}
	 */
	@RequestMapping(value="saveRole${url.suffix}", method = RequestMethod.POST)
	@RequiresPermissions("adminRoleRole")
	@ResponseBody
	public BaseVO saveRole(Role role,Model model, HttpServletRequest request){
		sqlService.save(role);
		ActionLogCache.insert(request, role.getId(), "角色保存", role.getName()+"，"+role.getDescription());
		return success();
	}
	

	/**
	 * 删除角色
	 * @param id 要删除的角色id，Role.id
	 */
	@RequestMapping(value="deleteRole${url.suffix}", method = RequestMethod.POST)
	@RequiresPermissions("adminRoleDeleteRole")
	@ResponseBody
	public BaseVO deleteRole(@RequestParam(value = "id", required = true) int id, Model model, HttpServletRequest request){
		if(id>0){
			Role role = sqlService.findById(Role.class, id);
			if(role!=null){
				sqlService.delete(role);
				ActionLogCache.insert(request, role.getId(), "删除角色", role.getName()+"，"+role.getDescription());
				return success();
			}
		}
		return error("删除失败");
	}
	
	/**
	 * 角色列表
	 */
	@RequestMapping("roleList${url.suffix}")
	@RequiresPermissions("adminRoleRoleList")
	public String roleList(Model model, HttpServletRequest request){
		List<Role> list = sqlService.findAll(Role.class);
		
		ActionLogCache.insert(request, "角色列表");
		model.addAttribute("list", list);
		return "iw/admin/role/roleList";
	}
	
	/**
	 * 添加资源permission
	 * @param parentId 所添加的资源的所属上级资源。如果是顶级资源，则为0
	 */
	@RequiresPermissions("adminRolePermission")
	@RequestMapping("addPermission${url.suffix}")
	public String addPermission(
			@RequestParam(value = "parentId", required = true) int parentId,
			Model model, HttpServletRequest request){
		Permission parentPermission = sqlService.findById(Permission.class, parentId);
		String parentPermissionDescription = "顶级";
		if(parentPermission!=null){
			parentPermissionDescription = parentPermission.getName() +","+ parentPermission.getDescription();
		}
		
		Permission permission = new Permission();
		permission.setParentId(parentId);
		
		ActionLogCache.insert(request, parentId, "进入添加资源Permission页面", "所属上级："+parentPermissionDescription);
		model.addAttribute("permission", permission);
		model.addAttribute("parentPermissionDescription", parentPermissionDescription);
		return "iw/admin/role/permission";
	}
	
	/**
	 * 编辑资源
	 * @param id 资源的id，Permission.id 
	 */
	@RequiresPermissions("adminRolePermission")
	@RequestMapping("editPermission${url.suffix}")
	public String editPermission(@RequestParam(value = "id", required = true) int id,Model model, HttpServletRequest request){
		if(id>0){
			Permission permission = sqlService.findById(Permission.class, id);
			if(permission!=null){
				String parentPermissionDescription="顶级";
				if(permission.getParentId()>0){
					Permission parentPermission = sqlService.findById(Permission.class, permission.getParentId());
					parentPermissionDescription = parentPermission.getName() +","+ parentPermission.getDescription();
				}
				
				ActionLogCache.insert(request, permission.getId(), "进入修改资源Permission页面", "所属上级："+parentPermissionDescription);
				
				model.addAttribute("permission", permission);
				model.addAttribute("parentPermissionDescription", parentPermissionDescription);
				return "iw/admin/role/permission";
			}
		}
		return error(model, "出错，参数错误");
	}

	/**
	 * Permission提交保存
	 * @param permission 要保存的{@link Permission}
	 */
	@RequiresPermissions("adminRolePermission")
	@RequestMapping(value="savePermission${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO savePermission(Permission permissionInput,Model model, HttpServletRequest request){
		Permission permission;
		if(permissionInput.getId() != null && permissionInput.getId() > 0){
			//修改
			permission = sqlService.findById(Permission.class, permissionInput.getId());
			if(permission == null){
				return error("修改的资源不存在");
			}
		}else{
			permission = new Permission();
		}
		
		permission.setDescription(permissionInput.getDescription());
		permission.setName(permissionInput.getName());
		permission.setParentId(permissionInput.getParentId());
		permission.setPercode(permissionInput.getPercode());
		permission.setUrl(permissionInput.getUrl());
		sqlService.save(permission);
		ActionLogCache.insert(request, permission.getId(), "资源Permission保存", permission.getName()+"，"+permission.getDescription());
		return success();
	}

	/**
	 * 删除资源Permission
	 * @param id 要删除的资源的id，Permission.id ，根据此来删除
	 */
	@RequiresPermissions("adminRoleDeletePermission")
	@RequestMapping(value="deletePermission${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO deletePermission(@RequestParam(value = "id", required = true) int id, HttpServletRequest request){
		if(id>0){
			Permission permission = sqlService.findById(Permission.class, id);
			if(permission!=null){
				sqlService.delete(permission);
				ActionLogCache.insert(request, permission.getId(), "删除资源Permission", permission.getName()+"，"+permission.getDescription());
				return success();
			}
		}
		
		return error("删除失败");
	}
	
	/**
	 * 资源Permission列表
	 */
	@RequiresPermissions("adminRolePermissionList")
	@RequestMapping("permissionList${url.suffix}")
	public String permissionList(HttpServletRequest request,Model model){
		Sql sql = new Sql(request);
		sql.setSearchColumn(new String[]{"description","url","name","parent_id","percode"});
		int count = sqlService.count("permission", sql.getWhere());
		Page page = new Page(count, 1000, request);
		sql.setSelectFromAndPage("SELECT * FROM permission", page);
		sql.setDefaultOrderBy("permission.id DESC");
		List<Permission> list = sqlService.findBySql(sql, Permission.class);
		List<PermissionTree> permissionTreeList = new ShiroFunc().PermissionToTree(new ArrayList<Permission>(), list);
		
		ActionLogCache.insert(request, "资源Permission列表");
		
		model.addAttribute("page", page);
		model.addAttribute("list", permissionTreeList);
		return "iw/admin/role/permissionList";
	}
	
	/**
	 * 编辑某个权限下拥有的资源
	 * @param roleId 角色id，Rold.id
	 */
	@RequiresPermissions("adminRoleEditRolePermission")
	@RequestMapping("editRolePermission${url.suffix}")
	public String editRolePermission(@RequestParam(value = "roleId", required = true) int roleId, Model model, HttpServletRequest request){
		if(roleId>0){
			List<Permission> myList = roleService.findPermissionByRoleId(roleId);	//选中的
			List<Permission> allList = sqlService.findAll(Permission.class);
			//转换为树状集合
			List<PermissionTree> list = new ShiroFunc().PermissionToTree(myList, allList);	
			
			Role role = sqlService.findById(Role.class, roleId);
			
			ActionLogCache.insert(request, "资源Permission列表");
			model.addAttribute("role", role);
			model.addAttribute("list", list);
			return "iw/admin/role/rolePermission";
		}
		return null;
	}

	/**
	 * 保存角色－资源设置
	 * @param roleId 角色id，Role.id
	 * @param permission 多选框的资源列表，如 1,2,3,4
	 */
	@RequiresPermissions("adminRoleEditRolePermission")
	@RequestMapping("saveRolePermission${url.suffix}")
	public String saveRolePermission(
			@RequestParam(value = "roleId", required = true) int roleId,
			@RequestParam(value = "permission", required = false) String permission,
			Model model, HttpServletRequest request){
		BaseVO vo = roleService.saveRolePermission(roleId, permission);
		if(vo.getResult() - BaseVO.SUCCESS == 0){
			ActionLogCache.insert(request, roleId, "保存角色所管理的资源");
			return success(model, "保存成功","admin/role/roleList.do");
		}else{
			return error(model, "保存失败："+vo.getInfo());
		}
	}
	

	/**
	 * 编辑用户－权限关系
	 * @param userid 用户id，User.id
	 */
	@RequiresPermissions("adminRoleEditUserRole")
	@RequestMapping("editUserRole${url.suffix}")
	public String editUserRole(@RequestParam(value = "userid", required = true) int userid, Model model, HttpServletRequest request){
		User user = sqlService.findById(User.class, userid);
		List<RoleMark> list = roleService.getUserRoleMarkList(userid);
		
		ActionLogCache.insert(request, "修改用户的角色权限页面");
			
		model.addAttribute("currentUser", user);
		model.addAttribute("list", list);
		return "iw/admin/role/userRole";
	}

	/**
	 * 保存用户－角色设置
	 * @param userid 修改的是哪个用户的权限，用户id, User.id
	 * @param role 权限多选框提交列表，如 1,2,3,4,5
	 */
	@RequiresPermissions("adminRoleEditUserRole")
	@RequestMapping(value="saveUserRole${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO saveUserRole(
			@RequestParam(value = "userid", required = true) int userid,
			@RequestParam(value = "role", required = false) String role,
			Model model, HttpServletRequest request){
		BaseVO vo = roleService.saveUserRole(userid, role);
		if(vo.getResult() - BaseVO.SUCCESS == 0){
			ActionLogCache.insert(request, "修改用户的角色权限");
			return success();
		}else{
			return error(vo.getInfo());
		}
	}
	
}