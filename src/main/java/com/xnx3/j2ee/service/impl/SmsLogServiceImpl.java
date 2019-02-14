package com.xnx3.j2ee.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.xnx3.DateUtil;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.entity.SmsLog;
import com.xnx3.j2ee.func.Language;
import com.xnx3.j2ee.func.Safety;
import com.xnx3.j2ee.service.SmsLogService;
import com.xnx3.j2ee.util.IpUtil;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.net.AliyunSMSUtil;
import com.xnx3.net.SMSUtil;

@Service
public class SmsLogServiceImpl implements SmsLogService {
	
	@Resource
	private SqlDAO sqlDAO;
	
	public SqlDAO getSqlDAO() {
		return sqlDAO;
	}

	public void setSqlDAO(SqlDAO sqlDAO) {
		this.sqlDAO = sqlDAO;
	}
	
	public int findByPhoneNum(String phone,Short type) {
		int weeHours = DateUtil.dateToInt10(DateUtil.weeHours(new Date()));
		return sqlDAO.count("sms_log", "WHERE addtime > "+weeHours + " AND phone = '"+Sql.filter(phone)+"' AND type = "+type);
	}


	public int findByIpNum(String ip,Short type) {
		int weeHours = DateUtil.dateToInt10(DateUtil.weeHours(new Date()));
		return sqlDAO.count("sms_log", "WHERE addtime > "+weeHours + " AND ip = '"+Sql.filter(ip)+"' AND type = "+type);
	}
	
	/**
	 * 发送手机号登录的验证码
	 * @param request {@link HttpServletRequest}
	 * 			<br/>form表单需提交参数：phone(发送到的手机号)
	 * @return {@link BaseVO}
	 */
	public BaseVO sendPhoneLoginCode(HttpServletRequest request) {
		String phone = StringUtil.filterXss(request.getParameter("phone"));
		BaseVO baseVO = sendSMS(request, phone, SmsLog.TYPE_LOGIN);
		if(baseVO.getResult() - BaseVO.SUCCESS == 0){
			//发送短信
			String result = SMSUtil.send(phone, Language.show("sms_loginSendCodeText").replaceAll("\\$\\{code\\}", baseVO.getInfo()+""));
			if(result == null){
				baseVO.setBaseVO(BaseVO.SUCCESS, Language.show("sms_codeSendYourPhoneSuccess"));
			}else{
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("sms_saveFailure")+"-"+result);
			}
		}
		return baseVO;
	}

	public BaseVO sendSms(HttpServletRequest request, String phone, String content, Short type) {
		BaseVO baseVO = sendSMS(request, phone, type);
		if(baseVO.getResult() - BaseVO.SUCCESS == 0){
			//发送短信
			String result = SMSUtil.send(phone, content.replaceAll("\\$\\{code\\}", baseVO.getInfo()+""));
			if(result == null){
				baseVO.setBaseVO(BaseVO.SUCCESS, Language.show("sms_codeSendYourPhoneSuccess"));
			}else{
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("sms_saveFailure")+"-"+result);
			}
		}
		return baseVO;
	}

	/**
	 * 通用手机验证码验证方法。输入手机号、动态验证码，验证是否成功
	 * @param phone 目标手机号
	 * @param code 六位数动态验证码
	 * @param type 发送类型，位于 {@link SmsLog}， {@link SmsLog}.type的值
	 * 				<ul>
	 * 					<li>1:{@link SmsLog#TYPE_LOGIN}登录 </li>，若是使用此类型，则后面的overdue过期时间无用
	 * 					<li>2:{@link SmsLog#TYPE_FIND_PASSWORD}找回密码 </li>
	 * 					<li>3:{@link SmsLog#TYPE_BIND_PHONE}绑定手机 </li>
	 * 				</ul>
	 * @param overdue 验证码过期时间，单位为秒。除了type为{@link SmsLog#TYPE_LOGIN}以外都有效
	 * @return {@link BaseVO}
	 */
	public BaseVO verifyPhoneAndCode(String phone, String code, Short type, int overdue) {
		BaseVO baseVO = new BaseVO();
		if(phone==null || phone.length() != 11){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("sms_sendSmsPhoneNumberFailure"));
			return baseVO;
		}
		if(code==null || code.length() != 6){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginByPhoneAndCodeCodeFailure"));
			return baseVO;
		}
		
		int queryAddtime = 0;
		if(type - SmsLog.TYPE_LOGIN ==0){
			//登录，传入的过期时间无效，使用xml中配置的登录的过期时间
			overdue = SmsLog.codeValidity;
		}
		if(overdue > 0){
			int currentTime = DateUtil.timeForUnix10();
			queryAddtime = currentTime-overdue;
		}
		
		SmsLog smsLog = findByPhoneAddtimeUsedTypeCode(phone, queryAddtime, SmsLog.USED_FALSE, type, code);
    	
    	if(smsLog != null){
    		/****更改SmsLog状态*****/
    		smsLog.setUserid(0);
    		smsLog.setUsed(SmsLog.USED_TRUE);
    		sqlDAO.save(smsLog);
    		
			baseVO.setResult(BaseVO.SUCCESS);
			return baseVO;
    	}else{
    		baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginByPhoneAndCodeCodeNotFind"));
    		return baseVO;
    	}
	}
	
	public BaseVO sendByAliyunSMS(HttpServletRequest request, AliyunSMSUtil aliyunSMSUtil, String signName, String templateCode, String phone, Short type) {
		BaseVO baseVO = sendSMS(request, phone, type);
		if(baseVO.getResult() - BaseVO.SUCCESS == 0){
			//发送短信
			baseVO.setBaseVOForSuper(aliyunSMSUtil.send(signName,templateCode,"{\"code\":\""+baseVO.getInfo()+"\"}",""+phone+""));
		}
		return baseVO;
	}
	
	/**
	 * 
	 * @param phone 发送至的手机号
	 * @param content 发送的包含验证码的内容
	 * @param type 发送类型，位于 {@link SmsLog}，以下几个数已使用,可从10以后开始用。此会计入 {@link SmsLog}.type数据字段
	 * 				<ul>
	 * 					<li>1:{@link SmsLog#TYPE_LOGIN}登录 </li>
	 * 					<li>2:{@link SmsLog#TYPE_FIND_PASSWORD}找回密码 </li>
	 * 					<li>3:{@link SmsLog#TYPE_BIND_PHONE}绑定手机 </li>
	 * 				</ul>
	 * @return {@link BaseVO}
	 */
	
	/**
	 * 向指定手机号发送指定内容的验证码，内容里六位动态验证码用${code}表示，这里只是记录跟判断，判断是否发送。最终返回能否发送的结果，具体的发送过程拿到允许发送后再进行发送操作
	 * @param phone 发送至的手机号
	 * @param type 发送类型，位于 {@link SmsLog}，以下几个数已使用,可从10以后开始用。此会计入 {@link SmsLog}.type数据字段
	 * 				<ul>
	 * 					<li>1:{@link SmsLog#TYPE_LOGIN}登录 </li>
	 * 					<li>2:{@link SmsLog#TYPE_FIND_PASSWORD}找回密码 </li>
	 * 					<li>3:{@link SmsLog#TYPE_BIND_PHONE}绑定手机 </li>
	 * 				</ul>
	 * @return {@link BaseVO}
	 * 				<ul>
	 * 					<li>若result = SUCCESS，则info为六位数动态验证码code</li>
	 * 					<li>若result = FAIULRE，则不允许发送短信验证，效验失败，发送达到当日的上限</li>
	 * 				</ul>
	 */
	private BaseVO sendSMS(HttpServletRequest request, String phone, Short type){
		BaseVO baseVO = new BaseVO();
		if(phone==null || phone.length() != 11){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("sms_sendSmsPhoneNumberFailure"));
			return baseVO;
		}else{
			//查询当前手机号是否达到当天发送短信的限额
			if(SmsLog.everyDayPhoneNum > 0){
				int phoneNum = findByPhoneNum(phone, type);
				if(phoneNum<SmsLog.everyDayPhoneNum){
				}else{
					baseVO.setBaseVO(BaseVO.FAILURE, Language.show("sms_thisPhoneNumberDayUpperLimit"));
					return baseVO;
				}
			}
			
			//查询当前ip是否已经达到当天发送限额
			if(SmsLog.everyDayIpNum > 0){
				int ipNum = findByIpNum(IpUtil.getIpAddress(request), type);
				if(ipNum<SmsLog.everyDayIpNum){
				}else{
					baseVO.setBaseVO(BaseVO.FAILURE, Language.show("sms_thisIpDayUpperLimit"));
					return baseVO;
				}
			}
			
			//发送验证码流程逻辑
			Random random = new Random();
			String code = random.nextInt(10)+""+random.nextInt(10)+""+random.nextInt(10)+""+random.nextInt(10)+""+random.nextInt(10)+""+random.nextInt(10);
			
			SmsLog smsLog = new SmsLog();
			smsLog.setAddtime(DateUtil.timeForUnix10());
			smsLog.setCode(code);
			smsLog.setIp(IpUtil.getIpAddress(request));
			smsLog.setPhone(Safety.filter(phone));
			smsLog.setType(type);
			smsLog.setUsed(SmsLog.USED_FALSE);
			smsLog.setUserid(0);
			sqlDAO.save(smsLog);
			
			if(smsLog.getId()>0){
				baseVO.setBaseVO(BaseVO.SUCCESS, code);
				return baseVO;
//					String result = SMSUtil.send(phone, content.replaceAll("\\$\\{code\\}", code+""));
//					if(result == null){
//						baseVO.setBaseVO(BaseVO.SUCCESS, Language.show("sms_codeSendYourPhoneSuccess"));
//					}else{
//						baseVO.setBaseVO(BaseVO.FAILURE, Language.show("sms_saveFailure")+"-"+result);
//					}
			}else{
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("sms_saveFailure"));
				return baseVO;
			}
		}
	}
	

	/**
	 * 根据手机号、是否使用，类型，以及发送时间，查询符合的数据列表，即查询验证码是否存在
	 * @param phone 手机号
	 * @param addtime 添加使用，即发送时间，查询数据的时间大于此时间
	 * @param used 是否使用，如 {@link SmsLog#USED_FALSE}
	 * @param type 短信验证码类型，如 {@link SmsLog#TYPE_LOGIN}
	 * @param code 短信验证码
	 * @return 若查询到验证码存在，返回 {@link SmsLog}，若查询不到，返回null，即验证码不存在
	 */
	public SmsLog findByPhoneAddtimeUsedTypeCode(String phone,int addtime,Short used,Short type,String code){
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("phone", phone);
		parameterMap.put("addtime", addtime);
		parameterMap.put("used", used);
		parameterMap.put("type", type);
		parameterMap.put("code", code);
		List<SmsLog> list = sqlDAO.findByHql("from SmsLog as model where model.phone= :phone and model.addtime > :addtime and model.used = :used and model.type = :type and model.code = :code", parameterMap, 0);
		if(list.size() > 0){
			return list.get(0);
		}
		
		return null;
	}

}
