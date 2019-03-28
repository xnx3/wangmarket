package com.xnx3.j2ee.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UploadFileVO;

/**
 * 用户
 * @author 管雷鸣
 *
 */
public interface UserService {
	
	/**
	 * 根据手机号取用户信息。若手机号不存在，返回null
	 * @param phone
	 */
	public User findByPhone(Object phone);

	/**
	 * 用户名＋密码进行登陆
	 * 		<br/>登陆时form表单需提交两个参数：username(用户名/邮箱)、password(密码)
	 * @return {@link BaseVO}
	 */
	public BaseVO loginByUsernameAndPassword(HttpServletRequest request);
	
	/**
	 * 用户名＋密码 进行登陆
	 * @param username 登陆的用户名或邮箱
	 * @param password 登陆的密码，明文密码，原始密码，用户登陆输入的密码
	 * @return {@link BaseVO}
	 */
	public BaseVO loginByUsernameAndPassword(HttpServletRequest request, String username, String password);
	
	
	/**
	 * 手机号＋动态验证码登陆。
	 * 		<br/>登陆时form表单需提交两个参数：phone(手机号)、code(手机收到的动态验证码)
	 * @return {@link BaseVO}
	 */
	public BaseVO loginByPhoneAndCode(HttpServletRequest request);
	
	/**
	 * 传入一个用户id，使当前登陆的用户为此用户。会自动判断当前登陆的ip地址，是否跟此用户最后一次登陆的ip相同。若相同，则可以登陆成功。若不相同，则登陆不成功。
	 * @param userid 要登陆的用户的user.id
	 * @return {@link BaseVO}
	 */
	public BaseVO loginByUserid(HttpServletRequest request,int userid);
	
	/**
	 * 手机号登陆，会自动检测上次登陆的ip，若上次登陆的ip跟当前的ip一样，则这个手机用户登陆成功
	 * @param request {@link HttpServletRequest} 
	 * 		<br/>登陆时form表单需提交一个参数：phone(手机号)
	 * @return {@link BaseVO}
	 */
	public BaseVO loginByPhone(HttpServletRequest request);
	
	/**
	 * 传入一个 {@link User}.id 让此用户变为当前得登陆用户
	 * @param userId 要登陆得 {@link User}.id
	 */
	public BaseVO loginForUserId(HttpServletRequest request,int userId);
	
	/**
	 * 注册页面初始化数据，注册页面填写表单时先调用此，初始化推荐人相关数据
	 * <br/>GET/POST参数：inviteid 推荐人id，user.id。可为空为不传
	 */
	public void regInit(HttpServletRequest request);
	
	/**
	 * 注册
	 * <br/>用户名、密码为必填，其余可不填。会自动先监测用户名、密码是否都有输入。若其中有的没有输入，则拦截返回提示信息。
	 * <br/>自动确认用户名、邮箱、手机号(若填写了)的唯一性，若不是，返回已使用提示信息
	 * <br/><b>用户名、邮箱、密码这三项为必填，通过user传入</b>
	 * @param user {@link User} 
	 * 		<br/>表单的用户名(username)、 密码(password)为必填项
	 * @param request {@link HttpServletRequest} 作用为获取推荐人信息、获取注册用户的ip
	 * @return {@link BaseVO} 
	 * 			<ul>
	 * 				<li>若result为SUCCESS，则info为注册成功的user.id</li>
	 * 				<li>若result为FAILURE，则info为失败的原因</li>
	 * 			</ul>
	 */
	public BaseVO reg(User user ,HttpServletRequest request);
	
	/**
	 * 当前已登录的用户退出登录，注销
	 */
	public void logout();
	
	/**
	 * 冻结用户
	 * @param id 用户id，user.id
	 */
	public BaseVO freezeUser(int id);
	
	/**
	 * 解除冻结用户
	 * @param id 用户id，user.id
	 */
	public BaseVO unfreezeUser(int id);
	
	/**
	 * 利用OSS上传头像
	 * <br/>头像所在OSS的路径为 /image/head/下
	 * <br/>默认的头像名为 default.png，如果用户之前的头像是这个，那么上传新头像后，default.png 默认头像不会被删除，其余的则就会再新的头像上传完毕后，删除原先的头像
	 * @param head {@link MultipartFile}
	 * @return {@link UploadFileVO}
	 */
	public UploadFileVO updateHeadByOSS(MultipartFile head);
	
	/**
	 * 利用OSS上传头像
	 * <br/>头像所在OSS的路径为 /image/head/下
	 * <br/>默认的头像名为 default.png，如果用户之前的头像是这个，那么上传新头像后，default.png 默认头像不会被删除，其余的则就会再新的头像上传完毕后，删除原先的头像
	 * @param request SpringMVC接收的 {@link MultipartFile},若是有上传图片文件，会自动转化为{@link MultipartFile}保存
	 * @param formFileName form表单上传的单个图片文件，表单里上传文件的文件名
	 * @return {@link UploadFileVO}
	 */
	public UploadFileVO updateHeadByOSS(HttpServletRequest request,String formFileName);
	
	/**
	 * 利用OSS上传头像
	 * <br/>头像所在OSS的路径为 /image/head/下
	 * <br/>默认的头像名为 default.png，如果用户之前的头像是这个，那么上传新头像后，default.png 默认头像不会被删除，其余的则就会再新的头像上传完毕后，删除原先的头像
	 * @param request SpringMVC接收的 {@link MultipartFile},若是有上传图片文件，会自动转化为{@link MultipartFile}保存
	 * @param formFileName form表单上传的单个图片文件，表单里上传文件的文件名
	 * @param maxWidth 上传图片的最大宽度，若超过这个宽度，会对图片进行等比缩放为当前宽度。若未0，则不启用缩放功能
	 * @return {@link UploadFileVO}
	 */
	public UploadFileVO updateHeadByOSS(HttpServletRequest request, String formFileName, int maxWidth);
	
	
	/**
	 * 修改性别
	 * @param request GET／POST传入如： sex=男
	 */
	public BaseVO updateSex(HttpServletRequest request);
	
	/**
	 * 修改昵称 ，会自动进行 xss 过滤
	 * @param request GET／POST传入如： nickname=管雷鸣  不允许为空。字符限制1～15个汉字或英文
	 * @return 若result为SUCCESS，则info返回修改成功的昵称
	 */
	public BaseVO updateNickname(HttpServletRequest request);
	
	/**
	 * 修改签名
	 * @param request GET／POST传入如： sign=我是签名  允许为空。字符限制0～40个汉字或英文
	 */
	public BaseVO updateSign(HttpServletRequest request);
	
	/**
	 * 修改密码
	 * @param userid 要修改密码的用户id，user.id
	 * @param newPassword 更改后的新密码
	 */
	public BaseVO updatePassword(int userid, String newPassword);
	
	/**
	 * 创建用户，此接口是为二次开发准备的，可以在后台或者其他地方自由创建新用户
	 * @param user 要创建的新用户的一些信息，注意，传入的值：
	 * 			<ul>
	 * 				<li><b>{@link User}.username 必须有值，不能为空</b></li>
	 * 				<li><b>{@link User}.password 必须有值，不能为空。这里赋予用户登录时的密码，此接口中会自动转化为加密后的密码存储</b></li>
	 * 				<li>{@link User} .id 即使有值也会无效，自动赋予null，以创建新用户</li>				
	 * 				<li>{@link User}.nickname 用户昵称，若是不传值，默认赋予其 {@link User}.username的值</li>
	 * 				<li>{@link User}.regtime、regip 注册时间、注册ip、最后登录时间、最后登录ip，若是不传入值，则默认从request中取当前开通这个用户的ip</li>
	 * 				<li>{@link User}.authority 这个用户的权限。只限传入一个roleid，这里不支持多个roleid的传入！若不传入，默认赋予用户普通会员的权限（系统管理－系统变量中，变量名为USER_REG_ROLE的值）。</li>
	 * 				<li>{@link User}.phone、email 手机号、邮箱，如果填写了，会自动判断其唯一性，是否是唯一的</li>
	 * 			</ul>
	 * @param request {@link HttpServletRequest}主要拿其中的注册ip等信息
	 * @return BaseVO.getResult()  成功|失败， 若成功，getInfo() 获得所创建用户的id，若失败，getInfo() 获得失败原因提示
	 */
	public BaseVO createUser(User user ,HttpServletRequest request);
	
	/**
	 * 获取当前用户的头像，拿到的是绝对路径的网址，如 http://res.weiunity.com/image/imqq.jpg
	 * @param defaultHead 默认头像网址（带http的绝对路径图片网址），如果用户没有头像，或者使用的是默认头像，那么会自动返回这里设置的默认头像网址。如 http://res.weiunity.com/image/imqq.jpg
	 * @return 头像的绝对路径网址，如 http://res.weiunity.com/image/imqq.jpg
	 */
	public String getHead(String defaultHead);
	
	/**
	 * 用明文密码+ salt ，生成 User.password 中， 加密后的密码
	 * @param originalPassword 明文密码
	 * @param salt 盐
	 * @return MD5加密后的密码
	 */
	public String generateMd5Password(String originalPassword, String salt);
}