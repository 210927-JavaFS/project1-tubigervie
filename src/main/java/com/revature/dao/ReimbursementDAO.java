package com.revature.dao;

import java.util.List;

import com.revature.models.Reimbursement;

public interface ReimbursementDAO {
	public List<Reimbursement> getAllReimbursements();
	public List<Reimbursement> getAllReimbursementsFromUserID(int id);
	public Reimbursement getReimbursementByID(int id);
	public boolean insert(Reimbursement reimbursement);
	public boolean update(Reimbursement reimbursement);
	public boolean delete(Reimbursement reimbursement);
}
