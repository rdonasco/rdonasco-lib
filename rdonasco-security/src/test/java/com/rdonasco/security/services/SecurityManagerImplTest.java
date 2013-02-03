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

import com.rdonasco.security.dao.CapabilityDAO;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.SecuredAction;
import com.rdonasco.security.model.SecuredResource;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import java.util.ArrayList;
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
//@RunWith(Arquillian.class)
public class SecurityManagerImplTest
{

	private static final Logger LOG = Logger.getLogger(SecurityManagerImplTest.class.getName());
	private static CapabilityDAO securityDAOMock;
	private static UserSecurityProfile userSecurityProfile;
	
	public SecurityManagerImplTest()
	{
	}
//	@EJB
//	private SecurityManager securityManager;

//	@Deployment
//	public static JavaArchive createTestArchive()
//	{
//		JavaArchive archive = ArchiveCreator.createCommonArchive();
//		addAdditionalResources(archive);
//		return archive;
//	}
	@BeforeClass
	public static void setUpClass()
	{
		securityDAOMock = mock(CapabilityDAO.class);
		userSecurityProfile = mock(UserSecurityProfile.class);
	}

	@AfterClass
	public static void tearDownClass()
	{
	}

	@Before
	public void setUp()
	{
		reset(securityDAOMock);
		reset(userSecurityProfile);
	}

	@After
	public void tearDown()
	{
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
				.setUserProfile(userSecurityProfile)
				.createAccessRightsVO();

		SecurityManager instance = new SecurityManagerImpl();
		instance.setSecurityDAO(securityDAOMock);

		when(securityDAOMock.loadCapabilitiesOf(userSecurityProfile)).thenReturn(getCapabilityOnAddingUser());

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
				.setUserProfile(userSecurityProfile)
				.createAccessRightsVO();

		SecurityManager instance = new SecurityManagerImpl();
		instance.setSecurityDAO(securityDAOMock);

		when(securityDAOMock.loadCapabilitiesOf(userSecurityProfile)).thenReturn(getCapabilityOnAddingUser());

		instance.checkAccessRights(accessRights);
	}

	private List<Capability> getCapabilityOnAddingUser()
	{
		Capability capability = new Capability();
		capability.setId(Long.MIN_VALUE);
		capability.setTitle("Manage Users");
		SecuredResource resource = new SecuredResource();
		resource.setName("User");
		resource.setId(Long.MIN_VALUE);
		capability.setResource(resource);
		List<SecuredAction> actions = new ArrayList<SecuredAction>();
		SecuredAction addAction = new SecuredAction();
		addAction.setName("Add");
		addAction.setId(Long.MIN_VALUE);
		actions.add(addAction);
		capability.setActions(actions);
		List<Capability> capabilities = new ArrayList<Capability>();
		capabilities.add(capability);
		return capabilities;
	}
}
