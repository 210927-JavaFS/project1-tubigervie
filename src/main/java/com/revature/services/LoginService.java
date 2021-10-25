package com.revature.services;

import com.revature.models.ERSUser;
import com.revature.models.UserDTO;

public class LoginService 
{
	private ERSUserService userService = new ERSUserService();
	
	public boolean login(UserDTO userDTO)
	{
		ERSUser user = userService.getUser(userDTO.username);
		return(user != null && (userDTO.password.hashCode() == user.getPassword().hashCode()));
	}
}
