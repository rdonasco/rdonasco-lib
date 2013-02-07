/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.services;

import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.rdonasco.security.vo.UserSecurityProfileVOBuilder;
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
public class SecurityEntityValueObjectConverterTest
{
	
	public SecurityEntityValueObjectConverterTest()
	{
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
	 * Test of toUserProfile method, of class SecurityEntityValueObjectConverter.
	 */
	@Test
	public void testToUserProfileNoCapability() throws Exception
	{
		System.out.println("toUserProfileNoCapability");
		UserSecurityProfileVO userSecurityProfileVO = new UserSecurityProfileVOBuilder()
				.setId(Long.MIN_VALUE)
				.setLoginId(anyString())
				.createUserSecurityProfileVO();
		UserSecurityProfile result = SecurityEntityValueObjectConverter.toUserProfile(userSecurityProfileVO);
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(new Long(Long.MIN_VALUE), result.getId());
		
	}

	/**
	 * Test of toCapabilityVO method, of class SecurityEntityValueObjectConverter.
	 */
	@Test
	public void testToCapabilityVO() throws Exception
	{
		System.out.println("toCapabilityVO");
		Capability capability = null;
		CapabilityVO expResult = null;
		CapabilityVO result = SecurityEntityValueObjectConverter.toCapabilityVO(capability);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toResource method, of class SecurityEntityValueObjectConverter.
	 */
	@Test
	public void testToResource()
	{
		System.out.println("toResource");
		ResourceVO resource = null;
		Resource expResult = null;
		Resource result = SecurityEntityValueObjectConverter.toResource(resource);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
