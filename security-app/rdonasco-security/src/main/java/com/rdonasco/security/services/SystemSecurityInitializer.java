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
import com.rdonasco.config.exceptions.LoadValueException;
import com.rdonasco.config.services.ConfigDataManagerVODecoratorRemote;
import com.rdonasco.config.vo.ConfigAttributeVO;
import com.rdonasco.config.vo.ConfigElementVO;
import com.rdonasco.security.exceptions.ApplicationManagerException;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.exceptions.SystemSecurityInitializationException;
import com.rdonasco.security.model.ApplicationHost;
import com.rdonasco.security.utils.SecurityConstants;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.ApplicationHostVO;
import com.rdonasco.security.vo.ApplicationVO;
import com.rdonasco.security.vo.ApplicationVOBuilder;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
public class SystemSecurityInitializer implements SystemSecurityInitializerLocal,
		SystemSecurityInitializerRemote
{

	private static final Logger LOG = Logger.getLogger(SystemSecurityInitializer.class.getName());
	private ConfigDataManagerVODecoratorRemote configDataManager;
	private CapabilityManagerLocal capabilityManager;
	private ApplicationManagerLocal applicationManager;

	@EJB
	public void setConfigDataManager(
			ConfigDataManagerVODecoratorRemote configDataManager)
	{
		this.configDataManager = configDataManager;
	}

	@EJB
	public void setCapabilityManager(CapabilityManagerLocal capabilityManager)
	{
		this.capabilityManager = capabilityManager;
	}

	@EJB
	public void setApplicationManager(ApplicationManagerLocal applicationManager)
	{
		this.applicationManager = applicationManager;
	}

	@Override
	public void initializeDefaultSystemAccessCapabilities()
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
	private static final String PATH_SEPARATOR = "/";

	void initializeConfigurationEntries() throws Exception
	{
		for (String[] configInitString : SystemSecurityInitializerLocal.DEFAULT_CAPABILITY_ELEMENTS)
		{
			String xpath = configInitString[SystemSecurityInitializerLocal.ELEMENT_XPATH];
			String title = configInitString[SystemSecurityInitializerLocal.ELEMENT_CAPABILITY_TITLE];
			String resource = configInitString[SystemSecurityInitializerLocal.ELEMENT_RESOURCE];
			String configDefaultCapabilityXPath = new StringBuilder(xpath)
					.append(PATH_SEPARATOR).append(title).toString();
			String configResourceXpath = new StringBuilder(configDefaultCapabilityXPath)
					.append(PATH_SEPARATOR).append(SystemSecurityInitializerLocal.ATTRIBUTE_RESOURCE).toString();
			try
			{
				configDataManager.loadValue(configResourceXpath, String.class, resource);
				ConfigElementVO capabilityConfig = configDataManager.findConfigElementWithXpath(configDefaultCapabilityXPath);
				CapabilityVO capability;
				try
				{
					capabilityManager.findCapabilityWithTitle(capabilityConfig.getName());
				}
				catch (NonExistentEntityException e)
				{
					LOG.warning("Default capability not found. Creating one");
					capability = createDefaultCapability(configResourceXpath, capabilityConfig);
					List<ConfigAttributeVO> actions = loadDefaultActionsFromConfig(configDefaultCapabilityXPath, configInitString);
					addMissingActionsToCapability(actions, capability);
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

	CapabilityVO createDefaultCapability(String configResourceXpath,
			ConfigElementVO capabilityConfig) throws
			CapabilityManagerException,
			NonExistentEntityException, DataAccessException,
			ApplicationManagerException, ConfigXPathException
	{
		CapabilityVO capabilityVO;
		ConfigAttributeVO resourceAttributeVO = configDataManager.findConfigAttributeWithXpath(configResourceXpath);
		ResourceVO resourceVOToAdd = capabilityManager.findOrAddResourceNamedAs(resourceAttributeVO.getValue());
		ApplicationVO applicationVO = loadOrCreateApplicationInfo();

		capabilityVO = new CapabilityVOBuilder()
				.setTitle(capabilityConfig.getName())
				.setDescription(capabilityConfig.getName())
				.setResource(resourceVOToAdd)
				.setApplication(applicationVO)
				.createCapabilityVO();
		return capabilityManager.createNewCapability(capabilityVO);
	}

	List<ConfigAttributeVO> loadDefaultActionsFromConfig(
			String configDefaultCapabilityXPath, String[] configInitString)
			throws DataAccessException, ConfigXPathException
	{
		String configActionXPath = new StringBuilder(configDefaultCapabilityXPath)
				.append(PATH_SEPARATOR).append(SystemSecurityInitializerLocal.ATTRIBUTE_ACTION).toString();
		List<ConfigAttributeVO> actions = configDataManager.findConfigAttributesWithXpath(configActionXPath);
		if (actions.isEmpty())
		{
			for (int i = SystemSecurityInitializerLocal.ELEMENT_RESOURCE + 1; i < configInitString.length; i++)
			{
				actions.add(configDataManager.createAttributeFromXpath(configActionXPath, configInitString[i]));
			}
		}
		return actions;
	}

	void addMissingActionsToCapability(
			List<ConfigAttributeVO> actions, CapabilityVO capability) throws
			CapabilityManagerException
	{
		ActionVO action;
		List<ActionVO> actionsToAdd = new ArrayList<ActionVO>();
		for (ConfigAttributeVO actionAttributeVO : actions)
		{
			action = capabilityManager.findOrAddActionNamedAs(actionAttributeVO.getValue());
			if (null == capability.findActionNamed(actionAttributeVO.getValue()))
			{
				actionsToAdd.add(action);
			}
		}
		if (!actionsToAdd.isEmpty())
		{
			capabilityManager.addActionsToCapability(actionsToAdd, capability);
		}
	}

	ApplicationVO loadOrCreateApplicationInfo() throws
			ApplicationManagerException, DataAccessException
	{
		Long applicationID;
		try
		{
			applicationID = configDataManager.loadValue(SecurityConstants.CONFIG_SYSTEM_APPLICATION_ID, Long.class);
		}
		catch (LoadValueException ex)
		{
			LOG.log(Level.WARNING, "Application ID not set, probably not yet created. Creating application", ex);
			try
			{
				applicationID = createDefaultApplication().getId();
			}
			catch (ConfigXPathException e)
			{
				throw new DataAccessException(e);
			}
		}
		ApplicationVO applicationVO = applicationManager.loadApplicationWithID(applicationID);
		return applicationVO;
	}

	ApplicationVO createDefaultApplication() throws
			DataAccessException, ApplicationManagerException, ConfigXPathException
	{
		try
		{
			ApplicationVO applicationVO = new ApplicationVOBuilder()
					.setName(SecurityConstants.APPLICATION_NAME)
					.setToken(SecurityConstants.APPLICATION_DEFAULT_TOKEN)
					.createApplicationVO();
			ApplicationHostVO applicationHostVO = new ApplicationHostVO();
			applicationHostVO.setHostNameOrIpAddress(InetAddress.getLocalHost().getHostName());
			applicationVO.getHosts().add(applicationHostVO);
			ApplicationVO createdApplication = applicationManager.createNewApplication(applicationVO);
			configDataManager.createAttributeFromXpath(SecurityConstants.CONFIG_SYSTEM_APPLICATION_ID, createdApplication.getId());
			configDataManager.createAttributeFromXpath(SecurityConstants.CONFIG_SYSTEM_APPLICATION_TOKEN, createdApplication.getToken());
			return createdApplication;
		}
		catch (UnknownHostException ex)
		{
			throw new ApplicationManagerException(ex);
		}
	}
}
