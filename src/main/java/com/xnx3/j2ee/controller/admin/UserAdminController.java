package com.xnx3.j2ee.controller.admin;

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
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.controller.BaseController;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * 用户管理
 * @author 管雷鸣
 */
@Controller(value="WMUserAdminController")
@RequestMapping("/admin/user")
public class UserAdminController extends BaseController {
	@Resource
	private UserService userService;
	@Resource
	private SqlService sqlService;
	
	/**
	 * 删除用户
	 * @param id 要删除的用户id，User.id
	 */
	@RequiresPermissions("adminUserDelete")
	@RequestMapping(value="deleteUser${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO deleteUser(HttpServletRequest request,
			@RequestParam(value = "id", required = true) int id){
		if(id>0){
			User u = sqlService.findById(User.class, id);
			if(u!=null){
				sqlService.delete(u);
				ActionLogUtil.insertUpdateDatabase(request,u.getId(), "总管理后台,删除用户",u.toString());
				return success();
			}
		}
		
		return error("删除失败");
	}
	
	/**
	 * 用户列表
	 */
	@RequiresPermissions("adminUserList")
	@RequestMapping("list${url.suffix}")
	public String list(HttpServletRequest request,Model model){
		Sql sql = new Sql(request);
		sql.setSearchColumn(new String[]{"username","email","nickname","authority","referrerid","phone","id=","regtime(date:yyyy-MM-dd hh:mm:ss)>"});
		int count = sqlService.count("user", sql.getWhere());
		Page page = new Page(count, SystemUtil.getInt("LIST_EVERYPAGE_NUMBER"), request);
		sql.setSelectFromAndPage("SELECT * FROM user", page);
		sql.setDefaultOrderBy("user.id DESC");
		sql.setOrderByField(new String[]{"id","lasttime","money","currency"});
		List<User> list = sqlService.findBySql(sql, User.class);
		
		ActionLogUtil.insert(request, "总管理后台-用户列表", "第"+page.getCurrentPageNumber()+"页");
		
		model.addAttribute("page", page);
		model.addAttribute("list", list);
		return "/wm/admin/user/list";
	}
	
	/**
	 * 用户详情
	 * @param id 要查看详情的用户的id, 对应 user.id
	 */
	@RequiresPermissions("adminUserView")
	@RequestMapping("view${url.suffix}")
	public String view(HttpServletRequest request,
			@RequestParam(value = "id", required = true) int id,Model model){
		User user = sqlService.findById(User.class, id);
		if(user == null){
			return error(model, "要查看的用户不存在");
		}
		
		if(user.getReferrerid()==null || user.getReferrerid()==0){
			model.addAttribute("referrer", "无邀请人");
		}else{
			User parentUser = sqlService.findById(User.class, user.getReferrerid());
			model.addAttribute("referrer", "<a href='view.do?id="+user.getReferrerid()+"'>id:"+user.getReferrerid()+","+parentUser.getUsername()+"</a>");
		}
		
		ActionLogUtil.insert(request, user.getId(), "总管理后台-用户详情", user.toString());
		
		model.addAttribute("u", user);
		return "/wm/admin/user/view";
	}
	
	/**
	 * 冻结／解除冻结用户。
	 * 冻结的用户，在登录时(也就是使用 {@link UserService#loginByUsernameAndPassword(HttpServletRequest)} 这种账号密码登录、手机号+验证码方式登录的 )登录不上，会提示账号已被冻结
	 * @param id {@link User}.id 要冻结的用户的编号
	 * @param isfreeze 要更改的值，取值如： {@link User}.ISFREEZE_FREEZE
	 */
	@RequiresPermissions("adminUserUpdateFreeze")
	@RequestMapping(value="updateFreeze${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO updateFreeze(HttpServletRequest request,
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "isfreeze", required = true) int isfreeze,
			Model model){
		BaseVO baseVO = new BaseVO();
		if(isfreeze==User.ISFREEZE_FREEZE){
			baseVO = userService.freezeUser(id);
			ActionLogUtil.insertUpdateDatabase(request, id, "总管理后台-冻结用户", baseVO.getInfo());
		}else if (isfreeze==User.ISFREEZE_NORMAL) {
			baseVO = userService.unfreezeUser(id);
			ActionLogUtil.insertUpdateDatabase(request, id, "总管理后台-解除冻结用户", baseVO.getInfo());
		}else{
			baseVO.setBaseVO(BaseVO.FAILURE, "未知参数！");
			ActionLogUtil.insertError(request, "此接口要么冻结，要么解冻，出现了非正常情况！");
		}
		
		if(baseVO.getResult() == BaseVO.SUCCESS){
			return success("操作成功！");
		}else{
			return error("操作失败！");
		}
	}
	
}
