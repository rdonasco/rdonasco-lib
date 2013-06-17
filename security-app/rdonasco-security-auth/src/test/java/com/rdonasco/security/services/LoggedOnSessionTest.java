/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 07-Mar-2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rdonasco.security.services;

import com.rdonasco.security.authentication.services.LoggedOnSession;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.rdonasco.security.vo.UserSecurityProfileVOBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Roy F. Donasco
 */
public class LoggedOnSessionTest
{

	private static final Logger LOG = Logger.getLogger(LoggedOnSessionTest.class.getName());
	private static final String LOGON_ID = "roy";

	public LoggedOnSessionTest()
	{
	}

	@BeforeClass
	public static void setUpClass()
	{
	}

	@AfterClass
	public static void tearDownClass()
	{
	}

	@Before
	public void setUp()
	{
	}

	@After
	public void tearDown()
	{
	}

	/**
	 * Test of setLoggedOnUser method, of class LoggedOnSession.
	 */
	@Test
	public void testSetLoggedOnUser()
	{
		System.out.println("setLoggedOnUser");
		UserSecurityProfileVO loggedOnUser = createTestDataUserSecurityProfileVO(LOGON_ID);
		LoggedOnSession instance = new LoggedOnSession();
		instance.setLoggedOnUser(loggedOnUser);
		UserSecurityProfileVO loggedOnUserResult = instance.getLoggedOnUser();
		assertNotNull(loggedOnUserResult);
	}

	/**
	 * Test of getLoggedOnUser method, of class LoggedOnSession.
	 */
	@Test
	public void testGetLoggedOnUser()
	{
		System.out.println("getLoggedOnUser");
		LoggedOnSession instance = new LoggedOnSession();
		instance.setLoggedOnUser(createTestDataUserSecurityProfileVO(LOGON_ID));
		assertTrue(instance.isLoggedOn());
		UserSecurityProfileVO result = instance.getLoggedOnUser();
		assertNotNull(result);
		assertEquals(LOGON_ID, result.getLogonId());
	}

	@Test
	public void testSetNewLoggedOnUser()
	{
		System.out.println("setNewLoggedOnUser");
		LoggedOnSession instance = new LoggedOnSession();
		instance.setLoggedOnUser(createTestDataUserSecurityProfileVO(LOGON_ID));
		assertTrue(instance.isLoggedOn());
		instance.setLoggedOnUser(createTestDataUserSecurityProfileVO("newUser"));
		UserSecurityProfileVO result = instance.getLoggedOnUser();
		assertEquals("newUser", result.getLogonId());
	}

	@Test
	public void testNotLoggedOnUserOnDifferentThread()
	{
		System.out.println("getLoggedOnUserOnDifferentThread");
		final AssertableObject<Boolean> isLoggedOn = new AssertableObject<Boolean>();
		Runnable threadSimulator = new Runnable()
		{
			@Override
			public void run()
			{
				LoggedOnSession instance = new LoggedOnSession();
				isLoggedOn.setValue(instance.isLoggedOn());
			}
		};
		Thread thread = new Thread(threadSimulator);
		thread.start();
		delayBy(1000);
		assertFalse("userIsLoggedOn",isLoggedOn.getValue().booleanValue());		
	}

	@Test
	public void testClearSessionOnDifferentThread()
	{
		System.out.println("clearSessionOnDifferentThread");
		final AssertableObject<Boolean> isLoggedOn = new AssertableObject<Boolean>();
		Runnable threadSimulator = new Runnable()
		{
			@Override
			public void run()
			{
				LoggedOnSession instance = new LoggedOnSession();
				final String userToClear = "userToClear";
				instance.setLoggedOnUser(createTestDataUserSecurityProfileVO(userToClear));
				assertEquals(userToClear, instance.getLoggedOnUser().getLogonId());
				instance.clear();
				isLoggedOn.setValue(instance.isLoggedOn());
			}
		};
		new Thread(threadSimulator).start();
		delayBy(1000);
		assertFalse(isLoggedOn.getValue());
	}

	UserSecurityProfileVO createTestDataUserSecurityProfileVO(String userID)
	{
		UserSecurityProfileVO loggedOnUser = new UserSecurityProfileVOBuilder()
				.setId(Long.MAX_VALUE)
				.setLoginId((null == userID) ? LOGON_ID : userID)
				.setPassword("pogi")
				.createUserSecurityProfileVO();
		return loggedOnUser;
	}

	void delayBy(long delay)
	{
		try
		{
			Thread.sleep(delay);
		}
		catch (InterruptedException ex)
		{
			LOG.log(Level.WARNING, ex.getMessage(), ex);
		}
	}
}