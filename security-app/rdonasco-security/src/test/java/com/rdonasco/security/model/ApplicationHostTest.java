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
package com.rdonasco.security.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Roy F. Donasco
 */
public class ApplicationHostTest
{
	private static final String COMPARISON_FAILED = "comparison failed";

	private static final Logger LOG = Logger.getLogger(ApplicationHostTest.class.getName());


	/**
	 * Test of equals method, of class ApplicationHost.
	 */
	@Test
	public void testEqualsWithApplication()
	{
		LOG.log(Level.INFO, "equalsWithApplication");
		Application application = new Application();
		application.setId(1L);
		ApplicationHost instance = new ApplicationHost();
		instance.setApplication(application);
		ApplicationHost compared = new ApplicationHost();
		compared.setApplication(application);
		Object obj = compared;

		boolean expResult = true;
		boolean result = instance.equals(obj);
		assertEquals(COMPARISON_FAILED, expResult, result);
	}

	@Test
	public void testNotEqualsWithApplication()
	{
		LOG.log(Level.INFO, "notEqualsWithApplication");
		Application application = new Application();
		application.setId(1L);
		ApplicationHost instance = new ApplicationHost();
		instance.setApplication(application);
		Application applicationCompared = new Application();
		applicationCompared.setId(2L);
		ApplicationHost compared = new ApplicationHost();
		compared.setApplication(applicationCompared);
		Object obj = compared;

		boolean expResult = false;
		boolean result = instance.equals(obj);
		assertEquals(COMPARISON_FAILED, expResult, result);
	}

	@Test
	public void testEqualsWithHostName()
	{
		LOG.log(Level.INFO, "equalsWithHostName");
		ApplicationHost instance = new ApplicationHost();
		final String hostName = "roy.donasco.com";
		instance.setHostNameOrIpAddress(hostName);
		ApplicationHost compared = new ApplicationHost();
		compared.setHostNameOrIpAddress(hostName);
		Object obj = compared;

		boolean expResult = true;
		boolean result = instance.equals(obj);
		assertEquals(COMPARISON_FAILED, expResult, result);
	}

	@Test
	public void testNotEqualsWithHostName()
	{
		LOG.log(Level.INFO, "notEqualsWithHostName");
		ApplicationHost instance = new ApplicationHost();
		String hostName = "roy.donasco.com";
		instance.setHostNameOrIpAddress(hostName);

		ApplicationHost compared = new ApplicationHost();
		compared.setHostNameOrIpAddress("heidi.donasco.com");
		Object obj = compared;

		boolean expResult = false;
		boolean result = instance.equals(obj);
		assertEquals(COMPARISON_FAILED, expResult, result);
	}

	@Test
	public void testEqualsWithApplicationAndHostName()
	{
		LOG.log(Level.INFO, "equalsWithApplication");
		Application application = new Application();
		application.setId(1L);
		ApplicationHost instance = new ApplicationHost();
		instance.setApplication(application);
		instance.setHostNameOrIpAddress("roy.donasco.com");
		Application applicationCompared = new Application();
		applicationCompared.setId(1L);
		ApplicationHost compared = new ApplicationHost();
		compared.setApplication(applicationCompared);
		compared.setHostNameOrIpAddress("roy.donasco.com");
		Object obj = compared;

		boolean expResult = true;
		boolean result = instance.equals(obj);
		assertEquals(COMPARISON_FAILED, expResult, result);
	}

	@Test
	public void testNotEqualHostWithApplicationAndHostName()
	{
		LOG.log(Level.INFO, "NotEqualsWithApplication");
		Application application = new Application();
		application.setId(1L);
		ApplicationHost instance = new ApplicationHost();
		instance.setApplication(application);
		instance.setHostNameOrIpAddress("roy.donasco.comw");
		Application applicationCompared = new Application();
		applicationCompared.setId(1L);
		ApplicationHost compared = new ApplicationHost();
		compared.setApplication(applicationCompared);
		compared.setHostNameOrIpAddress("roy.donasco.com");
		Object obj = compared;

		boolean expResult = false;
		boolean result = instance.equals(obj);
		assertEquals(COMPARISON_FAILED, expResult, result);
	}

	@Test
	public void testNotEqualApplicationWithApplicationAndHostName()
	{
		LOG.log(Level.INFO, "equalsWithApplication");
		Application application = new Application();
		application.setId(1L);
		ApplicationHost instance = new ApplicationHost();
		instance.setApplication(application);
		instance.setHostNameOrIpAddress("roy.donasco.com");
		Application applicationCompared = new Application();
		applicationCompared.setId(2L);
		ApplicationHost compared = new ApplicationHost();
		compared.setApplication(applicationCompared);
		compared.setHostNameOrIpAddress("roy.donasco.com");
		Object obj = compared;

		boolean expResult = false;
		boolean result = instance.equals(obj);
		assertEquals(COMPARISON_FAILED, expResult, result);
	}
}
