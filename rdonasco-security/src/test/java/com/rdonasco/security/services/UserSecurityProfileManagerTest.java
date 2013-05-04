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

import com.rdonasco.config.dao.ConfigElementDAO;
import com.rdonasco.config.data.ConfigElement;
import com.rdonasco.config.parsers.ValueParser;
import com.rdonasco.config.services.ConfigDataManagerLocal;
import com.rdonasco.config.util.ConfigDataValueObjectConverter;
import com.rdonasco.config.vo.ConfigAttributeVO;
import com.rdonasco.security.dao.ActionDAO;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.utils.ArchiveCreator;
import com.rdonasco.security.utils.CapabilityTestUtility;
import com.rdonasco.security.utils.RoleTestUtility;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.RoleVO;
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

/**
 *
 * @author Roy F. Donasco
 */
@RunWith(Arquillian.class)
public class UserSecurityProfileManagerTest
{

	@EJB
	private UserSecurityProfileManagerLocal userSecurityProfileManager;

	@EJB
	private CapabilityManagerLocal capabilityManager;

	private CapabilityTestUtility capabilityTestUtility;

	private RoleTestUtility roleTestUtility;

	@EJB
	private RoleManagerLocal userRoleManager;

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
		capabilityTestUtility = new CapabilityTestUtility(capabilityManager);
		roleTestUtility = new RoleTestUtility(userRoleManager);
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
		// prepare the capabilities
		CapabilityVO capability = capabilityTestUtility.createTestDataCapabilityWithActionAndResourceName("edit", "role");

		// prepare the roles with capability
		RoleVO role = roleTestUtility.createRoleNamedAndWithCapability("role editor", capability);

		// create new user profile


		// assign the roles to the profile
	}
}