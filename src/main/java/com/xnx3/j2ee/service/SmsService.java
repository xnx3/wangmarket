package com.xnx3.j2ee.service;

import javax.servlet.http.HttpServletRequest;
import com.xnx3.j2ee.entity.SmsLog;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.net.AliyunSMSUtil;
import com.xnx3.net.HuaweiSMSUtil;

/**
 * 手机短信
 * @author 管雷鸣
 *
 */
public interface SmsService {
	
	/**
	 * 获取当前条件下的这个手机号，当天信息记录有多少
	 * @param ip 发送者ip
	 * @param type 类型，如{@link SmsLog#TYPE_LOGIN}
	 * @return 记录数
	 */
	public int findByPhoneNum(String phone,Short type);
	
	/**
	 * 获取当前条件下的IP，当天信息记录有多少
	 * @param ip 发送者ip
	 * @param type 类型，如{@link SmsLog#TYPE_LOGIN}
	 * @return 记录数
	 */
	public int findByIpNum(String ip,Short type);
	
	/**
	 * 根据手机号、是否使用，类型，以及发送时间，查询符合的数据列表。即查询验证码是否存在
	 * @param phone 手机号
	 * @param addtime 添加使用，即发送时间，查询数据的时间大于此时间
	 * @param used 是否使用，如 {@link SmsLog#USED_FALSE}
	 * @param type 短信验证码类型，如 {@link SmsLog#TYPE_LOGIN}
	 * @param code 验证码
	 * @return 若查询到验证码存在，返回 {@link SmsLog}，若查询不到，返回null，即验证码不存在
	 */
	public SmsLog findByPhoneAddtimeUsedTypeCode(String phone,int addtime,Short used,Short type,String code);
	
	/**
	 * 发送手机号登录的验证码
	 * @param request {@link HttpServletRequest}
	 * 			<br/>form表单需提交参数：phone(发送到的手机号)
	 * @return {@link BaseVO}
	 */
	public BaseVO sendPhoneLoginCode(HttpServletRequest request);
	
	/**
	 * 向指定手机号发送指定内容的验证码，内容里六位动态验证码用${code}表示
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
	public BaseVO sendSms(HttpServletRequest request, String phone, String content, Short type);
	

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
	public BaseVO verifyPhoneAndCode(String phone, String code, Short type, int overdue);
	
	/**
	 * 使用阿里云短信通道，向指定手机号发送指定内容的验证码。
	 * <br/><b>注意，只支持一个变量，变量名为code，在设置模版的时候变量要用${code}</b>
	 * @param aliyunSMSUtil 项目中自行持久化的 {@link AliyunSMSUtil} 对象，主要拿它里面的 {@link AliyunSMSUtil#AliyunSMSUtil(String, String, String)} 初始化之后的参数信息regionId、accessKeyId、accessKeySecret
	 * @param signName 控制台创建的签名名称（状态必须是验证通过）
	 * 				<br/>&nbsp;&nbsp;&nbsp;&nbsp; https://sms.console.aliyun.com/?spm=#/sms/Sign
	 * @param templateCode 控制台创建的模板CODE（状态必须是验证通过）
	 * 				<br/>&nbsp;&nbsp;&nbsp;&nbsp; https://sms.console.aliyun.com/?spm=#/sms/Template
	 * @param phone 目标手机号，此处只支持单个手机号，若想用多个手机号，自行用 {@link AliyunSMSUtil#send(String, String, String, String)} 
	 * @param type 发送类型，位于 {@link SmsLog}，以下几个数已使用,可从10以后开始用。此会计入 {@link SmsLog}.type数据字段
	 * 				<ul>
	 * 					<li>1:{@link SmsLog#TYPE_LOGIN}登录 </li>
	 * 					<li>2:{@link SmsLog#TYPE_FIND_PASSWORD}找回密码 </li>
	 * 					<li>3:{@link SmsLog#TYPE_BIND_PHONE}绑定手机 </li>
	 * 				</ul>
	 * @return {@link BaseVO}
	 * 			<ul>
	 * 				<li>若result = SUCCESS，则info为六位数动态验证码code</li>
	 * 				<li>若result = FAIULRE，则不允许发送短信验证，效验失败，发送达到当日的上限</li>
	 * 			</ul>
	 */
	public BaseVO sendByAliyunSMS(HttpServletRequest request, AliyunSMSUtil aliyunSMSUtil, String signName,String templateCode, String phone, Short type);
	

	/**
	 * 使用华为云短信通道，向指定手机号发送指定内容的验证码。
	 * <br/><b>注意，只支持一个变量，在设置模版的时候变量要用${1}</b>
	 * @param huaweiSMSUtil 项目中自行持久化的 {@link HuaweiSMSUtil} 对象
	 * @param templateId 发送短信的模版id
	 * @param phone 目标手机号，要发送的手机号。可以传入 +8618788888888 ，也可以不带+86，接口里面会自动加上 
	 * @param type 发送类型，位于 {@link SmsLog}，以下几个数已使用,可从10以后开始用。此会计入 {@link SmsLog}.type数据字段
	 * 				<ul>
	 * 					<li>1:{@link SmsLog#TYPE_LOGIN}登录 </li>
	 * 					<li>2:{@link SmsLog#TYPE_FIND_PASSWORD}找回密码 </li>
	 * 					<li>3:{@link SmsLog#TYPE_BIND_PHONE}绑定手机 </li>
	 * 				</ul>
	 * @return {@link BaseVO}
	 * 			<ul>
	 * 				<li>若result = SUCCESS，则发送成功（华为云短信接口返回的成功，至于真的能不能到手机，还会涉及到手机号是否真的存在、用户开机没，这就不是我们管的了）</li>
	 * 				<li>若result = FAIULRE，则是发送失败，可能是不允许发送短信验证，效验失败，发送达到当日的上限等</li>
	 * 			</ul>
	 */
	public BaseVO sendByHuaweiSMS(HttpServletRequest request, HuaweiSMSUtil huaweiSMSUtil, String templateId, String phone, Short type);

}