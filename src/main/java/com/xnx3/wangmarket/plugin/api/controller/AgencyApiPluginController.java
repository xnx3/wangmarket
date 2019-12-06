package com.xnx3.wangmarket.plugin.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.service.ApiService;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UserVO;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.plugin.api.service.KeyManageService;
import com.xnx3.wangmarket.plugin.api.vo.UserBeanVO;
import com.xnx3.wangmarket.agencyadmin.entity.Agency;
import com.xnx3.wangmarket.agencyadmin.service.TransactionalService;

/**
 * Api接口相关
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/plugin/api/")
public class AgencyApiPluginController extends com.xnx3.wangmarket.admin.controller.BaseController {
	@Resource
	private ApiService apiService;
	@Resource
	private SqlService sqlService;
	@Resource
	private TransactionalService transactionalService;
	@Resource
	private KeyManageService keyManageService;

	/**
	 * 代理创建网站接口
	 * <br/> http://api.wscso.com/2777.html
	 */
	@RequestMapping(value="agencyCreateSite${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO agencyCreateSite(HttpServletRequest request, Model model,
			@RequestParam(value = "key", required = false , defaultValue="") String key,
			@RequestParam(value = "username", required = false , defaultValue="") String username,
			@RequestParam(value = "password", required = false , defaultValue="") String password){
		//key安全校验
		UserBeanVO vo = keyManageService.verify(key);
		if(vo.getResult() - UserVO.FAILURE == 0){
			return error(vo.getInfo());
		}
		
		Agency agency = sqlService.findById(Agency.class, vo.getAgency().getId());
		
		//要创建得网站得user
		User user = new User();
		user.setReferrerid(vo.getUser().getId());
		user.setUsername(username);
		user.setPassword(password);
		
		Site site = new Site();
		site.setName("站点名字");
		site.setClient(Site.CLIENT_CMS);
		
		return transactionalService.agencyCreateSite(request, agency, user, site, "");
	}
	

}
