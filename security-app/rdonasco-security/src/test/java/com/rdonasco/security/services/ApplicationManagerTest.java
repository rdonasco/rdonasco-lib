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
import com.rdonasco.security.dao.ApplicationDAOJPAImpl;
import com.rdonasco.security.model.Application;
import com.rdonasco.security.utils.ArchiveCreator;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
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
				.setId(Long.MIN_VALUE)
				.setName("Security")
				.setToken("Token")
				.createApplicationVO();
		ApplicationVO application = applicationManager.createNewApplication(applicationVO);
		assertNotNull(application);
	}
}