package com.xnx3.j2ee.service;

import java.util.List;
import com.xnx3.j2ee.entity.FriendLog;

/**
 * 好友纪录
 * @author 管雷鸣
 *
 */
public interface FriendLogService {

	public void save(FriendLog transientInstance);

	public void delete(FriendLog persistentInstance);

	public FriendLog findById(java.lang.Integer id);

	public List<FriendLog> findByExample(FriendLog instance);

	public List findByProperty(String propertyName, Object value);

	public List<FriendLog> findBySelf(Object self);

	public List<FriendLog> findByOther(Object other);

	public List<FriendLog> findByTime(Object time);

	public List<FriendLog> findByState(Object state);

	public List<FriendLog> findByIp(Object ip);

	public List findAll();

	public FriendLog merge(FriendLog detachedInstance);

	public void attachDirty(FriendLog instance);

	public void attachClean(FriendLog instance);

}