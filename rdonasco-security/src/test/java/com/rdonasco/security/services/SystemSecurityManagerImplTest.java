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

import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.security.dao.ActionDAO;
import com.rdonasco.security.dao.CapabilityDAO;
import com.rdonasco.security.dao.ResourceDAO;
import com.rdonasco.security.dao.UserSecurityProfileDAO;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.rdonasco.security.vo.UserSecurityProfileVOBuilder;
import java.util.ArrayList;
import java.util.List;
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
public class SystemSecurityManagerImplTest
{

	private static CapabilityDAO capabilityDAOMock;// =mock(CapabilityDAO.class);
	private static UserSecurityProfileVO userSecurityProfileVOMock;
	private static UserSecurityProfile userSecurityProfileMock;
	private static ResourceDAO resourceDAOMock;
	private static ActionDAO actionDAOMock;
	private static UserSecurityProfileDAO userSecurityProfileDAOMock;

	public SystemSecurityManagerImplTest()
	{
	}

	@BeforeClass
	public static void setUpClass()
	{
		capabilityDAOMock = mock(CapabilityDAO.class);
		userSecurityProfileVOMock = mock(UserSecurityProfileVO.class);
		resourceDAOMock = mock(ResourceDAO.class);
		userSecurityProfileMock = mock(UserSecurityProfile.class);
		actionDAOMock = mock(ActionDAO.class);
		userSecurityProfileDAOMock = mock(UserSecurityProfileDAO.class);
	}

	@AfterClass
	public static void tearDownClass()
	{
	}

	@Before
	public void setUp()
	{
		reset(capabilityDAOMock);
		reset(userSecurityProfileVOMock);
		reset(resourceDAOMock);
		reset(userSecurityProfileMock);
		reset(actionDAOMock);
		reset(userSecurityProfileDAOMock);
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
		Capability capability = new Capability();
		capability.setId(Long.MIN_VALUE);
		capability.setTitle("Manage Users");
		Resource resource = new Resource();
		resource.setName("User");
		resource.setId(Long.MIN_VALUE);
		capability.setResource(resource);
		List<Action> actions = new ArrayList<Action>();
		Action addAction = new Action();
		addAction.setName("Add");
		addAction.setId(Long.MIN_VALUE);
		actions.add(addAction);
		capability.setActions(actions);
		List<Capability> capabilities = new ArrayList<Capability>();
		capabilities.add(capability);
		return capabilities;
	}

	private List<Capability> getCapabilityOnEditingUser()
	{
		Capability capability = new Capability();
		capability.setId(Long.MIN_VALUE);
		capability.setTitle("Manage Users");
		Resource resource = new Resource();
		resource.setName("User");
		resource.setId(Long.MIN_VALUE);
		capability.setResource(resource);
		List<Action> actions = new ArrayList<Action>();
		Action addAction = new Action();
		addAction.setName("Edit");
		addAction.setId(Long.MIN_VALUE + 1L);
		actions.add(addAction);
		capability.setActions(actions);
		List<Capability> capabilities = new ArrayList<Capability>();
		capabilities.add(capability);
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

		System.out.println("checkAccessRights");
		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString("Add")
				.setActionID(Long.MIN_VALUE)
				.setResourceAsString("User")
				.setResourceID(Long.MIN_VALUE)
				.setUserProfileVO(userSecurityProfileVOMock)
				.createAccessRightsVO();
		
		

		Action action = createTestDataForActionNamed("Add");
				
		when(userSecurityProfileDAOMock.loadCapabilitiesOf(userSecurityProfileMock)).thenReturn(getCapabilityOnAddingUser());
		when(resourceDAOMock.findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class)))
				.thenReturn(SecurityEntityValueObjectConverter.toResource(accessRights.getResource()));
		when(actionDAOMock.findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class))).thenReturn(action);
		instance.checkAccessRights(accessRights);
	}

	@Test(expected = SecurityException.class)
	public void testCheckInvalidAccessRights() throws Exception
	{
		System.out.println("checkInvalidAccessRights");
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString("Edit")
				.setActionID(Long.MIN_VALUE + 1L)
				.setResourceAsString("User")
				.setResourceID(Long.MIN_VALUE)
				.setUserProfileVO(userSecurityProfileVOMock)
				.createAccessRightsVO();

		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();
		
		when(userSecurityProfileDAOMock.loadCapabilitiesOf(userSecurityProfileMock)).thenReturn(getCapabilityOnAddingUser());

		instance.checkAccessRights(accessRights);
		verify(actionDAOMock,times(1)).findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class));
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
				.createAccessRightsVO();
		List<Capability> emptyCapability = new ArrayList<Capability>();
		when(userSecurityProfileDAOMock.loadCapabilitiesOf(userSecurityProfileMock)).thenReturn(emptyCapability);
		when(resourceDAOMock.findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class)))
				.thenThrow(NonExistentEntityException.class);	
		when(actionDAOMock.findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class)))
				.thenThrow(NonExistentEntityException.class);			
		instance.checkAccessRights(accessRights);	
		Resource resource = new Resource();
		resource.setDescription(accessRights.getResource().getDescription());
		resource.setName(accessRights.getResource().getName());		
		verify(resourceDAOMock,times(1)).create(resource);
		verify(actionDAOMock,times(1)).findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class));
		verify(actionDAOMock,times(1)).create(any(Action.class));
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
				.createAccessRightsVO();

		SystemSecurityManagerImpl instance = prepareSecurityManagerInstanceToTest();
		
		when(userSecurityProfileDAOMock.loadCapabilitiesOf(userSecurityProfile)).thenReturn(getCapabilityOnEditingUser());
		
		instance.checkAccessRights(accessRights);
	}
	
	//@Test
	public void testCreateNewSecurityProfile() throws Exception
	{
		System.out.println("createNewSecurityProfile");
		SystemSecurityManagerImpl ssm = prepareSecurityManagerInstanceToTest();
		UserSecurityProfileVO userSecurityProfileVO = createTestDataUserSecurityProfileVO();

		ssm.createNewSecurityProfile(userSecurityProfileVO);
	}

	private Action createTestDataForActionNamed(String name)
	{
		Action action = new Action();
		action.setDescription(name);
		action.setName(name);
		action.setId(Long.MIN_VALUE);
		return action;
	}

	private SystemSecurityManagerImpl prepareSecurityManagerInstanceToTest()
	{
		CapabilityManagerImpl capabilityManager = new CapabilityManagerImpl();
		capabilityManager.setCapabilityDAO(capabilityDAOMock);
		capabilityManager.setResourceDAO(resourceDAOMock);
		capabilityManager.setActionDAO(actionDAOMock);
		SystemSecurityManagerImpl systemSecurityManager = new SystemSecurityManagerImpl();
		systemSecurityManager.setCapabilityManager(capabilityManager);
		systemSecurityManager.setUserSecurityProfileDAO(userSecurityProfileDAOMock);
		return systemSecurityManager;
	}


}
