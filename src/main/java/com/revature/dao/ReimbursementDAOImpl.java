package com.revature.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.revature.models.Reimbursement;
import com.revature.models.Reimbursement.ReimburseStatus;
import com.revature.utils.CollectionUtil;
import com.revature.utils.HibernateUtil;

public class ReimbursementDAOImpl implements ReimbursementDAO
{

	@Override
	public List<Reimbursement> getAllReimbursements() {
		try {
			Session session = HibernateUtil.getSession();
			List<Reimbursement> reimbursements = CollectionUtil.castList(Reimbursement.class, session.createQuery("FROM Reimbursement").list());
			return reimbursements;
		}
		catch(NoResultException e)
		{
			return null;
		}
	}
	
	@Override
	public List<Reimbursement> getAllReimbursementsFromUserID(int id) {
		try {
			Session session = HibernateUtil.getSession();
			Query query = session.createQuery("FROM Reimbursement WHERE author_id = :author");
			query.setParameter("author", id);
			List<Reimbursement> reimbursements = CollectionUtil.castList(Reimbursement.class, query.getResultList());
			return reimbursements;
		}
		catch(NoResultException e)
		{
			return null;
		}
	}

	@Override
	public Reimbursement getReimbursementByID(int id) {
		try {
			Session session = HibernateUtil.getSession();
			return session.get(Reimbursement.class, id);
		}catch(NoResultException e)
		{
			return null;
		}
	}

	@Override
	public boolean insert(Reimbursement reimbursement) {
		try {
			Session session = HibernateUtil.getSession();
			Transaction tx = session.beginTransaction();
			session.save(reimbursement);
			tx.commit();
			HibernateUtil.closeSession();
			return true;
		} catch (HibernateException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(Reimbursement reimbursement) {
		try {
			Session session = HibernateUtil.getSession();
			Transaction tx = session.beginTransaction();
			session.merge(reimbursement);
			tx.commit();
			HibernateUtil.closeSession();
			return true;
		} catch (HibernateException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(Reimbursement reimbursement) {
		try {
			Session session = HibernateUtil.getSession();
			Transaction tx = session.beginTransaction();
			session.delete(reimbursement);
			tx.commit();
			HibernateUtil.closeSession();
			return true;
		} catch (HibernateException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Reimbursement> getReimbursementsByStatus(ReimburseStatus status) {
		try {
			Session session = HibernateUtil.getSession();
			Query query = session.createQuery("FROM Reimbursement WHERE status = :status");
			query.setParameter("status", status);
			List<Reimbursement> reimbursements = CollectionUtil.castList(Reimbursement.class, query.getResultList());
			return reimbursements;
		}catch(NoResultException e)
		{
			return null;
		}
	}

	@Override
	public List<Reimbursement> getAllPastReimbursements() {
		try {
			Session session = HibernateUtil.getSession();
			Query query = session.createQuery("FROM Reimbursement WHERE status != :status");
			query.setParameter("status", ReimburseStatus.Pending);
			List<Reimbursement> reimbursements = CollectionUtil.castList(Reimbursement.class, query.getResultList());
			return reimbursements;
		}
		catch(NoResultException e)
		{
			return null;
		}
	}

}
