/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 04-May-2013
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

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.config.dao.ConfigElementDAO;
import com.rdonasco.config.data.ConfigElement;
import com.rdonasco.config.parsers.ValueParser;
import com.rdonasco.config.services.ConfigDataManagerLocal;
import com.rdonasco.config.util.ConfigDataValueObjectConverter;
import com.rdonasco.config.vo.ConfigAttributeVO;
import com.rdonasco.security.dao.ActionDAO;
import com.rdonasco.security.exceptions.ApplicationManagerException;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.utils.ArchiveCreator;
import com.rdonasco.security.utils.CapabilityTestUtility;
import com.rdonasco.security.utils.GroupTestUtility;
import com.rdonasco.security.utils.RoleTestUtility;
import com.rdonasco.security.utils.UserSecurityProfileTestUtility;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.RoleVO;
import com.rdonasco.security.vo.SecurityGroupVO;
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserGroupVO;
import com.rdonasco.security.vo.UserRoleVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author Roy F. Donasco
 */
@RunWith(Arquillian.class)
public class UserSecurityProfileManagerTest
{

	@EJB
	private CapabilityManagerLocal capabilityManager;
	@EJB
	private ApplicationManagerLocal applicationManager;

	private CapabilityTestUtility capabilityTestUtility;

	private RoleTestUtility roleTestUtility;

	private GroupTestUtility groupTestUtility;

	private UserSecurityProfileTestUtility userSecurityProfileTestUtility;

	@EJB
	private SecurityGroupDataManagerLocal securityGroupDataManager;

	@EJB
	private RoleManagerLocal userRoleManager;

	@EJB
	private SystemSecurityManagerLocal systemSecurityManager;

	@EJB
	private UserSecurityProfileManagerLocal userSecurityProfileManager;
	
	public UserSecurityProfileManagerTest()
	{
	}

	@Deployment
	public static JavaArchive createTestArchive()
	{
		JavaArchive archive = ArchiveCreator.createCommonArchive()
				.addPackage(ConfigElementDAO.class.getPackage())
				.addPackage(ValueParser.class.getPackage())
				.addPackage(ConfigElement.class.getPackage())
				.addPackage(ConfigDataManagerLocal.class.getPackage())
				.addPackage(ConfigAttributeVO.class.getPackage())
				.addPackage(ConfigDataValueObjectConverter.class.getPackage())
				.addPackage(ActionDAO.class.getPackage())
				.addPackage(SystemSecurityManagerLocal.class.getPackage())
				.addPackage(ActionVO.class.getPackage())
				.addPackage(Action.class.getPackage());

		return archive;
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
		capabilityTestUtility = new CapabilityTestUtility(capabilityManager,applicationManager);
		roleTestUtility = new RoleTestUtility(userRoleManager);
		groupTestUtility = new GroupTestUtility(securityGroupDataManager);
		userSecurityProfileTestUtility = new UserSecurityProfileTestUtility(capabilityManager, systemSecurityManager,applicationManager);
	}

	@After
	public void tearDown()
	{
	}

	/**
	 * Test of setUserCapabilityDAO method, of class UserSecurityProfileManager.
	 */
	@Test
	public void testRetrieveRolesOfUser() throws Exception
	{
		System.out.println("retrieveRolesOfUser");
		UserSecurityProfileVO createdSecurityProfile = createUserProfileWithRoleCapabilityTo("edit", "role", "role editor");
		System.out.println("created " + createdSecurityProfile);
		assertNotNull(createdSecurityProfile);
		Collection<UserRoleVO> associatedRoles = createdSecurityProfile.getRoles();
		assertNotNull(associatedRoles);
		assertEquals("roles not associated", 1, associatedRoles.size());
		List<RoleVO> roles = userSecurityProfileManager.retrieveRolesOfUser(createdSecurityProfile);
		assertNotNull(roles);
		assertEquals("roles not associated", 1, roles.size());

	}

	@Test
	public void testRetrieveGroupsOfUser() throws Exception
	{
		System.out.println("retrieveGroupsOfUser");
		UserSecurityProfileVO createdSecurityProfile = createUserProfileAssignedToGroups("Administrators", "Facilitators");
		assertNotNull(createdSecurityProfile);
		Collection<UserGroupVO> userGroups = createdSecurityProfile.getGroups();
		assertNotNull(userGroups);
		assertEquals("group not associated", 2, userGroups.size());
		List<SecurityGroupVO> groups = userSecurityProfileManager.retrieveGroupsOfUser(createdSecurityProfile);
		assertEquals("groups.size is wrong", 2, groups.size());
	}

	@Test
	public void testUpdateGroupsOfUser() throws Exception
	{
		System.out.println("updateGroupsOfUser");
		UserSecurityProfileVO createdSecurityProfile = createUserProfileAssignedToGroups("Some Administrators", "Some Facilitators");
		UserSecurityProfileVO userSecurityProfileVO = userSecurityProfileManager.findSecurityProfileWithLogonID(createdSecurityProfile.getLogonId());
		assertNotNull("user for update not created", userSecurityProfileVO);
		List<UserGroupVO> groupsToUpdate = new ArrayList<UserGroupVO>(userSecurityProfileVO.getGroups());
		Iterator<UserGroupVO> iterator = groupsToUpdate.iterator();
		if (iterator.hasNext())
		{
			iterator.next();
			iterator.remove();
		}
		userSecurityProfileVO.setGroups(groupsToUpdate);
		createUserGroupsNamed(userSecurityProfileVO, "Idols");
		userSecurityProfileVO.setPassword(null);
		userSecurityProfileManager.updateUserSecurityProfile(userSecurityProfileVO);
		UserSecurityProfileVO updatedUserSecurityProfileVO = userSecurityProfileManager.findSecurityProfileWithLogonID(userSecurityProfileVO.getLogonId());
		assertNotNull("updated data not found", updatedUserSecurityProfileVO);
		assertEquals("wrong group size", 2, updatedUserSecurityProfileVO.getGroups().size());
		boolean foundIt = false;
		for (UserGroupVO userGroup : updatedUserSecurityProfileVO.getGroups())
		{
			if (userGroup.getGroup().getName().equals("Idols"))
			{
				foundIt = true;
				break;
			}
		}
		assertTrue("added group not found", foundIt);
	}

	@Test
	public void testRemoveUserCapability() throws Exception
	{
		System.out.println("removeUserCapability");
		UserSecurityProfileVO createdSecurityProfile = userSecurityProfileTestUtility.createNewUserSecurityProfileWithCapability();
		assertNotNull(createdSecurityProfile);
		final Iterator<UserCapabilityVO> capabilityIterator = createdSecurityProfile
				.getCapabilities().iterator();
		UserCapabilityVO userCapabilityVO;
		if (capabilityIterator.hasNext())
		{
			userCapabilityVO = capabilityIterator.next();
			int removedCount = userSecurityProfileManager.removeAllAssignedUserCapability(userCapabilityVO.getCapability());
			assertTrue("removedCount is zero ", removedCount > 0);
		}
		UserSecurityProfileVO foundSecurityProfile = userSecurityProfileManager.findSecurityProfileWithLogonID(createdSecurityProfile.getLogonId());
		assertNotNull(foundSecurityProfile);
		assertTrue(foundSecurityProfile.getCapabilities().size() <= 0);

	}

	// start of utilities here. 
	private UserSecurityProfileVO createUserProfileWithRoleCapabilityTo(
			final String action, final String resource, final String roleTitle)
			throws
			CapabilityManagerException, DataAccessException,
			SecurityManagerException, ApplicationManagerException
	{
		// prepare the capabilities
		CapabilityVO capability = capabilityTestUtility.createTestDataCapabilityWithActionAndResourceNameOnSystem(action, resource, "secured system 1");
		// prepare the roles with capability
		RoleVO role = roleTestUtility.createRoleNamedAndWithCapability(roleTitle, capability);
		// create new user profile
		UserSecurityProfileVO userSecurityProfile = userSecurityProfileTestUtility.createTestDataWithoutCapability();
		// assign the roles to the profile
		userSecurityProfile.addRole(role);
		UserSecurityProfileVO createdProfile = userSecurityProfileManager.createNewUserSecurityProfile(userSecurityProfile);
		return createdProfile;
	}

	private UserSecurityProfileVO createUserProfileAssignedToGroups(
			String... groups) throws DataAccessException,
			SecurityManagerException
	{
		UserSecurityProfileVO userSecurityProfile = userSecurityProfileTestUtility.createTestDataWithoutCapability();
		createUserGroupsNamed(userSecurityProfile, groups);
		return userSecurityProfileManager.createNewUserSecurityProfile(userSecurityProfile);
	}

	private void createUserGroupsNamed(UserSecurityProfileVO userSecurityProfile,
			String... groups) throws
			DataAccessException
	{
		for (String group : groups)
		{
			SecurityGroupVO securityGroupVO = groupTestUtility.createSecurityGroupNamed(group);
			userSecurityProfile.addGroup(securityGroupVO);
		}
	}
}
