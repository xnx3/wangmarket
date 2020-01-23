package com.xnx3.wangmarket.agencyadmin.service.impl;

import java.util.Random;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xnx3.DateUtil;
import com.xnx3.MD5Util;
import com.xnx3.StringUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.entity.UserRole;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.util.IpUtil;
import com.xnx3.j2ee.util.LanguageUtil;
import com.xnx3.j2ee.util.SafetyUtil;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.net.AliyunLogUtil;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.vo.SiteVO;
import com.xnx3.wangmarket.admin.vo.UserVO;
import com.xnx3.wangmarket.agencyadmin.entity.Agency;
import com.xnx3.wangmarket.agencyadmin.entity.SiteSizeChange;
import com.xnx3.wangmarket.agencyadmin.service.TransactionalService;
import com.xnx3.wangmarket.agencyadmin.util.SessionUtil;
import com.xnx3.wangmarket.agencyadmin.util.SiteSizeChangeLog;

@Service("transactionalService")
@Transactional
public class TransactionalServiceImpl implements TransactionalService {

	@Resource
	private SqlDAO sqlDAO;
	@Resource
	private SiteService siteService;

	public BaseVO transferSiteSizeToSubAgency(HttpServletRequest request, int targetAgencyId, int transferSiteSize) {
		BaseVO vo = new BaseVO();
		if(transferSiteSize < 1){
			vo.setBaseVO(BaseVO.FAILURE, "请输入要充值站币的数量！");
			return vo;
		}
		
		Agency shiroMyAgency = com.xnx3.wangmarket.agencyadmin.Func.getMyAgency();
		
		//我的代理信息
		Agency myAgency = sqlDAO.findById(Agency.class, shiroMyAgency.getId());
		
		if(myAgency.getSiteSize() - transferSiteSize <= 0){
			vo.setBaseVO(BaseVO.FAILURE, "您当前只拥有"+myAgency.getSiteSize()+"站币！给下级充值金额超出，充值失败！");
			ActionLogUtil.insertUpdateDatabase(request, myAgency.getId(), "warn", "给下级代理充值站币："+vo.getInfo());
			return vo;
		}
		
		//我的下级代理信息，要给他转账的代理信息
		Agency agency = sqlDAO.findById(Agency.class, targetAgencyId);
		
		if(agency.getParentId() - myAgency.getId() != 0){
			vo.setBaseVO(BaseVO.FAILURE, "要充值的代理不是您的直属下级，无法充值");
			ActionLogUtil.insertUpdateDatabase(request, myAgency.getId(), "warn", "给下级代理充值站币："+vo.getInfo());
			return vo;
		}
		
		//我的代理信息里，减去转账的站币
		myAgency.setSiteSize(myAgency.getSiteSize() - transferSiteSize);
		sqlDAO.save(myAgency);
		//将资金变动记录入数据库，以我（当前用户）为主
		SiteSizeChange ssc = new SiteSizeChange();
		ssc.setAddtime(DateUtil.timeForUnix10());
		ssc.setAgencyId(myAgency.getId());
		ssc.setChangeAfter(myAgency.getSiteSize());
		ssc.setChangeBefore(myAgency.getSiteSize() + transferSiteSize);
		ssc.setGoalid(agency.getId());
		ssc.setSiteSizeChange(0-transferSiteSize);
		ssc.setUserid(myAgency.getUserid());
		sqlDAO.save(ssc);
		
		//下级代理的信息里，增加转账的站币
		agency.setSiteSize(agency.getSiteSize() + transferSiteSize);
		sqlDAO.save(agency);
		//将资金变动记录入数据库，以对方为主，这是对方的金钱变动日志，充值日志
		SiteSizeChange ssc_other = new SiteSizeChange();
		ssc_other.setAddtime(DateUtil.timeForUnix10());
		ssc_other.setAgencyId(agency.getId());
		ssc_other.setChangeAfter(agency.getSiteSize());
		ssc_other.setChangeBefore(agency.getSiteSize() - transferSiteSize);
		ssc_other.setGoalid(myAgency.getId());
		ssc_other.setSiteSizeChange(transferSiteSize);
		ssc_other.setUserid(agency.getUserid());
		sqlDAO.save(ssc_other);
		
		//当前我的IP地址
		String ip = IpUtil.getIpAddress(request);
		
		//记录我的资金消费记录
		SiteSizeChangeLog.xiaofei(myAgency.getName(), "给下级代理"+agency.getName()+"充值站币", ssc.getSiteSizeChange(), myAgency.getSiteSize()+transferSiteSize, myAgency.getSiteSize(), agency.getId(), ip);
		
		//记录我下线代理的资金充值记录
		User user = sqlDAO.findById(User.class, agency.getUserid());
		SiteSizeChangeLog.chongzhi(user.getId(), user.getUsername(), agency.getName(), "直属上级给充值站币", transferSiteSize, agency.getSiteSize()-transferSiteSize, agency.getSiteSize(), myAgency.getId(), ip);
		
		//记录操作日志
		ActionLogUtil.insertUpdateDatabase(request, agency.getId(), "给下级代理"+agency.getName()+"充值站币："+transferSiteSize);
		
		//发送短信通知对方，待短信模板通过审核
		//G.aliyunSMSUtil.send(G.AliyunSMS_SignName, G.AliyunSMS_agencySiteSizeRecharge_TemplateCode, "{\"chongzhi\":\""+transferSiteSize+"\", \"username\":\""+agency.getName()+"\", \"siteSize\":\""+agency.getSiteSize()+"\"}", agency.getPhone());
		
		//刷新Session中我的代理信息缓存
		SessionUtil.setAgency(myAgency);
		
		return vo;
	}

	public BaseVO siteXuFei(HttpServletRequest request, int siteid, int year) {
		BaseVO vo = new BaseVO();
		
		if(year < 1){
			vo.setBaseVO(BaseVO.FAILURE, "请输入要续费的年数，1～10");
			return vo;
		}
		if(year > 10){
			vo.setBaseVO(BaseVO.FAILURE, "请输入要续费的年数，1～10，最大可往后续费10年");
			return vo;
		}
		
		Agency shiroMyAgency = com.xnx3.wangmarket.agencyadmin.Func.getMyAgency();
		
		//我的代理信息
		Agency myAgency = sqlDAO.findById(Agency.class, shiroMyAgency.getId());
		
		if(myAgency.getSiteSize() - year <= 0){
			vo.setBaseVO(BaseVO.FAILURE, "您当前只拥有"+myAgency.getSiteSize()+"站币！续费花费的金额超出，续费失败！");
			return vo;
		}
		
		//我的下级的网站信息，要给他续费的网站信息
		Site site = sqlDAO.findById(Site.class, siteid);
		//我的下级的网站所属人的信息
		User user = sqlDAO.findById(User.class, site.getUserid());
		if(user.getReferrerid() - myAgency.getUserid() != 0){
			vo.setBaseVO(BaseVO.FAILURE, "要续费的网站不是您的直属下级，无法续费");
			ActionLogUtil.insertError(request, "myAgency.id:"+myAgency.getId()+",给我开通的站点续费："+vo.getInfo());
			return vo;
		}
		
		//避免int溢出
		long expiretime = site.getExpiretime();
		expiretime = expiretime + (year * 31622400);
		System.out.println(expiretime);
		if(expiretime > 2147483647){
			vo.setBaseVO(BaseVO.FAILURE, "网站往后续费最大可续费到2038年！此年份后将开启全新建站时代。");
			return vo;
		}
		
		//判断续费后的网站是否超过了10年 ,当前时间 ＋ 3660天
		if(expiretime > (DateUtil.timeForUnix10() + 316224000)){
			vo.setBaseVO(BaseVO.FAILURE, "网站往后续费最大为10年！");
			return vo;
		}
		
		
		//当前我的IP地址
		String ip = IpUtil.getIpAddress(request);
		
		/**** 进行数据保存 ****/
		
		//我的代理信息里，减去续费花费的站币
		myAgency.setSiteSize(myAgency.getSiteSize() - year);
		sqlDAO.save(myAgency);
		//数据库保存我的消费记录
		SiteSizeChange ssc = new SiteSizeChange();
		ssc.setAddtime(DateUtil.timeForUnix10());
		ssc.setAgencyId(myAgency.getId());
		ssc.setChangeAfter(myAgency.getSiteSize());
		ssc.setChangeBefore(myAgency.getSiteSize() + year);
		ssc.setGoalid(site.getId());
		ssc.setSiteSizeChange(0-year);
		ssc.setUserid(myAgency.getUserid());
		sqlDAO.save(ssc);
		//记录我的资金消费记录
		SiteSizeChangeLog.xiaofei(myAgency.getName(), "给网站"+site.getName()+"续费"+year+"年", year, myAgency.getSiteSize()+year, myAgency.getSiteSize(), site.getId(), ip);
		
		//网站增加过期时间
		site.setExpiretime(site.getExpiretime() + (year * 31622400));
		sqlDAO.save(site);
		
		//到期时间
		String daoqishijian = "";
		try {
			daoqishijian = DateUtil.dateFormat(site.getExpiretime(), "yyyy年MM月dd日");
		} catch (NotReturnValueException e) {
			e.printStackTrace();
		}
		
		//记录操作日志
		ActionLogUtil.insertUpdateDatabase(request, site.getId(), "给网站"+site.getName()+"续费"+year+"年。网站续费后，日期到"+daoqishijian);
		
		//发送短信通知对方
//		G.aliyunSMSUtil.send(G.AliyunSMS_SignName, G.AliyunSMS_siteYanQi_templateCode, "{\"siteName\":\""+site.getName()+"\", \"year\":\""+year+"\", \"time\":\""+daoqishijian+"\"}", site.getPhone());
		
		//刷新Session中我的代理信息缓存
		SessionUtil.setAgency(myAgency);
		return vo;
	}

	@Override
	public BaseVO agencyCreateSite(HttpServletRequest request, Agency agency,
			User user, Site site, String email) {
		BaseVO vo = new BaseVO();
		
		if(agency.getSiteSize() == 0){
			vo.setBaseVO(BaseVO.FAILURE, "您的账户余额还剩 "+agency.getSiteSize()+" 站，不足以再开通网站！请联系相关人员充值");
			return vo;
		}
		
		if(site.getClient() == 0){
			vo.setBaseVO(BaseVO.FAILURE, "请选择站点类型，是电脑网站呢，还是手机网站呢？");
			return vo;
		}
		if(site.getName().length() == 0 || site.getName().length() > 30){
			vo.setBaseVO(BaseVO.FAILURE, "请输入1～30个字符的要建立的站点名字");
			return vo;
		}
		
		//创建用户
		user.setPhone(StringUtil.filterXss(Sql.filter(site.getPhone())));
		user.setEmail(StringUtil.filterXss(Sql.filter((email))));
		user.setReferrerid(agency.getUserid());	//设定用户的上级是当前代理商本人
		UserVO userVO = regUser(user, request, false);
		if(userVO.getResult() == BaseVO.SUCCESS){
			
			//创建站点
			site.setExpiretime(DateUtil.timeForUnix10() + 31622400);	//到期，一年后，366天后

			site.setmShowBanner(Site.MSHOWBANNER_SHOW);
			SiteVO siteVO = siteService.saveSite(site, userVO.getUser().getId(), request);
			if(siteVO.getResult() - SiteVO.SUCCESS == 0){
				
				//减去当前代理的账户余额的站币
				agency.setSiteSize(agency.getSiteSize() - 1);
				sqlDAO.save(agency);
//				if(getUserId() > 0){
//					//如果是登录用户，那么要刷新用户的缓存
//					Func.getUserBeanForShiroSession().setMyAgency(agency); 	//刷新缓存
//				}
				
				//将变动记录入数据库
				SiteSizeChange ssc = new SiteSizeChange();
				ssc.setAddtime(DateUtil.timeForUnix10());
				ssc.setAgencyId(agency.getId());
				ssc.setChangeAfter(agency.getSiteSize());
				ssc.setChangeBefore(agency.getSiteSize()+1);
				ssc.setGoalid(siteVO.getSite().getId());
				ssc.setSiteSizeChange(-1);
				ssc.setUserid(agency.getUserid());
				sqlDAO.save(ssc);
				
				//将变动记录入日志服务的站币变动中
				SiteSizeChangeLog.xiaofei(agency.getName(), "代理开通网站："+site.getName(), ssc.getSiteSizeChange(), ssc.getChangeBefore(), ssc.getChangeAfter(), ssc.getGoalid(), IpUtil.getIpAddress(request));
				
				//记录动作日志
				ActionLogUtil.insertUpdateDatabase(request, site.getId(), "开通网站："+site.getName());
				
				vo.setInfo(userVO.getUser().getId()+"_"+passwordMD5(userVO.getUser().getPassword()));
			}else{
				vo.setBaseVO(BaseVO.FAILURE, "添加用户成功，但添加站点失败！");
			}
		}else{
			vo.setBaseVO(BaseVO.FAILURE, userVO.getInfo());
		}
		
		return vo;
	}

	@Override
	public UserVO regUser(User user, HttpServletRequest request,
			boolean isAgency) {
		UserVO baseVO = new UserVO();
		user.setUsername(StringUtil.filterXss(user.getUsername()));
		user.setEmail(SafetyUtil.filter(user.getEmail()));
		user.setPhone(SafetyUtil.filter(user.getPhone()));
		
		//判断用户名、邮箱、手机号是否有其中为空的
		if(user.getUsername()==null||user.getUsername().equals("")){
			baseVO.setBaseVO(BaseVO.FAILURE, LanguageUtil.show("user_userNameToLong"));
		}
		
		//判断用户名、邮箱、手机号是否有其中已经注册了，唯一性
		//判断用户名唯一性
		
		if(sqlDAO.findByProperty(User.class, "username", user.getUsername()).size() > 0){
			baseVO.setBaseVO(BaseVO.FAILURE, LanguageUtil.show("user_regFailureForUsernameAlreadyExist"));
			return baseVO;
		}
		
		//判断邮箱是否被注册了，若被注册了，则邮箱设置为空
		if(sqlDAO.findByProperty(User.class, "email", user.getEmail()).size() > 0){
			user.setEmail("");
		}
		
		//判断手机号是否被用过。若被用过了，则自动将手机号给抹除，不写入User表
		if(user.getPhone() != null && user.getPhone().length() > 0){
			if(sqlDAO.findByProperty(User.class, "phone", user.getPhone()).size() > 0){
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
		
		Random random = new Random();
		user.setSalt(random.nextInt(10)+""+random.nextInt(10)+""+random.nextInt(10)+""+random.nextInt(10)+"");
        String md5Password = new Md5Hash(user.getPassword(), user.getSalt(),Global.USER_PASSWORD_SALT_NUMBER).toString();
		user.setPassword(md5Password);
		
		sqlDAO.save(user);
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
			sqlDAO.save(userRole);
			
			baseVO.setBaseVO(BaseVO.SUCCESS, LanguageUtil.show("user_regSuccess"));
			baseVO.setUser(user);
		}else{
			baseVO.setBaseVO(BaseVO.FAILURE, LanguageUtil.show("user_regFailure"));
		}
		
		return baseVO;
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
	
}
