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
package com.rdonasco.config.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import com.rdonasco.config.data.ConfigAttribute;
import com.rdonasco.config.data.ConfigData;
import com.rdonasco.config.data.ConfigElement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roy F. Donasco
 */
public class XpathToConfigTransformer
{

	private static final Logger LOG = Logger.getLogger(XpathToConfigTransformer.class.getName());

	public static List<ConfigData> transform(String xpath)
	{
		StringTokenizer tokenizer = new StringTokenizer(xpath, "/");
		int tokens = tokenizer.countTokens();
		List<ConfigData> configList = new ArrayList<ConfigData>(tokens);
		ConfigElement element = null;
		for (int i = 0; i < tokens - 1; i++)
		{
			ConfigElement parent = element;
			element = new ConfigElement();
			element.setName(tokenizer.nextToken());
			if (parent != null)
			{
				parent.addSubConfig(element);
			}
			configList.add(element);
			configureXpath(element);

		}
		ConfigAttribute attribute = new ConfigAttribute();
		attribute.setName(tokenizer.nextToken());
		attribute.setParentConfig(element);
		configureXpath(attribute);
		if(element != null)
		{
			element.addConfigAttribute(attribute);
		}
		configList.add(attribute);
		return configList;
	}

	public static ConfigData configureXpath(ConfigData configData)
	{
		try
		{
			StringBuilder xPathBuilder = new StringBuilder();
			if (null != configData && null != configData.getParentConfig())
			{
				if (null == configData.getParentXpath() || !configData.getParentXpath().startsWith("/"))
				{
					configureXpath(configData.getParentConfig());
				}
				xPathBuilder.append(configData.getParentXpath());
			}
			if (null != configData)
			{
				xPathBuilder.append("/").append(configData.getName());
				configData.setXpath(xPathBuilder.toString());
			}
		}
		catch (Exception e)
		{
			LOG.log(Level.FINE,e.getMessage(),e);
		}
		return configData;
	}
}
