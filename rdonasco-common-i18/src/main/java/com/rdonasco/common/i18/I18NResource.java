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

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roy F. Donasco
 */
public class I18NResource
{

    private static ResourceBundle bundle;

    public static String localize(String stringToLocalize)
    {
        String key = createKeyFrom(stringToLocalize);
        try
        {
            return getBundle().getString(key);
        }
        catch (MissingResourceException e)
        {
            Logger.getLogger(I18NResource.class.getName()).log(Level.FINE, e.getMessage(), e);
            return new StringBuilder("cannot localize string with key ").append(key).toString();
        }
    }

    public static String localizeWithParameter(String stringToLocalize, Object... parameters)
    {
        String localizedString = localize(stringToLocalize);
        StringBuilder parameterizedString = new StringBuilder(localizedString);
        for (int i = 0; i < parameters.length; i++)
        {       
            int index = 0;
            String stringToReplace = "{" + i + "}";
            while ( ( index = parameterizedString.indexOf(stringToReplace) ) != -1)
            {
                if(null == parameters[i])
                {
                    parameters[i] = "null";
                }
                parameterizedString.replace(index, index + stringToReplace.length(), parameters[i].toString());                
            }            
        }

        return parameterizedString.toString();
    }

    public static String createKeyFrom(String stringToLocalize)
    {
        return stringToLocalize.replace(' ', '.').toLowerCase();
    }

	public static ResourceBundle getBundle()
	{
		if(null == bundle)
		{
			throw new NullPointerException("I18NResource.bundle not set. Please use I18NResource.setBundle() to set it.");
		}
		return bundle;
	}

	public static void setBundle(ResourceBundle aBundle)
	{
		bundle = aBundle;
	}
}
