/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 27-Jan-2013
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

import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.security.dao.UserCapabilityDAO;
import com.rdonasco.security.dao.UserSecurityProfileDAO;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.exceptions.NotSecuredResourceException;
import com.rdonasco.security.exceptions.SecurityAuthorizationException;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import com.rdonasco.security.vo.CapabilityActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class SystemSecurityManagerImpl implements SystemSecurityManagerRemote,
		SystemSecurityManagerLocal
{

	private static final Logger LOG = Logger.getLogger(SystemSecurityManagerImpl.class.getName());
	@EJB
	private CapabilityManagerLocal capabilityManager;

	@EJB
	private UserSecurityProfileManagerLocal userSecurityProfileManager;

	public void setUserSecurityProfileManager(
			UserSecurityProfileManagerLocal userSecurityProfileManager)
	{
		this.userSecurityProfileManager = userSecurityProfileManager;
	}

	public void setCapabilityManager(CapabilityManagerLocal capabilityManager)
	{
		this.capabilityManager = capabilityManager;
	}

	@Override
	public void checkAccessRights(final AccessRightsVO requestedAccessRight)
	{
		if (null == requestedAccessRight)
		{
			throw new SecurityException("accessRights is null. Please provide valid access rights to check");
		}
		try
		{
			List<CapabilityVO> capabilities = userSecurityProfileManager.retrieveCapabilitiesOfUser(requestedAccessRight);
			List<CapabilityVO> roleCapabilities = userSecurityProfileManager.retrieveCapabilitiesOfUserBasedOnRoles(requestedAccessRight);
			List<CapabilityVO> groupCapabilities = userSecurityProfileManager.retrieveCapabilitiesOfUserBasedOnGroups(requestedAccessRight);
			capabilities.addAll(roleCapabilities);
			capabilities.addAll(groupCapabilities);
			Set<AccessRightsVO> accessRightsSet = new HashSet<AccessRightsVO>();
			boolean capabilitiesNotFound = capabilities.isEmpty();
			capabilityManager.findOrAddActionNamedAs(requestedAccessRight.getAction().getName());
			ResourceVO securedResourceVO = ensureThatResourceExistsAndIsSecured(requestedAccessRight.getResource().getName());
			if (capabilitiesNotFound)
			{
				if (null != securedResourceVO)
				{
					throwSecurityAuthorizationExceptionFor(requestedAccessRight);
				}
			}
			else
			{
				for (CapabilityVO capability : capabilities)
				{
					for (CapabilityActionVO action : capability.getActions())
					{
						AccessRightsVO rights = new AccessRightsVOBuilder()
								.setActionVO(action.getActionVO())
								.setResourceVO(capability.getResource())
								.setUserProfileVO(requestedAccessRight.getUserProfile()).createAccessRightsVO();
						accessRightsSet.add(rights);
					}
				}
				if (!accessRightsSet.contains(requestedAccessRight))
				{
					logAccessRights(accessRightsSet);
					throwSecurityAuthorizationExceptionFor(requestedAccessRight);
				}
			}

		}
		catch (NotSecuredResourceException e)
		{
			LOG.warning(e.getMessage());
			createDefaultCapabilityBasedOnRequestedAccessRight(requestedAccessRight);
		}
		catch (Exception e)
		{
			LOG.log(Level.FINE, e.getMessage(), e);
			throw new SecurityAuthorizationException(e);
		}
	}

	@Override
	public UserSecurityProfileVO createNewSecurityProfile(
			UserSecurityProfileVO userSecurityProfile)
			throws SecurityManagerException
	{
		return userSecurityProfileManager.createNewUserSecurityProfile(userSecurityProfile);
	}

	@Override
	public void updateSecurityProfile(UserSecurityProfileVO userSecurityProfile)
			throws SecurityManagerException
	{
		userSecurityProfileManager.updateUserSecurityProfile(userSecurityProfile);
	}

	@Override
	public UserSecurityProfileVO findSecurityProfileWithLogonID(String logonId)
			throws SecurityManagerException
	{
		return userSecurityProfileManager.findSecurityProfileWithLogonID(logonId);
	}

	@Override
	public void removeSecurityProfile(
			UserSecurityProfileVO securityProfileToRemove) throws
			SecurityManagerException
	{
		userSecurityProfileManager.removeUserSecurityProfile(securityProfileToRemove);
	}

	@Override
	public boolean isSecuredResource(String resource)
	{
		boolean secured = true;
		try
		{
			capabilityManager.findOrAddSecuredResourceNamedAs(resource);
		}
		catch (NotSecuredResourceException e)
		{
			LOG.log(Level.FINE, e.getMessage(), e);
			secured = false;
		}
		catch (CapabilityManagerException e)
		{
			LOG.log(Level.FINE, e.getMessage());
			secured = false;
		}
		return secured;
	}

	@Override
	public void addCapabilityForUser(UserSecurityProfileVO userSecurityProfileVO,
			CapabilityVO capability) throws SecurityManagerException
	{
		userSecurityProfileManager.addCapabilityForUser(userSecurityProfileVO, capability);
	}

	@Override
	public void setupDefaultCapabilitiesForUser(
			UserSecurityProfileVO userSecurityProfile) throws
			SecurityManagerException
	{
		userSecurityProfileManager.setupDefaultCapabilitiesForUser(userSecurityProfile);
	}

	private void throwSecurityAuthorizationExceptionFor(
			AccessRightsVO accessRights) throws
			SecurityAuthorizationException
	{
		StringBuilder errorStringBuild = new StringBuilder("Access Denied on Resource {")
				.append(accessRights.getResource().getName())
				.append("} and Action {").append(accessRights.getAction().getName())
				.append("} for profile with login id {").append(accessRights.getUserProfile().getLogonId()).append("}");
		throw new SecurityAuthorizationException(errorStringBuild.toString());
	}

	private ResourceVO ensureThatResourceExistsAndIsSecured(String resourceName)
			throws
			NotSecuredResourceException, CapabilityManagerException
	{
		return capabilityManager.findOrAddSecuredResourceNamedAs(resourceName);
	}

	private void createDefaultCapabilityBasedOnRequestedAccessRight(
			AccessRightsVO requestedAccessRight)
	{
		try
		{
			String capabilityTitleOrDescription =
					String.format("%1$s %2$s",
					requestedAccessRight.getAction().getName(),
					requestedAccessRight.getResource().getName());
			try
			{
				capabilityManager.findCapabilityWithTitle(capabilityTitleOrDescription);
			}
			catch (NonExistentEntityException e)
			{
				LOG.log(Level.INFO, "Capability [{0}] not found. Creating...", capabilityTitleOrDescription);
				CapabilityVO capabilityVO = new CapabilityVOBuilder()
						.setTitle(capabilityTitleOrDescription)
						.setDescription(capabilityTitleOrDescription)
						.createCapabilityVO();
				capabilityManager.createNewCapability(capabilityVO);
			}
		}
		catch (CapabilityManagerException ex)
		{
			LOG.log(Level.WARNING, ex.getMessage(), ex);
		}
	}

	@Override
	public List<UserSecurityProfileVO> findAllProfiles() throws
			SecurityManagerException
	{
		return userSecurityProfileManager.findAllProfiles();
	}

	private void logAccessRights(
			Set<AccessRightsVO> accessRightsSet)
	{
		for (AccessRightsVO accessRightsVO : accessRightsSet)
		{
			Object[] params =
			{
				accessRightsVO.getAction(), accessRightsVO.getResource()
			};
			LOG.log(Level.FINE, "access right action: {0}, resource: {1}", params);
		}
	}
}
