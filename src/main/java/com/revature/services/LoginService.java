package com.revature.services;

import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.controllers.ERSUserController;
import com.revature.models.ERSUser;
import com.revature.models.UserDTO;
import com.revature.utils.CryptoUtils;

public class LoginService 
{
	private static Logger log = LoggerFactory.getLogger(ERSUserController.class);
	private ERSUserService userService = new ERSUserService();
	
	public boolean login(UserDTO userDTO)
	{
		ERSUser user = userService.getUser(userDTO.username);
		try 
		{
			byte[] sha = CryptoUtils.getSHA(userDTO.password);
			String encryptedPassword = CryptoUtils.Encrypt(sha);
			return(user != null && (encryptedPassword.equals(user.getPassword())));
		} catch (NoSuchAlgorithmException e) {
			log.error("Could not encrypt password");
			log.error(e.toString());
			return false;
		}
	}
}
