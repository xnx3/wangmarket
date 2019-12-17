package com.xnx3.j2ee.service;

import javax.servlet.http.HttpServletRequest;
import com.xnx3.j2ee.entity.Message;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.MessageVO;

/**
 * 站内信
 * @author 管雷鸣
 *
 */
public interface MessageService {
	/**
	 * 删除信息，逻辑删除，改变isdelete=1
	 * @param id 要删除信息的message.id
	 * @return {@link BaseVO}
	 */
	public BaseVO delete(int id);
	
	/**
	 * 发送信息
	 * @param request {@link HttpServletRequest}
	 * 		<br/>form表单进行发送信息提交时，需传递两个参数：recipientid(接收者用户id)，content(信息内容)
	 * @return {@link BaseVO} 若成功，info为新增消息的id编号
	 */
	public BaseVO sendMessage(HttpServletRequest request);
	
	/**
	 * 发送信息，发送者就是当前登陆的用户
	 * @param recipientid 接收者用户id
	 * @param content 信息内容
	 * @return {@link BaseVO} 若成功，info为新增消息的id编号
	 */
	public BaseVO sendMessage(int recipientid, String content);
	
	/**
	 * 发送信息
	 * @param userid 发信者的userid
	 * @param recipientid 接收者用户id
	 * @param content 信息内容
	 * @return {@link BaseVO} 若成功，info为新增消息的id编号
	 */
	public BaseVO sendMessage(int userid, int recipientid, String content);
	
	/**
	 * 向某人发送一条系统信息（发件人用户id为0）
	 * @param recipientid 接收者用户id
	 * @param content 信息内容
	 * @return {@link BaseVO} 若成功，info为新增消息的id编号
	 */
	public BaseVO sendSystemMessage(int recipientid, String content);
	
	/**
	 * 阅读信息，通过 {@link Message}.id获取此条站内信的具体信息，包含 {@link Message}、 {@link MessageData}
	 * 		<ul>
	 * 			<li>若收信者阅读，并且此信息的状态还是未阅读状态时，则将信息标记为已阅读状态</li>
	 * 			<li>若发信者自己阅读，不做任何处理，单纯只是阅读</li>
	 * 		</ul>
	 * @param id {@link Message}.id
	 * @return {@link MessageVO}
	 * 		<br/>首先判断getResult()是否是 {@link BaseVO#SUCCESS}，若是，才可以调取其他的值。若不是，可通过getInfo()获取错误信息
	 */
	public MessageVO read(int id);
}