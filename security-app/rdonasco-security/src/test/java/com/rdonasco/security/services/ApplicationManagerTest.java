/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 20-Jul-2013
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

import com.rdonasco.security.dao.ApplicationDAO;
import com.rdonasco.security.exceptions.ApplicationManagerException;
import com.rdonasco.security.model.Application;
import com.rdonasco.security.utils.ArchiveCreator;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.vo.ApplicationHostVO;
import com.rdonasco.security.vo.ApplicationVO;
import com.rdonasco.security.vo.ApplicationVOBuilder;
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
public class ApplicationManagerTest
{

	@EJB
	private ApplicationManagerLocal applicationManager;

	public ApplicationManagerTest()
	{
	}

	@Deployment
	public static JavaArchive createTestArchive()
	{
		JavaArchive archive = ArchiveCreator.createCommonArchive()
				.addPackage(ApplicationDAO.class.getPackage())
				.addClass(ApplicationManager.class)
				.addClass(ApplicationManagerLocal.class)
				.addPackage(SecurityEntityValueObjectConverter.class.getPackage())
				.addPackage(Application.class.getPackage())
				.addPackage(ApplicationVO.class.getPackage());

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

	/**
	 * Test of createNewApplication method, of class ApplicationManager.
	 */
	@Test
	public void testCreateNewApplication() throws Exception
	{
		ApplicationVO applicationVO = new ApplicationVOBuilder()
				.setName("Security")
				.setToken("Token")
				.createApplicationVO();
		ApplicationVO savedApplication = applicationManager.createNewApplication(applicationVO);
		assertNotNull("id is not set", savedApplication.getId());
	}

	@Test(expected = RuntimeException.class)
	public void testCreateNewApplicationWithNullName() throws Exception
	{
		ApplicationVO applicationVO = new ApplicationVOBuilder()
				.setToken("Token")
				.createApplicationVO();
		applicationManager.createNewApplication(applicationVO);
	}

	@Test(expected = RuntimeException.class)
	public void testCreateNewApplicationWithNullToken() throws Exception
	{
		ApplicationVO applicationVO = new ApplicationVOBuilder()
				.setName("Security")
				.createApplicationVO();
		applicationManager.createNewApplication(applicationVO);
	}

	@Test(expected = RuntimeException.class)
	public void testCreateNewApplicationWithDuplicateName() throws Exception
	{
		ApplicationVO applicationVO = new ApplicationVOBuilder()
				.setName("SecurityTwo")
				.setToken("Token1")
				.createApplicationVO();
		applicationManager.createNewApplication(applicationVO);
		ApplicationVO duplicateApplicationVO = new ApplicationVOBuilder()
				.setName("SecurityTwo")
				.setToken("Token")
				.createApplicationVO();
		applicationManager.createNewApplication(duplicateApplicationVO);
	}

	@Test
	public void testCreateNewApplicationWithHosts() throws Exception
	{
		ApplicationHostVO hostVO = new ApplicationHostVO();
		hostVO.setHostNameOrIpAddress("roy.donasco.com");
		ApplicationVO applicationVO = new ApplicationVOBuilder()
				.setName("Security with hosts")
				.setToken("Token")
				.addHost(hostVO)
				.createApplicationVO();
		ApplicationVO savedApplication = applicationManager.createNewApplication(applicationVO);
		assertNotNull("id is not set", savedApplication.getId());
		ApplicationHostVO savedHost = savedApplication.getHosts().get(0);
		assertEquals("host not saved", hostVO.getHostNameOrIpAddress(), savedHost.getHostNameOrIpAddress());
	}

	@Test
	public void testFindApplication() throws Exception
	{
		ApplicationVO applicationVO = new ApplicationVOBuilder()
				.setName("ApplicationName to find")
				.setToken("Token")
				.createApplicationVO();
		ApplicationVO savedApplication = applicationManager.createNewApplication(applicationVO);
		ApplicationVO loadedApplication = applicationManager.loadApplicationWithID(savedApplication.getId());
		assertEquals("name mismatch", savedApplication.getName(), loadedApplication.getName());
	}

	@Test
	public void testFindNonExistingApplication() throws Exception
	{
		ApplicationVO loadedApplication = applicationManager.loadApplicationWithID(Long.MAX_VALUE);
		assertNull("found an application?", loadedApplication);
	}

	@Test
	public void testUpdateApplication() throws Exception
	{
		ApplicationVO applicationVO = new ApplicationVOBuilder()
				.setName("ApplicationName to update")
				.setToken("Token")
				.createApplicationVO();
		ApplicationVO savedApplication = applicationManager.createNewApplication(applicationVO);
		savedApplication.setName("updated name");
		applicationManager.updateApplication(savedApplication);
		ApplicationVO loadedApplication = applicationManager.loadApplicationWithID(savedApplication.getId());
		assertEquals("name mismatch", savedApplication.getName(), loadedApplication.getName());
	}

	@Test
	public void testLoadApplicationByNameAndToken() throws Exception
	{
		ApplicationVO applicationVO = new ApplicationVOBuilder()
				.setName("ApplicationName to find by name")
				.setToken("Token")
				.createApplicationVO();
		ApplicationVO savedApplication = applicationManager.createNewApplication(applicationVO);
		ApplicationVO foundApplication = applicationManager
				.loadApplicationByNameAndToken(savedApplication.getName(), savedApplication.getToken());
		assertNotNull("application is not found", foundApplication);
	}

	@Test(expected = ApplicationManagerException.class)
	public void testLoadApplicationByNameAndTokenNotFound() throws Exception
	{
		ApplicationVO applicationVO = new ApplicationVOBuilder()
				.setName("ApplicationName that cannot be found")
				.setToken("Token")
				.createApplicationVO();
		applicationManager
				.loadApplicationByNameAndToken(applicationVO.getName(), applicationVO.getToken());		
	}	
}