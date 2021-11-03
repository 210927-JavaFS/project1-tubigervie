package com.revature.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.models.ERSUser;
import com.revature.models.ERSUser.UserRole;
import com.revature.models.Reimbursement.ReimburseStatus;
import com.revature.models.Reimbursement;
import com.revature.services.ERSUserService;
import com.revature.services.ReimbursementService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class ReimbursementController implements Controller
{
	private static Logger log = LoggerFactory.getLogger(ReimbursementController.class);
	
	private ReimbursementService reimbursementService = new ReimbursementService();
	private ERSUserService userService = new ERSUserService();
	
	public Handler getAllReimbursements = (ctx) -> {
		if(ctx.req.getSession(false) != null)
		{
			int userID = ctx.sessionAttribute("userid");
			ERSUser user = userService.getUser(userID);
			if(user.getUserRole() != UserRole.MANAGER) 
			{
				log.error(String.format("User (ID: %d) attempted to get all reimbursements without authorized access.", userID));
				ctx.status(401);
				return;
			}
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
				String idString = ctx.pathParam("reimbursement");
				int id = Integer.parseInt(idString);
				Reimbursement reimbursement = reimbursementService.getReimbursement(id);
				if(reimbursement == null)
				{
					ctx.status(404);
					return;
				}
				int userID = ctx.sessionAttribute("userid");
				ERSUser user = userService.getUser(userID);
				if(user.getUserRole() == UserRole.EMPLOYEE && userID != reimbursement.getAuthor().getID()) 
				{
					log.error(String.format("User (ID: %d) attempted to get request reimbursement (ID: %d) without authorized access.", userID, id));
					ctx.status(401);
					return;
				}
				
				ctx.json(reimbursement);
				ctx.status(200);
			}catch(NumberFormatException e)
			{
				log.error(e.toString());
				ctx.status(406);
			}
		} else {
			ctx.status(401);
		}
	};
	
	public Handler getReimbursementsByUsername = (ctx) -> {
		if(ctx.req.getSession(false) != null)
		{
			String queryUsername = ctx.pathParam("username");
			int sessionUserID = ctx.sessionAttribute("userid");
			ERSUser sessionUser = userService.getUser(sessionUserID);
			if(sessionUser.getUserRole() == UserRole.EMPLOYEE && !sessionUser.getUsername().equals(queryUsername)) 
			{
				log.error(String.format("User (ID: %d) attempted to get request reimbursements without authorized access.", sessionUserID));
				ctx.status(401);
				return;
			}
			
			ERSUser queryUser = userService.getUser(queryUsername);
			if(queryUser == null)
			{
				ctx.status(404);
				return;
			}
			List<Reimbursement> list = reimbursementService.getAllReimbursementsFromUser(queryUser.getID());
			ctx.json(list);
			ctx.status(200);
		} else {
			ctx.status(401);
		}
	};
	
	public Handler getReimbursementsByStatus = (ctx) -> {
		if(ctx.req.getSession(false) != null)
		{
			int sessionUserID = ctx.sessionAttribute("userid");
			ERSUser sessionUser = userService.getUser(sessionUserID);
			if(sessionUser.getUserRole() == UserRole.EMPLOYEE) 
			{
				log.error(String.format("User (ID: %d) attempted to get reimbursements without authorized access.", sessionUserID));
				ctx.status(401);
				return;
			}
			String statusString = ctx.pathParam("status");
			List<Reimbursement> list = null;
			if(statusString.equals("past"))
			{
				list = reimbursementService.getAllPastReimbursements();
			}
			else 
			{
				ReimburseStatus status = ReimburseStatus.valueOf(statusString);
				list = reimbursementService.getReimbursementsByStatus(status);
			}
			ctx.json(list);
			ctx.status(200);
		} else {
			ctx.status(401);
		}
	};
	
	public Handler getUserReimbursementsByStatus = (ctx) -> {
		if(ctx.req.getSession(false) != null)
		{
			String queryUsername = ctx.pathParam("username");
			int sessionUserID = ctx.sessionAttribute("userid");
			ERSUser sessionUser = userService.getUser(sessionUserID);
			if(sessionUser.getUserRole() == UserRole.EMPLOYEE && !sessionUser.getUsername().equals(queryUsername)) 
			{
				log.error(String.format("User (ID: %d) attempted to get request reimbursements without authorized access.", sessionUserID));
				ctx.status(401);
				return;
			}
			
			ERSUser queryUser = userService.getUser(queryUsername);
			if(queryUser == null)
			{
				ctx.status(404);
				return;
			}
			String statusString = ctx.pathParam("status");
			ReimburseStatus status = ReimburseStatus.valueOf(statusString);
			List<Reimbursement> list = reimbursementService.getUserReimbursementsByStatus(status, queryUser.getID());
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
				log.info(String.format("Successfully added reimbursement (ID: %d)", reimbursement.getID()));
				ctx.status(201);
			} else {
				log.error(String.format("Could not add reimbursement (ID: %d)", reimbursement.getID()));
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
				log.info(String.format("Successfully updated reimbursement (ID: %d)", reimbursement.getID()));
				ctx.status(200);
			} else {
				log.error(String.format("Could not update reimbursement (ID: %d)", reimbursement.getID()));
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
					log.info(String.format("Successfully deleted reimbursement (ID: %d)", reimbursement));
					ctx.status(200);
				} else {
					log.info(String.format("Could not delete reimbursement (ID: %d)", reimbursement));
					ctx.status(400);
				}
			} catch (NumberFormatException e) {
				log.error(e.toString());
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
		app.get("/reimbursements/statuses/:status/:username", this.getUserReimbursementsByStatus);
		app.get("/reimbursements/statuses/:status", this.getReimbursementsByStatus);
		app.delete("/reimbursements/:reimbursement", this.deleteReimbursement);
		app.get("/reimbursements/:username", this.getReimbursementsByUsername);
		app.get("/reimbursements/:username/:reimbursement", this.getReimbursement);
	}

}
