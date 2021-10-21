package com.revature.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.revature.models.ERSUser;
import com.revature.utils.HibernateUtil;

public class ERSUserDAOImpl implements ERSUserDAO
{

	@Override
	public List<ERSUser> getAllUsers() {
		Session session = HibernateUtil.getSession();
		List<ERSUser> users;
		CriteriaBuilder critBuilder = session.getCriteriaBuilder();
		CriteriaQuery<ERSUser> query = critBuilder.createQuery(ERSUser.class);
		Root<ERSUser> root = query.from(ERSUser.class);
		CriteriaQuery<ERSUser> allUsers = query.select(root);
		TypedQuery<ERSUser> typed = session.createQuery(allUsers);
		
		users = typed.getResultList();
		return users;
	}

	@Override
	public ERSUser getUserByID(int id) {
		Session session = HibernateUtil.getSession();
		return session.get(ERSUser.class, id);
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
			return false;
		}
	}

	
}
