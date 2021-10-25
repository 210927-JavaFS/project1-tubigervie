package com.revature.dao;

import java.util.List;

import com.revature.models.ERSUser;

public interface ERSUserDAO 
{
	public List<ERSUser> getAllUsers();
	public ERSUser getUserByID(int id);
	public ERSUser getUserByName(String username);
	public boolean insert(ERSUser user);
	public boolean update(ERSUser user);
	public boolean delete(ERSUser user);
}
