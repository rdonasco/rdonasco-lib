/*
 * EncryptionUtil.java
 *
 * Created on April 19, 2006, 12:18 AM
 *
 * Copyright 2004-2006 Roy Donasco Software Foundation.
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
 *
 */
package com.rdonasco.security.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.apache.commons.codec.binary.Base64;

/**
 * This class is used for encrypting decrypting String using password.
 *
 * @author Roy F. Donasco
 */
public class EncryptionUtil
{

	/**
	 * Creates a new instance of EncryptionUtil
	 */
	public EncryptionUtil()
	{
	}
	// Salt
	private static final byte[] SALT =
	{
		(byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
		(byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99
	};
	// Iteration count
	private static final int COUNT = 24;
	private static final PBEParameterSpec PBE_PARAM_SPEC = new PBEParameterSpec(SALT, COUNT);
	private static SecretKeyFactory keyFactory;
//	private static final String CIPHER_TYPE = "DES/ECB/PKCS5Padding";
	private static final String CIPHER_KEY_SPEC = "PBEWithMD5AndDES";
//	private static final String CIPHER_ALGORITHM = "DES";

	public static SecretKeyFactory getKeyFactory() throws Exception
	{
		if (null == keyFactory)
		{
			keyFactory = SecretKeyFactory.getInstance(CIPHER_KEY_SPEC);
		}
		return keyFactory;
	}

	public static String encryptWithPassword(String stringToEncrypt,
			String password) throws Exception
	{
		PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), SALT, COUNT);
		SecretKey pbeKey = getKeyFactory().generateSecret(pbeKeySpec);
		Cipher pbeCipher = Cipher.getInstance(CIPHER_KEY_SPEC);
		pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, PBE_PARAM_SPEC);
		byte[] encryptedBytes = pbeCipher.doFinal(stringToEncrypt.getBytes());
		return Base64.encodeBase64String(encryptedBytes);
	}

	public static String decryptWithPassword(String encryptedString,
			String password) throws Exception
	{
		PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), SALT, COUNT);
		SecretKey pbeKey = getKeyFactory().generateSecret(pbeKeySpec);
		Cipher pbeCipher = Cipher.getInstance(CIPHER_KEY_SPEC);
		pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, PBE_PARAM_SPEC);
		byte[] decryptedBytes = pbeCipher.doFinal(Base64.decodeBase64(encryptedString));
		return new String(decryptedBytes);
	}

	public static void main(String[] args) throws Exception
	{
		if (0 == args.length)
		{
			System.out.println("usage: EncryptionUtil <propertyfile>,<output.keyfile>");
			System.exit(-1);

		}
		generateEncryptedPassword(args[0], args[1]);
	}

	private static void generateEncryptedPassword(String propsFile,
			String outFile) throws Exception
	{
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try
		{
			fis = new FileInputStream(new File(propsFile));
			Properties props = new Properties();
			props.load(fis);
			String stringToEncrypt = props.getProperty("password");
			String passphrase = props.getProperty("passphrase");
			System.out.println("passphrase:<" + passphrase + ">");
			System.out.println("encrypting " + stringToEncrypt);
			String encrypted = EncryptionUtil.encryptWithPassword(stringToEncrypt, passphrase);
			System.out.println("encrypted:" + encrypted);
			String decrypted = EncryptionUtil.decryptWithPassword(encrypted, passphrase);
			System.out.println("decrypted:" + decrypted);
			if (!stringToEncrypt.equals(decrypted))
			{
				throw new Exception("password cannot be decrypted properly, please choose another password or change passphrase.");
			}
			Properties keyProperties = new Properties();
			keyProperties.put("encrypted", encrypted);
			fos = new FileOutputStream(new File(outFile));
			keyProperties.store(fos, "last updated " + new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss").format(new Date()));

		}
		finally
		{
			if (null != fis)
			{
				try
				{
					fis.close();
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}
			if (null != fos)
			{
				try
				{
					fos.close();
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}
}
