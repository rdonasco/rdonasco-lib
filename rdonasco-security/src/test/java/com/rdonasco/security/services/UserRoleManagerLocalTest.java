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

import com.rdonasco.security.dao.UserRoleDAO;
import com.rdonasco.security.model.UserRole;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.vo.UserRoleVO;
import com.rdonasco.security.vo.UserRoleVOBuilder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import static org.junit.Assert.*;
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
	private UserRoleManagerLocal userRoleManager;

	@Deployment
	public static JavaArchive createTestArchive()
	{
		JavaArchive archive = ArchiveCreator.createCommonArchive();
		archive.addPackage(UserRoleDAO.class.getPackage())
				.addPackage(UserRole.class.getPackage())
				.addPackage(UserRoleManagerLocal.class.getPackage())
				.addPackage(UserRoleVO.class.getPackage())
				.addClass(SecurityEntityValueObjectConverter.class);

		return archive;
	}

	@Test
	public void testDeleteUserRole() throws Exception
	{
		LOG.log(Level.INFO, "deleteUserRole");
		UserRoleVO userRole = new UserRoleVOBuilder()
				.setName("data to delete")
				.createUserRoleVO();
		userRole = userRoleManager.saveData(userRole);
		userRoleManager.deleteData(userRole);
		UserRoleVO savedRole = userRoleManager.loadData(userRole);
		assertNull(savedRole);
	}

	@Test
	public void testCreateUserRole() throws Exception
	{
		LOG.log(Level.INFO, "createUserRole");
		UserRoleVO userRole = new UserRoleVOBuilder()
				.setName("new role")
				.createUserRoleVO();
		userRoleManager.saveData(userRole);
		assertNotNull(userRole.getId());
	}

	@Test
	public void testUpdateUserRole() throws Exception
	{
		LOG.log(Level.INFO, "updateUserRole");
		UserRoleVO userRole = new UserRoleVOBuilder()
				.setName("data to delete")
				.createUserRoleVO();
		userRole = userRoleManager.saveData(userRole);
		String newName = userRole.getName() + "-modified";
		userRole.setName(newName);
		userRoleManager.updateData(userRole);
		UserRoleVO updatedUserRole = userRoleManager.loadData(userRole);
		assertEquals("data not equal", userRole, updatedUserRole);
		assertEquals("mismatch on name", newName, updatedUserRole.getName());
	}

	@Test
	public void testRetrieveAll() throws Exception
	{
		LOG.log(Level.INFO, "retrieveAll");
		UserRoleVO userRole = new UserRoleVOBuilder()
				.setName("data to retrieve")
				.createUserRoleVO();
		userRole = userRoleManager.saveData(userRole);
		List<UserRoleVO> roles = userRoleManager.retrieveAllData();
		assertTrue("failed to find user role", roles.contains(userRole));

	}
}