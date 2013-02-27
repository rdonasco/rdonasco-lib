/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 27-Feb-2013
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
package com.rdonasco.config.vo;

import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;

/**
 *
 * @author Roy F. Donasco
 */
public class ConfigElementVOTest extends TestCase
{

	public ConfigElementVOTest(String testName)
	{
		super(testName);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * Test of organizeAttributesByName method, of class ConfigElementVO.
	 */
	public void testOrganizeAttributesByName()
	{
		System.out.println("organizeAttributesByName");

		List<ConfigAttributeVO> attributes = prepareTestDataConfigAttributeVOs();
		ConfigElementVO instance = prepareTestDataConfigElementVOs(attributes);
		assertEquals("A", instance.getAttributeVOList().get(0).getName());
		assertEquals("B", instance.getAttributeVOList().get(1).getName());
		assertEquals("X", instance.getAttributeVOList().get(2).getName());
		assertEquals("X", instance.getAttributeVOList().get(3).getName());

	}

	/**
	 * Test of getAttributesNamed method, of class ConfigElementVO.
	 */
	public void testGetAttributesNamed()
	{
		System.out.println("getAttributesNamed");
		List<ConfigAttributeVO> attributes = prepareTestDataConfigAttributeVOs();
		ConfigElementVO instance = prepareTestDataConfigElementVOs(attributes);
		List<ConfigAttributeVO> foundAttribs = instance.getAttributesNamed("X");
		assertEquals(2, foundAttribs.size());
		foundAttribs = instance.getAttributesNamed("B");
		assertEquals(1, foundAttribs.size());
		foundAttribs = instance.getAttributesNamed("A");
		assertEquals(1, foundAttribs.size());		
	}

	private List<ConfigAttributeVO> prepareTestDataConfigAttributeVOs()
	{
		List<ConfigAttributeVO> attributes = new ArrayList<ConfigAttributeVO>();
		attributes.add(new ConfigAttributeVOBuilder()
				.setId(Long.MIN_VALUE)
				.setName("X")
				.setValue("2")
				.createConfigAttributeVO());
		attributes.add(new ConfigAttributeVOBuilder()
				.setId(Long.MIN_VALUE)
				.setName("X")
				.setValue("1")
				.createConfigAttributeVO());
		attributes.add(new ConfigAttributeVOBuilder()
				.setId(Long.MIN_VALUE)
				.setName("B")
				.setValue("1")
				.createConfigAttributeVO());
		attributes.add(new ConfigAttributeVOBuilder()
				.setId(Long.MIN_VALUE)
				.setName("A")
				.setValue("1")
				.createConfigAttributeVO());
		return attributes;
	}

	private ConfigElementVO prepareTestDataConfigElementVOs(
			List<ConfigAttributeVO> attributes)
	{
		ConfigElementVO instance = new ConfigElementVOBuilder()
				.setName("sample")
				.setValue(null)
				.createConfigElementVO();
		instance.setAttributeVOList(attributes);
		return instance;
	}
}
