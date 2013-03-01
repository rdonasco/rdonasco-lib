/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 24-Feb-2013
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
package com.rdonasco.security.services;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.config.exceptions.ConfigXPathException;
import com.rdonasco.config.services.ConfigDataManagerProxyRemote;
import com.rdonasco.config.vo.ConfigElementVO;
import com.rdonasco.security.exceptions.SystemSecurityInitializationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class SystemSecurityInitializer implements SystemSecurityInitializerLocal
{

	private static final Logger LOG = Logger.getLogger(SystemSecurityInitializer.class.getName());
	@EJB
	private ConfigDataManagerProxyRemote configDataManager;

	@Override
	public void initializeDefaultSystemAccessCapabilities() throws
			SystemSecurityInitializationException
	{
		try
		{
			initializeConfigurationEntries();
		}
		catch (Exception e)
		{
			throw new SystemSecurityInitializationException(e);
		}

	}

	void initializeConfigurationEntries()
	{
		for (String[] configInitString : SystemSecurityInitializerLocal.DEFAULT_CAPABILITY_ELEMENTS)
		{
			String xpath = configInitString[SystemSecurityInitializerLocal.ELEMENT_XPATH];
			String title = configInitString[SystemSecurityInitializerLocal.ELEMENT_CAPABILITY_TITLE];
			String resource = configInitString[SystemSecurityInitializerLocal.ELEMENT_RESOURCE];
			final String pathSeparator = "/";
			String configDefaultCapabilityXPath = new StringBuilder(xpath)
					.append(pathSeparator).append(title).toString();
			String configResourceXpath = new StringBuilder(configDefaultCapabilityXPath)
					.append(pathSeparator).append(SystemSecurityInitializerLocal.ATTRIBUTE_RESOURCE).toString();
			try
			{
				configDataManager.createAttributeFromXpath(configResourceXpath, resource);
				String configActionXPath;
				for (int i = SystemSecurityInitializerLocal.ELEMENT_RESOURCE + 1; i < configInitString.length; i++)
				{
					configActionXPath = new StringBuilder(configDefaultCapabilityXPath)
							.append(pathSeparator).append(SystemSecurityInitializerLocal.ATTRIBUTE_ACTION).toString();
					configDataManager.createAttributeFromXpath(configActionXPath, configInitString[i]);
				}
			}
			catch (DataAccessException ex)
			{
				LOG.log(Level.SEVERE, ex.getMessage(), ex);
			}
			catch (ConfigXPathException ex)
			{
				LOG.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
	}

}
