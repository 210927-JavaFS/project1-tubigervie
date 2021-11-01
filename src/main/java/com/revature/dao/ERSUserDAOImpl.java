package com.revature.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.models.ERSUser;
import com.revature.utils.CollectionUtil;
import com.revature.utils.HibernateUtil;

public class ERSUserDAOImpl implements ERSUserDAO
{
	private static Logger log = LoggerFactory.getLogger(ERSUserDAOImpl.class);

	@Override
	public List<ERSUser> getAllUsers() {
		Session session = HibernateUtil.getSession();
		List<ERSUser> users = CollectionUtil.castList(ERSUser.class, session.createQuery("FROM ERSUser").list());
		return users;
	}

	@Override
	public ERSUser getUserByID(int id) {
		try {
			Session session = HibernateUtil.getSession();
			return session.get(ERSUser.class, id);	
		} catch(HibernateException e)
		{
			e.printStackTrace();
			return null;
		}
		catch(NoResultException e)
		{
			log.error(e.toString());
			return null;
		}
	}

	@Override
	public boolean insert(ERSUser user) {
		try {
			Session session = HibernateUtil.getSession();
			Transaction tx = session.beginTransaction();
			session.save(user);
			tx.commit();
			HibernateUtil.closeSession();
			return true;
		} catch (HibernateException e) {
			log.error(e.toString());
			return false;
		}	
	}

	@Override
	public boolean update(ERSUser user) {
		try {
			Session session = HibernateUtil.getSession();
			Transaction tx = session.beginTransaction();
			session.merge(user);
			tx.commit();
			HibernateUtil.closeSession();
			return true;
		} catch (HibernateException e) {
			log.error(e.toString());
			return false;
		}	
	}

	@Override
	public boolean delete(ERSUser user) {
		try {
			Session session = HibernateUtil.getSession();
			Transaction tx = session.beginTransaction();
			session.delete(user);
			tx.commit();
			HibernateUtil.closeSession();
			return true;
		} catch (HibernateException e) {
			log.error(e.toString());
			return false;
		}
	}

	@Override
	public ERSUser getUserByName(String username) {
		try {
			Session session = HibernateUtil.getSession();
			Query query = session.createQuery("FROM ERSUser WHERE username = :user_name");
			query.setParameter("user_name", username);
			return (ERSUser) query.getSingleResult();
		}catch(HibernateException e)
		{
			log.error(e.toString());
			return null;
		}
		catch(NoResultException e)
		{
			log.error(e.toString());
			return null;
		}
	}

	
}
