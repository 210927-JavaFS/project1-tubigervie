package com.revature.controllers;

import java.util.List;

import com.revature.models.ERSUser;
import com.revature.models.Reimbursement;
import com.revature.services.ERSUserService;
import com.revature.services.ReimbursementService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class ReimbursementController implements Controller
{
	private ReimbursementService reimbursementService = new ReimbursementService();
	private ERSUserService userService = new ERSUserService();
	
	public Handler getAllReimbursements = (ctx) -> {
		if(ctx.req.getSession(false) != null)
		{
			List<Reimbursement> list = reimbursementService.getAllReimbursements();
			ctx.json(list);
			ctx.status(200);
		}
		else {
			ctx.status(401);
		}
	};
	
	public Handler getReimbursement = (ctx) -> {
		if(ctx.req.getSession(false) != null)
		{
			try {
				String idString = ctx.pathParam("reimbursements");
				int id = Integer.parseInt(idString);
				Reimbursement reimbursement = reimbursementService.getReimbursement(id);
				ctx.json(reimbursement);
				ctx.status(200);
			}catch(NumberFormatException e)
			{
				e.printStackTrace();
				ctx.status(406);
			}
		} else {
			ctx.status(401);
		}
	};
	
	public Handler getReimbursementsById = (ctx) -> {
		if(ctx.req.getSession(false) != null)
		{
			String username = ctx.pathParam("username");
			ERSUser user = userService.getUser(username);
			List<Reimbursement> list = reimbursementService.getAllReimbursementsFromUser(user.getID());
			ctx.json(list);
			ctx.status(200);
		} else {
			ctx.status(401);
		}
	};
	
	public Handler addReimbursement = (ctx) -> {
		if(ctx.req.getSession(false) != null)
		{
			Reimbursement reimbursement = ctx.bodyAsClass(Reimbursement.class);
			if (reimbursementService.addReimbursement(reimbursement)) {
				ctx.status(201);
			} else {
				ctx.status(400);
			}
		} else {
			ctx.status(401);
		}
	};
	
	public Handler updateReimbursement = (ctx) -> {
		if(ctx.req.getSession(false) != null)
		{
			Reimbursement reimbursement = ctx.bodyAsClass(Reimbursement.class);
			if (reimbursementService.updateReimbursement(reimbursement)) {
				ctx.status(200);
			} else {
				ctx.status(400);
			}
		} else {
			ctx.status(401);
		}
	};
	
	public Handler deleteReimbursement = (ctx) -> {
		if(ctx.req.getSession(false) != null)
		{
			String id = ctx.pathParam("reimbursements");
			try {
				int reimbursement = Integer.parseInt(id);
				if (reimbursementService.deleteReimbursement(reimbursement)) {
					ctx.status(200);
				} else {
					ctx.status(400);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				ctx.status(406);
			}
		} else {
			ctx.status(401);
		}
	};
	
	@Override
	public void addRoutes(Javalin app) {
		app.get("/reimbursements", this.getAllReimbursements);
		app.post("/reimbursements", this.addReimbursement);
		app.put("/reimbursements", this.updateReimbursement);
		app.delete("/reimbursements/:reimbursement", this.deleteReimbursement);
		app.get("/reimbursements/:username", this.getReimbursementsById);
	}

}
