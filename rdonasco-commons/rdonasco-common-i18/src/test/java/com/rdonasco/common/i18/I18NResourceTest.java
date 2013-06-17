/*
 * Copyright 2011 Roy F. Donasco.
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
package com.rdonasco.common.i18;

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
public class I18NResourceTest
{

    public I18NResourceTest()
    {
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
	
	private void configureBundleLocation()
	{
		 I18NResource.setBundle(java.util.ResourceBundle.getBundle("com/rdonasco/common/i18/i18nResource"));			
	}

    /**
     * Test of localize method, of class I18NResource.
     */
	
	@Test(expected=NullPointerException.class)
	public void testBundleNotSet() throws Exception
	{
		I18NResource.setBundle(null);
		I18NResource.localize("string to localize");
	}
	
    @Test
    public void testLocalizeString()
    {
        System.out.println("localizeString");
		configureBundleLocation();
        String stringToLocalize = "SAVE";
        String expResult = "Save";
        String result = I18NResource.localize(stringToLocalize);
        assertEquals(expResult, result);
    }

    @Test
    public void testKeyCreation()
    {
        System.out.println("testCreateKeyFrom");
        String keyToConvert = "ROY dOnasco";
        String expectedConversion = "roy.donasco";
        String result = I18NResource.createKeyFrom(keyToConvert);
        assertEquals(expectedConversion, result);
    }

    @Test
    public void testLocalizeWithParameters()
    {
        System.out.println("testLocalizeWithParameters");
		configureBundleLocation();
        String keyToConvert = "your name is _ and lastname is _";
        String expectedConversion = "your name is Roy and lastname is Donasco";
        String result = I18NResource.localizeWithParameter(keyToConvert, "Roy",
                "Donasco");
        assertEquals(expectedConversion, result);
    }

    @Test
    public void testLocalizeWithNullParameters()
    {
        System.out.println("testLocalizeWithNullParameters");
		configureBundleLocation();
        String keyToConvert = "your name is _ and lastname is _";
        String expectedConversion = "your name is null and lastname is null";
        String firstName = null;
        String lastName = null;
        String result = I18NResource.localizeWithParameter(keyToConvert, firstName, lastName);
        assertEquals(expectedConversion, result);
    }
}
