package com.revature.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import com.revature.models.Reimbursement;
import com.revature.models.Reimbursement.ReimburseStatus;
import com.revature.services.ReimbursementService;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class ReimbursementServiceTesting
{
	ReimbursementService service = new ReimbursementService();
	
	Reimbursement testReimbursement;
	
	@BeforeAll
	public void initialization()
	{
		testReimbursement = new Reimbursement(null, "15", "test reimbursement.", "Approved", "Food", null, null, null);
	}
	
	@Test
	@Order(1)
	public void addReimbursementTest()
	{
		assertTrue(service.addReimbursement(testReimbursement));
	}
	
	@Test
	@Order(2)
	public void getReimbursementTest()
	{
		System.out.println(testReimbursement.getID());
		Reimbursement fetchedReimbursement = service.getReimbursement(testReimbursement.getID());
		System.out.println("fetchedReimburse:" + fetchedReimbursement);
		assertEquals(fetchedReimbursement, testReimbursement);
	}
	
	  @Test
	  @Order(3) 
	  public void updateReimbursementTest() 
	  { 
		  ReimburseStatus prevStatus = testReimbursement.getStatus();
		  testReimbursement.setStatus(ReimburseStatus.Approved);
		  service.updateReimbursement(testReimbursement);
		  assertNotEquals(service.getReimbursement(testReimbursement.getID()), prevStatus); 
	  }
	  
	  @Test
	  @Order(4)
	  public void deleteReimbursementTest() 
	  {
		  assertTrue(service.deleteReimbursement(testReimbursement.getID())); 
	  }
	  
	  @Test	  
	  @Order(5) 
	  public void getAllReimbursementsTest() 
	  {
		  assertNotNull(service.getAllReimbursements()); 
		  int reimbursementCount = service.getAllReimbursements().size();
		  service.addReimbursement(testReimbursement);
		  assertEquals(service.getAllReimbursements().size(), reimbursementCount + 1);
		  service.deleteReimbursement(testReimbursement.getID());
		  assertEquals(service.getAllReimbursements().size(), reimbursementCount); 
	  }
	  
	  @Test	 
	  @Order(6) 
	  public void getAllPastReimbursementsTest() 
	  {
		  assertNotNull(service.getAllPastReimbursements()); 
		  int reimbursementCount = service.getAllPastReimbursements().size();
		  service.addReimbursement(testReimbursement);
		  assertEquals(service.getAllPastReimbursements().size(), reimbursementCount + 1);
		  service.deleteReimbursement(testReimbursement.getID());
		  assertEquals(service.getAllPastReimbursements().size(), reimbursementCount); 
	  }
	  
	  @Test	 
	  @Order(7) 
	  public void getAllReimbursementsByStatusTest() 
	  {
		  assertNotNull(service.getReimbursementsByStatus(ReimburseStatus.Approved)); 
		  int reimbursementCount = service.getReimbursementsByStatus(ReimburseStatus.Approved).size();
		  service.addReimbursement(testReimbursement);
		  assertEquals(service.getReimbursementsByStatus(ReimburseStatus.Approved).size(), reimbursementCount + 1);
		  service.deleteReimbursement(testReimbursement.getID());
		  assertEquals(service.getReimbursementsByStatus(ReimburseStatus.Approved).size(), reimbursementCount); 
	  }
	 
	
	@AfterAll
	public void cleanUp()
	{
		if(service.getReimbursement(testReimbursement.getID()) != null)
		{
			service.deleteReimbursement(testReimbursement.getID());
		}
	}
	
	
	
}
