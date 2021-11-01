package com.revature;

import com.revature.controllers.Controller;
import com.revature.controllers.ERSUserController;
import com.revature.controllers.ReimbursementController;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Driver 
{
	private static Javalin app;
	
	public static void main(String[] args)
	{
		app = Javalin.create((config)->{
			config.addStaticFiles("/static", Location.CLASSPATH);
		});
		app.before(ctx->ctx.header("Access-Control-Allow-Origin", "http://ers-system.s3-website-us-west-1.amazonaws.com"));
		configure(new ReimbursementController(), new ERSUserController());
		
		app.start(8081);

	}
	
	private static void configure(Controller... controllers) {
		for(Controller c:controllers) {
			c.addRoutes(app);
		}
	}

}
