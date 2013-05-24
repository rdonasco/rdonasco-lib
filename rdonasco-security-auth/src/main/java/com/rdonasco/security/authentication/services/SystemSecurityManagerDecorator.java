/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 07-Mar-2013
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

package com.rdonasco.security.authentication.services;

import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.services.SystemSecurityManager;
import com.rdonasco.security.services.SystemSecurityManagerRemote;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author Roy F. Donasco
 */
public class SystemSecurityManagerDecorator implements SystemSecurityManager
{
	@EJB
	private SystemSecurityManagerRemote systemSecurityManager;

	@Override
	public void checkAccessRights(AccessRightsVO accessRights)
	{
		systemSecurityManager.checkAccessRights(accessRights);
	}

	@Override
	public UserSecurityProfileVO createNewSecurityProfile(
			UserSecurityProfileVO userSecurityProfile) throws
			SecurityManagerException
	{
		return systemSecurityManager.createNewSecurityProfile(userSecurityProfile);
	}

	@Override
	public void removeSecurityProfile(
			UserSecurityProfileVO securityProfileToRemove) throws
			SecurityManagerException
	{
		systemSecurityManager.removeSecurityProfile(securityProfileToRemove);
	}

	@Override
	public UserSecurityProfileVO findSecurityProfileWithLogonID(String logonID)
			throws SecurityManagerException
	{
		return systemSecurityManager.findSecurityProfileWithLogonID(logonID);
	}

	@Override
	public boolean isSecuredResource(String resource)
	{
		return systemSecurityManager.isSecuredResource(resource);
	}

	@Override
	public void setupDefaultCapabilitiesForUser(
			UserSecurityProfileVO userSecurityProfile) throws
			SecurityManagerException
	{
		systemSecurityManager.setupDefaultCapabilitiesForUser(userSecurityProfile);
	}

	@Override
	public void addCapabilityForUser(UserSecurityProfileVO userSecurityProfile,
			CapabilityVO capability) throws SecurityManagerException
	{
		systemSecurityManager.addCapabilityForUser(userSecurityProfile, capability);
	}

	@Override
	public void updateSecurityProfile(UserSecurityProfileVO userSecurityProfile)
			throws SecurityManagerException
	{
		systemSecurityManager.updateSecurityProfile(userSecurityProfile);
	}

	@Override
	public List<UserSecurityProfileVO> findAllProfiles() throws
			SecurityManagerException
	{
		return systemSecurityManager.findAllProfiles();
	}
}
