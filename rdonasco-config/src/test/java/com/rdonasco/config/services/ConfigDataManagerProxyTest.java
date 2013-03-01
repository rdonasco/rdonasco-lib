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

import javax.ejb.EJB;
import com.rdonasco.common.dao.BaseDAO;
import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.datamanager.utils.CommonConstants;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.config.dao.ConfigElementDAO;
import com.rdonasco.config.data.ConfigElement;
import com.rdonasco.config.parsers.ValueParser;
import com.rdonasco.config.vo.ConfigAttributeVO;
import com.rdonasco.config.vo.ConfigAttributeVOBuilder;
import com.rdonasco.config.vo.ConfigElementVO;
import com.rdonasco.config.vo.ConfigElementVOBuilder;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ConfigDataManagerProxyTest
{
	private static final Logger LOG = Logger.getLogger(ConfigDataManagerProxyTest.class.getName());
	private static final String VALUE_BALIGYA = "baligya";

	public ConfigDataManagerProxyTest()
	{
	}
	@EJB
	private ConfigDataManagerProxyRemote configDataManagerProxyUnderTest;
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
	
	@Test
	public void testSaveData() throws Exception
	{
		LOG.info("saveData");
		ConfigElementVO configElement = new ConfigElementVOBuilder()
				.setName("testElement")
				.createConfigElementVO();

		ConfigElementVO result = configDataManagerProxyUnderTest.saveConfigElement(configElement);
		assertNotNull(result.getId());
	}

	@Test
	public void testSaveSubConfig() throws Exception
	{
		LOG.info("saveSubConfig");
		ConfigElementVO savedParentConfig = createAndSaveParentConfig();
		createAndSaveSubConfig(savedParentConfig);

		savedParentConfig = configDataManagerProxyUnderTest.loadConfigElement(savedParentConfig);
		assertNotNull("subConfigElements not initialized", savedParentConfig.
				getSubConfigElementVOList());
		assertTrue("subConfigElements not loaded", savedParentConfig.
				getSubConfigElementVOList().size() > 0);
	}

	private ConfigAttributeVO createAndSaveAttribute(
			ConfigElementVO parentConfigElement)
			throws DataAccessException
	{
		ConfigAttributeVO attribute = new ConfigAttributeVOBuilder()
				.setName("host")
				.setValue("roy.pogi.com")
				.setParentConfig(parentConfigElement)
				.createConfigAttributeVO();


		ConfigAttributeVO savedConfigAttribute = configDataManagerProxyUnderTest.
				saveAttribute(attribute);
		assertNotNull(savedConfigAttribute.getId());
		return savedConfigAttribute;
	}

    private ConfigAttributeVO createAndSaveIntegerAttribute(ConfigElementVO parentConfigElement)
            throws DataAccessException
    {
        ConfigAttributeVO attribute = new ConfigAttributeVOBuilder()
				.setName("interval")
				.setValue("1")
				.setParentConfig(parentConfigElement)
				.createConfigAttributeVO();

        ConfigAttributeVO savedConfigAttribute = configDataManagerProxyUnderTest.
                saveAttribute(attribute);
        assertNotNull(savedConfigAttribute.getId());
        return savedConfigAttribute;
    }

    private ConfigAttributeVO createAndSaveDateAttribute(ConfigElementVO parentConfigElement)
            throws DataAccessException
    {
        ConfigAttributeVO attribute = new ConfigAttributeVOBuilder()
				.setName("birthDate")
				.setValue("1970-06-11")
				.setParentConfig(parentConfigElement)
				.createConfigAttributeVO();

        ConfigAttributeVO savedConfigAttribute = configDataManagerProxyUnderTest.
                saveAttribute(attribute);
        assertNotNull(savedConfigAttribute.getId());
        return savedConfigAttribute;
    }

	private ConfigElementVO createAndSaveSubConfig(
			ConfigElementVO savedParentConfig)
			throws DataAccessException
	{
		ConfigElementVO subConfigElement = new ConfigElementVOBuilder()
				.setName("subConfigElement")
				.setParentConfig(savedParentConfig)
				.createConfigElementVO();
		ConfigElementVO savedSubConfig = configDataManagerProxyUnderTest.saveConfigElement(subConfigElement);
		assertNotNull("failed to save sub config element",
				savedSubConfig.getId());
		return savedSubConfig;
	}

	private ConfigElementVO createAndSaveParentConfig() throws
			DataAccessException
	{
		ConfigElementVO configElement = new ConfigElementVOBuilder()
				.setName("parentConfig" + (rootElementSeed++))
				.createConfigElementVO();
		ConfigElementVO savedParentConfig = configDataManagerProxyUnderTest.saveConfigElement(configElement);
		assertNotNull("failed to save parent config", savedParentConfig.getId());
		return savedParentConfig;
	}

	@Test
	public void testSaveAttributeOfRootConfig() throws Exception
	{
		LOG.info("saveAttributeOfRootConfig");
		ConfigElementVO rootConfig = new ConfigElementVOBuilder()
				.setName("rootConfig")
				.createConfigElementVO();
		ConfigElementVO savedRootConfig = configDataManagerProxyUnderTest.saveConfigElement(rootConfig);
		createAndSaveAttribute(savedRootConfig);

		savedRootConfig = configDataManagerProxyUnderTest.loadConfigElement(savedRootConfig);

		assertNotNull("attributes not initalized",
				savedRootConfig.getAttributeVOList());
		assertTrue("attributes not loaded",
				savedRootConfig.getAttributeVOList().size() > 0);

	}

    @Test
    public void testSaveAttributeOfSubConfig() throws Exception
    {
        LOG.info("saveAttributeOfSubConfig");
        ConfigElementVO rootConfig = createAndSaveParentConfig();
        ConfigElementVO subConfig = createAndSaveSubConfig(rootConfig);
        ConfigAttributeVO attribute = createAndSaveAttribute(subConfig);
        assertEquals(subConfig, attribute.getParentConfig());
    }

    @Test
    public void testDeleteSubRecursive() throws Exception
    {
        LOG.info("deleteSubRecursive");
        ConfigElementVO rootConfig = createAndSaveParentConfig();
        ConfigElementVO subConfig = createAndSaveSubConfig(rootConfig);
        createAndSaveAttribute(subConfig);
        configDataManagerProxyUnderTest.deleteConfigElement(subConfig);
        ConfigElementVO deletedElement = configDataManagerProxyUnderTest.loadConfigElement(subConfig);
        assertNull(deletedElement);
        rootConfig = configDataManagerProxyUnderTest.loadConfigElement(rootConfig);
        configDataManagerProxyUnderTest.deleteConfigElement(rootConfig);
        deletedElement = configDataManagerProxyUnderTest.loadConfigElement(rootConfig);
        assertNull(deletedElement);

    }

    @Test
    public void testDeleteRootRecursive() throws Exception
    {
        LOG.info("deleteRootRecursive");
        ConfigElementVO rootConfig = createAndSaveParentConfig();
        ConfigElementVO subConfig = createAndSaveSubConfig(rootConfig);
        createAndSaveAttribute(subConfig);
        configDataManagerProxyUnderTest.deleteConfigElement(rootConfig);
        ConfigElementVO deletedElement = configDataManagerProxyUnderTest.loadConfigElement(rootConfig);
        assertNull(deletedElement);
    }

    @Test
    public void testFindElementWithXpath() throws Exception
    {
        LOG.info("findElementWithXpath");
        ConfigElementVO parent = createAndSaveParentConfig();
        ConfigElementVO sub = createAndSaveSubConfig(parent);
        ConfigElementVO foundSubElement = configDataManagerProxyUnderTest.
                findConfigElementWithXpath(sub.getXpath());
        assertNotNull(foundSubElement);
		assertEquals(sub.getXpath(),foundSubElement.getXpath());
		LOG.log(Level.INFO, "xpath={0}", foundSubElement.getXpath());

    }

    @Test(expected = com.rdonasco.common.exceptions.DataAccessException.class)
    public void testFindElementWithXpathReturningMultipleRecords() throws
            Exception
    {
        LOG.info("findElementWithXpathReturningMultipleRecords");
        ConfigElementVO parent = createAndSaveParentConfig();
        ConfigElementVO sub = createAndSaveSubConfig(parent);
        createAndSaveSubConfig(parent);
        configDataManagerProxyUnderTest.findConfigElementWithXpath(sub.getXpath());
    }

    @Test
    public void testFindElementsWithXpath() throws
            Exception
    {
        LOG.info("findElementsWithXpath");
        ConfigElementVO parent = createAndSaveParentConfig();
        ConfigElementVO sub = createAndSaveSubConfig(parent);
        createAndSaveSubConfig(parent);
        List<ConfigElementVO> foundSubElements = configDataManagerProxyUnderTest.
                findConfigElementsWithXpath(sub.getXpath());
        assertEquals(2, foundSubElements.size());
    }

    @Test
    public void testFindAttributeWithXpath() throws Exception
    {
        LOG.info("findAttributeWithXpath");
        ConfigElementVO parent = createAndSaveParentConfig();
        ConfigElementVO sub = createAndSaveSubConfig(parent);
        ConfigAttributeVO attrib = createAndSaveAttribute(sub);
        ConfigAttributeVO foundAttribute = configDataManagerProxyUnderTest.
                findConfigAttributeWithXpath(attrib.getXpath());
        assertNotNull(foundAttribute);
    }

    @Test(expected = com.rdonasco.common.exceptions.DataAccessException.class)
    public void testFindAttributeWithXpathReturningMultipleRecords() throws
            Exception
    {
        LOG.info("findAttributeWithXpathReturningMultipleRecords");
        ConfigElementVO parent = createAndSaveParentConfig();
        ConfigElementVO sub = createAndSaveSubConfig(parent);
        createAndSaveAttribute(sub);
        ConfigAttributeVO attrib2 = createAndSaveAttribute(sub);
        ConfigAttributeVO foundAttribute = configDataManagerProxyUnderTest.
                findConfigAttributeWithXpath(attrib2.getXpath());
        assertNotNull(foundAttribute);
    }

    @Test
    public void testFindAttributesWithXpath() throws
            Exception
    {
        System.out.println("findAttributesWithXpath");
        ConfigElementVO parent = createAndSaveParentConfig();
        ConfigElementVO sub = createAndSaveSubConfig(parent);
        createAndSaveAttribute(sub);
        ConfigAttributeVO attrib2 = createAndSaveAttribute(sub);
        List<ConfigAttributeVO> foundAttributes = configDataManagerProxyUnderTest.
                findConfigAttributesWithXpath(attrib2.getXpath());
        assertEquals(2, foundAttributes.size());
    }

    @Test
    public void testGetStringValue() throws Exception
    {
        LOG.info("getStringValue");
        ConfigElementVO parent = createAndSaveParentConfig();
        ConfigElementVO sub = createAndSaveSubConfig(parent);
        ConfigAttributeVO attrib = createAndSaveAttribute(sub);
        String value = configDataManagerProxyUnderTest.loadValue(attrib.getXpath(),
                String.class);
        assertEquals(attrib.getValue(), value);

    }

    @Test
    public void testGetIntegerValue() throws Exception
    {
        LOG.info("getIntegerValue");
        ConfigElementVO parent = createAndSaveParentConfig();
        ConfigElementVO sub = createAndSaveSubConfig(parent);
        ConfigAttributeVO attrib = createAndSaveIntegerAttribute(sub);
        Integer value = configDataManagerProxyUnderTest.loadValue(attrib.getXpath(),
                Integer.class);
        assertEquals(new Integer(attrib.getValue()), value);
    }

    @Test
    public void testGetDateValue() throws Exception
    {
        LOG.info("getDateValue");
        ConfigElementVO parent = createAndSaveParentConfig();
        ConfigElementVO sub = createAndSaveSubConfig(parent);
        ConfigAttributeVO attrib = createAndSaveDateAttribute(sub);
        Date value = configDataManagerProxyUnderTest.loadValue(attrib.getXpath(),
                Date.class);
        Format sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expected = (Date) sdf.parseObject(attrib.getValue());
        assertEquals(expected, value);
    }
    
    
    @Test
	public void testCreateAttributeFromXpath() throws Exception
	{
		LOG.info("createAttributeFromXpath");
		String xpath = "/application/name";
		String value = VALUE_BALIGYA;
		ConfigAttributeVO attribute = configDataManagerProxyUnderTest.createAttributeFromXpath(xpath, value);
		assertNotNull(attribute);
		assertEquals(xpath, attribute.getXpath());
		assertEquals(value, attribute.getValue());
		String savedValue = configDataManagerProxyUnderTest.loadValue(xpath, String.class);
		assertNotNull(savedValue);
	}
	
//    @Test
//	public void testCreateAttributeFromXpathThreeLevels() throws Exception
//	{
//		LOG.info("createAttributeFromXpath");
//		String xpath = "/bla/bla/sublevel";
//		String value = VALUE_BALIGYA;
//		ConfigAttributeVO attribute = configDataManagerProxyUnderTest.createAttributeFromXpath(xpath, value);
//		assertNotNull(attribute);
//		assertEquals(xpath, attribute.getXpath());
//		assertEquals(value, attribute.getValue());
//		String savedValue = configDataManagerProxyUnderTest.loadValue(xpath, String.class);
//		assertNotNull(savedValue);
//	}	

    @Test
    public void testGetDefaultStringValue() throws Exception
    {
        LOG.info("getDefaultStringValue");
        String defaultValue = "baligya";
        String value = configDataManagerProxyUnderTest.loadValue("/system/name",
                String.class,defaultValue);
        assertEquals(defaultValue, value);
        String savedValue = configDataManagerProxyUnderTest.loadValue("/system/name", String.class);
        assertEquals(defaultValue,savedValue);
    }
	
    @Test
    public void testGetDefaultIntegerValue() throws Exception
    {
        LOG.info("getDefaultIntegerValue");
		String xpath = "/system/timeout";
        Integer expectedValue = 100;
        Integer value = configDataManagerProxyUnderTest.loadValue(xpath,
                Integer.class,expectedValue);
        assertEquals(expectedValue, value);
        Integer savedValue = configDataManagerProxyUnderTest.loadValue(xpath, Integer.class);
        assertEquals(expectedValue,savedValue);
    }	
}
