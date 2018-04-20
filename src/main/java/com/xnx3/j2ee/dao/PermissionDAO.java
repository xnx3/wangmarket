package com.xnx3.j2ee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import static org.hibernate.criterion.Example.create;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xnx3.j2ee.entity.Permission;

/**
 * A data access object (DAO) providing persistence and search support for User
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see com.xnx3.j2ee.entity.User
 * @author MyEclipse Persistence Tools
 */
@Transactional
@Repository
public class PermissionDAO {
	private static final Logger log = LoggerFactory.getLogger(PermissionDAO.class);
	// property constants
	public static final String ID = "id";

    @PersistenceContext
    EntityManager entityManager;  

	public Session getCurrentSession() {
		return entityManager.unwrap(org.hibernate.Session.class);
	}

	protected void initDao() {
		// do nothing
	}

	public void save(Permission transientInstance) {
		log.debug("saving Permission instance");
		try {
			getCurrentSession().saveOrUpdate(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Permission persistentInstance) {
		log.debug("deleting Permission instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Permission findById(java.lang.Integer id) {
		log.debug("getting Permission instance with id: " + id);
		try {
			Permission instance = (Permission) getCurrentSession().get(
					"com.xnx3.j2ee.entity.Permission", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}


	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Permission instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Permission as model where model."
					+ propertyName + "= ?0";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter("0", value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	public List<Permission> findByExample(Permission instance) {
		log.debug("finding User instance by example");
		try {
			List<Permission> results = (List<Permission>) getCurrentSession()
					.createCriteria("com.xnx3.j2ee.entity.Permission")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}


	public List findAll() {
		log.debug("finding all Permission instances");
		try {
			String queryString = "from Permission";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}


	/**
	 * 根据user.id查此用户拥有的permission
	 * @param userid 用户id
	 * @return
	 */
	public List getPermissionByUserId(int userid){
		try {
			String queryString = "SELECT permission.* FROM permission,role_permission WHERE permission.id = role_permission.permissionid  AND role_permission.roleid IN ( SELECT user_role.roleid  FROM user_role WHERE user_role.userid = ? )";
			Query queryObject = getCurrentSession().createSQLQuery(queryString).addEntity(Permission.class);
			queryObject.setParameter(0, userid);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public static PermissionDAO getFromApplicationContext(ApplicationContext ctx) {
		return (PermissionDAO) ctx.getBean("PermissionDAO");
	}
}