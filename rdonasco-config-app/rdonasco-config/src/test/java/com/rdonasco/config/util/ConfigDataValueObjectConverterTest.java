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
import java.util.ArrayList;
import java.util.List;
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
		assertEquals(configAttribute.getName(), result.getName());
		assertEquals(configAttribute.getValue(), result.getValue());
		assertEquals(configAttribute.getXpath(), result.getXpath());
	}
	
	@Test
	public void testToConfigAttributeVOWithParent()
	{
		System.out.println("ToConfigAttributeVOWithParent");
		ConfigAttribute configAttribute = createTestDataConfigAttribute();
		ConfigElement parentConfigElement = createTestDataConfigElement();
		configAttribute.setParentConfig(parentConfigElement);
		ConfigAttributeVO result = ConfigDataValueObjectConverter.toConfigAttributeVO(configAttribute);
		assertNotNull(result);
		assertNotNull(result.getParentConfig());
		assertEquals(parentConfigElement.getId(),result.getParentConfig().getId());
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
	public void testToConfigAttribute() throws Exception
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
	
	@Test
	public void testToConfigAttributeWithParent() throws Exception
	{
		System.out.println("ToConfigAttributeWithParent");
		ConfigAttributeVO configAttributeVO = createTestDataConfigAttributeVO();
		ConfigElementVO parentElementVO = createTestDataConfigElementVO();
		configAttributeVO.setParentConfig(parentElementVO);
		ConfigAttribute configAttribute = ConfigDataValueObjectConverter.toConfigAttribute(configAttributeVO);
		assertNotNull(configAttribute);
		assertNotNull(configAttribute.getParentConfig());
		assertEquals(parentElementVO.getId(),configAttribute.getParentConfig().getId());
	}

	/**
	 * Test of toConfigElementVO method, of class ConfigDataValueObjectConverter.
	 */
	@Test
	public void testToConfigElementVO()
	{
		System.out.println("toConfigElementVO");
		ConfigElement configElement = createTestDataConfigElement();
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
	
	@Test 
	public void testToConfigElementVOWithParent()
	{
		System.out.println("ToConfigElementVOWithParent");
		ConfigElement parentElement = createTestDataConfigElement();
		ConfigElement childElement = createTestDataConfigElement();
		childElement.setParentConfig(parentElement);
		ConfigElementVO convertedChild = ConfigDataValueObjectConverter.toConfigElementVO(childElement);
		assertNotNull(convertedChild.getParentConfig());
		assertEquals(parentElement.getId(),convertedChild.getParentConfig().getId());
	}

	/**
	 * Test of toConfigElement method, of class ConfigDataValueObjectConverter.
	 */
	@Test
	public void testToConfigElement() throws Exception
	{
		System.out.println("toConfigElement");
		ConfigElementVO configElementVO = createTestDataConfigElementVO();
		ConfigElement expResult = new ConfigElement();
		expResult.setId(configElementVO.getId());
		expResult.setName(configElementVO.getName());
		expResult.setValue(configElementVO.getValue());
		expResult.setVersion(configElementVO.getVersion());
		expResult.setXpath(configElementVO.getXpath());
		ConfigElement result = ConfigDataValueObjectConverter.toConfigElement(configElementVO);
		assertEquals(expResult, result);
		assertEquals(expResult.getId(),result.getId());
		assertEquals(expResult.getName(),result.getName());
		assertTrue("expected attribute.size > 0",result.getAttributes().size() > 0);
		
	}
	
	@Test
	public void testToConfigElementWithParentElement() throws Exception
	{
		ConfigElementVO parentElementVO = createTestDataConfigElementVO();
		ConfigElementVO childElementVO = createTestDataConfigElementVO();
		childElementVO.setParentConfig(parentElementVO);
		ConfigElement converted = ConfigDataValueObjectConverter.toConfigElement(childElementVO);
		assertNotNull(converted);
		assertEquals(parentElementVO.getId(),converted.getParentConfig().getId());
	}

	private ConfigAttribute createTestDataConfigAttribute()
	{
		ConfigAttribute configAttribute = new ConfigAttribute();
		configAttribute.setId(NumberUtilities.generateRandomLongValue());
		configAttribute.setName("sampleName" + NumberUtilities.generateRandomLongValue());
		Long value = NumberUtilities.generateRandomLongValue();
		configAttribute.setValue("sampleValue-" + value);
		configAttribute.setXpath("/sampleValue-" + value);
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

	private ConfigElement createTestDataConfigElement()
	{
		ConfigElement configElement = new ConfigElement();
		configElement.setId(NumberUtilities.generateRandomLongValue());
		configElement.setName("configElementName-"+NumberUtilities.generateRandomLongValue());
		configElement.setVersion(NumberUtilities.generateRandomIntValue());
		configElement.setValue("value-" + NumberUtilities.generateRandomIntValue());
		configElement.setXpath("/root/sub");
		return configElement;
	}

	private ConfigElementVO createTestDataConfigElementVO()
	{
		ConfigAttributeVO configAttributeVO = createTestDataConfigAttributeVO();
		List<ConfigAttributeVO> attributes = new ArrayList<ConfigAttributeVO>();
		attributes.add(configAttributeVO);
		ConfigElementVO configElementVO = new ConfigElementVOBuilder()
				.setId(NumberUtilities.generateRandomLongValue())
				.setName(NumberUtilities.generateRandomIntValue() + "-name")
				.setValue("value-"+NumberUtilities.generateRandomIntValue())
				.setVersion(NumberUtilities.generateRandomIntValue())
				.setAttributeVOList(attributes)
				.setRoot(true)
				.setXpath("/xpath/subpath/"+NumberUtilities.generateRandomIntValue())
				.createConfigElementVO();
		return configElementVO;
	}
}