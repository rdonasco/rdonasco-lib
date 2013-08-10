/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 03-May-2013
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

import com.rdonasco.security.exceptions.ApplicationManagerException;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.services.ApplicationManagerLocal;
import com.rdonasco.security.services.CapabilityManager;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.ApplicationVO;
import com.rdonasco.security.vo.ApplicationVOBuilder;
import com.rdonasco.security.vo.CapabilityActionVO;
import com.rdonasco.security.vo.CapabilityActionVOBuilder;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityTestUtility
{

	private CapabilityManager capabilityManager;
	private ApplicationManagerLocal applicationManager;

	public CapabilityTestUtility(CapabilityManager capabilityManager, ApplicationManagerLocal applicationManager)
	{
		this.capabilityManager = capabilityManager;
		this.applicationManager = applicationManager;
	}

	public CapabilityManager getCapabilityManager()
	{
		return capabilityManager;
	}

	public void setApplicationManager(ApplicationManagerLocal applicationManager)
	{
		this.applicationManager = applicationManager;
	}

	public ApplicationManagerLocal getApplicationManager()
	{
		return applicationManager;
	}	

	public void setCapabilityManager(CapabilityManager capabilityManager)
	{
		this.capabilityManager = capabilityManager;
	}

	public ActionVO createTestDataActionNamed(String name) throws
			CapabilityManagerException
	{
		ActionVO action = new ActionVO();
		action.setName(name);
		ActionVO savedAction = capabilityManager.createNewAction(action);
		return savedAction;
	}

	public ResourceVO createTestDataResourceNamed(String name) throws
			CapabilityManagerException
	{
		ResourceVO resourceToAdd = new ResourceVOBuilder()
				.setName(name)
				.createResourceVO();

		ResourceVO resourceAdded = capabilityManager.addResource(resourceToAdd);
		return resourceAdded;
	}

	private static int KEY = 0;
	public CapabilityVO createTestDataCapabilityWithActionAndResourceNameOnSystem(
			final String actionName,
			final String resourceName,
			final String systemName) throws CapabilityManagerException, ApplicationManagerException
	{
		ApplicationVO applicationVO = new ApplicationVOBuilder()
				.setName(systemName)
				.setToken("hrSystemToken"+(KEY++))
				.createApplicationVO();
		ApplicationVO createdApplication = applicationManager.createNewApplication(applicationVO);
		
		ActionVO action = createTestDataActionNamed(actionName);
		ResourceVO resource = createTestDataResourceNamed(resourceName);
		final String capabilityTitle = "capability to " + actionName + " " + resourceName;
		CapabilityVO capabilityVO = new CapabilityVOBuilder()
				.addAction(action)
				.setApplication(createdApplication)
				.setResource(resource)
				.setTitle(capabilityTitle)
				.setDescription(capabilityTitle + " description")
				.createCapabilityVO();
		CapabilityVO savedCapabilityVO = capabilityManager.createNewCapability(capabilityVO);
		return savedCapabilityVO;
	}

	public CapabilityActionVO createTestDataCapabilityActionNamed(
			final String actionName, CapabilityVO capabilityVOtoUpdate) throws
			CapabilityManagerException
	{
		ActionVO actionVOtoAdd = createTestDataActionNamed(actionName);
		CapabilityActionVO capabilityActionToAdd = new CapabilityActionVOBuilder()
				.setActionVO(actionVOtoAdd)
				.setCapabilityVO(capabilityVOtoUpdate)
				.createCapabilityActionVO();
		return capabilityActionToAdd;
	}

	public List<ActionVO> createTestDataActions(String... actionNames) throws
			CapabilityManagerException
	{
		List<ActionVO> actions = new ArrayList<ActionVO>(actionNames.length);
		for (String name : actionNames)
		{
			actions.add(createTestDataActionNamed(name));
		}

		return actions;
	}

	public void createAndAssociateActionsToCapability(
			List<ActionVO> actions, CapabilityVO capability)
	{
		for (ActionVO actionVO : actions)
		{
			CapabilityActionVO capabilityActionVO = new CapabilityActionVOBuilder()
					.setActionVO(actionVO)
					.setCapabilityVO(capability)
					.createCapabilityActionVO();
			capability.getActions().add(capabilityActionVO);
		}
	}
}
