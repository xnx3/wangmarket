package com.xnx3.j2ee.func;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.aliyuncs.green.model.v20160621.TextKeywordFilterResponse;
import com.xnx3.ConfigManagerUtil;
import com.xnx3.Lang;
import com.xnx3.j2ee.Global;
import com.xnx3.net.MailUtil;
import com.xnx3.net.TxtFilterUtil;

/**
 * 文本过滤，检测文本是否是合法的
 * @author 管雷鸣
 */
public class TextFilter {
	
	static TxtFilterUtil txtFilterUtil = null;
	static String receiveEmail = "";
	
	static{
		receiveEmail = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("aliyunTextFilter.receiveEmail");
		String use = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("aliyunTextFilter.use");
		String regionId = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("aliyunTextFilter.regionId");
		String accessKeyId = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("aliyunTextFilter.accessKeyId");
		String accessKeySecret = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("aliyunTextFilter.accessKeySecret");
		if(use != null && use.equals("true")){
			if(accessKeyId == null || accessKeyId.length() == 0){
				//取数据库的
				accessKeyId = Global.get("ALIYUN_ACCESSKEYID");
			}
			if(accessKeySecret == null || accessKeySecret.length() == 0){
				//取数据库的
				accessKeySecret = Global.get("ALIYUN_ACCESSKEYSECRET");
			}
			
			if(accessKeyId.length() > 10){
				txtFilterUtil = new TxtFilterUtil(regionId, accessKeyId, accessKeySecret);
				System.out.println("开启文本合法性过滤");
			}else{
				//此处可能是还没执行install安装
				use = "false";
			}
		}
		
		if(use == null || !use.equals("true")){
			//不使用文本安全性自动过滤服务，txtFilterUtil＝null即可
			System.out.println("未开启对用户输入的违规文本自动识别过滤服务。若想开启，可参考网址 http://www.guanleiming.com/2357.html");
		}
	}
	
	/**
	 * 进行字符串文本过滤，判断是否有违法字符及文本
	 * @param request HttpServletRequest
	 * @param title 此仅仅只是发现违规后通知您时，给你备注的。方便你看，知道是哪出的问题，此外无其他任何作用。
	 * @param url 此仅仅只是发现违规后通知您时，邮件中会包含此网址。比如您可以点击此网址就能看到这个违法文章进行查看或者审核。只是方便管理者审核使用，此外无其他任何作用。
	 * @param text 要检测的字符文本
	 * @return 取值：
	 * 			<ul>
	 * 				<li>true:不合法，发现有违规存在</li>
	 * 				<li>false:合法，没有检测出什么问题。文本正常</li>
	 * 			</ul>
	 */
	public static boolean filter(HttpServletRequest request, String title, String url, String text) {
		if(txtFilterUtil == null){
			//不使用文本过滤服务，那么进行识别的肯定就全部都是合法的了
			return false;
		}
		if(text == null || text.length() == 0){
			return false;
		}
		List<TextKeywordFilterResponse.KeywordResult> list = txtFilterUtil.filterGainList(text);
		if(list.size() > 0){
			//发现了违规，那么读取此用户发违规的条数，此次登陆Session中，记录了几次违规记录
			Object textIllegalNumber = request.getSession().getAttribute("textIllegalNumber");
			int IllegalNumber = 0;
			if(textIllegalNumber != null){
				IllegalNumber = Lang.stringToInt(textIllegalNumber.toString(), 0);
			}
			IllegalNumber++;		//增加一次纪律记录
			request.getSession().setAttribute("textIllegalNumber", IllegalNumber);
			
			//如果是第一次或者第五次，则发送邮件记录
			if(IllegalNumber - 1 == 0 || IllegalNumber - 5 == 0){
				int length = list.size();
				String IllegalText = "";
				for (int i = 0; i < length; i++) {
					TextKeywordFilterResponse.KeywordResult kr = list.get(i);
					IllegalText += MailUtil.BR + (i+1) +"."+kr.getKeywordCtx();
				}
				
				String sendContent = "";	//发送邮件的内容
				if(IllegalNumber - 5 == 0){
					sendContent = "警告，当前此用户已发现五次疑似违规文本记录！"+MailUtil.BR+MailUtil.BR;
				}
				sendContent = sendContent+"疑似违规内容如下："+IllegalText+MailUtil.BR+url;
				
				try{
					MailUtil.sendMail(receiveEmail, title, sendContent);
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			return true;
		}
		return false;
	}

	
}
