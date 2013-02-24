/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 24-Feb-2013
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
package com.rdonasco.config.util;

import com.rdonasco.common.utils.NumberUtilities;
import com.rdonasco.config.data.ConfigAttribute;
import com.rdonasco.config.data.ConfigElement;
import com.rdonasco.config.vo.ConfigAttributeVO;
import com.rdonasco.config.vo.ConfigAttributeVOBuilder;
import com.rdonasco.config.vo.ConfigElementVO;
import com.rdonasco.config.vo.ConfigElementVOBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Roy F. Donasco
 */
public class ConfigDataValueObjectConverterTest
{
	
	public ConfigDataValueObjectConverterTest()
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
	 * Test of toConfigAttributeVO method, of class ConfigDataValueObjectConverter.
	 */
	@Test
	public void testToConfigAttributeVO()
	{
		System.out.println("toConfigAttributeVO");
		ConfigAttribute configAttribute = createTestDataConfigAttribute();
		ConfigAttributeVO result = ConfigDataValueObjectConverter.toConfigAttributeVO(configAttribute);
		assertNotNull(result);
		assertEquals("sampleName", result.getName());
		assertEquals("sampleValue", result.getValue());
		assertEquals("/sampleValue", result.getXpath());
	}
	
	@Test
	public void testToConfigAttributeVOwithNull()
	{
		System.out.println("ToConfigAttributeVOwithNull");
		ConfigAttributeVO nullAttributeVO = ConfigDataValueObjectConverter.toConfigAttributeVO(null);
		assertNull(nullAttributeVO);
	}

	/**
	 * Test of toConfigAttribute method, of class ConfigDataValueObjectConverter.
	 */
	@Test
	public void testToConfigAttribute()
	{
		System.out.println("toConfigAttribute");
		ConfigAttributeVO configAttributeVO = createTestDataConfigAttributeVO();
		ConfigAttribute expResult = new ConfigAttribute();
		expResult.setId(configAttributeVO.getId());
		expResult.setName(configAttributeVO.getName());
		expResult.setValue(configAttributeVO.getValue());
		expResult.setXpath(configAttributeVO.getXpath());
		ConfigAttribute result = ConfigDataValueObjectConverter.toConfigAttribute(configAttributeVO);
		assertNotNull(result);
		assertEquals(expResult.getId(), result.getId());		
		assertEquals(expResult.getName(), result.getName());
		assertEquals(expResult.getValue(), result.getValue());
		assertEquals(expResult.getXpath(), result.getXpath());		
	}

	/**
	 * Test of toConfigElementVO method, of class ConfigDataValueObjectConverter.
	 */
	@Test
	public void testToConfigElementVO()
	{
		System.out.println("toConfigElementVO");
		ConfigElement configElement = new ConfigElement();
		configElement.setId(NumberUtilities.generateRandomLongValue());
		configElement.setName("configElementName-"+NumberUtilities.generateRandomLongValue());
		configElement.setVersion(NumberUtilities.generateRandomIntValue());
		configElement.setValue("value-" + NumberUtilities.generateRandomIntValue());
		configElement.setXpath("/root/sub");
		ConfigElementVO expResult = new ConfigElementVOBuilder()
				.setId(configElement.getId())
				.setName(configElement.getName())
				.setRoot(configElement.isRoot())
				.setValue(configElement.getValue())
				.setVersion(configElement.getVersion())
				.setXpath(configElement.getXpath())
				.createConfigElementVO();
		ConfigElementVO result = ConfigDataValueObjectConverter.toConfigElementVO(configElement);
		assertEquals(expResult, result);
		assertEquals(expResult.getId(), result.getId());
		assertEquals(expResult.getName(), result.getName());
		assertEquals(expResult.getValue(), result.getValue());
		assertEquals(expResult.getVersion(), result.getVersion());
		assertEquals(expResult.getXpath(), result.getXpath());
	}

	/**
	 * Test of toConfigElement method, of class ConfigDataValueObjectConverter.
	 */
	@Test
	public void testToConfigElement()
	{
		System.out.println("toConfigElement");
		ConfigElementVO configElementVO = null;
		ConfigElement expResult = null;
		ConfigElement result = ConfigDataValueObjectConverter.toConfigElement(configElementVO);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	private ConfigAttribute createTestDataConfigAttribute()
	{
		ConfigAttribute configAttribute = new ConfigAttribute();
		configAttribute.setId(Long.MIN_VALUE);
		configAttribute.setName("sampleName");
		configAttribute.setValue("sampleValue");
		configAttribute.setXpath("/sampleValue");
		return configAttribute;
	}

	private ConfigAttributeVO createTestDataConfigAttributeVO()
	{
		ConfigAttributeVO testData = new ConfigAttributeVOBuilder()
				.setId(NumberUtilities.generateRandomLongValue())
				.setName("testData-" + NumberUtilities.generateRandomLongValue())
				.setRoot(true)
				.setValue("value-" + NumberUtilities.generateRandomLongValue())
				.setXpath("/root/sub/attribute")
				.createConfigAttributeVO();
		return testData;
	}
}