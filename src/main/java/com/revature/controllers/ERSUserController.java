package com.revature.controllers;


import com.revature.models.ERSUser;
import com.revature.models.UserDTO;
import com.revature.models.ERSUser.UserRole;
import com.revature.services.ERSUserService;
import com.revature.services.LoginService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class ERSUserController implements Controller
{
	private LoginService loginService = new LoginService();
	private ERSUserService userService = new ERSUserService();
	
	private Handler loginAttempt = (ctx) ->
	{
		UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
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
			System.out.println("login failed");
			ctx.req.getSession().invalidate();
			ctx.status(401);
		}
	};
	
	/*
	 * private Handler getUserByName = (ctx) -> { if(ctx.req.getSession(false) !=
	 * null) { String username = ctx.pathParam("username"); ERSUser user =
	 * userService.getUser(username); ctx.json(user); ctx.status(200); } else {
	 * ctx.status(401); } };
	 */
	
	private Handler getUserByID = (ctx) ->
	{
		if(ctx.req.getSession(false) != null)
		{
			int userID = ctx.sessionAttribute("userid");
			ERSUser sessionUser = userService.getUser(userID);
			int id = Integer.valueOf(ctx.pathParam("userid"));
			if(sessionUser.getUserRole() == UserRole.EMPLOYEE && userID != id) 
			{
				ctx.status(401);
				return;
			}
			ERSUser queriedUser = userService.getUser(id);
			if(queriedUser == null)
			{
				ctx.status(404);
				return;
			}
			ctx.json(queriedUser);
			ctx.status(200);
		} else {
			ctx.status(401);
		}
	};
	
	@Override
	public void addRoutes(Javalin app) {
		app.post("/login", this.loginAttempt);
		app.get("/users/:userid", this.getUserByID);
	}

}
