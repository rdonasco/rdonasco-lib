/*
 * PasswordGenerator.java
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

import com.rdonasco.common.utils.NumberUtilities;


/**
 *
 * @author Roy F. Donasco
 */
public class PasswordGenerator
{

	/** Creates a new instance of PasswordGenerator */
	public PasswordGenerator()
	{
	}
	private static String passwordChars =
			"AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";

	public static String generate(int passwordLength)
	{
		int start = 0;
		int end = passwordChars.length() - 1;
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < passwordLength; i++)
		{
			int index = NumberUtilities.generateRandomIntValue(start, end);
			b.append(passwordChars.substring(index, index + 1));
		}

		return b.toString();
	}

	public static String[] generateCaptcha(int wordLength, int numberOfWords)
	{
		String captcha[] = new String[numberOfWords];
		for(int i = 0; i<numberOfWords;i++)
		{
			captcha[i] = generate(wordLength);
		}
		return captcha;
	}
}
