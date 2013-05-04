/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 02-May-2013
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

import com.rdonasco.security.dao.UserCapabilityDAO;
import com.rdonasco.security.dao.UserSecurityProfileDAO;
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
public class UserRoleBasedCapabilityTest
{
	private static final Logger LOG = Logger.getLogger(UserRoleBasedCapabilityTest.class.getName());
	private CapabilityManagerLocal capabilityManager;
	private UserCapabilityDAO userCapabilityDAO;
	private UserSecurityProfileDAO userSecurityProfileDAO;

	public UserRoleBasedCapabilityTest()
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

	@Test
	public void testUserManagerRole() throws Exception
	{
		SystemSecurityManagerImpl systemSecurityManager = new SystemSecurityManagerImpl();
		systemSecurityManager.setCapabilityManager(capabilityManager);

		assertTrue(true);
	}
}