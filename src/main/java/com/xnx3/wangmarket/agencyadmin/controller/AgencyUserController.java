package com.xnx3.wangmarket.agencyadmin.controller;

import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.aliyun.openservices.log.exception.LogException;
import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.MD5Util;
import com.xnx3.StringUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.entity.UserRole;
import com.xnx3.j2ee.service.ApiService;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.util.IpUtil;
import com.xnx3.j2ee.util.LanguageUtil;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.net.AliyunLogPageUtil;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.vo.SiteVO;
import com.xnx3.wangmarket.admin.vo.UserVO;
import com.xnx3.wangmarket.domain.bean.MQBean;
import com.xnx3.wangmarket.domain.bean.SimpleSite;
import com.xnx3.wangmarket.agencyadmin.entity.Agency;
import com.xnx3.wangmarket.agencyadmin.entity.SiteSizeChange;
import com.xnx3.wangmarket.agencyadmin.service.TransactionalService;
import com.xnx3.wangmarket.agencyadmin.util.SiteSizeChangeLog;
import net.sf.json.JSONArray;

/**
 * 代理商
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/agency")
public class AgencyUserController extends BaseController {
	@Resource
	private SqlService sqlService;
	@Resource
	private UserService userService;
	@Resource
	private SiteService siteService;
	@Resource
	private TransactionalService transactionalService;
	@Resource
	private ApiService apiService;
	
	
	/**
	 * 我的的下级代理商列表
	 */
	@RequiresPermissions("agencySubAgencyList")
	@RequestMapping("subAgencyList${url.suffix}")
	public String subAgencyList(HttpServletRequest request, Model model){
		Agency agency = getMyAgency();
		
		Sql sql = new Sql(request);
		sql.setSearchTable("agency");
		sql.appendWhere("agency.parent_id = "+agency.getId());
		sql.setSearchColumn(new String[]{"name","phone"});
		int count = sqlService.count("agency", sql.getWhere());
		Page page = new Page(count, G.PAGE_WAP_NUM, request);
		sql.setSelectFromAndPage("SELECT agency.*, user.username FROM agency,user", page);
		sql.appendWhere("user.id = agency.userid");
		sql.setOrderByField(new String[]{"id","expiretime","addtime"});
		List<Map<String, Object>> list = sqlService.findMapBySql(sql);
		ActionLogUtil.insert(request, agency.getId(), "查看我的下级代理列表");
		
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		return "agency/subAgencyList";
	}
	
	/**
	 * 添加用户、网站
	 * @return
	 */
	@RequiresPermissions("agencyAdd")
	@RequestMapping("add${url.suffix}")
	public String add(HttpServletRequest request, Model model){
		userService.regInit(request);
		
		//提前判断一下，此用户是否还有余额
		Agency agency = getMyAgency();
		if(agency.getSiteSize() == 0){
			return error(model, "您的账户余额还剩 "+agency.getSiteSize()+" 站，不足以再开通网站！请联系相关人员充值");
		}
		
		ActionLogUtil.insert(request, agency.getId(), "进入添加站点的页面");
		
		return "agency/add";
	}
	
	/**
	 * 开通我的下级代理
	 * @return
	 */
	@RequiresPermissions("AgencyNormalAdd")
	@RequestMapping("addAgency${url.suffix}")
	public String addAgency(HttpServletRequest request, Model model){
		Agency myAgency = getMyAgency();
		if(myAgency.getSiteSize() < G.agencyAddSubAgency_siteSize){
			return error(model, "您的账户余额还剩 "+myAgency.getSiteSize()+" 站币，不足以再开通下级！请联系您的上级充值");
		}
		
		userService.regInit(request);
		ActionLogUtil.insert(request,"进入添加下级代理的页面");
		return "agency/addAgency";
	}
	
	
	/**
	 * 添加用户、网站
	 * @param client 客户端类型，1电脑，2手机，v2.1增加3为高级模版站CMS站
	 * @param siteName 站点名字
	 * @param contactUsername 联系人姓名
	 * @param sitePhone 联系人手机号
	 * @param siteQQ 站点联系人的QQ号
	 * @param address 联系人公司地址
	 * @param companyName 公司名字／个体户名字，或者个人姓名
	 * @param email 电子邮箱
	 * @param templateName v2.1增加，开通网站时，若client=3，则此处为选择的模版。如果不选择模版，建立空白的CMS新站，则为空字符串即可
	 */
	@RequiresPermissions("agencyAdd")
	@RequestMapping("addSubmit${url.suffix}")
	@ResponseBody
	public BaseVO addSubmit(HttpServletRequest request, Model model,
			User user,
			@RequestParam(value = "client", required = false , defaultValue="0") Short client,
			@RequestParam(value = "siteName", required = false , defaultValue="") String siteName,
			@RequestParam(value = "contactUsername", required = false , defaultValue="") String contactUsername,
			@RequestParam(value = "sitePhone", required = false , defaultValue="") String sitePhone,
			@RequestParam(value = "siteQQ", required = false , defaultValue="") String siteQQ,
			@RequestParam(value = "address", required = false , defaultValue="") String address,
			@RequestParam(value = "companyName", required = false , defaultValue="") String companyName,
			@RequestParam(value = "email", required = false , defaultValue="") String email,
			@RequestParam(value = "templateName", required = false , defaultValue="") String templateName
			){
		
		Agency agency = sqlService.findById(Agency.class, getMyAgency().getId());
		if(agency.getSiteSize() == 0){
			return error("您的账户余额还剩 "+agency.getSiteSize()+" 站，不足以再开通网站！请联系相关人员充值");
		}
		
		if(client == 0){
			return error("请选择站点类型，是电脑网站呢，还是手机网站呢？");
		}
		if(siteName.length() == 0 || siteName.length() > 20){
			return error("请输入1～30个字符的要建立的站点名字");
		}
		

		//要创建得网站得user
		user.setReferrerid(agency.getUserid());
		
		Site site = new Site();
		site.setName(filter(siteName));
		site.setPhone(filter(sitePhone));
		site.setQq(filter(siteQQ));
		site.setAddress(filter(address));
		site.setClient(client);
		site.setUsername(filter(contactUsername));
		site.setCompanyName(filter(companyName));
		site.setTemplateName(filter(templateName));
		
		ActionLogUtil.insertUpdateDatabase(request, agency.getId(), "代理后台创建网站", site.getName());
		return createSite(request, agency, user, site, email);
	}
	
	/**
	 * 创建站点
	 * @param request
	 * @param agency 当前登录用户的agency对象。需要从数据库中新查询的，保存会直接保存此对象 sql.save(agency)
	 * @param user		要创建得用户信息
	 * @param site 要创建得网站
	 * @param email	邮箱，存入用户表，user.email
	 * @return
	 */
	private BaseVO createSite(HttpServletRequest request,
			Agency agency,
			User user,
			Site site,
			String email
		){
		
		if(agency.getSiteSize() == 0){
			return error("您的账户余额还剩 "+agency.getSiteSize()+" 站，不足以再开通网站！请联系相关人员充值");
		}
		if(site.getName().length() == 0 || site.getName().length() > 30){
			return error("请输入1～30个字符的要建立的站点名字");
		}
		//开通的都是CMS类型，v5.1
		site.setClient(Site.CLIENT_CMS);
		
		//创建用户 
		user.setPhone(filter(site.getPhone()));
		user.setEmail(filter(email));
//		user.setOssSizeHave(agency.getRegOssHave());
		UserVO vo = transactionalService.regUser(user, request, false);
//		UserVO vo = regUser(user, request, false);
		if(vo.getResult() == BaseVO.SUCCESS){
			
			//创建站点
			site.setExpiretime(DateUtil.timeForUnix10() + 31622400);	//到期，一年后，366天后

			site.setmShowBanner(Site.MSHOWBANNER_SHOW);
			SiteVO siteVO = siteService.saveSite(site, vo.getUser().getId(), request);
			if(siteVO.getResult() == SiteVO.SUCCESS){
				
				//减去当前代理的账户余额的站币
				agency.setSiteSize(agency.getSiteSize() - 1);
				sqlService.save(agency);
				if(getUserId() > 0){
					//如果是登录用户，那么要刷新用户的缓存
					Func.getUserBeanForShiroSession().setMyAgency(agency); 	//刷新缓存
				}
				
				//将变动记录入数据库
				SiteSizeChange ssc = new SiteSizeChange();
				ssc.setAddtime(DateUtil.timeForUnix10());
				ssc.setAgencyId(agency.getId());
				ssc.setChangeAfter(agency.getSiteSize());
				ssc.setChangeBefore(agency.getSiteSize()+1);
				ssc.setGoalid(siteVO.getSite().getId());
				ssc.setSiteSizeChange(-1);
				ssc.setUserid(agency.getUserid());
				sqlService.save(ssc);
				
				//将变动记录入日志服务的站币变动中
				SiteSizeChangeLog.xiaofei(agency.getName(), "代理开通网站："+site.getName(), ssc.getSiteSizeChange(), ssc.getChangeBefore(), ssc.getChangeAfter(), ssc.getGoalid(), IpUtil.getIpAddress(request));
				
				//记录动作日志
				ActionLogUtil.insertUpdateDatabase(request, site.getId(), "代理后台创建网站", site.getName());
				
				return success(vo.getUser().getId()+"_"+passwordMD5(vo.getUser().getPassword()));
			}else{
				return error("添加用户成功，但添加站点失败！");
			}
			
		}else{
			return error(vo.getInfo());
		}
	}
	
	/**
	 * api接口身份校验
	 * @param key 要校验的key， id_password的4次加密，128长度的字符
	 * @return {@link UserVO} 成功：vo.getResult = success
	 */
	private UserVO apiIdentityVerify(String key){
		UserVO vo = new UserVO();
		/*
		 * 验证Key的格式
		 */
		if(key.length() < 128 || key.indexOf("_") == -1){
			vo.setBaseVO(UserVO.FAILURE, "key错误1");
			return vo;
		}
		
		String[] ks = key.split("_");
		if(ks[0].length() == 0 || ks[1].length() == 0){
			vo.setBaseVO(UserVO.FAILURE, "key错误2");
			return vo;
		}
		
		int userid = Lang.stringToInt(ks[0], 0);
		if(userid == 0){
			vo.setBaseVO(UserVO.FAILURE, "key错误3");
			return vo;
		}
		String pwd = StringUtil.removeBlank(ks[1]);
		if(pwd.length() != 128){
			vo.setBaseVO(UserVO.FAILURE, "key错误4");
			return vo;
		}
		
		/*
		 * 验证Key是否存在
		 * 获取代理商的user信息
		 */
		User user = sqlService.findById(User.class, userid);
		if(user == null){
			//统一提示，避免被利用
			vo.setBaseVO(UserVO.FAILURE, "key错误5");
			return vo;	
		}
		if(!passwordMD5(user.getPassword()).equals(pwd)){
			vo.setBaseVO(UserVO.FAILURE, "key错误6");
			return vo;
		}
			
		vo.setUser(user);
		return vo;
	}
	
	/**
	 * 将32位的 user.password 密码进行再加密，生成128位加密字符串
	 * @param password user.password 密码
	 * @return 新生成的128位加密字符串
	 */
	private static String passwordMD5(String password){
		String p1 = password.substring(0, 8);
		String p2 = password.substring(8, 16);
		String p3 = password.substring(16, 24);
		String p4 = password.substring(24, 32);
		
		return MD5Util.MD5(p1)+MD5Util.MD5(p2)+MD5Util.MD5(p3)+MD5Util.MD5(p4);
	}
	
	/**
	 * 开通我的下级代理账户
	 * @param contactUsername 联系人姓名
	 * @param sitePhone 联系人手机号
	 * @param siteQQ 站点联系人的QQ号
	 * @param address 联系人公司地址
	 * @param companyName 公司名字／个体户名字，或者个人姓名
	 * @param email 电子邮箱
	 */
	@RequiresPermissions("AgencyNormalAdd")
	@RequestMapping("addAgencySave${url.suffix}")
	@ResponseBody
	public BaseVO addAgencySave(HttpServletRequest request, Model model,
			User user,
			@RequestParam(value = "contactUsername", required = false , defaultValue="") String contactUsername,
			@RequestParam(value = "phone", required = false , defaultValue="") String phone,
			@RequestParam(value = "qq", required = false , defaultValue="") String qq,
			@RequestParam(value = "address", required = false , defaultValue="") String address,
			@RequestParam(value = "companyName", required = false , defaultValue="") String companyName,
			@RequestParam(value = "email", required = false , defaultValue="") String email
			){
		
		// 参数合法校验
		if(contactUsername == null || companyName.trim().equals("")) {
			return error("请输入联系人姓名");
		}
		if(phone == null || phone.trim().equals("")) {
			return error("请输入联系人手机号");
		}
		if(contactUsername == null || companyName.trim().equals("")) {
			return error("请输入联系人姓名");
		}
		if(address == null || address.trim().equals("")) {
			return error("请输入公司地址");
		}
		if(companyName == null || companyName.trim().equals("")) {
			return error("请输入公司名称");
		}

		Agency myAgency = sqlService.findById(Agency.class, getMyAgency().getId());
		if(myAgency.getSiteSize() < G.agencyAddSubAgency_siteSize){
			return error("您的账户余额还剩 "+myAgency.getSiteSize()+" 站币，不足以再开通下级！请联系您的上级充值");
		}

		//创建用户
//		user.setOssSizeHave(myAgency.getRegOssHave());
		user.setPhone(filter(phone));
		user.setEmail(filter(email));
		user.setReferrerid(myAgency.getUserid());
		UserVO vo = transactionalService.regUser(user, request, true);
//		UserVO vo = regUser(user, request, true);
		if(vo.getResult() == BaseVO.SUCCESS){
			//创建完用户了，再创建代理
			Agency agency = new Agency();
			agency.setName(filter(companyName));
			agency.setPhone(filter(phone));
			agency.setUserid(vo.getUser().getId());
			agency.setQq(filter(qq));
			agency.setAddress(filter(address));
			agency.setSiteSize(0);
			agency.setParentId(myAgency.getId());
			agency.setAddtime(DateUtil.timeForUnix10());
			agency.setExpiretime(DateUtil.timeForUnix10() + 31622400);	//到期，一年后，366天后
			agency.setState(Agency.STATE_NORMAL);
			sqlService.save(agency);
			
			if(agency.getId() != null && agency.getId() > 0){
				
				//减去当前代理的账户余额的站币
				myAgency.setSiteSize(myAgency.getSiteSize() - G.agencyAddSubAgency_siteSize);
				sqlService.save(myAgency);
				Func.getUserBeanForShiroSession().setMyAgency(myAgency); 	//刷新当前登录用户的代理缓存
				
				//将资金变动记录入数据库
				SiteSizeChange ssc = new SiteSizeChange();
				ssc.setAddtime(DateUtil.timeForUnix10());
				ssc.setAgencyId(myAgency.getId());
				ssc.setChangeAfter(myAgency.getSiteSize());
				ssc.setChangeBefore(myAgency.getSiteSize() + G.agencyAddSubAgency_siteSize);
				ssc.setGoalid(agency.getId());
				ssc.setSiteSizeChange(0-G.agencyAddSubAgency_siteSize);
				ssc.setUserid(myAgency.getUserid());
				sqlService.save(ssc);
				
				//将变动记录入日志服务的站币变动中
				SiteSizeChangeLog.xiaofei(myAgency.getName(), "开通下级代理："+agency.getName(), ssc.getSiteSizeChange(), ssc.getChangeBefore(), ssc.getChangeAfter(), ssc.getGoalid(), IpUtil.getIpAddress(request));
				
				//动作日志
				ActionLogUtil.insertUpdateDatabase(request, agency.getId(), "开通下级代理成功", agency.getName());
				
				return success();
			}else{
				return error("创建用户信息成功，但是创建代理记录失败！请联系官网");
			}
			
		}else{
			return error(vo.getInfo());
		}
	}
	
	
	/**
	 * 注册User，代理商开通用户网站
	 * @param user
	 * @param request
	 * @param isAgency 是否是开通的普通代理，true，是开通普通代理
	 * @return 生成的用户User对象
	 */
	private UserVO regUsersss(User user, HttpServletRequest request, boolean isAgency) {
		UserVO baseVO = new UserVO();
		user.setUsername(filter(user.getUsername()));
		user.setEmail(filter(user.getEmail()));
		user.setPhone(filter(user.getPhone()));
		
		//判断用户名、邮箱、手机号是否有其中为空的
		if(user.getUsername()==null||user.getUsername().equals("")){
			baseVO.setBaseVO(BaseVO.FAILURE, LanguageUtil.show("user_userNameToLong"));
		}
		
		//判断用户名、邮箱、手机号是否有其中已经注册了，唯一性
		//判断用户名唯一性
		
		if(sqlService.findByProperty(User.class, "username", user.getUsername()).size() > 0){
			baseVO.setBaseVO(BaseVO.FAILURE, LanguageUtil.show("user_regFailureForUsernameAlreadyExist"));
			return baseVO;
		}
		
		//判断邮箱是否被注册了，若被注册了，则邮箱设置为空
		if(sqlService.findByProperty(User.class, "email", user.getEmail()).size() > 0){
			user.setEmail("");
		}
		
		//判断手机号是否被用过。若被用过了，则自动将手机号给抹除，不写入User表
		if(user.getPhone() != null && user.getPhone().length() > 0){
			if(sqlService.findByProperty(User.class, "phone", user.getPhone()).size() > 0){
				if(isAgency){
					//如果是创建代理，手机号必须的，并且唯一
					baseVO.setBaseVO(BaseVO.FAILURE, LanguageUtil.show("user_regFailureForPhoneAlreadyExist"));
					return baseVO;
				}else{
					//如果只是建站，则可以允许手机号为空
					user.setPhone("");
				}
			}
		}
		
		user.setRegip(IpUtil.getIpAddress(request));
		user.setLastip(IpUtil.getIpAddress(request));
		user.setRegtime(DateUtil.timeForUnix10());
		user.setLasttime(DateUtil.timeForUnix10());
		user.setNickname(user.getUsername());
		user.setAuthority(isAgency? SystemUtil.get("AGENCY_ROLE")+"":SystemUtil.get("USER_REG_ROLE"));	//设定是普通代理，还是会员权限
		user.setCurrency(0);
		user.setFreezemoney(0);
		user.setMoney(0);
		user.setIsfreeze(User.ISFREEZE_NORMAL);
		user.setHead("default.png");
		
		if(getUserId() > 0){
			user.setReferrerid(getUserId());		//设定用户的上级是当前代理商本人
		}
		
		Random random = new Random();
		user.setSalt(random.nextInt(10)+""+random.nextInt(10)+""+random.nextInt(10)+""+random.nextInt(10)+"");
        String md5Password = new Md5Hash(user.getPassword(), user.getSalt(),Global.USER_PASSWORD_SALT_NUMBER).toString();
		user.setPassword(md5Password);
		
		sqlService.save(user);
		if(user.getId()>0){
			//赋予该用户系统设置的默认角色，是代理，还是会员
			UserRole userRole = new UserRole();
			int roleid = 0;
			if(isAgency){
				roleid = SystemUtil.getInt("AGENCY_ROLE");
			}else{
				roleid = SystemUtil.getInt("USER_REG_ROLE");
			}
			userRole.setRoleid(roleid);
			userRole.setUserid(user.getId());
			sqlService.save(userRole);
			
			baseVO.setBaseVO(BaseVO.SUCCESS, LanguageUtil.show("user_regSuccess"));
			baseVO.setUser(user);
		}else{
			baseVO.setBaseVO(BaseVO.FAILURE, LanguageUtil.show("user_regFailure"));
		}
		
		return baseVO;
	}
	

	/**
	 * 增值服务，收费服务
	 */
	@RequestMapping("zengzhifuwu${url.suffix}")
	public String zengzhifuwu(HttpServletRequest request,Model model){
		ActionLogUtil.insert(request, "进入增值服务页面");
		
		return "agency/zengzhifuwu";
	}
	
	

	/**
	 * 操作日志
	 * @throws LogException 
	 */
	@RequiresPermissions("agencyActionLogList")
	@RequestMapping("actionLogList${url.suffix}")
	public String actionLogList(HttpServletRequest request, Model model) throws LogException{
		if(ActionLogUtil.aliyunLogUtil == null){
			return error(model, "未开启日志服务");
		}
		AliyunLogPageUtil log = new AliyunLogPageUtil(ActionLogUtil.aliyunLogUtil);
		
		//得到当前页面的列表数据
		JSONArray jsonArray = log.list("userid="+getUserId(), "", false, 15, request);
		
		//得到当前页面的分页相关数据（必须在执行了list方法获取列表数据之后，才能调用此处获取到分页）
		Page page = log.getPage();
		
		ActionLogUtil.insert(request, "获取代理操作记录");
		
		model.addAttribute("list", jsonArray);
		model.addAttribute("page", page);
		return "agency/actionLogList";
	}
	
	/**
	 * 资金变动日志
	 * @throws LogException 
	 */
	@RequiresPermissions("agencySiteSizeLogList")
	@RequestMapping("siteSizeLogList${url.suffix}")
	public String siteSizeLogList(HttpServletRequest request, Model model) throws LogException{
		if(SiteSizeChangeLog.aliyunLogUtil == null){
			return error(model, "未开启日志服务");
		}
		//当前10位时间戳
		String query = "userid="+getUserId();
		AliyunLogPageUtil log = new AliyunLogPageUtil(SiteSizeChangeLog.aliyunLogUtil);
		
		//得到当前页面的列表数据
		JSONArray jsonArray = log.list(query, "", true, 15, request);
		
		//得到当前页面的分页相关数据（必须在执行了list方法获取列表数据之后，才能调用此处获取到分页）
		Page page = log.getPage();
		//设置分页，出现得上几页、下几页跳转按钮的个数
		page.setListNumber(2);
		
		ActionLogUtil.insert(request, "查看资金变动日志");
		model.addAttribute("list", jsonArray);
		model.addAttribute("page", page);
		return "agency/siteSizeLogList";
	}
	

	/**
	 * 我的下级网站列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequiresPermissions("agencyUserList")
	@RequestMapping("userList${url.suffix}")
	public String userList(HttpServletRequest request, Model model){
		Sql sql = new Sql(request);
		sql.setSearchTable("user");
		sql.appendWhere("user.referrerid = "+getUserId()+" AND user.authority = "+SystemUtil.getInt("USER_REG_ROLE"));
		sql.setSearchColumn(new String[]{"username","email","phone","userid="});
		int count = sqlService.count("user", sql.getWhere());
		Page page = new Page(count, G.PAGE_WAP_NUM, request);
		sql.setSelectFromAndPage("SELECT site.*, user.lasttime, user.username AS userusername  FROM site,user", page);
		sql.appendWhere("user.id = site.userid");
		sql.setOrderByField(new String[]{"id","expiretime","addtime"});
		sql.setDefaultOrderBy("site.expiretime ASC");
		List<Map<String, Object>> list = sqlService.findMapBySql(sql);
		ActionLogUtil.insert(request, "代理商后台，查看属于我的站点列表");
		
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		return "agency/userList";
	}
	
	/**
	 * 转移站币到子代理账户，向自己的下级代理转账（站币），为其充值站币
	 * @param targetAgencyId 要充值站币的代理的id，agency.id
	 * @param transferSiteSize 要充值的站币数量，必须大于0
	 * @return
	 */
	@RequiresPermissions("agencyTransferSiteSizeToSubAgencyList")
	@RequestMapping(value="transferSiteSizeToSubAgency${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO transferSiteSizeToSubAgency(HttpServletRequest request, Model model,
			@RequestParam(value = "targetAgencyId", required = true) int targetAgencyId,
			@RequestParam(value = "transferSiteSize", required = false, defaultValue = "0") int transferSiteSize){
		BaseVO vo = transactionalService.transferSiteSizeToSubAgency(request, targetAgencyId, transferSiteSize);
		ActionLogUtil.insertUpdateDatabase(request, "转移站币到子代理账户，向自己的下级代理转账（站币），为其充值站币", vo.toString());
		return vo;
	}
	
	/**
	 * 站点续费，给自己开通的站点续费时长
	 * @param siteid 要续费的站点id，site.id
	 * @param year 要续费的年数，支持1～10，最大续费10年
	 * @return
	 */
	@RequiresPermissions("agencySiteXuFie")
	@RequestMapping(value="siteXuFie${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO siteXuFie(HttpServletRequest request, Model model,
			@RequestParam(value = "siteid", required = true) int siteid,
			@RequestParam(value = "year", required = false, defaultValue = "0") int year){
		BaseVO vo = transactionalService.siteXuFei(request, siteid, year);
		ActionLogUtil.insertUpdateDatabase(request, "站点续费，给自己开通的站点续费时长", vo.toString());
		return vo;
	}
	
	/**
	 * 暂停网站，冻结网站。冻结后，site、user数据表都会记录
	 * 暂停后，网站依旧正常计费！
	 * @param siteid 要暂停的网站的site.id
	 */
	@RequiresPermissions("agencySiteFreeze")
	@RequestMapping("siteFreeze${url.suffix}")
	@ResponseBody
	public BaseVO sitePause(HttpServletRequest request,
			@RequestParam(value = "siteid", required = true) int siteid){
		Site site = sqlService.findById(Site.class, siteid);
		User user = sqlService.findById(User.class, site.getUserid());
		if(user == null){
			return error("用户不存在！");
		}
		//判断是否是其直属上级
		if(user.getReferrerid() - getMyAgency().getUserid() != 0){
			return error("要暂停的网站不是您的直属下级，操作失败");
		}
		//判断网站状态是否符合，只有当网站状态为正常时，才可以对网站进行暂停冻结操作
		if(site.getState() - Site.STATE_NORMAL != 0){
			return error("当前网站的状态不符，暂停失败");
		}
		
		site.setState(Site.STATE_FREEZE);
		sqlService.save(site);
		userService.freezeUser(site.getUserid());
		
		//记录操作日志
		ActionLogUtil.insertUpdateDatabase(request, site.getId(), getMyAgency().getName()+"将网站"+site.getName()+"暂停");
		
		//更新域名服务器
		MQBean mqBean = new MQBean();
		mqBean.setType(MQBean.TYPE_STATE);
		mqBean.setSimpleSite(new SimpleSite(site));
		siteService.updateDomainServers(mqBean);
		
		return success();
	}
	
	/**
	 * 解除暂停网站，将暂停的网站恢复正常
	 * @param siteid 要暂停的网站的site.id
	 */
	@RequiresPermissions("agencySiteFreeze")
	@RequestMapping("siteUnFreeze${url.suffix}")
	@ResponseBody
	public BaseVO siteRemovePause(HttpServletRequest request,
			@RequestParam(value = "siteid", required = true) int siteid){
		Site site = sqlService.findById(Site.class, siteid);
		User user = sqlService.findById(User.class, site.getUserid());
		if(user == null){
			return error("用户不存在！");
		}
		//判断是否是其直属上级
		if(user.getReferrerid() - getMyAgency().getUserid() != 0){
			return error("要暂停的网站不是您的直属下级，操作失败");
		}
		//判断网站状态是否符合，只有当网站状态为正常时，才可以对网站进行暂停冻结操作
		if(site.getState() - Site.STATE_FREEZE != 0){
			return error("当前网站的状态不符，暂停失败");
		}
		
		site.setState(Site.STATE_NORMAL);
		sqlService.save(site);
		userService.unfreezeUser(site.getUserid());
		
		//记录操作日志
		ActionLogUtil.insertUpdateDatabase(request, site.getId(), getMyAgency().getName()+"将暂停的网站"+site.getName()+"恢复正常");
		
		//更新域名服务器
		MQBean mqBean = new MQBean();
		mqBean.setType(MQBean.TYPE_STATE);
		mqBean.setSimpleSite(new SimpleSite(site));
		siteService.updateDomainServers(mqBean);
		
		return success();
	}
	
	
	/**
	 * 代理商给其下的某个站点更改密码
	 * @param userid 要更改密码的user.id
	 * @param newPassword 要更改上的新密码
	 */
	@RequiresPermissions("agencySiteUpdatePassword")
	@RequestMapping("siteUpdatePassword${url.suffix}")
	@ResponseBody
	public BaseVO siteUpdatePassword(HttpServletRequest request,
			@RequestParam(value = "userid", required = true) int userid,
			@RequestParam(value = "newPassword", required = true) String newPassword){
		User user = sqlService.findById(User.class, userid);
		if(user == null){
			return error("用户不存在");
		}
		//判断是否是其直属下级
		if(user.getReferrerid() - getMyAgency().getUserid() != 0){
			return error("要更改密码的网站不是您的直属下级，操作失败");
		}
		
		ActionLogUtil.insertUpdateDatabase(request, userid, "代理商给其下的某个站点更改密码", newPassword);
		return userService.updatePassword(userid, newPassword);
	}
	

	/**
	 * 给某个代理延长使用期限
	 * @param agencyId 要续费的代理id，agency.id
	 * @param year 要续费的年数，支持1～10，最大续费10年
	 */
	@RequiresPermissions("agencyYanQi")
	@RequestMapping(value="agencyYanQi${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO agencyYanQi(HttpServletRequest request, Model model,
			@RequestParam(value = "agencyId", required = true) int agencyId,
			@RequestParam(value = "year", required = false, defaultValue = "0") int year){
		if(year < 1){
			return error("请输入要续费的年数，1～10");
		}
		if(year > 10){
			return error("请输入要续费的年数，1～10，最大可往后续费10年");
		}
		
		//我的代理信息
		Agency myAgency = sqlService.findById(Agency.class, getMyAgency().getId());
		
		//续费所需的站币
		int xufeiZhanBi = year * 20;
		
		if(myAgency.getSiteSize() - xufeiZhanBi <= 0){
			return error("您当前只拥有"+myAgency.getSiteSize()+"站币！续费花费的金额超出，续费失败！");
		}
		
		//我的下级的代理信息，要给他延期的，代理的信息
		Agency subAgency = sqlService.findById(Agency.class, agencyId);
		//我的下级代理，所属人的信息
		User subUser = sqlService.findById(User.class, subAgency.getUserid());
		if(subUser.getReferrerid() - myAgency.getUserid() != 0){
			return error("要延期的代理不是您的直属下级，为其延期失败");
		}
		
		//判断延期后的代理使用期限是否超过了10年 ,当前时间 ＋ 3660天
		if((subAgency.getExpiretime() + (year * 31622400)) > (DateUtil.timeForUnix10() + 316224000)){
			return error("代理资格往后延期的最大期限为10年！");
		}
		
		//当前我的IP地址
		String ip = IpUtil.getIpAddress(request);
		
		
		/**** 进行数据保存 ****/
		
		//我的代理信息里，减去续费花费的站币
		myAgency.setSiteSize(myAgency.getSiteSize() - xufeiZhanBi);
		sqlService.save(myAgency);
		//数据库保存我的消费记录
		SiteSizeChange ssc = new SiteSizeChange();
		ssc.setAddtime(DateUtil.timeForUnix10());
		ssc.setAgencyId(myAgency.getId());
		ssc.setChangeAfter(myAgency.getSiteSize());
		ssc.setChangeBefore(myAgency.getSiteSize() + xufeiZhanBi);
		ssc.setGoalid(subAgency.getId());
		ssc.setSiteSizeChange(0-xufeiZhanBi);
		ssc.setUserid(myAgency.getUserid());
		sqlService.save(ssc);
		//记录我的资金消费记录
		SiteSizeChangeLog.xiaofei(myAgency.getName(), "给代理"+subAgency.getName()+"延长使用期限"+year+"年", ssc.getSiteSizeChange(), ssc.getChangeBefore(), ssc.getChangeAfter(), ssc.getGoalid(), ip);
		
		//对方代理平台延长使用过期时间
		subAgency.setExpiretime(subAgency.getExpiretime() + (year * 31622400));
		sqlService.save(subAgency);
		
		//到期时间
		String daoqishijian = "";
		try {
			daoqishijian = DateUtil.dateFormat(subAgency.getExpiretime(), "yyyy年MM月dd日");
		} catch (NotReturnValueException e) {
			e.printStackTrace();
		}
		
		//记录操作日志
		ActionLogUtil.insertUpdateDatabase(request, ssc.getId(), "给代理"+subAgency.getName()+"延长使用期限"+year+"年。代理资格延期后，日期到"+daoqishijian);
		
		//发送短信通知对方
		//这里等转为公司模式后，用公司资格申请短信发送资格
//		G.aliyunSMSUtil.send(G.AliyunSMS_SignName, G.AliyunSMS_siteYanQi_templateCode, "{\"siteName\":\""+site.getName()+"\", \"year\":\""+year+"\", \"time\":\""+daoqishijian+"\"}", site.getPhone());
		
		//刷新Session中我的代理信息缓存
		Func.getUserBeanForShiroSession().setMyAgency(myAgency);
		
		return success();
	}
	

	/**
	 * 暂停下级代理，冻结自己当前的下级代理的使用资格。冻结后，agency , user数据表会记录，将无法登录
	 * 暂停后，代理平台依旧正常计费！不会退返延期的20站币
	 * @param agencyId 要暂停、冻结的代理的agency.id
	 */
	@RequiresPermissions("agencyAgencyFreeze")
	@RequestMapping("agencyFreeze${url.suffix}")
	@ResponseBody
	public BaseVO agencyFreeze(HttpServletRequest request,
			@RequestParam(value = "agencyId", required = true) int agencyId){
		//调出子代理的代理信息跟User信息
		Agency subAgency = sqlService.findById(Agency.class, agencyId);
		User subUser = sqlService.findById(User.class, subAgency.getUserid());
		if(subUser == null){
			return error("用户不存在！");
		}
		//判断是否是其直属上级
		if(subUser.getReferrerid() - getMyAgency().getUserid() != 0){
			return error("要冻结的代理不是您的直属下级，操作失败");
		}
		//判断代理的状态是否符合，只有当代理状态为正常时，才可以对代理进行暂停冻结操作
		if(subAgency.getState() - Site.STATE_NORMAL != 0){
			return error("当前网站的状态不符，暂停失败");
		}
		
		subAgency.setState(Agency.STATE_FREEZE);
		sqlService.save(subAgency);
		userService.freezeUser(subAgency.getUserid());
		
		//记录操作日志
		ActionLogUtil.insertUpdateDatabase(request, subAgency.getId(), getMyAgency().getName()+"将代理"+subAgency.getName()+"暂停、冻结");
		
		//要给对方发送右键提醒
		//待加入，要先加入邮箱改动／绑定体系
		
		return success();
	}
	
	/**
	 * 解除暂停、冻结的我的下级代理，将暂停的代理恢复正常
	 * @param agencyId 冻结的代理的agency.id
	 */
	@RequiresPermissions("agencyAgencyUnFreeze")
	@RequestMapping("agencyUnFreeze${url.suffix}")
	@ResponseBody
	public BaseVO agencyUnFreeze(HttpServletRequest request,
			@RequestParam(value = "agencyId", required = true) int agencyId){
		Agency subAgency = sqlService.findById(Agency.class, agencyId);
		User subUser = sqlService.findById(User.class, subAgency.getUserid());
		if(subUser == null){
			return error("用户不存在！");
		}
		//判断是否是其直属上级
		if(subUser.getReferrerid() - getMyAgency().getUserid() != 0){
			return error("要恢复的代理不是您的直属下级，操作失败");
		}
		//判断网站状态是否符合，只有当网站状态为正常时，才可以对网站进行暂停冻结操作
		if(subAgency.getState() - Agency.STATE_FREEZE != 0){
			return error("当前代理的状态不符，是正常状态，无需恢复正常");
		}
		
		subAgency.setState(Agency.STATE_NORMAL);
		sqlService.save(subAgency);
		userService.unfreezeUser(subAgency.getUserid());
		
		//记录操作日志
		ActionLogUtil.insertUpdateDatabase(request, subAgency.getId(), getMyAgency().getName()+"将冻结的代理"+subAgency.getName()+"恢复正常");
		
		return success();
	}
	

	
}
