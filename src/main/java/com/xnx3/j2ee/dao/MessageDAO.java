package com.xnx3.j2ee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import static org.hibernate.criterion.Example.create;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xnx3.DateUtil;
import com.xnx3.j2ee.entity.Message;

/**
 * A data access object (DAO) providing persistence and search support for
 * Message entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.xnx3.j2ee.entity.Message
 * @author MyEclipse Persistence Tools
 */
@Transactional
@Repository
public class MessageDAO {
	private static final Logger log = LoggerFactory.getLogger(MessageDAO.class);
	// property constants
	public static final String SELF = "self";
	public static final String OTHER = "other";
	public static final String TIME = "time";
	public static final String STATE = "state";

    @PersistenceContext
    EntityManager entityManager;  

	public Session getCurrentSession() {
		return entityManager.unwrap(org.hibernate.Session.class);
	}

	protected void initDao() {
		// do nothing
	}

	public void save(Message transientInstance) {
		log.debug("saving Message instance");
		try {
			getCurrentSession().saveOrUpdate(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Message persistentInstance) {
		log.debug("deleting Message instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Message findById(java.lang.Integer id) {
		log.debug("getting Message instance with id: " + id);
		try {
			Message instance = (Message) getCurrentSession().get(
					"com.xnx3.j2ee.entity.Message", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Message> findByExample(Message instance) {
		log.debug("finding Message instance by example");
		try {
			List<Message> results = (List<Message>) getCurrentSession()
					.createCriteria("com.xnx3.j2ee.entity.Message")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Message instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Message as model where model."
					+ propertyName + "= ?0";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter("0", value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Message> findBySelf(Object self) {
		return findByProperty(SELF, self);
	}

	public List<Message> findByOther(Object other) {
		return findByProperty(OTHER, other);
	}

	public List<Message> findByTime(Object time) {
		return findByProperty(TIME, time);
	}

	public List<Message> findByState(Object state) {
		return findByProperty(STATE, state);
	}

	public List findAll() {
		log.debug("finding all Message instances");
		try {
			String queryString = "from Message";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Message merge(Message detachedInstance) {
		log.debug("merging Message instance");
		try {
			Message result = (Message) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Message instance) {
		log.debug("attaching dirty Message instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Message instance) {
		log.debug("attaching clean Message instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static MessageDAO getFromApplicationContext(ApplicationContext ctx) {
		return (MessageDAO) ctx.getBean("MessageDAO");
	}
	
	/**
	 * 搜索所有Message信息列表
	 * @return
	 */
	public List findAllAndData(){
		try {
			String queryString = "SELECT message.*,message_data.content, (SELECT user.nickname FROM user WHERE user.id=message.other) AS other_nickname ,(SELECT user.nickname FROM user WHERE user.id=message.self) AS self_nickname FROM message ,message_data ,user WHERE message.id=message_data.id GROUP BY message.id ORDER BY message.id DESC ";
			Query queryObject = getCurrentSession().createSQLQuery(queryString).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
}