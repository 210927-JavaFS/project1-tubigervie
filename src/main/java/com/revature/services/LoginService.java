package com.revature.services;

import java.security.NoSuchAlgorithmException;

import com.revature.models.ERSUser;
import com.revature.models.UserDTO;
import com.revature.utils.CryptoUtils;

public class LoginService 
{
	private ERSUserService userService = new ERSUserService();
	
	public boolean login(UserDTO userDTO)
	{
		ERSUser user = userService.getUser(userDTO.username);
		try 
		{
			System.out.println("passed in values: " + userDTO.password);
			byte[] sha = CryptoUtils.getSHA(userDTO.password);
			String encryptedPassword = CryptoUtils.Encrypt(sha);
			return(user != null && (encryptedPassword.equals(user.getPassword())));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Could not encrypt password");
			e.printStackTrace();
			return false;
		}
	}
}
