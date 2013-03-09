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

package com.rdonasco.security.services;

import com.rdonasco.security.exceptions.SecurityAuthorizationException;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
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
	public void checkAccessRights(AccessRightsVO accessRights) throws
			SecurityAuthorizationException
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
	
	
}