package com.xnx3.j2ee.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.dao.MessageDAO;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.entity.Message;
import com.xnx3.j2ee.entity.MessageData;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.func.Language;
import com.xnx3.j2ee.func.Safety;
import com.xnx3.j2ee.service.MessageService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.MessageVO;
@Service
public class MessageServiceImpl implements MessageService {

	@Resource
	private MessageDAO messageDao;
	@Resource
	private SqlDAO sqlDAO;

	public SqlDAO getSqlDAO() {
		return sqlDAO;
	}

	public void setSqlDAO(SqlDAO sqlDAO) {
		this.sqlDAO = sqlDAO;
	}
	
	public MessageDAO getMessageDao() {
		return messageDao;
	}

	public void setMessageDao(MessageDAO messageDao) {
		this.messageDao = messageDao;
	}

	public BaseVO delete(int id) {
		BaseVO baseVO = new BaseVO();
		if(id>0){
			Message m =sqlDAO.findById(Message.class, id);
			if(m!=null){
				m.setIsdelete(Message.ISDELETE_DELETE);
				sqlDAO.save(m);
//				logDao.insert(m.getId(), "MESSAGE_DELETE");
			}else{
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("message_notFind"));
			}
		}else{
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("message_idFailure"));
		}
		return baseVO;
	}

	public BaseVO sendMessage(HttpServletRequest request) {
		BaseVO baseVO = new BaseVO();
		int recipientid = Lang.stringToInt(request.getParameter("recipientid"), 0);
		String content = request.getParameter("content");
		
		return sendMessage(recipientid, content);
	}

	public MessageVO read(int id) {
		MessageVO messageVO = new MessageVO();
		if(id>0){
			Message message = sqlDAO.findById(Message.class, id);
			if(message!=null){
				int userId = ShiroFunc.getUser().getId();
				//查看此信息是此人发的，或者是发送给此人的，此人有权限查看
				if(userId - message.getRecipientid() == 0 || userId - message.getSenderid() == 0){
					//检测此信息是否已被删除
					if(message.getIsdelete() - Message.ISDELETE_DELETE == 0){
						messageVO.setBaseVO(MessageVO.FAILURE, Language.show("message_alreadyDelete"));
					}else{
						//拿到信息的内容
						MessageData messageData = sqlDAO.findById(MessageData.class, id);
						messageVO.setContent(messageData.getContent());
						
						//如果阅读的人是收信人，且之前没有阅读过，则标注此信息为已阅读
						if(userId - message.getRecipientid() == 0 && message.getState() - Message.MESSAGE_STATE_UNREAD == 0){
							message.setState(Message.MESSAGE_STATE_READ);
							sqlDAO.save(message);
//							logDao.insert(message.getId(), "MESSAGE_READ", messageData.getContent());
						}
						messageVO.setMessage(message);
						
						//拿到发件人信息
						User senderUser = sqlDAO.findById(User.class, message.getSenderid());
						
						//检验目标用户状态是否正常，是否被冻结
						if(senderUser.getIsfreeze() - User.ISFREEZE_FREEZE == 0){
							messageVO.setBaseVO(BaseVO.FAILURE, Language.show("message_senderUserFreeze"));
							return messageVO;
						}
						messageVO.setSenderUser(senderUser);
						
						//拿到收件人信息
						User recipientUser = sqlDAO.findById(User.class, message.getRecipientid());
						messageVO.setRecipientUser(recipientUser);
					}
				}else{
					messageVO.setBaseVO(MessageVO.FAILURE, Language.show("message_notRoleRead"));
				}
			}else{
				messageVO.setBaseVO(BaseVO.FAILURE, Language.show("message_notFind"));
			}
		}else{
			messageVO.setBaseVO(BaseVO.FAILURE, Language.show("message_idFailure"));
		}
		
		return messageVO;
	}

	public BaseVO sendMessage(int recipientid, String content) {
		BaseVO baseVO = new BaseVO();
		User user = ShiroFunc.getUser();
		if(user == null){
			baseVO.setBaseVO(BaseVO.FAILURE, "请先登录");
			return baseVO;
		}
		return sendMessage(user.getId(), recipientid, content);
	}
	
	public BaseVO sendMessage(int userid, int recipientid, String content){
		BaseVO baseVO = new BaseVO();
		if(recipientid<1){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("message_unknowRecipient"));
			return baseVO;
		}
		
		if(recipientid == ShiroFunc.getUser().getId()){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("message_notSendOneself"));
			return baseVO;
		}
		
		if(content == null){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("message_pleaseInputText"));
		}else if(content.length()>Global.MESSAGE_CONTENT_MINLENGTH&&content.length()<Global.MESSAGE_CONTENT_MAXLENGTH) {
			//正常
			
			//拿到收信人信息
			User recipiendUser = sqlDAO.findById(User.class, recipientid);
			if(recipiendUser == null){
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("message_sendRecipientNotFind"));
				return baseVO;
			}
			//检验目标用户状态是否正常，是否被冻结
			if(recipiendUser.getIsfreeze() == User.ISFREEZE_FREEZE){
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("message_sendRecipientUserFreeze"));
				return baseVO;
			}
			
			Message message = new Message();
			message.setSenderid(ShiroFunc.getUser().getId());
			message.setRecipientid(recipientid);
			message.setTime(DateUtil.timeForUnix10());
			message.setState(Message.MESSAGE_STATE_UNREAD);
			message.setIsdelete(Message.ISDELETE_NORMAL);
			sqlDAO.save(message);
			baseVO.setBaseVO(BaseVO.SUCCESS, message.getId()+"");
			
			MessageData messageData = new MessageData();
			messageData.setId(message.getId());
			messageData.setContent(Safety.filter(content));
			sqlDAO.save(messageData);
			
			if(messageData.getId()==0){
				baseVO.setBaseVO(BaseVO.FAILURE,Language.show("message_saveFailure"));
			}else{
				//日志记录， MESSAGE_SEND
//				logDao.insert(message.getId(), "MESSAGE_SEND",content);
			}
		}else{
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("message_sizeFailure").replaceAll("\\$\\{min\\}", Global.MESSAGE_CONTENT_MINLENGTH+"").replaceAll("\\$\\{max\\}", Global.MESSAGE_CONTENT_MAXLENGTH+""));
		}
		
		return baseVO;
	}

	public BaseVO sendSystemMessage(int recipientid, String content) {
		return sendMessage(0, recipientid, content);
	}
}
