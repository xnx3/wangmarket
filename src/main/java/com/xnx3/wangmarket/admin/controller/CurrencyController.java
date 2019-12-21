package com.xnx3.wangmarket.admin.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.DateUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.net.MailUtil;
import com.xnx3.wangmarket.admin.entity.Exchange;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;

/**
 * 金钱、积分
 * @author 管雷鸣
 * @deprecated
 */
@Controller
@RequestMapping("/currency")
public class CurrencyController extends BaseController {
	@Resource
	private SqlService sqlService;
	@Resource
	private SiteService siteService;
	@Resource
	private UserService userService;

	/**
	 * 积分兑换，首页点击左侧积分兑换功能时，弹出的页面
	 */
	@RequestMapping("index${url.suffix}")
	public String index(HttpServletRequest request,Model model){
		userService.regInit(request);	//注册记录下线
		model.addAttribute("user", getUser());
		
		ActionLogUtil.insert(request, "打开积分兑换首页");
		return "money/index";
	}
	
	/**
	 * 积分获取途径
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("gainChannel${url.suffix}")
	public String gainChannel(HttpServletRequest request,Model model){
		ActionLogUtil.insert(request, "进入查看积分获取途径页面");
		return "money/gainChannel";
	}
	

	/**
	 * 我邀请注册的用户列表，我的下线
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("inviteList${url.suffix}")
	public String exchangeList(HttpServletRequest request,Model model){
		List<User> list = sqlService.findBySqlQuery("SELECT * FROM user WHERE referrerid = "+getUserId()+" ORDER BY id DESC", User.class);
		
		ActionLogUtil.insert(request, "进入查看我邀请的用户下线的页面", "当前我的下线人数："+list.size()+"人");
		model.addAttribute("userList", list);
		model.addAttribute("size", list.size());
		return "money/inviteList";
	}
	
	/**
	 * 用户申请兑换商品
	 * @param inputExchange 用到了用户传入的goodsNo、userRemark
	 */
//	@RequestMapping("apply${url.suffix}")
//	@ResponseBody
//	public BaseVO apply(Exchange inputExchange, HttpServletRequest request,Model model){
//		//判断
//		if(inputExchange.getGoodsid() == null || inputExchange.getGoodsid() == 0){
//			return error("要兑换的商品为空");
//		}
//		Goods goods = sqlService.findById(Goods.class, inputExchange.getGoodsid());
//		if(goods == null){
//			return error("要兑换的商品不存在");
//		}
//		if(getUser().getCurrency() - goods.getMoney() < 0){
//			return error("您当前的"+Global.get("CURRENCY_NAME")+"只有"+getUser().getCurrency()+"，<br/>兑换"+goods.getType()+"需要"+goods.getMoney()+"！");
//		}
//		
//		Exchange exchange = new Exchange();
//		exchange.setAddtime(DateUtil.timeForUnix10());
//		exchange.setGoodsid(inputExchange.getGoodsid());
//		exchange.setSiteid(getSiteId());
//		exchange.setStatus(Exchange.STATUS_APPLY_ING);
//		exchange.setType(goods.getType());
//		exchange.setUserid(getUserId());
//		exchange.setUserRemark(filter(inputExchange.getUserRemark()));
//		
//		sqlService.save(exchange);
//		if(exchange.getId() != null && exchange.getId() > 0){
//			//减去积分
//			try {
//				User user = sqlService.findById(User.class, getUserId());
//				user.setCurrency(user.getCurrency()-goods.getMoney());
//				sqlService.save(user);
//				//更新缓存
//				ShiroFunc.getCurrentActiveUser().setUser(user);
//				
//				//发送邮件提醒
//				MailUtil.sendMail(Global.get("SERVICE_MAIL"), "有人在"+Global.get("SITE_NAME")+"用积分兑换商品了", getUser().getUsername()+"兑换"+goods.getType()+","+goods.getExplain());
//				
//				ActionLogUtil.insertUpdateDatabase(request, exchange.getId(), "用户申请用积分："+goods.getMoney()+"，兑换商品："+goods.getType()+"，申请已提交");
//				return success();
//			} catch (Exception e) {
//			    sqlService.delete(exchange);
//			    ActionLogUtil.insertUpdateDatabase(request, exchange.getId(), "用户申请用积分："+goods.getMoney()+"，兑换商品："+goods.getType()+"，出现异常！申请记录自动删除");
//			    return error("您的积分校验异常，请重新尝试！");
//			}
//		}else{
//			return error("兑换失败，请再次尝试。若多次仍失败，请联系管理员");
//		}
//	}
}
