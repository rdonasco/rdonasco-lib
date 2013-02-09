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
import com.rdonasco.security.dao.CapabilityDAO;
import com.rdonasco.security.dao.ResourceDAO;
import com.rdonasco.security.exceptions.NotSecuredResourceException;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.rdonasco.security.vo.UserSecurityProfileVOBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Roy F. Donasco
 */
public class SecurityManagerImplTest
{

	private static CapabilityDAO capabilityDAOMock;// =mock(CapabilityDAO.class);
	private static UserSecurityProfileVO userSecurityProfileVOMock;
	private static UserSecurityProfile userSecurityProfileMock;
	private static ResourceDAO resourceDAOMock;

	public SecurityManagerImplTest()
	{
	}

	@BeforeClass
	public static void setUpClass()
	{
		capabilityDAOMock = mock(CapabilityDAO.class);
		userSecurityProfileVOMock = mock(UserSecurityProfileVO.class);
		resourceDAOMock = mock(ResourceDAO.class);
		userSecurityProfileMock = mock(UserSecurityProfile.class);
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
	}

	@After
	public void tearDown()
	{
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
	/**
	 * Test of checkAccessRights method, of class SecurityManagerImpl.
	 */
	@Test
	public void testCheckValidAccessRights() throws Exception
	{

		System.out.println("checkAccessRights");
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString("Add")
				.setActionID(Long.MIN_VALUE)
				.setResourceAsString("User")
				.setResourceID(Long.MIN_VALUE)
				.setUserProfileVO(userSecurityProfileVOMock)
				.createAccessRightsVO();

		SystemSecurityManagerImpl instance = new SystemSecurityManagerImpl();
		instance.setCapabilityDAO(capabilityDAOMock);
		instance.setResourceDAO(resourceDAOMock);
		when(capabilityDAOMock.loadCapabilitiesOf(userSecurityProfileMock)).thenReturn(getCapabilityOnAddingUser());
		when(resourceDAOMock.findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class)))
				.thenReturn(SecurityEntityValueObjectConverter.toResource(accessRights.getResource()));
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

		SystemSecurityManagerImpl instance = new SystemSecurityManagerImpl();
		instance.setCapabilityDAO(capabilityDAOMock);

		when(capabilityDAOMock.loadCapabilitiesOf(userSecurityProfileMock)).thenReturn(getCapabilityOnAddingUser());

		instance.checkAccessRights(accessRights);
	}

	@Test
	public void testNonRestrictedResource() throws Exception
	{
		System.out.println("nonRestrictedResource");
		SystemSecurityManagerImpl instance = new SystemSecurityManagerImpl();
		instance.setCapabilityDAO(capabilityDAOMock);
		instance.setResourceDAO(resourceDAOMock);
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString("Edit")
				.setActionID(Long.MIN_VALUE + 1L)
				.setResourceAsString("User")
				.setResourceID(Long.MIN_VALUE)
				.setUserProfileVO(userSecurityProfileVOMock)
				.createAccessRightsVO();
		List<Capability> emptyCapability = new ArrayList<Capability>();
		when(capabilityDAOMock.loadCapabilitiesOf(userSecurityProfileMock)).thenReturn(emptyCapability);
		when(resourceDAOMock.findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class)))
				.thenThrow(NonExistentEntityException.class);	
		instance.checkAccessRights(accessRights);	
		Resource resource = new Resource();
		resource.setDescription(accessRights.getResource().getDescription());
		resource.setName(accessRights.getResource().getName());		
		verify(resourceDAOMock,times(1)).create(resource);
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

		SystemSecurityManagerImpl instance = new SystemSecurityManagerImpl();
		instance.setCapabilityDAO(capabilityDAOMock);
		instance.setResourceDAO(resourceDAOMock);
		when(capabilityDAOMock.loadCapabilitiesOf(userSecurityProfile)).thenReturn(getCapabilityOnEditingUser());
		
		instance.checkAccessRights(accessRights);
	}

	@Test
	public void testSuccessfulFindResourceNamed() throws Exception
	{
		System.out.println("successfulFindResourceNamed");
		SystemSecurityManagerImpl instance = new SystemSecurityManagerImpl();
		instance.setResourceDAO(resourceDAOMock);
		Resource returnedResource = new Resource();
		returnedResource.setId(Long.MIN_VALUE);
		returnedResource.setName("anyResource");
		when(resourceDAOMock.findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class)))
				.thenReturn(returnedResource);
		ResourceVO foundResource = instance.findResourceNamedAs(returnedResource.getName());
		assertNotNull(foundResource);
	}

	@Test(expected = NonExistentEntityException.class)
	public void testNoRecordFoundInFindResourceNamed() throws Exception
	{
		System.out.println("noRecordFoundInFindResourceNamed");
		SystemSecurityManagerImpl instance = new SystemSecurityManagerImpl();
		instance.setResourceDAO(resourceDAOMock);
		Resource returnedResource = new Resource();
		returnedResource.setId(Long.MIN_VALUE);
		returnedResource.setName("anyResource");
		when(resourceDAOMock.findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class)))
				.thenThrow(NonExistentEntityException.class);
		instance.findResourceNamedAs(returnedResource.getName());
	}

	@Test
	public void testSuccessfulFindSecuredResourceNamed() throws Exception
	{
		System.out.println("successfulFindSecuredResourceNamed");
		SystemSecurityManagerImpl instance = new SystemSecurityManagerImpl();
		Resource returnedResource = new Resource();
		returnedResource.setId(Long.MIN_VALUE);
		returnedResource.setName("anyResource");
		instance.setCapabilityDAO(capabilityDAOMock);
		List<Capability> capabilities = getCapabilityOnAddingUser();
		when(capabilityDAOMock.findAllDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class)))
				.thenReturn(capabilities);

		ResourceVO foundResource = instance.findOrAddSecuredResourceNamedAs(returnedResource.getName());
		assertNotNull(foundResource);
	}

	@Test(expected = NotSecuredResourceException.class)
	public void testNotFoundSecuredResourceNamed() throws Exception
	{
		System.out.println("notFoundSecuredResourceNamed");
		SystemSecurityManagerImpl instance = new SystemSecurityManagerImpl();
		Resource returnedResource = new Resource();
		returnedResource.setId(Long.MIN_VALUE);
		returnedResource.setName("anyResource");
		instance.setCapabilityDAO(capabilityDAOMock);
		instance.setResourceDAO(resourceDAOMock);
		when(capabilityDAOMock.findAllDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class)))
				.thenReturn(null);
		when(resourceDAOMock.findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class)))
				.thenThrow(NonExistentEntityException.class);
		instance.findOrAddSecuredResourceNamedAs(returnedResource.getName());
		verify(resourceDAOMock,times(1)).create(returnedResource);
	}

	private UserSecurityProfileVO createTestDataUserProfileVO()
	{
		UserSecurityProfileVO userSecurityProfileVO = new UserSecurityProfileVOBuilder()
				.setId(Long.MIN_VALUE)
				.setLoginId("test Login ID")
				.createUserSecurityProfileVO();
		return userSecurityProfileVO;
	}
}
