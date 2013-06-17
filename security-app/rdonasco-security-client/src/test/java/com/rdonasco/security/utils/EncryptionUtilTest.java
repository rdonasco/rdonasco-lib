/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 06-Mar-2013
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
package com.rdonasco.security.utils;

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
public class EncryptionUtilTest
{
	private static final String PASSWORD_TO_ENCRYPT = "password to encrypt";
	
	public EncryptionUtilTest()
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
	 * Test of encryptWithPassword method, of class EncryptionUtil.
	 */
	@Test
	public void testEncryptWithPassword() throws Exception
	{
		System.out.println("encryptWithPassword");
		String stringToEncrypt = "cool password of you know who";
		String password = PASSWORD_TO_ENCRYPT;
		String result = EncryptionUtil.encryptWithPassword(stringToEncrypt, password);
		System.out.println("result = " + result);
		assertNotSame(stringToEncrypt, result);
	}

	@Test
	public void testEncryptSameStringAsPassword() throws Exception
	{
		System.out.println("EncryptSameStringAsPassword");
		String stringToEncrypt = PASSWORD_TO_ENCRYPT;
		String password = PASSWORD_TO_ENCRYPT;
		String result = EncryptionUtil.encryptWithPassword(stringToEncrypt, password);
		System.out.println("result = " + result);
		assertNotSame(stringToEncrypt, result);
	}

	/**
	 * Test of decryptWithPassword method, of class EncryptionUtil.
	 */
	@Test
	public void testDecryptWithPassword() throws Exception
	{
		System.out.println("decryptWithPassword");
		String encryptedString = "CcodRLCmRG904cla/U374NYVvCk75Z1UI+kpD8H5fVk=";
		String password = PASSWORD_TO_ENCRYPT;
		String expResult = "cool password of you know who";
		String result = EncryptionUtil.decryptWithPassword(encryptedString, password);
		System.out.println("result = " + result);
		assertEquals(expResult, result);
	}
}