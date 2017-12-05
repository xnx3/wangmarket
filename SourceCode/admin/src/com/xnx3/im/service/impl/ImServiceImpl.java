package com.xnx3.im.service.impl;

import java.util.List;
import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import com.xnx3.im.Global;
import com.xnx3.im.entity.Im;
import com.xnx3.im.service.ImService;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.admin.Func;

@Service("ImService")
public class ImServiceImpl implements ImService {

	@Resource
	private SqlDAO sqlDAO;

	@Override
	public Im getImByCache() {
		Im im = Func.getUserBeanForShiroSession().getSiteIm();
		if(im == null){
			//如果im为空，则从数据库找找
			List<Im> list = sqlDAO.findByProperty(Im.class, "userid" , ShiroFunc.getUserId());
			if(list.size() == 0){
				//不存在，创建一个空的
				im = new Im();
				im.setAutoReply("");
				im.setUseKefu(Im.USE_FALSE);
				im.setUseOffLineEmail(Im.USE_FALSE);
			}else{
				im = list.get(0);
			}
			//将查出来的进行缓存
			Func.getUserBeanForShiroSession().setSiteIm(im);
		}
		
		return im;
	}

	@Override
	public void updateImForCache(Im im) {
		Func.getUserBeanForShiroSession().setSiteIm(im);
	}

	@Override
	public void updateIMServer(boolean haveImSet, Im im) {
		if(im == null){
			im = new Im();
		}
		
		JSONObject json = new JSONObject();
		json.put("autoReply", im.getAutoReply() == null ? "":im.getAutoReply());
		json.put("email", im.getEmail() == null ? "":im.getEmail());
		json.put("haveImSet", haveImSet);
		json.put("useOffLineEmail", im.getUseOffLineEmail() == null ? Im.USE_FALSE : im.getUseOffLineEmail()-Im.USE_TRUE==0);
		json.put("userid", im.getUserid());
		
		Global.kefuMNSUtil.putMessage(Global.kefuMNSUtil_queueName, json.toString());
	}

	
	/**
	 * 取得当前网站的 Im 对象。从数据库中取。若数据库中没有，用户还没设置过，那么自动new一个出来
	 */
	public Im getImByDB(){
		User user = ShiroFunc.getUser();
		List<Im> list = sqlDAO.findByProperty(Im.class, "userid", user.getId());
		Im si;
		if(list.size() > 0){
			si = list.get(0);
		}else{
			si = new Im();
			si.setUserid(user.getId());
			si.setUseOffLineEmail(Im.USE_FALSE);
			si.setUseKefu(Im.USE_FALSE);
		}
		return si;
	}
}
