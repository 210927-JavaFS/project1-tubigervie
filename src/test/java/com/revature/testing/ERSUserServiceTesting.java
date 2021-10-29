package com.revature.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import com.revature.models.ERSUser;
import com.revature.models.ERSUser.UserRole;
import com.revature.services.ERSUserService;
import com.revature.utils.CryptoUtils;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class ERSUserServiceTesting 
{
	ERSUserService userService = new ERSUserService();
	
	ERSUser testUser;
	
	@BeforeAll
	public void initializeUser()
	{
		testUser = new ERSUser("testuser", "password", "Ervie", "Tubig", "etubig@uci.edu", UserRole.EMPLOYEE);
	}
	
	@Test
	@Order(1)
	public void addUserTest()
	{
		assertTrue(userService.addUser(testUser));
	}
	
	@Test
	@Order(2)
	public void getUserTest()
	{
		ERSUser fetchedUserByName = userService.getUser(testUser.getUsername());
		assertEquals(fetchedUserByName, testUser);
		ERSUser fetchUserByID = userService.getUser(testUser.getID());
		assertEquals(fetchUserByID, testUser);
	}
	
	@Test
	@Order(3)
	public void updateUserTest()
	{
		UserRole previousRole = testUser.getUserRole();
		testUser.setUserRole(UserRole.MANAGER);
		userService.updateUser(testUser);
		assertNotEquals(userService.getUser(testUser.getID()).getUserRole(), previousRole);
	}
	
	@Test
	@Order(4)
	public void deleteUserTest()
	{
		assertTrue(userService.deleteUser(testUser.getID()));
	}
	
	@Test
	@Order(5)
	public void getAllUsersTest()
	{
		assertNotNull(userService.getAllUsers());
		int userCount = userService.getAllUsers().size();
		userService.addUser(testUser);
		assertEquals(userService.getAllUsers().size(), userCount + 1);
		userService.deleteUser(testUser.getID());
		assertEquals(userService.getAllUsers().size(), userCount);
	}
	
	@AfterAll
	public void cleanUp()
	{
		if(userService.getUser(testUser.getUsername()) != null)
		{
			userService.deleteUser(testUser.getID());
		}
	}
		
}
