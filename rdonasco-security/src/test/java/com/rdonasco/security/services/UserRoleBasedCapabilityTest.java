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
import com.rdonasco.security.dao.UserRoleDAO;
import com.rdonasco.security.dao.UserSecurityProfileDAO;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.CapabilityAction;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.model.Role;
import com.rdonasco.security.model.RoleCapability;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;

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

	private static RoleDAO roleDAOmock;

	private static UserRoleDAO userRoleDAOmock;

	public UserRoleBasedCapabilityTest()
	{
	}

	@BeforeClass
	public static void setUpClass()
	{
		userSecurityProfileVOMock = mock(UserSecurityProfileVO.class);
		userSecurityProfileMock = mock(UserSecurityProfile.class);
		userSecurityProfileDAOMock = mock(UserSecurityProfileDAO.class);
		capabilityManagerMock = mock(CapabilityManagerLocal.class);
		userCapabilityDAOMock = mock(UserCapabilityDAO.class);
		userRoleDAOmock = mock(UserRoleDAO.class);
		roleDAOmock = mock(RoleDAO.class);

	}

	@AfterClass
	public static void tearDownClass()
	{
	}

	@Before
	public void setUp()
	{
		reset(userSecurityProfileVOMock);
		reset(userSecurityProfileMock);
		reset(userSecurityProfileDAOMock);
		reset(capabilityManagerMock);
		reset(userCapabilityDAOMock);
		reset(userRoleDAOmock);
		reset(roleDAOmock);
		when(userSecurityProfileVOMock.getLogonId()).thenReturn("dummyUser");
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

	private List<Capability> getCapabilityOnAddingUser()
	{
		List<Capability> capabilities = new ArrayList<Capability>();
		capabilities.add(SecurityEntityValueObjectDataUtility
				.createTestDataCapabilityOnResourceAndAction("User", "Add"));
		return capabilities;
	}

	private List<Capability> getEmptyUserCapabilities()
	{
		return new ArrayList<Capability>();
	}

	@Test
	public void testUserManagerRole() throws Exception
	{
		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString("Add")
				.setActionID(Long.MIN_VALUE)
				.setResourceAsString("User")
				.setResourceID(Long.MIN_VALUE)
				.setUserProfileVO(userSecurityProfileVOMock)
				.createAccessRightsVO();
		when(userSecurityProfileVOMock.getRegistrationToken()).thenReturn("token");
		when(userSecurityProfileVOMock.getRegistrationTokenExpiration()).thenReturn(new Date());
		when(userCapabilityDAOMock.loadCapabilitiesOf(any(UserSecurityProfile.class))).thenReturn(getEmptyUserCapabilities());
		when(userRoleDAOmock.loadRolesOf(any(UserSecurityProfile.class))).thenReturn(getUserRoles());
		instance.checkAccessRights(accessRights);
	}

	private List<Role> getUserRoles()
	{
		List<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		Resource resource = new Resource();
		Capability capability = new Capability();
		Action action = new Action();
		action.setId(Long.MIN_VALUE);
		action.setName("Add");
		CapabilityAction capabilityAction = new CapabilityAction();
		capabilityAction.setAction(action);
		capabilityAction.setCapability(capability);
		List<CapabilityAction> capabilityActionList = new ArrayList<CapabilityAction>();
		capabilityActionList.add(capabilityAction);
		capability.setResource(resource);
		capability.setActions(capabilityActionList);
		RoleCapability roleCapability = new RoleCapability();
		roleCapability.setCapability(capability);
		roleCapability.setId(Long.MIN_VALUE);
		roleCapability.setRole(role);
		List<RoleCapability> roleCapabilities = new ArrayList<RoleCapability>();
		roleCapabilities.add(roleCapability);
		role.setId(Long.MIN_VALUE);
		role.setCapabilities(roleCapabilities);
		roles.add(role);
		return roles;
	}

}