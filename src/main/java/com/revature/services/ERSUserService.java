package com.revature.services;

import java.util.List;

import com.revature.dao.ERSUserDAO;
import com.revature.dao.ERSUserDAOImpl;
import com.revature.models.ERSUser;

public class ERSUserService 
{
	private ERSUserDAO userDAO = new ERSUserDAOImpl();
	
	public List<ERSUser> getAllUsers()
	{
		return userDAO.getAllUsers();
	}
	
	public ERSUser getUser(int id)
	{
		ERSUser user = userDAO.getUserByID(id);
		return user;
	}
	
	public ERSUser getUser(String username)
	{
		ERSUser user = userDAO.getUserByName(username);
		return user;
	}
	
	
	public boolean addUser(ERSUser user)
	{
		return userDAO.insert(user);
	}
	
	public boolean updateUser(ERSUser user)
	{
		return userDAO.update(user);
	}
	
	public boolean deleteUser(int id)
	{
		ERSUser user = getUser(id);
		if(user == null) return false;
		return userDAO.delete(user);
	}
}
