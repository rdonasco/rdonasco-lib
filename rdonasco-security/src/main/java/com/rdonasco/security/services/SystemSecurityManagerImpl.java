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

import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.security.dao.UserCapabilityDAO;
import com.rdonasco.security.dao.UserSecurityProfileDAO;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.exceptions.NotSecuredResourceException;
import com.rdonasco.security.exceptions.SecurityAuthorizationException;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.exceptions.SecurityProfileNotFoundException;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserCapabilityVOBuilder;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	@Inject
	private UserSecurityProfileDAO userSecurityProfileDAO;
	@Inject
	private UserCapabilityDAO userCapabilityDAO;
	@EJB
	private CapabilityManagerLocal capabilityManager;

	public void setUserCapabilityDAO(UserCapabilityDAO userCapabilityDAO)
	{
		this.userCapabilityDAO = userCapabilityDAO;
	}

	public void setUserSecurityProfileDAO(
			UserSecurityProfileDAO userSecurityProfileDAO)
	{
		this.userSecurityProfileDAO = userSecurityProfileDAO;
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
			List<CapabilityVO> capabilities = retrieveCapabilitiesOfUser(requestedAccessRight);
			Set<AccessRightsVO> accessRightsSet = new HashSet<AccessRightsVO>();
			boolean capabilitiesNotFound = (capabilities == null || capabilities.isEmpty());
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
			LOG.log(Level.SEVERE, e.getMessage(), e);
			throw new SecurityAuthorizationException(e);
		}
	}

	private List<CapabilityVO> retrieveCapabilitiesOfUser(
			AccessRightsVO accessRights)
			throws DataAccessException
	{
		List<CapabilityVO> capabilityVOList = null;
		try
		{

			UserSecurityProfile userProfile = SecurityEntityValueObjectConverter.toUserProfile(accessRights.getUserProfile());
			List<Capability> capabilities = userCapabilityDAO
					.loadCapabilitiesOf(userProfile);
			capabilityVOList = new ArrayList<CapabilityVO>(capabilities.size());
			CapabilityVO capabilityVO = null;
			for (Capability capability : capabilities)
			{
				capabilityVO = SecurityEntityValueObjectConverter.toCapabilityVO(capability);
				capabilityVOList.add(capabilityVO);
			}

		}
		catch (Exception ex)
		{
			throw new DataAccessException(ex);
		}
		return capabilityVOList;
	}

	@Override
	public UserSecurityProfileVO createNewSecurityProfile(
			UserSecurityProfileVO userSecurityProfile)
			throws SecurityManagerException
	{
		UserSecurityProfileVO createdProfile = null;
		try
		{
			UserSecurityProfile profileToCreate = SecurityEntityValueObjectConverter.toUserProfile(userSecurityProfile);
			userSecurityProfileDAO.create(profileToCreate);
			createdProfile = SecurityEntityValueObjectConverter.toUserProfileVO(profileToCreate);
		}
		catch (Exception e)
		{
			throw new SecurityManagerException(e);
		}
		return createdProfile;
	}

	@Override
	public UserSecurityProfileVO findSecurityProfileWithLogonID(String logonId)
			throws SecurityManagerException
	{
		UserSecurityProfileVO foundSecurityProfileVO = null;
		try
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(UserSecurityProfile.QUERY_PARAM_LOGON_ID, logonId);
			UserSecurityProfile userSecurityProfile = userSecurityProfileDAO.findUniqueDataUsingNamedQuery(UserSecurityProfile.NAMED_QUERY_FIND_SECURITY_PROFILE_BY_LOGON_ID, parameters);
			foundSecurityProfileVO = SecurityEntityValueObjectConverter.toUserProfileVO(userSecurityProfile);
		}
		catch (NonExistentEntityException e)
		{
			throw new SecurityProfileNotFoundException(e);
		}
		catch (Exception e)
		{
			throw new SecurityManagerException(e);
		}

		return foundSecurityProfileVO;
	}

	@Override
	public void removeSecurityProfile(
			UserSecurityProfileVO securityProfileToRemove) throws
			SecurityManagerException
	{
		try
		{
			this.userSecurityProfileDAO.delete(UserSecurityProfile.class, securityProfileToRemove.getId());
			LOG.log(Level.INFO, "Security profile {0} removed", securityProfileToRemove.toString());
		}
		catch (Exception e)
		{
			throw new SecurityManagerException(e);
		}
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
		try
		{
			UserCapabilityVO userCapabilityVO = new UserCapabilityVOBuilder()
					.setCapability(capability)
					.setUserProfile(userSecurityProfileVO)
					.createUserCapabilityVO();
			userSecurityProfileVO.addCapbility(userCapabilityVO);
			UserSecurityProfile userSecurityProfile = SecurityEntityValueObjectConverter.toUserProfile(userSecurityProfileVO);
			userSecurityProfileDAO.update(userSecurityProfile);
		}
		catch (Exception e)
		{
			throw new SecurityManagerException(e);
		}
	}

	@Override
	public void setupDefaultCapabilitiesForUser(
			UserSecurityProfileVO userSecurityProfile) throws
			SecurityManagerException
	{
		for (String[] capabilityArray : SystemSecurityInitializerLocal.DEFAULT_CAPABILITY_ELEMENTS)
		{
			try
			{
				CapabilityVO capability = capabilityManager.findCapabilityWithTitle(
						capabilityArray[SystemSecurityInitializerLocal.ELEMENT_CAPABILITY_TITLE]);
				addCapabilityForUser(userSecurityProfile, capability);

			}
			catch (CapabilityManagerException ex)
			{
				LOG.log(Level.WARNING, ex.getMessage(), ex);
			}
			catch (NonExistentEntityException ex)
			{
				LOG.log(Level.WARNING, ex.getLocalizedMessage(), ex);
			}
			catch (Exception ex)
			{
				throw new SecurityManagerException(ex);
			}
		}
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
}
