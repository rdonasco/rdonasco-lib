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

import com.rdonasco.security.dao.RoleDAO;
import com.rdonasco.security.dao.UserCapabilityDAO;
import com.rdonasco.security.dao.UserSecurityProfileDAO;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 *
 * @author Roy F. Donasco
 */
public class UserRoleBasedCapabilityTest
{

	private static final Logger LOG = Logger.getLogger(UserRoleBasedCapabilityTest.class.getName());

	private static UserSecurityProfileVO userSecurityProfileVOMock;

	private static UserSecurityProfile userSecurityProfileMock;

	private static UserSecurityProfileDAO userSecurityProfileDAOMock;

	private static CapabilityManagerLocal capabilityManagerMock;

	private static UserCapabilityDAO userCapabilityDAOMock;

	private static UserSecurityProfileManager userSecurityProfileManager;

	private static RoleDAO roleDAO;

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
		userSecurityProfileVOMock = mock(UserSecurityProfileVO.class);
		userSecurityProfileMock = mock(UserSecurityProfile.class);
		userSecurityProfileDAOMock = mock(UserSecurityProfileDAO.class);
		capabilityManagerMock = mock(CapabilityManagerLocal.class);
		userCapabilityDAOMock = mock(UserCapabilityDAO.class);
		roleDAO = mock(RoleDAO.class);
	}

	@After
	public void tearDown()
	{
	}

	private SystemSecurityManagerImpl prepareSecurityManagerInstanceToTest()
	{
		userSecurityProfileManager = new UserSecurityProfileManager();
		userSecurityProfileManager.setUserSecurityProfileDAO(userSecurityProfileDAOMock);
		userSecurityProfileManager.setUserCapabilityDAO(userCapabilityDAOMock);
		SystemSecurityManagerImpl systemSecurityManager = new SystemSecurityManagerImpl();
		systemSecurityManager.setCapabilityManager(capabilityManagerMock);
		systemSecurityManager.setUserSecurityProfileManager(userSecurityProfileManager);
		return systemSecurityManager;
	}

	@Test
	public void testUserManagerRole() throws Exception
	{
		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();
		AccessRightsVO accessRight = new AccessRightsVOBuilder()
				.setUserProfileVO(userSecurityProfileVOMock)
				.setResourceAsString("employee")
				.setActionAsString("edit")
				.createAccessRightsVO();
		instance.checkAccessRights(accessRight);
	}

}