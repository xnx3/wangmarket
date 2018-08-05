package com.xnx3.wangmarket.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UserVO;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.vo.NewsVO;
import com.xnx3.wangmarket.admin.vo.bean.NewsInit;
import com.xnx3.wangmarket.api.vo.UserBeanVO;

/**
 * Key的管理
 * @author 管雷鸣
 */
public interface KeyManageService {
	
	/**
	 * 传入 key ，来判断是否存在，key是否正确
	 * @param key 有用户的id编号+加密密码串构成。当修改密码时，key也会变动。 详情见 http://api.wscso.com/2779.html
	 * @return {@link UserVO} 需首先判断 result 参数：
	 * 			<ul>
	 * 				<li>0 (UserVO.FAILURE) ：失败。 同时用 {@link UserVO#getInfo()} 获取失败原因 </li>
	 * 				<li>1 (UserVO.SUCCESS) : 成功。只有成功时，才有 {@link UserBeanVO} 这些信息 </li>
	 * 			</ul>
	 */
	public UserBeanVO verify(String key);
	
}
