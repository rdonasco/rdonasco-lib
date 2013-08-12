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
import com.rdonasco.security.dao.UserGroupDAO;
import com.rdonasco.security.dao.UserRoleDAO;
import com.rdonasco.security.dao.UserSecurityProfileDAO;
import com.rdonasco.security.exceptions.SecurityAuthorizationException;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.model.Application;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.CapabilityAction;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.model.Role;
import com.rdonasco.security.model.RoleCapability;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import com.rdonasco.security.vo.ApplicationVO;
import com.rdonasco.security.vo.ApplicationVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
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
	private static UserGroupDAO userGroupDAOMock;
	private static String resourceName = "User";
	private static ResourceVO resourceVO;
	private static Resource resource;
	private static ApplicationManagerLocal applicationManagerMock;
	private static ApplicationVO applicationVoMock;
	private static long ID_GEN;

	public UserRoleBasedCapabilityTest()
	{
	}

	@BeforeClass
	public static void setUpClass()
	{
		try
		{
			userSecurityProfileVOMock = mock(UserSecurityProfileVO.class);
			userSecurityProfileMock = mock(UserSecurityProfile.class);
			userSecurityProfileDAOMock = mock(UserSecurityProfileDAO.class);
			capabilityManagerMock = mock(CapabilityManagerLocal.class);
			userCapabilityDAOMock = mock(UserCapabilityDAO.class);
			userRoleDAOmock = mock(UserRoleDAO.class);
			roleDAOmock = mock(RoleDAO.class);
			userGroupDAOMock = mock(UserGroupDAO.class);
			resourceVO = new ResourceVOBuilder()
					.setId(Long.MIN_VALUE)
					.setName(resourceName)
					.createResourceVO();
			applicationManagerMock = mock(ApplicationManagerLocal.class);
			applicationVoMock = new ApplicationVOBuilder()
					.setId(ID_GEN++)
					.setName("rdonasco-security")
					.setToken("rdonasco-token")
					.createApplicationVO();

			resource = SecurityEntityValueObjectConverter.toResource(resourceVO);
		}
		catch (Exception ex)
		{
			LOG.log(Level.SEVERE, ex.getMessage(), ex);
		}

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
		userSecurityProfileManager.setUserRoleDAO(userRoleDAOmock);
		userSecurityProfileManager.setUserGroupDAO(userGroupDAOMock);
		SystemSecurityManagerImpl systemSecurityManager = new SystemSecurityManagerImpl();
		systemSecurityManager.setCapabilityManager(capabilityManagerMock);
		systemSecurityManager.setUserSecurityProfileManager(userSecurityProfileManager);
		systemSecurityManager.setApplicationManager(applicationManagerMock);
		return systemSecurityManager;
	}

	private List<Capability> getEmptyUserCapabilities()
	{
		return new ArrayList<Capability>();
	}

	private List<Role> getUserRolesThatCanDoThisToAUser(final String actionName)
	{
		List<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		Capability capability = new Capability();
		Application application = new Application();
		application.setId(applicationVoMock.getId());
		application.setName(applicationVoMock.getName());
		application.setToken(applicationVoMock.getToken());
		capability.setApplication(application);
		Action action = new Action();
		action.setId(Long.MIN_VALUE);
		action.setName(actionName);
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

	@Test
	public void testWithRoleThatCanAddUser() throws Exception
	{
		System.out.println("withRoleThatCanAddUser");
		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();
		final String addAction = "Add";
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString(addAction)
				.setActionID(Long.MIN_VALUE)
				.setResourceAsString(resourceName)
				.setResourceID(Long.MIN_VALUE)
				.setUserProfileVO(userSecurityProfileVOMock)
				.setApplicationID(applicationVoMock.getId())
				.setApplicationToken(applicationVoMock.getToken())
				.createAccessRightsVO();

		when(userSecurityProfileVOMock.getId()).thenReturn(Long.MIN_VALUE);
		when(userSecurityProfileVOMock.getRegistrationToken()).thenReturn("token");
		when(userSecurityProfileVOMock.getRegistrationTokenExpiration()).thenReturn(new Date());
		when(userCapabilityDAOMock.loadCapabilitiesOnApplicationOf(any(UserSecurityProfile.class), any(Application.class))).thenReturn(getEmptyUserCapabilities());
		when(capabilityManagerMock.findOrAddSecuredResourceNamedAs(accessRights.getResource().getName())).thenReturn(resourceVO);
		when(userRoleDAOmock.loadRolesOf(any(UserSecurityProfile.class))).thenReturn(getUserRolesThatCanDoThisToAUser(addAction));
		when(applicationManagerMock.loadApplicationWithID(applicationVoMock.getId())).thenReturn(applicationVoMock);
		instance.checkAccessRights(accessRights);
		verify(userRoleDAOmock, times(1)).loadRolesOf(any(UserSecurityProfile.class));
	}

	@Test(expected = SecurityAuthorizationException.class)
	public void testWithRoleThatCanNotAddUser() throws Exception
	{
		System.out.println("withRoleThatCannotAddUser");
		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();
		final String addAction = "Add";
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString(addAction)
				.setActionID(Long.MIN_VALUE)
				.setResourceAsString(resourceName)
				.setResourceID(Long.MIN_VALUE)
				.setUserProfileVO(userSecurityProfileVOMock)
				.setApplicationID(applicationVoMock.getId())
				.setApplicationToken(applicationVoMock.getToken())
				.createAccessRightsVO();

		when(userSecurityProfileVOMock.getId()).thenReturn(Long.MIN_VALUE);
		when(userSecurityProfileVOMock.getRegistrationToken()).thenReturn("token");
		when(userSecurityProfileVOMock.getRegistrationTokenExpiration()).thenReturn(new Date());
		when(userCapabilityDAOMock.loadCapabilitiesOnApplicationOf(any(UserSecurityProfile.class), any(Application.class))).thenReturn(getEmptyUserCapabilities());
		when(capabilityManagerMock.findOrAddSecuredResourceNamedAs(accessRights.getResource().getName())).thenReturn(resourceVO);
		when(userRoleDAOmock.loadRolesOf(any(UserSecurityProfile.class))).thenReturn(getUserRolesThatCanDoThisToAUser("Edit"));
		when(applicationManagerMock.loadApplicationWithID(applicationVoMock.getId())).thenReturn(applicationVoMock);
		instance.checkAccessRights(accessRights);
		verify(userRoleDAOmock, times(1)).loadRolesOf(any(UserSecurityProfile.class));
	}
}
