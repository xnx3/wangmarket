package com.xnx3.superadmin.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.util.IpUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.admin.Func;
import com.xnx3.admin.entity.Site;
import com.xnx3.admin.util.AliyunLog;
import com.xnx3.superadmin.entity.Agency;
import com.xnx3.superadmin.entity.SiteSizeChange;
import com.xnx3.superadmin.service.TransactionalService;
import com.xnx3.superadmin.util.SiteSizeChangeLog;

@Service("transactionalService")
@Transactional
public class TransactionalServiceImpl implements TransactionalService {

	@Resource
	private SqlDAO sqlDAO;

	public BaseVO transferSiteSizeToSubAgency(HttpServletRequest request, int targetAgencyId, int transferSiteSize) {
		BaseVO vo = new BaseVO();
		if(transferSiteSize < 1){
			vo.setBaseVO(BaseVO.FAILURE, "请输入要充值站币的数量！");
			return vo;
		}
		
		Agency shiroMyAgency = com.xnx3.superadmin.Func.getMyAgency();
		
		//我的代理信息
		Agency myAgency = sqlDAO.findById(Agency.class, shiroMyAgency.getId());
		
		if(myAgency.getSiteSize() - transferSiteSize <= 0){
			vo.setBaseVO(BaseVO.FAILURE, "您当前只拥有"+myAgency.getSiteSize()+"站币！给下级充值金额超出，充值失败！");
			AliyunLog.insert(request, myAgency.getId(), "warn", "给下级代理充值站币："+vo.getInfo());
			return vo;
		}
		
		//我的下级代理信息，要给他转账的代理信息
		Agency agency = sqlDAO.findById(Agency.class, targetAgencyId);
		
		if(agency.getParentId() - myAgency.getId() != 0){
			vo.setBaseVO(BaseVO.FAILURE, "要充值的代理不是您的直属下级，无法充值");
			AliyunLog.insert(request, myAgency.getId(), "warn", "给下级代理充值站币："+vo.getInfo());
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
		AliyunLog.addActionLog(agency.getId(), "给下级代理"+agency.getName()+"充值站币："+transferSiteSize);
		
		//发送短信通知对方，待短信模板通过审核
		//G.aliyunSMSUtil.send(G.AliyunSMS_SignName, G.AliyunSMS_agencySiteSizeRecharge_TemplateCode, "{\"chongzhi\":\""+transferSiteSize+"\", \"username\":\""+agency.getName()+"\", \"siteSize\":\""+agency.getSiteSize()+"\"}", agency.getPhone());
		
		//刷新Session中我的代理信息缓存
		Func.getUserBeanForShiroSession().setMyAgency(myAgency);
		
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
		
		Agency shiroMyAgency = com.xnx3.superadmin.Func.getMyAgency();
		
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
			AliyunLog.insert(request, myAgency.getId(), "warn", "给我开通的站点续费："+vo.getInfo());
			return vo;
		}
		
		//判断续费后的网站是否超过了10年 ,当前时间 ＋ 3660天
		if((site.getExpiretime() + (year * 31622400)) > (DateUtil.timeForUnix10() + 316224000)){
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
		AliyunLog.addActionLog(site.getId(), "给网站"+site.getName()+"续费"+year+"年。网站续费后，日期到"+daoqishijian);
		
		//发送短信通知对方
//		G.aliyunSMSUtil.send(G.AliyunSMS_SignName, G.AliyunSMS_siteYanQi_templateCode, "{\"siteName\":\""+site.getName()+"\", \"year\":\""+year+"\", \"time\":\""+daoqishijian+"\"}", site.getPhone());
		
		//刷新Session中我的代理信息缓存
		Func.getUserBeanForShiroSession().setMyAgency(myAgency);
		return vo;
	}
	
	
	
}
