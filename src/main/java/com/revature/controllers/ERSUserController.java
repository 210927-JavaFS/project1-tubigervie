package com.revature.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.models.ERSUser;
import com.revature.models.UserDTO;
import com.revature.models.ERSUser.UserRole;
import com.revature.services.ERSUserService;
import com.revature.services.LoginService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class ERSUserController implements Controller
{
	private static Logger log = LoggerFactory.getLogger(ERSUserController.class);
	private LoginService loginService = new LoginService();
	private ERSUserService userService = new ERSUserService();
	
	private Handler loginAttempt = (ctx) ->
	{
		UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
		log.info(String.format("Attempting to login for username: %s.", userDTO.username));
		if(loginService.login(userDTO))
		{
			ERSUser user = userService.getUser(userDTO.username);
			ctx.req.getSession();
			ctx.sessionAttribute("userid", user.getID());
			ctx.json(user);
			ctx.status(200);                                                                  
		}
		else
		{
			log.info(String.format("Login attempt for username: %s has failed. Invalidating session", userDTO.username));
			ctx.req.getSession().invalidate();
			ctx.status(401);
		}
	};
	
	private Handler getUserByID = (ctx) ->
	{
		if(ctx.req.getSession(false) != null)
		{
			int userID = ctx.sessionAttribute("userid");
			ERSUser sessionUser = userService.getUser(userID);
			int id = Integer.valueOf(ctx.pathParam("userid"));
			if(sessionUser.getUserRole() == UserRole.EMPLOYEE && userID != id) 
			{
				log.error(String.format("User (ID: %d) attempted to get request user (ID: %d) without authorized access.", userID, id));
				ctx.status(401);
				return;
			}
			ERSUser queriedUser = userService.getUser(id);
			if(queriedUser == null)
			{
				log.error(String.format("A user of ID: %d does not exist.", id));
				ctx.status(404);
				return;
			}
			ctx.json(queriedUser);
			ctx.status(200);
		} else {
			log.error(String.format("An attempt to send a get request without a valid session has been made."));
			ctx.status(401);
		}
	};
	
	@Override
	public void addRoutes(Javalin app) {
		app.post("/login", this.loginAttempt);
		app.get("/users/:userid", this.getUserByID);
	}

}
