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
import static junit.framework.Assert.*;
import org.junit.Test;

/**
 *
 * @author Roy F. Donasco
 */
public class ConfigElementVOTest
{

	/**
	 * Test of organizeAttributesByName method, of class ConfigElementVO.
	 */
	@Test
	public void testOrganizeAttributesByName() throws Exception
	{
		System.out.println("organizeAttributesByName");

		List<ConfigAttributeVO> attributes = prepareTestDataConfigAttributeVOs();
		ConfigElementVO instance = prepareTestDataConfigElementVOs("organizeAttributesByName", attributes);
		assertEquals("A", instance.getAttributeVOList().get(0).getName());
		assertEquals("B", instance.getAttributeVOList().get(1).getName());
		assertEquals("X", instance.getAttributeVOList().get(2).getName());
		assertEquals("X", instance.getAttributeVOList().get(3).getName());

	}

	/**
	 * Test of getAttributesNamed method, of class ConfigElementVO.
	 */
	@Test
	public void testGetAttributesNamed() throws Exception
	{
		System.out.println("getAttributesNamed");
		List<ConfigAttributeVO> attributes = prepareTestDataConfigAttributeVOs();
		ConfigElementVO instance = prepareTestDataConfigElementVOs("getAttributesNamed", attributes);
		List<ConfigAttributeVO> foundAttribs = instance.getAttributesNamed("X");
		assertEquals(2, foundAttribs.size());
		foundAttribs = instance.getAttributesNamed("B");
		assertEquals(1, foundAttribs.size());
		foundAttribs = instance.getAttributesNamed("A");
		assertEquals(1, foundAttribs.size());
	}

	@Test
	public void testOrganizeSubElementsByName() throws Exception
	{
		System.out.println("organizeSubElementsByName");
		List<ConfigElementVO> subElements = prepareTestDataConfigElementVOs("X", "X", "A", "B");
		ConfigElementVO instance = prepareTestDataConfigElementVOs("organizeSubElementsByName", null);
		instance.setSubConfigElementVOList(subElements);
		assertEquals("A", instance.getSubConfigElementVOList().get(0).getName());
		assertEquals("B", instance.getSubConfigElementVOList().get(1).getName());
		assertEquals("X", instance.getSubConfigElementVOList().get(2).getName());
		assertEquals("X", instance.getSubConfigElementVOList().get(3).getName());
	}

	@Test
	public void testGetSubElementsByName() throws Exception
	{
		System.out.println("getSubElementsByName");
		List<ConfigElementVO> subElements = prepareTestDataConfigElementVOs("X", "X", "A", "B");
		ConfigElementVO instance = prepareTestDataConfigElementVOs("organizeSubElementsByName", null);
		instance.setSubConfigElementVOList(subElements);
		List<ConfigElementVO> foundSubElements = instance.getSubElementsNamed("X");
		assertEquals(2, foundSubElements.size());
		foundSubElements = instance.getSubElementsNamed("B");
		assertEquals(1, foundSubElements.size());
		foundSubElements = instance.getSubElementsNamed("A");
		assertEquals(1, foundSubElements.size());
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

	private ConfigElementVO prepareTestDataConfigElementVOs(String elementName,
			List<ConfigAttributeVO> attributes)
	{
		ConfigElementVO instance = new ConfigElementVOBuilder()
				.setName(elementName)
				.setValue(null)
				.createConfigElementVO();
		instance.setAttributeVOList(attributes);
		return instance;
	}

	private List<ConfigElementVO> prepareTestDataConfigElementVOs(
			String... names)
	{
		List<ConfigElementVO> elements = new ArrayList<ConfigElementVO>(names.length);
		for (String name : names)
		{
			elements.add(prepareTestDataConfigElementVOs(name, null));
		}
		return elements;
	}
}
