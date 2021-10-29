package com.revature.testing;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.revature.models.UserDTO;
import com.revature.services.LoginService;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class LoginServiceTesting 
{
	LoginService service = new LoginService();
	
	private String testUsername = "admin";
	private String testUnencryptedPassword = "helloworld";
	
	private UserDTO testDTO;
	
	@BeforeAll
	public void InitializeDTO()
	{
		testDTO = new UserDTO();
		testDTO.username = testUsername;
		testDTO.password = testUnencryptedPassword;
	}
	
	@Test
	@Order(1)
	public void loginSuccessTest()
	{
		assertTrue(service.login(testDTO));
	}
	
	@Test
	@Order(2)
	public void loginFailTest()
	{
		testDTO.password = "wrongPassword";
		assertFalse(service.login(testDTO));
	}
}
