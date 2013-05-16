/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 16-May-2013
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
import com.rdonasco.security.dao.SecurityGroupDAO;
import com.rdonasco.security.model.SecurityGroup;
import com.rdonasco.security.model.SecurityGroupRole;
import com.rdonasco.security.utils.ArchiveCreator;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.vo.RoleCapabilityVOBuilder;
import com.rdonasco.security.vo.RoleVO;
import com.rdonasco.security.vo.RoleVOBuilder;
import com.rdonasco.security.vo.SecurityGroupRoleVO;
import com.rdonasco.security.vo.SecurityGroupRoleVOBuilder;
import com.rdonasco.security.vo.SecurityGroupVO;
import com.rdonasco.security.vo.SecurityGroupVOBuilder;
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
public class SecurityGroupDataManagerLocalTest
{

	@EJB
	private SecurityGroupDataManagerLocal securityGroupDataManager;

	@EJB
	private RoleManagerLocal roleManager;

	public SecurityGroupDataManagerLocalTest()
	{
	}

	@Deployment
	public static JavaArchive createTestArchive()
	{
		JavaArchive archive = ArchiveCreator.createCommonArchive();
		archive.addPackage(SecurityGroup.class.getPackage())
				.addPackage(SecurityGroupDataManagerLocal.class.getPackage())
				.addPackage(SecurityGroupDAO.class.getPackage())
				.addPackage(SecurityGroupVO.class.getPackage())
				.addClass(SecurityEntityValueObjectConverter.class);

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
	}

	@After
	public void tearDown()
	{
	}

	@Test
	public void testCreateGroup() throws Exception
	{
		System.out.println("createGroup");
		SecurityGroupVO savedData = createGroupNamed("new group");
		assertNotNull("data not saved", savedData);
		assertNotNull("id not assigned", savedData.getId());
	}

	@Test
	public void testCreateGroupWithRoles() throws Exception
	{
		System.out.println("createGroupWithRoles");
		SecurityGroupVO groupVO = createGroupNamed("User Group Managers");
		RoleVO role = createRoleNamed("UserGroup Manager");
		SecurityGroupRoleVO groupRole = new SecurityGroupRoleVOBuilder()
				.setRole(role)
				.setSecurityGroup(groupVO)
				.createSecurityGroupRoleVO();
		groupVO.getGroupRoleVOs().add(groupRole);
		securityGroupDataManager.updateData(groupVO);
		SecurityGroupVO updatedGroupVO = securityGroupDataManager.loadData(groupVO);
		assertNotNull(updatedGroupVO);
		assertNotNull("null roles", updatedGroupVO.getGroupRoleVOs());
		assertFalse("empty roles", updatedGroupVO.getGroupRoleVOs().isEmpty());
		assertTrue("role not found in group", updatedGroupVO.getGroupRoleVOs().contains(groupRole));

	}

	@Test
	public void testUpdateGroup() throws Exception
	{
		System.out.println("updateGroup");
		SecurityGroupVO securityGroup = new SecurityGroupVOBuilder()
				.setName("group to update")
				.createSecurityGroupVO();
		SecurityGroupVO savedData = securityGroupDataManager.saveData(securityGroup);
		savedData.setName("updated group");
		securityGroupDataManager.updateData(savedData);
		SecurityGroupVO updatedData = securityGroupDataManager.loadData(savedData);
		assertEquals("name not updated", savedData.getName(), updatedData.getName());
	}

	@Test(expected = DataAccessException.class)
	public void testDeleteGroup() throws Exception
	{
		System.out.println("deleteGroup");
		SecurityGroupVO securityGroup = new SecurityGroupVOBuilder()
				.setName("group to delete")
				.createSecurityGroupVO();
		SecurityGroupVO savedData = securityGroupDataManager.saveData(securityGroup);
		securityGroupDataManager.deleteData(savedData);
		securityGroupDataManager.loadData(savedData);
	}

	@Test
	public void testRetrieveAllData() throws Exception
	{
		System.out.println("retrieveAllData");
		SecurityGroupVO securityGroup = new SecurityGroupVOBuilder()
				.setName("group to retrieve")
				.createSecurityGroupVO();
		SecurityGroupVO savedData = securityGroupDataManager.saveData(securityGroup);
		List<SecurityGroupVO> allData = securityGroupDataManager.retrieveAllData();
		assertNotNull("empty or null group list", allData);
		assertTrue("empty group list", allData.size() > 0);
		assertTrue("data is missing from the list", allData.contains(savedData));
		
	}

	private SecurityGroupVO createGroupNamed(final String groupName) throws
			DataAccessException
	{
		SecurityGroupVO securityGroup = new SecurityGroupVOBuilder()
				.setName(groupName)
				.createSecurityGroupVO();
		SecurityGroupVO savedData = securityGroupDataManager.saveData(securityGroup);
		return savedData;
	}

	private RoleVO createRoleNamed(final String roleName) throws
			DataAccessException
	{
		RoleVO roleToUse = new RoleVOBuilder()
				.setName(roleName)
				.createUserRoleVO();
		return roleManager.saveData(roleToUse);
	}
}