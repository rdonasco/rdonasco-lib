/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 21-May-2013
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
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.CapabilityAction;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.model.Role;
import com.rdonasco.security.model.RoleCapability;
import com.rdonasco.security.model.SecurityGroup;
import com.rdonasco.security.model.SecurityGroupRole;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
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
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Roy F. Donasco
 */
public class UserGroupBasedCapabilityTest
{

	private static final Logger LOG = Logger.getLogger(UserGroupBasedCapabilityTest.class.getName());

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

	private static long ID_SEED = 1L;

	public UserGroupBasedCapabilityTest()
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
		reset(userGroupDAOMock);
		when(userSecurityProfileVOMock.getLogonId()).thenReturn("dummyUser");
	}

	@After
	public void tearDown()
	{
	}

	private long getNextID()
	{
		return ID_SEED++;
	}

	private List<Role> getUserRolesThatCanDoThisToAUser(
			final String... actionNames)
	{
		List<Role> roles = new ArrayList<Role>();
		for (String actionName : actionNames)
		{
			Role role = new Role();
			Capability capability = new Capability();
			capability.setId(getNextID());
			Action action = new Action();
			action.setId(getNextID());
			action.setName(actionName);
			CapabilityAction capabilityAction = new CapabilityAction();
			capabilityAction.setId(getNextID());
			capabilityAction.setAction(action);
			capabilityAction.setCapability(capability);
			List<CapabilityAction> capabilityActionList = new ArrayList<CapabilityAction>();
			capabilityActionList.add(capabilityAction);
			capability.setResource(resource);
			capability.setActions(capabilityActionList);
			RoleCapability roleCapability = new RoleCapability();
			roleCapability.setCapability(capability);
			roleCapability.setId(getNextID());
			roleCapability.setRole(role);
			List<RoleCapability> roleCapabilities = new ArrayList<RoleCapability>();
			roleCapabilities.add(roleCapability);
			role.setId(getNextID());
			role.setCapabilities(roleCapabilities);
			roles.add(role);
		}
		return roles;
	}

	private List<SecurityGroup> getUserGroupsThatCanDoThisToAUser(
			String... actions)
	{
		List<SecurityGroup> securityGroups = new ArrayList<SecurityGroup>();
		SecurityGroup group = new SecurityGroup();
		group.setId(getNextID());
		List<Role> roles = getUserRolesThatCanDoThisToAUser(actions);
		for (Role role : roles)
		{
			SecurityGroupRole securityGroupRole = new SecurityGroupRole();
			securityGroupRole.setId(getNextID());
			securityGroupRole.setRole(role);
			securityGroupRole.setSecurityGroup(group);
			group.getGroupRoles().add(securityGroupRole);
		}
		securityGroups.add(group);
		return securityGroups;
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
		return systemSecurityManager;
	}

	private List<Capability> getEmptyUserCapabilities()
	{
		return new ArrayList<Capability>();
	}

	private List<Role> getEmptyUserRoles()
	{
		return new ArrayList<Role>();
	}

	@Test
	public void testGroupWithCapabilityToAddUser() throws Exception
	{
		System.out.println("groupWithCapabilityToAddUser");
		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();
		AccessRightsVO accessRights = prepareAccessrightsRequestDataFor("add");

		when(userSecurityProfileVOMock.getId()).thenReturn(Long.MIN_VALUE);
		when(userSecurityProfileVOMock.getRegistrationToken()).thenReturn("token");
		when(userSecurityProfileVOMock.getRegistrationTokenExpiration()).thenReturn(new Date());
		when(userCapabilityDAOMock.loadCapabilitiesOf(any(UserSecurityProfile.class))).thenReturn(getEmptyUserCapabilities());
		when(capabilityManagerMock.findOrAddSecuredResourceNamedAs(accessRights.getResource().getName())).thenReturn(resourceVO);
		when(userRoleDAOmock.loadRolesOf(any(UserSecurityProfile.class))).thenReturn(getEmptyUserRoles());
		when(userGroupDAOMock.loadGroupsOf(any(UserSecurityProfile.class))).thenReturn(getUserGroupsThatCanDoThisToAUser("add", "edit", "delete"));
		instance.checkAccessRights(accessRights);
		verify(userRoleDAOmock, times(1)).loadRolesOf(any(UserSecurityProfile.class));
	}

	@Test
	public void testGroupThatCanAddUserAndRoleThatCanEditOrDeleteUser() throws
			Exception
	{
		System.out.println("groupThatCanAddUserAndRoleThatCanEditOrDeleteUser");
		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();
		AccessRightsVO accessrightsToAdd = prepareAccessrightsRequestDataFor("add");
		AccessRightsVO accessrightsToEdit = prepareAccessrightsRequestDataFor("edit");
		AccessRightsVO accessrightsToDelete = prepareAccessrightsRequestDataFor("Delete");

		when(userSecurityProfileVOMock.getId()).thenReturn(Long.MIN_VALUE);
		when(userSecurityProfileVOMock.getRegistrationToken()).thenReturn("token");
		when(userSecurityProfileVOMock.getRegistrationTokenExpiration()).thenReturn(new Date());
		when(userCapabilityDAOMock.loadCapabilitiesOf(any(UserSecurityProfile.class))).thenReturn(getEmptyUserCapabilities());
		when(capabilityManagerMock.findOrAddSecuredResourceNamedAs(accessrightsToAdd.getResource().getName())).thenReturn(resourceVO);
		when(userRoleDAOmock.loadRolesOf(any(UserSecurityProfile.class))).thenReturn(getUserRolesThatCanDoThisToAUser("edit", "delete"));
		when(userGroupDAOMock.loadGroupsOf(any(UserSecurityProfile.class))).thenReturn(getUserGroupsThatCanDoThisToAUser("add"));
		instance.checkAccessRights(accessrightsToAdd);
		instance.checkAccessRights(accessrightsToEdit);
		instance.checkAccessRights(accessrightsToDelete);

	}

	@Test(expected = SecurityAuthorizationException.class)
	public void testGroupThatCanAddUserAndRoleThatCanEditUserButCannotDelete()
			throws Exception
	{
		System.out.println("groupThatCanAddUserAndRoleThatCanEditUserButCannotDelete");
		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();
		AccessRightsVO accessrightsToAdd = prepareAccessrightsRequestDataFor("add");
		AccessRightsVO accessrightsToEdit = prepareAccessrightsRequestDataFor("edit");
		AccessRightsVO accessrightsToDelete = prepareAccessrightsRequestDataFor("Delete");

		when(userSecurityProfileVOMock.getId()).thenReturn(Long.MIN_VALUE);
		when(userSecurityProfileVOMock.getRegistrationToken()).thenReturn("token");
		when(userSecurityProfileVOMock.getRegistrationTokenExpiration()).thenReturn(new Date());
		when(userCapabilityDAOMock.loadCapabilitiesOf(any(UserSecurityProfile.class))).thenReturn(getEmptyUserCapabilities());
		when(capabilityManagerMock.findOrAddSecuredResourceNamedAs(accessrightsToAdd.getResource().getName())).thenReturn(resourceVO);
		when(userRoleDAOmock.loadRolesOf(any(UserSecurityProfile.class))).thenReturn(getUserRolesThatCanDoThisToAUser("edit"));
		when(userGroupDAOMock.loadGroupsOf(any(UserSecurityProfile.class))).thenReturn(getUserGroupsThatCanDoThisToAUser("add"));
		instance.checkAccessRights(accessrightsToAdd);
		instance.checkAccessRights(accessrightsToEdit);
		instance.checkAccessRights(accessrightsToDelete);
	}

	@Test
	public void testGroupThatCanAddUserAndRoleThatCanEditUserAndCapabilityThatCanDeleteUser()
			throws Exception
	{
		System.out.println("groupThatCanAddUserAndRoleThatCanEditUserAndCapabilityThatCanDeleteUser");
		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();
		AccessRightsVO accessrightsToAdd = prepareAccessrightsRequestDataFor("add");
		AccessRightsVO accessrightsToEdit = prepareAccessrightsRequestDataFor("edit");
		AccessRightsVO accessrightsToDelete = prepareAccessrightsRequestDataFor("Delete");
		when(userSecurityProfileVOMock.getId()).thenReturn(Long.MIN_VALUE);
		when(userSecurityProfileVOMock.getRegistrationToken()).thenReturn("token");
		when(userSecurityProfileVOMock.getRegistrationTokenExpiration()).thenReturn(new Date());
		when(userCapabilityDAOMock.loadCapabilitiesOf(any(UserSecurityProfile.class)))
				.thenReturn(getUserCapabilityThatCanDoThisToAUser("delete"));
		when(capabilityManagerMock.findOrAddSecuredResourceNamedAs(accessrightsToAdd.getResource().getName())).thenReturn(resourceVO);
		when(userRoleDAOmock.loadRolesOf(any(UserSecurityProfile.class))).thenReturn(getUserRolesThatCanDoThisToAUser("edit"));
		when(userGroupDAOMock.loadGroupsOf(any(UserSecurityProfile.class))).thenReturn(getUserGroupsThatCanDoThisToAUser("add"));
		instance.checkAccessRights(accessrightsToAdd);
		instance.checkAccessRights(accessrightsToEdit);
		instance.checkAccessRights(accessrightsToDelete);
	}

	private AccessRightsVO prepareAccessrightsRequestDataFor(String action)
	{
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString(action)
				.setResourceAsString(resourceName)
				.setUserProfileVO(userSecurityProfileVOMock)
				.createAccessRightsVO();
		return accessRights;
	}

	private List<Capability> getUserCapabilityThatCanDoThisToAUser(
			String... actions)
	{
		List<Capability> capabilities = new ArrayList<Capability>();
		for (String action : actions)
		{
			capabilities.add(SecurityEntityValueObjectDataUtility
					.createTestDataCapabilityOnResourceAndAction("User", action));
		}
		return capabilities;
	}
}
