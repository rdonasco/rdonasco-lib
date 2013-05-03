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

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.security.dao.RoleDAO;
import com.rdonasco.security.model.Role;
import com.rdonasco.security.utils.CapabilityTestUtility;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.RoleCapabilityVO;
import com.rdonasco.security.vo.RoleCapabilityVOBuilder;
import com.rdonasco.security.vo.RoleVO;
import com.rdonasco.security.vo.RoleVOBuilder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import test.util.ArchiveCreator;

/**
 *
 * @author Roy F. Donasco
 */
@RunWith(Arquillian.class)
public class UserRoleManagerLocalTest
{
	private static final Logger LOG = Logger.getLogger(UserRoleManagerLocalTest.class.getName());

	@EJB
	private RoleManagerLocal userRoleManager;

	@EJB
	private CapabilityManagerRemote capabilityManager;

	private CapabilityTestUtility testUtility;

	@Deployment
	public static JavaArchive createTestArchive()
	{
		JavaArchive archive = ArchiveCreator.createCommonArchive();
		archive.addPackage(RoleDAO.class.getPackage())
				.addPackage(Role.class.getPackage())
				.addPackage(RoleManagerLocal.class.getPackage())
				.addPackage(RoleVO.class.getPackage())
				.addClass(SecurityEntityValueObjectConverter.class);

		return archive;
	}

	@Before
	public void setup()
	{
		testUtility = new CapabilityTestUtility(capabilityManager);
	}

	@Test
	public void testDeleteUserRole() throws Exception
	{
		LOG.log(Level.INFO, "deleteUserRole");
		RoleVO userRole = new RoleVOBuilder()
				.setName("data to delete")
				.createUserRoleVO();
		userRole = userRoleManager.saveData(userRole);
		userRoleManager.deleteData(userRole);
		RoleVO savedRole = userRoleManager.loadData(userRole);
		assertNull(savedRole);
	}

	@Test
	public void testCreateUserRole() throws Exception
	{
		LOG.log(Level.INFO, "createUserRole");
		RoleVO userRole = createRoleWithNoCapability("new role");
		assertNotNull(userRole.getId());
	}

	@Test
	public void testUpdateUserRole() throws Exception
	{
		LOG.log(Level.INFO, "updateUserRole");
		RoleVO userRole = createRoleWithNoCapability("user manager");
		String newName = userRole.getName() + "-modified";
		userRole.setName(newName);
		userRoleManager.updateData(userRole);
		RoleVO updatedUserRole = userRoleManager.loadData(userRole);
		assertEquals("data not equal", userRole, updatedUserRole);
		assertEquals("mismatch on name", newName, updatedUserRole.getName());
	}

	@Test
	public void testRetrieveAll() throws Exception
	{
		LOG.log(Level.INFO, "retrieveAll");
		RoleVO userRole = createRoleWithNoCapability("data to retrieve");
		List<RoleVO> roles = userRoleManager.retrieveAllData();
		assertTrue("failed to find user role", roles.contains(userRole));

	}

	@Test
	public void testAddRoleWithCapability() throws Exception
	{
		LOG.log(Level.INFO, "addRoleWithCapability");
		CapabilityVO capability = testUtility.createTestDataCapabilityWithActionAndResourceName("edit", "user");
		RoleVO savedrole = createRoleNamedAndWithCapability("sexy role", capability);
		RoleVO newRole = userRoleManager.loadData(savedrole);
		assertNotNull("role not saved", newRole);
		assertNotNull("role did not have an id", newRole.getId());
		assertNotNull("capability not added to role", newRole.getRoleCapabilities());
		assertTrue("capability not added to role", !newRole.getRoleCapabilities().isEmpty());
		for (RoleCapabilityVO roleCapability : newRole.getRoleCapabilities())
		{
			assertNotNull("null roleCapability.id", roleCapability.getId());
		}
	}

	private RoleVO createRoleNamedAndWithCapability(String roleName,
			CapabilityVO capabilityVO) throws DataAccessException
	{
		RoleVO userRole = new RoleVOBuilder()
				.setName(roleName)
				.addCapability(capabilityVO)
				.createUserRoleVO();
		userRole = userRoleManager.saveData(userRole);
		return userRole;
	}

	private RoleVO createRoleWithNoCapability(String roleName) throws
			DataAccessException
	{
		return createRoleNamedAndWithCapability(roleName, null);
	}
}
