/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 27-Jan-2013
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
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.dao.UserSecurityProfileDAO;
import com.rdonasco.security.exceptions.NotSecuredResourceException;
import com.rdonasco.security.exceptions.SecurityAuthorizationException;
import com.rdonasco.security.model.Application;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.ApplicationVO;
import com.rdonasco.security.vo.ApplicationVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.rdonasco.security.vo.UserSecurityProfileVOBuilder;
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
public class UserBasedCapabilityTest
{

	private static final Logger LOG = Logger.getLogger(UserBasedCapabilityTest.class.getName());
	private static UserSecurityProfileVO userSecurityProfileVOMock;
	private static UserSecurityProfile userSecurityProfileMock;
	private static UserSecurityProfileDAO userSecurityProfileDAOMock;
	private static CapabilityManagerLocal capabilityManagerMock;
	private static UserCapabilityDAO userCapabilityDAOMock;
	private static UserSecurityProfileManager userSecurityProfileManager;
	private static RoleDAO roleDAO;
	static private UserRoleDAO userRoleDAOMock;
	private static UserGroupDAO userGroupDAOMock;
	private static ApplicationVO applicationVOMock = new ApplicationVOBuilder()
			.setId(Long.MIN_VALUE)
			.setToken("token")
			.createApplicationVO();
	private static Application applicationMock;
	private static ApplicationManagerLocal applicationManageMock;

	public UserBasedCapabilityTest()
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
		userRoleDAOMock = mock(UserRoleDAO.class);
		userGroupDAOMock = mock(UserGroupDAO.class);
		roleDAO = mock(RoleDAO.class);
		applicationManageMock = mock(ApplicationManagerLocal.class);
		applicationMock = SecurityEntityValueObjectConverter.toApplication(applicationVOMock);
	}

	@AfterClass
	public static void tearDownClass()
	{
	}

	@Before
	public void setUp()
	{
		reset(applicationManageMock);
		reset(userSecurityProfileVOMock);
		reset(userSecurityProfileMock);
		reset(userSecurityProfileDAOMock);
		reset(capabilityManagerMock);
		reset(userCapabilityDAOMock);
		reset(roleDAO);
	}

	@After
	public void tearDown()
	{
	}

	private UserSecurityProfileVO createTestDataUserProfileVO()
	{
		UserSecurityProfileVO userSecurityProfileVO = new UserSecurityProfileVOBuilder()
				.setId(Long.MIN_VALUE)
				.setLoginId("test Login ID")
				.createUserSecurityProfileVO();
		return userSecurityProfileVO;
	}

	private List<Capability> getCapabilityOnAddingUser()
	{
		List<Capability> capabilities = new ArrayList<Capability>();
		final Capability addUserCapability = SecurityEntityValueObjectDataUtility
									.createTestDataCapabilityOnApplicationResourceAndAction("User", "Add");
		addUserCapability.setApplication(applicationMock);
		capabilities.add(addUserCapability);
		return capabilities;
	}

	private List<Capability> getCapabilityOnEditingUser()
	{
		List<Capability> capabilities = new ArrayList<Capability>();
		final Capability editUserCapability = SecurityEntityValueObjectDataUtility.createTestDataCapabilityOnApplicationResourceAndAction("User", "Edit");
		editUserCapability.setApplication(applicationMock);
		capabilities.add(editUserCapability);
		return capabilities;
	}

	private UserSecurityProfileVO createTestDataUserSecurityProfileVO()
	{
		UserSecurityProfileVO testUserSecurityProfileVO = new UserSecurityProfileVOBuilder()
				.setLoginId("pogi@pogi.com")
				.setPassword("passwordMoTo")
				.createUserSecurityProfileVO();
		return testUserSecurityProfileVO;
	}

	/**
	 * Test of checkAccessRights method, of class SecurityManagerImpl.
	 */
	@Test
	public void testCheckValidAccessRights() throws Exception
	{

		System.out.println("checkValidAccessRights");
		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString("Add")
				.setActionID(Long.MIN_VALUE)
				.setResourceAsString("User")
				.setResourceID(Long.MIN_VALUE)
				.setApplicationID(applicationVOMock.getId())
				.setApplicationToken(applicationVOMock.getToken())
				.setUserProfileVO(userSecurityProfileVOMock)
				.createAccessRightsVO();
		when(userSecurityProfileVOMock.getRegistrationToken()).thenReturn("token");
		when(userSecurityProfileVOMock.getRegistrationTokenExpiration()).thenReturn(new Date());
		when(userCapabilityDAOMock.loadCapabilitiesOnApplicationOf(any(UserSecurityProfile.class), any(Application.class))).thenReturn(getCapabilityOnAddingUser());
		when(applicationManageMock.loadApplicationWithID(applicationVOMock.getId())).thenReturn(applicationVOMock);
		instance.checkAccessRights(accessRights);
	}

	@Test(expected = SecurityAuthorizationException.class)
	public void testCheckInvalidAccessRights() throws Exception
	{
		System.out.println("checkInvalidAccessRights");
		try
		{
			AccessRightsVO accessRights = new AccessRightsVOBuilder()
					.setActionAsString("Edit")
					.setActionID(Long.MIN_VALUE + 1L)
					.setResourceAsString("User")
					.setResourceID(Long.MIN_VALUE)
					.setUserProfileVO(userSecurityProfileVOMock)
					.createAccessRightsVO();

			SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();

			when(userCapabilityDAOMock.loadCapabilitiesOnApplicationOf(any(UserSecurityProfile.class), any(Application.class))).thenReturn(getCapabilityOnAddingUser());
			instance.checkAccessRights(accessRights);
		}
		catch (SecurityException e)
		{
			LOG.warning(e.getMessage());
			throw e;
		}
	}

	@Test(expected = SecurityAuthorizationException.class)
	public void testRestrictedResource() throws Exception
	{
		System.out.println("restrictedResource");
		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();

		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString("Edit")
				.setActionID(Long.MIN_VALUE + 1L)
				.setResourceAsString("restrictedResource")
				.setResourceID(Long.MIN_VALUE)
				.setUserProfileVO(userSecurityProfileVOMock)
				.setApplicationID(applicationVOMock.getId())
				.setApplicationToken(applicationVOMock.getToken())
				.createAccessRightsVO();
		List<Capability> emptyCapability = new ArrayList<Capability>();
		when(userCapabilityDAOMock.loadCapabilitiesOnApplicationOf(userSecurityProfileMock, applicationMock)).thenReturn(emptyCapability);

		ActionVO actionVOtoReturn = new ActionVO();
		actionVOtoReturn.setId(Long.MIN_VALUE);
		actionVOtoReturn.setName("Edit");

		ResourceVO resourceVOtoReturn = new ResourceVOBuilder()
				.setId(Long.MIN_VALUE)
				.setName("restrictedResource")
				.createResourceVO();

		when(capabilityManagerMock.findOrAddActionNamedAs(accessRights.getAction().getName())).thenReturn(actionVOtoReturn);
		when(capabilityManagerMock.findOrAddSecuredResourceNamedAs(accessRights.getResource().getName())).thenReturn(resourceVOtoReturn);
		try
		{
			instance.checkAccessRights(accessRights);
		}
		catch (Exception e)
		{
			LOG.info(e.getMessage());
			throw e;
		}

	}

	@Test
	public void testNonRestrictedResource() throws Exception
	{
		System.out.println("nonRestrictedResource");
		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();

		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString("Edit")
				.setActionID(Long.MIN_VALUE + 1L)
				.setResourceAsString("User")
				.setResourceID(Long.MIN_VALUE)
				.setUserProfileVO(userSecurityProfileVOMock)
				.setApplicationID(applicationVOMock.getId())
				.setApplicationToken(applicationVOMock.getToken())				
				.createAccessRightsVO();
		List<Capability> emptyCapability = new ArrayList<Capability>();
		when(userCapabilityDAOMock.loadCapabilitiesOnApplicationOf(userSecurityProfileMock, applicationMock)).thenReturn(emptyCapability);
		when(userSecurityProfileVOMock.getRegistrationToken()).thenReturn("token");
		when(userSecurityProfileVOMock.getRegistrationTokenExpiration()).thenReturn(new Date());
		ActionVO actionVOtoReturn = new ActionVO();
		actionVOtoReturn.setId(Long.MIN_VALUE);
		actionVOtoReturn.setName("Edit");

		when(capabilityManagerMock.findOrAddActionNamedAs(accessRights.getAction().getName())).thenReturn(actionVOtoReturn);
		when(capabilityManagerMock.findOrAddSecuredResourceNamedAs(accessRights.getResource().getName())).thenThrow(NotSecuredResourceException.class);
		when(applicationManageMock.loadApplicationWithID(applicationVOMock.getId())).thenReturn(applicationVOMock);
		instance.checkAccessRights(accessRights);

	}

	@Test
	public void testNonRestrictedResourceWithAUserThatHasCapability() throws
			Exception
	{
		System.out.println("NonRestrictedResourceWithAUserThatHasCapability");
		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();

		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString("Edit")
				.setActionID(Long.MIN_VALUE + 1L)
				.setResourceAsString("User")
				.setResourceID(Long.MIN_VALUE)
				.setUserProfileVO(userSecurityProfileVOMock)
				.setApplicationID(applicationVOMock.getId())
				.setApplicationToken(applicationVOMock.getToken())				
				.createAccessRightsVO();
		when(userCapabilityDAOMock.loadCapabilitiesOnApplicationOf(userSecurityProfileMock, applicationMock)).thenReturn(getCapabilityOnAddingUser());
		when(userSecurityProfileVOMock.getRegistrationToken()).thenReturn("token");
		when(userSecurityProfileVOMock.getRegistrationTokenExpiration()).thenReturn(new Date());
		ActionVO actionVOtoReturn = new ActionVO();
		actionVOtoReturn.setId(Long.MIN_VALUE);
		actionVOtoReturn.setName("Edit");

		when(capabilityManagerMock.findOrAddActionNamedAs(accessRights.getAction().getName()))
				.thenReturn(actionVOtoReturn);
		when(capabilityManagerMock.findOrAddSecuredResourceNamedAs(accessRights.getResource().getName()))
				.thenThrow(new NotSecuredResourceException("resource is not restricted"));
		when(applicationManageMock.loadApplicationWithID(applicationVOMock.getId()))
				.thenReturn(applicationVOMock);
		instance.checkAccessRights(accessRights);

	}

	@Test
	public void testCheckEditAccessRights() throws Exception
	{
		System.out.println("checkInvalidAccessRights");
		UserSecurityProfileVO userSecurityProfileVO = createTestDataUserProfileVO();
		UserSecurityProfile userSecurityProfile = SecurityEntityValueObjectConverter.toUserProfile(userSecurityProfileVO);
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString("Edit")
				.setActionID(Long.MIN_VALUE + 1L)
				.setResourceAsString("User")
				.setResourceID(Long.MIN_VALUE)
				.setUserProfileVO(userSecurityProfileVO)
				.setApplicationID(applicationVOMock.getId())
				.setApplicationToken(applicationVOMock.getToken())								
				.createAccessRightsVO();

		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();

		when(userCapabilityDAOMock.loadCapabilitiesOnApplicationOf(userSecurityProfileMock, applicationMock)).thenReturn(getCapabilityOnEditingUser());
		when(applicationManageMock.loadApplicationWithID(applicationVOMock.getId()))
				.thenReturn(applicationVOMock);
		instance.checkAccessRights(accessRights);
	}

	@Test
	public void testCreateNewSecurityProfile() throws Exception
	{
		System.out.println("createNewSecurityProfile");
		SystemSecurityManagerImpl ssm = prepareSecurityManagerInstanceToTest();
		UserSecurityProfileVO userSecurityProfileVO = createTestDataUserSecurityProfileVO();

		ssm.createNewSecurityProfile(userSecurityProfileVO);
	}

	private SystemSecurityManagerImpl prepareSecurityManagerInstanceToTest()
	{
		userSecurityProfileManager = new UserSecurityProfileManager();
		userSecurityProfileManager.setUserSecurityProfileDAO(userSecurityProfileDAOMock);
		userSecurityProfileManager.setUserCapabilityDAO(userCapabilityDAOMock);
		userSecurityProfileManager.setUserRoleDAO(userRoleDAOMock);
		userSecurityProfileManager.setUserGroupDAO(userGroupDAOMock);
		SystemSecurityManagerImpl systemSecurityManager = new SystemSecurityManagerImpl();
		systemSecurityManager.setCapabilityManager(capabilityManagerMock);
		systemSecurityManager.setUserSecurityProfileManager(userSecurityProfileManager);
		systemSecurityManager.setApplicationManager(applicationManageMock);
		return systemSecurityManager;
	}
}
