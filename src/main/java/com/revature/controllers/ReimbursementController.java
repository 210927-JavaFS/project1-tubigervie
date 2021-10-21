package com.revature.controllers;

import java.util.List;

import com.revature.models.Reimbursement;
import com.revature.services.ReimbursementService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class ReimbursementController implements Controller
{
	private ReimbursementService reimbursementService = new ReimbursementService();
	
	public Handler getAllReimbursements = (ctx) -> {
		List<Reimbursement> list = reimbursementService.getAllReimbursements();
		ctx.json(list);
		ctx.status(200);
	};
	
	public Handler getReimbursement = (ctx) -> {
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
	};
	
	public Handler addReimbursement = (ctx) -> {
		Reimbursement reimbursement = ctx.bodyAsClass(Reimbursement.class);
		if (reimbursementService.addReimbursement(reimbursement)) {
			ctx.status(201);
		} else {
			ctx.status(400);
		}
	};
	
	public Handler updateReimbursement = (ctx) -> {
		Reimbursement reimbursement = ctx.bodyAsClass(Reimbursement.class);
		if (reimbursementService.updateReimbursement(reimbursement)) {
			ctx.status(200);
		} else {
			ctx.status(400);
		}
	};
	
	public Handler deleteReimbursement = (ctx) -> {
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
	};
	
	@Override
	public void addRoutes(Javalin app) {
		app.get("/reimbursements", this.getAllReimbursements);
		app.get("/reimbursements/:reimbursement", this.getReimbursement);
		app.post("/reimbursements", this.addReimbursement);
		app.put("/reimbursements", this.updateReimbursement);
		app.delete("/reimbursements/:reimbursement", this.deleteReimbursement);
	}

}
