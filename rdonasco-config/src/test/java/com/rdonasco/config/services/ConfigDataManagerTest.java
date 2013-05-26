/*
 * Copyright 2012 Roy F. Donasco.
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
package com.rdonasco.config.services;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import com.rdonasco.common.dao.BaseDAO;
import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.datamanager.utils.CommonConstants;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.config.dao.ConfigElementDAO;
import com.rdonasco.config.data.ConfigAttribute;
import com.rdonasco.config.data.ConfigData;
import com.rdonasco.config.data.ConfigElement;
import com.rdonasco.config.parsers.ValueParser;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.runner.RunWith;

/**
 *
 * @author Roy F. Donasco
 */
@RunWith(Arquillian.class)
public class ConfigDataManagerTest
{

	public ConfigDataManagerTest()
	{
	}
	@EJB
	private ConfigDataManagerLocal configDataManagerUnderTest;

	private static int rootElementSeed;

	@Deployment
	public static JavaArchive createTestArchive()
	{
		return ShrinkWrap.create(JavaArchive.class, "ConfigDataManagerTest.jar")
				.addPackage(BaseDAO.class.getPackage())
				.addPackage(DataAccessException.class.getPackage())
				.addPackage(I18NResource.class.getPackage())
				.addPackage(CommonConstants.class.getPackage())
				.addPackage(DataManager.class.getPackage())
				.addPackage(ConfigElementDAO.class.getPackage())
				.addPackage(ValueParser.class.getPackage())
				.addPackage(ConfigElement.class.getPackage())
				.addPackage(ConfigDataManagerLocal.class.getPackage())
				.addAsManifestResource(new ByteArrayAsset("<beans/>".getBytes()), ArchivePaths.create("beans.xml"))
				.addAsResource(I18NResource.class.getPackage(), "i18nResource.properties", "/WEB-INF/classes/net/baligya/i18n")
				.addAsManifestResource("persistence.xml", ArchivePaths.create("persistence.xml"));

	}

	@BeforeClass
	public static void setUpClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownClass() throws Exception
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
	 * Test of saveData method, of class ConfigDataManager.
	 */
	@Test
	public void testSaveData() throws Exception
	{
		System.out.println("saveData");
		ConfigElement configElement = new ConfigElement();
		configElement.setName("testElement");

		ConfigElement result = configDataManagerUnderTest.saveData(configElement);
		assertNotNull(result.getId());
		assertTrue("Version not saved", result.getVersion() > 0);
	}

	@Test
	public void testSaveSubConfig() throws Exception
	{
		System.out.println("saveSubConfig");
		ConfigElement savedParentConfig = createAndSaveParentConfig();
		createAndSaveSubConfig(savedParentConfig);

		savedParentConfig = configDataManagerUnderTest.loadData(
				savedParentConfig);
		assertNotNull("subConfigElements not initialized", savedParentConfig.
				getSubConfigElements());
		assertTrue("subConfigElements not loaded", savedParentConfig.
				getSubConfigElements().size() > 0);
	}

	private ConfigAttribute createAndSaveAttribute(
			ConfigElement parentConfigElement)
			throws DataAccessException
	{
		ConfigAttribute attribute = new ConfigAttribute();
		attribute.setName("host");
		attribute.setValue("roy.pogi.com");
		attribute.setParentConfig(parentConfigElement);

		ConfigAttribute savedConfigAttribute = configDataManagerUnderTest.
				saveAttribute(attribute);
		assertNotNull(savedConfigAttribute.getId());
		return savedConfigAttribute;
	}

	private ConfigAttribute createAndSaveIntegerAttribute(
			ConfigElement parentConfigElement)
			throws DataAccessException
	{
		ConfigAttribute attribute = new ConfigAttribute();
		attribute.setName("interval");
		attribute.setValue("1");
		attribute.setParentConfig(parentConfigElement);

		ConfigAttribute savedConfigAttribute = configDataManagerUnderTest.
				saveAttribute(attribute);
		assertNotNull(savedConfigAttribute.getId());
		return savedConfigAttribute;
	}

	private ConfigAttribute createAndSaveDateAttribute(
			ConfigElement parentConfigElement)
			throws DataAccessException
	{
		ConfigAttribute attribute = new ConfigAttribute();
		attribute.setName("birthDate");
		attribute.setValue("1970-06-01");
		attribute.setParentConfig(parentConfigElement);

		ConfigAttribute savedConfigAttribute = configDataManagerUnderTest.
				saveAttribute(attribute);
		assertNotNull(savedConfigAttribute.getId());
		return savedConfigAttribute;
	}

	private ConfigElement createAndSaveSubConfig(ConfigElement savedParentConfig)
			throws DataAccessException
	{
		ConfigElement subConfigElement = new ConfigElement();
		subConfigElement.setName("subConfigElement");
		subConfigElement.setParentConfig(savedParentConfig);
		ConfigElement savedSubConfig = configDataManagerUnderTest.saveData(
				subConfigElement);
		assertNotNull("failed to save sub config element",
				savedSubConfig.getId());
		assertTrue("version not updated", savedSubConfig.getVersion() > 0);
		return savedSubConfig;
	}

	private ConfigElement createAndSaveParentConfig() throws DataAccessException
	{
		ConfigElement configElement = new ConfigElement();
		configElement.setName("parentConfig" + (rootElementSeed++));
		ConfigElement savedParentConfig = configDataManagerUnderTest.saveData(
				configElement);
		assertNotNull("failed to save parent config", savedParentConfig.getId());
		assertTrue("version not updated", savedParentConfig.getVersion() > 0);
		return savedParentConfig;
	}

	@Test
	public void testSaveAttributeOfRootConfig() throws Exception
	{
		System.out.println("saveAttributeOfRootConfig");
		ConfigElement rootConfig = new ConfigElement();
		rootConfig.setName("rootConfig");
		ConfigElement savedRootConfig = configDataManagerUnderTest.saveData(
				rootConfig);
		createAndSaveAttribute(savedRootConfig);

		savedRootConfig = configDataManagerUnderTest.loadData(savedRootConfig);

		assertNotNull("attributes not initalized",
				savedRootConfig.getAttributes());
		assertTrue("attributes not loaded",
				savedRootConfig.getAttributes().size() > 0);

	}

	@Test
	public void testSaveAttributeOfSubConfig() throws Exception
	{
		System.out.println("saveAttributeOfSubConfig");
		ConfigElement rootConfig = createAndSaveParentConfig();
		ConfigElement subConfig = createAndSaveSubConfig(rootConfig);
		ConfigAttribute attribute = createAndSaveAttribute(subConfig);
		assertEquals(subConfig, attribute.getParentConfig());
	}

	@Test
	public void testDeleteSubRecursive() throws Exception
	{
		System.out.println("deleteSubRecursive");
		ConfigElement rootConfig = createAndSaveParentConfig();
		ConfigElement subConfig = createAndSaveSubConfig(rootConfig);
		ConfigAttribute attribute = createAndSaveAttribute(subConfig);
		Long id = rootConfig.getId();
		configDataManagerUnderTest.deleteData(subConfig);
		ConfigElement deletedElement = configDataManagerUnderTest.loadData(
				subConfig);
		assertNull(deletedElement);
		rootConfig = configDataManagerUnderTest.loadData(rootConfig);
		configDataManagerUnderTest.deleteData(rootConfig);
		deletedElement = configDataManagerUnderTest.loadData(rootConfig);
		assertNull(deletedElement);

	}

	@Test
	public void testDeleteRootRecursive() throws Exception
	{
		System.out.println("deleteRootRecursive");
		ConfigElement rootConfig = createAndSaveParentConfig();
		ConfigElement subConfig = createAndSaveSubConfig(rootConfig);
		ConfigAttribute attribute = createAndSaveAttribute(subConfig);
		configDataManagerUnderTest.deleteData(rootConfig);
		ConfigElement deletedElement = configDataManagerUnderTest.loadData(
				rootConfig);
		assertNull(deletedElement);
	}

	@Test
	public void testConfigureXpath() throws Exception
	{
		System.out.println("configureXpath");
		ConfigData roy = new ConfigElement();
		roy.setName("roy");
		roy = configDataManagerUnderTest.configureXpath(roy);
		assertEquals("firstLevel", "/roy", roy.getXpath());
		ConfigData donasco = new ConfigElement();
		donasco.setName("donasco");
		donasco.setParentConfig(roy);
		donasco = configDataManagerUnderTest.configureXpath(donasco);
		assertEquals("secondLevel", "/roy/donasco", donasco.getXpath());
		ConfigData flores = new ConfigAttribute();
		flores.setName("flores");
		flores.setParentConfig(donasco);
		flores = configDataManagerUnderTest.configureXpath(flores);
		assertEquals("thirdLevel", "/roy/donasco/flores", flores.getXpath());
	}

	@Test
	public void testConfigureXpathRecursive() throws Exception
	{
		System.out.println("configureXpath");
		ConfigData roy = new ConfigElement();
		roy.setName("roy");
		ConfigData donasco = new ConfigElement();
		donasco.setName("donasco");
		donasco.setParentConfig(roy);
		ConfigData flores = new ConfigAttribute();
		flores.setName("flores");
		flores.setParentConfig(donasco);
		flores = configDataManagerUnderTest.configureXpath(flores);
		assertEquals("/roy/donasco/flores", flores.getXpath());
	}

	@Test
	public void testFindElementWithXpath() throws Exception
	{
		System.out.println("findElementWithXpath");
		ConfigElement parent = createAndSaveParentConfig();
		ConfigElement sub = createAndSaveSubConfig(parent);
		ConfigElement foundSubElement = configDataManagerUnderTest.
				findConfigElementWithXpath(sub.getXpath());
		assertNotNull(foundSubElement);

	}

	@Test(expected = NonExistentEntityException.class)
	public void testNotFoundElementWithXpath() throws Exception
	{
		System.out.println("NotFoundElementWithXpath");
		ConfigElement foundSubElement = configDataManagerUnderTest.
				findConfigElementWithXpath("/path/that/cannot/be/found");
		assertNull(foundSubElement);

	}

	@Test(expected = com.rdonasco.common.exceptions.DataAccessException.class)
	public void testFindElementWithXpathReturningMultipleRecords() throws
			Exception
	{
		System.out.println("findElementWithXpathReturningMultipleRecords");
		ConfigElement parent = createAndSaveParentConfig();
		ConfigElement sub = createAndSaveSubConfig(parent);
		createAndSaveSubConfig(parent);
		configDataManagerUnderTest.
				findConfigElementWithXpath(sub.getXpath());
	}

	@Test
	public void testFindElementsWithXpath() throws
			Exception
	{
		System.out.println("findElementsWithXpath");
		ConfigElement parent = createAndSaveParentConfig();
		ConfigElement sub = createAndSaveSubConfig(parent);
		createAndSaveSubConfig(parent);
		List<ConfigElement> foundSubElements = configDataManagerUnderTest.
				findConfigElementsWithXpath(sub.getXpath());
		assertEquals(2, foundSubElements.size());
	}

	@Test
	public void testNotFoundElementsWithXpath() throws
			Exception
	{
		System.out.println("NotFoundElementsWithXpath");
		List<ConfigElement> foundSubElements = configDataManagerUnderTest.
				findConfigElementsWithXpath("/element/that/cannot/be/found");
		assertTrue(foundSubElements.isEmpty());
	}

	@Test
	public void testFindAttributeWithXpath() throws Exception
	{
		System.out.println("findAttributeWithXpath");
		ConfigElement parent = createAndSaveParentConfig();
		ConfigElement sub = createAndSaveSubConfig(parent);
		ConfigAttribute attrib = createAndSaveAttribute(sub);
		ConfigAttribute foundAttribute = configDataManagerUnderTest.
				findConfigAttributeWithXpath(attrib.getXpath());
		assertNotNull(foundAttribute);
	}

	@Test(expected = NonExistentEntityException.class)
	public void testNotFoundAttributeWithXpath() throws Exception
	{
		System.out.println("NotFoundAttributeWithXpath");
		configDataManagerUnderTest.
				findConfigAttributeWithXpath("/attribute/that/cannot/be/found");
	}

	@Test(expected = com.rdonasco.common.exceptions.DataAccessException.class)
	public void testFindAttributeWithXpathReturningMultipleRecords() throws
			Exception
	{
		System.out.println("findAttributeWithXpathReturningMultipleRecords");
		ConfigElement parent = createAndSaveParentConfig();
		ConfigElement sub = createAndSaveSubConfig(parent);
		ConfigAttribute attrib = createAndSaveAttribute(sub);
		ConfigAttribute attrib2 = createAndSaveAttribute(sub);
		ConfigAttribute foundAttribute = configDataManagerUnderTest.
				findConfigAttributeWithXpath(attrib2.getXpath());
		assertNotNull(foundAttribute);
	}

	@Test
	public void testFindAttributesWithXpath() throws
			Exception
	{
		System.out.println("findAttributesWithXpath");
		ConfigElement parent = createAndSaveParentConfig();
		ConfigElement sub = createAndSaveSubConfig(parent);
		ConfigAttribute attrib = createAndSaveAttribute(sub);
		ConfigAttribute attrib2 = createAndSaveAttribute(sub);
		List<ConfigAttribute> foundAttributes = configDataManagerUnderTest.
				findConfigAttributesWithXpath(attrib2.getXpath());
		assertEquals(2, foundAttributes.size());
	}

	@Test
	public void testGetStringValue() throws Exception
	{
		System.out.println("getStringValue");
		ConfigElement parent = createAndSaveParentConfig();
		ConfigElement sub = createAndSaveSubConfig(parent);
		ConfigAttribute attrib = createAndSaveAttribute(sub);
		String value = configDataManagerUnderTest.loadValue(attrib.getXpath(),
				String.class);
		assertEquals(attrib.getValue(), value);

	}

	@Test
	public void testGetIntegerValue() throws Exception
	{
		System.out.println("getIntegerValue");
		ConfigElement parent = createAndSaveParentConfig();
		ConfigElement sub = createAndSaveSubConfig(parent);
		ConfigAttribute attrib = createAndSaveIntegerAttribute(sub);
		Integer value = configDataManagerUnderTest.loadValue(attrib.getXpath(),
				Integer.class);
		assertEquals(new Integer(attrib.getValue()), value);
	}

	@Test
	public void testGetDateValue() throws Exception
	{
		System.out.println("getDateValue");
		ConfigElement parent = createAndSaveParentConfig();
		ConfigElement sub = createAndSaveSubConfig(parent);
		ConfigAttribute attrib = createAndSaveDateAttribute(sub);
		Date value = configDataManagerUnderTest.loadValue(attrib.getXpath(),
				Date.class);
		Format sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date expected = (Date) sdf.parseObject(attrib.getValue());
		assertEquals(expected, value);
	}

	@Test
	public void testCreateAttributeFromXpath() throws Exception
	{
		System.out.println("createAttributeFromXpath");
		String xpath = "/application/name";
		String value = "baligya";
		ConfigAttribute attribute = configDataManagerUnderTest.createAttributeFromXpath(xpath, value);
		assertNotNull(attribute);
		assertEquals(xpath, attribute.getXpath());
		String savedValue = configDataManagerUnderTest.loadValue(xpath, String.class);
		assertNotNull(savedValue);
		assertEquals(attribute.getValue(), savedValue);

	}

	@Test
	public void testCreateDefaultAttributeFromXpathFourLevels() throws Exception
	{
		System.out.println("CreateDefaultAttributeFromXpathFourLevels");
		String xpath = "/levelOne/levelTwo/levelThree/levelFour";
		String value = "valueOfAttribute";
		String retrievedValue = configDataManagerUnderTest.loadValue(xpath, String.class, value);
		assertNotNull(retrievedValue);
		String savedValue = configDataManagerUnderTest.loadValue(xpath, String.class);
		assertNotNull(savedValue);
	}

	@Test
	public void testCreateAttributeFromXpathFourLevels() throws Exception
	{
		System.out.println("CreateAttributeFromXpathFourLevels");
		String xpath = "/levelOne/levelTwo/levelThree/levelFourA";
		String value = "valueOfAttribute";
		ConfigAttribute attribute = configDataManagerUnderTest.createAttributeFromXpath(xpath, value);
		assertNotNull(attribute);
		assertEquals(xpath, attribute.getXpath());
		String savedValue = configDataManagerUnderTest.loadValue(xpath, String.class);
		assertNotNull(savedValue);
		assertEquals(attribute.getValue(), savedValue);

	}

	@Test
	public void testCreateAttributeFromXpathThreeLevels() throws Exception
	{
		System.out.println("CreateAttributeFromXpathThreeLevels");
		String xpath = "/levelOne/levelTwo/levelThree";
		String value = "baligya";
		ConfigAttribute attribute = configDataManagerUnderTest.createAttributeFromXpath(xpath, value);
		assertNotNull(attribute);
		assertEquals(xpath, attribute.getXpath());
		String savedValue = configDataManagerUnderTest.loadValue(xpath, String.class);
		assertNotNull(savedValue);
		assertEquals(attribute.getValue(), savedValue);

	}

	@Test
	public void testCreateMultipleAttributeFromXPath() throws Exception
	{
		System.out.println("createMultipleAttributeFromXPath");
		String xpath = "/application/multipleAttribute";
		ConfigAttribute firstAttribute = configDataManagerUnderTest.createAttributeFromXpath(xpath, "value1");
		assertNotNull(firstAttribute);
		ConfigAttribute secondAttribute = configDataManagerUnderTest.createAttributeFromXpath(xpath, "value2");
		assertNotNull(secondAttribute);
		List<ConfigAttribute> attributes = configDataManagerUnderTest.findConfigAttributesWithXpath(xpath);
		assertNotNull(attributes);
		assertEquals(2, attributes.size());

	}

	@Test
	public void testGetDefaultStringValue() throws Exception
	{
		System.out.println("getDefaultValue");
		String defaultValue = "baligya";
		String value = configDataManagerUnderTest.loadValue("/system/name",
				String.class, defaultValue);
		assertEquals(defaultValue, value);
		String savedValue = configDataManagerUnderTest.loadValue("/system/name", String.class);
		assertEquals(defaultValue, savedValue);
	}

	@Test
	public void testGetDefaultBooleanValue() throws Exception
	{
		System.out.println("getDefaultBooleanValue");
		final String SYSTEM_INTERCEPTOR_ENABLED_XPATH = "/system/interceptors/enabled";
		Boolean defaultBooleanValue = Boolean.FALSE;
		Boolean savedValue = configDataManagerUnderTest.loadValue(SYSTEM_INTERCEPTOR_ENABLED_XPATH, Boolean.class, Boolean.FALSE);
		assertEquals(defaultBooleanValue, savedValue);
		Boolean nonDefaultValue = configDataManagerUnderTest.loadValue(SYSTEM_INTERCEPTOR_ENABLED_XPATH, Boolean.class);
		assertEquals("boolean value did not match", defaultBooleanValue, nonDefaultValue);
//		String stringValue = configDataManagerUnderTest.loadValue(SYSTEM_INTERCEPTOR_ENABLED_XPATH, String.class);
//		System.out.println("stringValue = " + stringValue);
//		assertNotNull(stringValue);
	}
}
