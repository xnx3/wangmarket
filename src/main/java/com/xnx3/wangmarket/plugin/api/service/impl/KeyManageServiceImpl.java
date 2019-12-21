package com.xnx3.wangmarket.plugin.api.service.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xnx3.Lang;
import com.xnx3.MD5Util;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.Func;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.vo.UserVO;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.plugin.api.service.KeyManageService;
import com.xnx3.wangmarket.plugin.api.vo.UserBeanVO;
import com.xnx3.wangmarket.agencyadmin.entity.Agency;

@Service("KeyManageService")
public class KeyManageServiceImpl implements KeyManageService {
	/*
	 * 缓存key-user信息。只要验证过的key，都会在这里出现。当在此出现此key时，直接从这里取，不走数据库
	 * key:  唯一识别码key
	 */
	static Map<String, UserBeanVO> keyUserMap = new HashMap<String, UserBeanVO>(); 
	
	@Resource
	private SqlDAO sqlDAO;
	
	public UserBeanVO verify(String key) {
		UserBeanVO vo = new UserBeanVO();
		/*
		 * 验证Key的格式
		 */
		if(key.length() < 128 || key.indexOf("_") == -1){
			vo.setBaseVO(UserVO.FAILURE, "key格式错误1");
			return vo;
		}
		
		String[] ks = key.split("_");
		if(ks[0].length() == 0 || ks[1].length() == 0){
			vo.setBaseVO(UserVO.FAILURE, "key格式错误");
			return vo;
		}
		
		int userid = Lang.stringToInt(ks[0], 0);
		if(userid == 0){
			vo.setBaseVO(UserVO.FAILURE, "key错误3");
			return vo;
		}
		String pwd = StringUtil.removeBlank(ks[1]);
		if(pwd.length() != 128){
			vo.setBaseVO(UserVO.FAILURE, "key位处出错");
			return vo;
		}
		
		/*
		 * 验证Key是否存在
		 * 获取代理商的user信息
		 */
		//先从内存找
		vo = keyUserMap.get(key);
		if(vo != null){
			return vo;
		}
		//userBeanVO为空，那么new一个新的，下面查询数据库，将结果加入 userBeanVO ，加入持久缓存Map
		vo = new UserBeanVO();
		
		//内存中没有，找数据库
		User user = sqlDAO.findById(User.class, userid);
		if(user == null){
			vo.setBaseVO(UserVO.FAILURE, "用户不存在");
			return vo;	
		}
		if(!passwordMD5(user.getPassword()).equals(pwd)){
			vo.setBaseVO(UserVO.FAILURE, "key校验密码错误");
			return vo;
		}
		vo.setUser(user);
		
		/*
		 * 根据 用户的权限，判断取用户的哪方面信息
		 */
		if(Func.isAuthorityBySpecific(user.getAuthority(), SystemUtil.get("ROLE_USER_ID"))){
			//是建站用户，那么取 Site 信息
			Site site = sqlDAO.findAloneBySqlQuery("SELECT * FROM site WHERE userid = "+user.getId(), Site.class);
			if(site == null){
				vo.setBaseVO(UserBeanVO.FAILURE, "站点不存在");
				return vo;	
			}
			vo.setSite(site);
		}else if (Func.isAuthorityBySpecific(user.getAuthority(), SystemUtil.get("AGENCY_ROLE"))) {
			//用户是代理商
			Agency agency = sqlDAO.findAloneBySqlQuery("SELECT * FROM agency WHERE userid = "+user.getId(), Agency.class);
			if(agency == null){
				vo.setBaseVO(UserBeanVO.FAILURE, "代理上信息不存在");
				return vo;	
			}
			vo.setAgency(agency);
		}
		
		
		//到这一步，便是找到了，那么将其key-User 进行缓存，避免下次还从数据库取
		keyUserMap.put(key, vo);
		
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
	
}
