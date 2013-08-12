/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 04-May-2013
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
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.services.ApplicationManagerLocal;
import com.rdonasco.security.services.CapabilityManagerLocal;
import com.rdonasco.security.services.SystemSecurityManagerLocal;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.ApplicationVO;
import com.rdonasco.security.vo.ApplicationVOBuilder;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserCapabilityVOBuilder;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.rdonasco.security.vo.UserSecurityProfileVOBuilder;

/**
 *
 * @author Roy F. Donasco
 */
public class UserSecurityProfileTestUtility
{

	private CapabilityManagerLocal capabilityManager;

	private SystemSecurityManagerLocal systemSecurityManager;
	
	private ApplicationManagerLocal applicationManager;

	public UserSecurityProfileTestUtility(
			CapabilityManagerLocal capabilityManager,
			SystemSecurityManagerLocal systemSecurityManager,ApplicationManagerLocal applicationManager)
	{
		this.capabilityManager = capabilityManager;
		this.systemSecurityManager = systemSecurityManager;
		this.applicationManager = applicationManager;
	}

	public ActionVO createTestDataActionNamed(String name) throws
			CapabilityManagerException
	{
		ActionVO savedAction = capabilityManager.findOrAddActionNamedAs(name);
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

	public CapabilityVO createTestDataCapabilityWithActionAndResourceName(
			final String actionName,
			final String resourceName) throws CapabilityManagerException,
			ApplicationManagerException
	{
		ActionVO action = createTestDataActionNamed(actionName);
		ResourceVO resource = createTestDataResourceNamed(resourceName + SecurityEntityValueObjectDataUtility.generateRandomID());
		final String capabilityTitle = "capability to " + action.getName() + " " + resource.getName();
		CapabilityVO capabilityVO = new CapabilityVOBuilder()
				.addAction(action)
				.setResource(resource)
				.setTitle(capabilityTitle)
				.setDescription(capabilityTitle + " description")
				.setApplication(createApplicationNamed("app"+resource.getName()))
				.createCapabilityVO();
		CapabilityVO savedCapabilityVO = capabilityManager.createNewCapability(capabilityVO);
		return savedCapabilityVO;
	}

	public UserCapabilityVO createTestDataUserCapabilityVO(
			CapabilityVO capabilityVO)
	{
		UserCapabilityVO userCapabilityVO = new UserCapabilityVOBuilder()
				.setCapability(capabilityVO)
				.createUserCapabilityVO();
		return userCapabilityVO;
	}

	public UserSecurityProfileVO createTestDataUserProfileWithCapability()
			throws CapabilityManagerException, ApplicationManagerException
	{
		UserSecurityProfileVO userProfile = createTestDataWithoutCapability();
		CapabilityVO capabilityVO = createTestDataCapabilityWithActionAndResourceName("edit", "pets");
		UserCapabilityVO userCapabilityVO = createTestDataUserCapabilityVO(capabilityVO);
		userProfile.addCapbility(userCapabilityVO);
		return userProfile;
	}

	public UserSecurityProfileVO createTestDataWithoutCapability()
	{
		UserSecurityProfileVO userProfile = new UserSecurityProfileVOBuilder()
				.setLoginId("rdonasco" + SecurityEntityValueObjectDataUtility.generateRandomID())
				.setPassword("rdonasco")
				.createUserSecurityProfileVO();
		return userProfile;
	}

	public UserSecurityProfileVO createNewUserSecurityProfile() throws
			SecurityManagerException
	{
		UserSecurityProfileVO userProfile = createTestDataWithoutCapability();
		UserSecurityProfileVO createdUser = systemSecurityManager.createNewSecurityProfile(userProfile);
		return createdUser;
	}

	public UserSecurityProfileVO createNewUserSecurityProfileWithCapability()
			throws SecurityManagerException, CapabilityManagerException, ApplicationManagerException
	{
		UserSecurityProfileVO userProfile = createTestDataUserProfileWithCapability();
		UserSecurityProfileVO createdUser = systemSecurityManager.createNewSecurityProfile(userProfile);
		return createdUser;
	}

	public ApplicationVO createApplicationNamed(String name) throws ApplicationManagerException
	{
		return applicationManager.createNewApplication(new ApplicationVOBuilder()
				.setName(name)
				.setToken("token-"+name)
				.createApplicationVO());
	}
}
