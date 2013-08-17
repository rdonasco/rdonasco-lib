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

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.config.exceptions.LoadValueException;
import com.rdonasco.config.services.ConfigDataManagerLocal;
import com.rdonasco.config.services.ConfigDataManagerVODecorator;
import com.rdonasco.security.exceptions.ApplicationManagerException;
import com.rdonasco.security.exceptions.ApplicationNotTrustedException;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.exceptions.DefaultAdminSecurityProfileAlreadyExist;
import com.rdonasco.security.exceptions.HostNotTrustedException;
import com.rdonasco.security.exceptions.NotSecuredResourceException;
import com.rdonasco.security.exceptions.SecurityAuthenticationException;
import com.rdonasco.security.exceptions.SecurityAuthorizationException;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.utils.EncryptionUtil;
import com.rdonasco.security.utils.SecurityConstants;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import com.rdonasco.security.vo.ApplicationHostVO;
import com.rdonasco.security.vo.ApplicationVO;
import com.rdonasco.security.vo.ApplicationVOBuilder;
import com.rdonasco.security.vo.CapabilityActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.rdonasco.security.vo.UserSecurityProfileVOBuilder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class SystemSecurityManagerImpl implements SystemSecurityManagerRemote,
		SystemSecurityManagerLocal
{

	private static final Logger LOG = Logger.getLogger(SystemSecurityManagerImpl.class.getName());
	private CapabilityManagerLocal capabilityManager;
	private UserSecurityProfileManagerLocal userSecurityProfileManager;
	private ApplicationManagerLocal applicationManager;
	private ConfigDataManagerLocal configDataManager;

	@EJB
	public void setUserSecurityProfileManager(
			UserSecurityProfileManagerLocal userSecurityProfileManager)
	{
		this.userSecurityProfileManager = userSecurityProfileManager;
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
	
	@EJB
	public void setConfigDataManager(ConfigDataManagerLocal configDataManager)
	{
		this.configDataManager = configDataManager;
	}

	@Override
	public void checkAccessRightsOnSecuritySystem(AccessRightsVO accessRights)
	{
		Long applicationID = null;
		String applicationToken = null;
		try
		{
			applicationID = configDataManager.loadValue(SecurityConstants.CONFIG_SYSTEM_APPLICATION_ID, Long.class);
			applicationToken = configDataManager.loadValue(SecurityConstants.CONFIG_SYSTEM_APPLICATION_TOKEN, String.class);
		}
		catch (LoadValueException ex)
		{
			throw new ApplicationNotTrustedException(ex);
		}
		AccessRightsVO accessRightsOnSecuritySystem = new AccessRightsVOBuilder()
				.setActionVO(accessRights.getAction())
				.setResourceVO(accessRights.getResource())
				.setUserProfileVO(accessRights.getUserProfile())
				.setApplicationID(applicationID)
				.setApplicationToken(applicationToken)
				.setHostNameOrIpAddress(accessRights.getHostNameOrIpAddress())
				.createAccessRightsVO();		
		checkAccessRights(accessRightsOnSecuritySystem);
	}
	
	@Override
	public void checkAccessRights(final AccessRightsVO requestedAccessRight)
	{
		if (null == requestedAccessRight)
		{
			throw new SecurityException("accessRights is null. Please provide valid access rights to check");
		}
		ApplicationVO trustedApplication = null;
		try
		{
			trustedApplication = ensureRequestedApplicationIsTrusted(requestedAccessRight);
			List<CapabilityVO> capabilities = retrieveAndConsolidateUserCapabilities(requestedAccessRight);
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
				Set<AccessRightsVO> accessRightsSet = consolidateAccessRightsFromAllCapabilities(capabilities, trustedApplication, requestedAccessRight.getUserProfile());
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
			createDefaultCapabilityBasedOnRequestedAccessRight(requestedAccessRight,trustedApplication);
		}
		catch (SecurityAuthorizationException e)
		{
			LOG.log(Level.FINER, e.getMessage(), e);
			throw e;
		}
		catch (Exception e)
		{
			LOG.log(Level.FINER, e.getMessage(), e);
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
	public UserSecurityProfileVO createDefaultAdminSecurityProfile() throws
			SecurityManagerException
	{
		UserSecurityProfileVO defaultAdminSecurityProfile = null;
		try
		{
			List<UserSecurityProfileVO> allProfiles = userSecurityProfileManager.findAllProfiles();
			if (allProfiles == null || allProfiles.isEmpty())
			{
				defaultAdminSecurityProfile = userSecurityProfileManager.createNewUserSecurityProfile(new UserSecurityProfileVOBuilder()
						.setLoginId("admin")
						.setPassword("admin")
						.createUserSecurityProfileVO());
				userSecurityProfileManager.setupDefaultCapabilitiesForUser(defaultAdminSecurityProfile);
			}
			else
			{
				throw new DefaultAdminSecurityProfileAlreadyExist(I18NResource.localize("Creation of admin profile is only allowed if there are no users"));
			}
		}
		catch (DefaultAdminSecurityProfileAlreadyExist ex)
		{
			throw ex;
		}
		catch (Exception ex)
		{
			throw new SecurityManagerException(ex);
		}
		return defaultAdminSecurityProfile;
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
	public UserSecurityProfileVO findSecurityProfileWithLogonIDandPassword(
			String logonID, String password) throws SecurityManagerException
	{
		UserSecurityProfileVO userSecurityProfileVO = findSecurityProfileWithLogonID(logonID);
		verifyPassword(userSecurityProfileVO, password);
		return userSecurityProfileVO;
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
			AccessRightsVO requestedAccessRight, ApplicationVO application)
	{
		String capabilityTitleOrDescription =
				String.format("%1$s %2$s on %3$s",
				requestedAccessRight.getAction().getName(),
				requestedAccessRight.getResource().getName(),
				application.getName());
		try
		{
			try
			{
				// TODO: need to add application as part of the parameter to ensure that capability is found for a specific application
				LOG.log(Level.FINE, "Finding Capability [{0}]...", capabilityTitleOrDescription);
				capabilityManager.findCapabilityWithTitle(capabilityTitleOrDescription);
				LOG.log(Level.FINE, "Capability [{0}] found.", capabilityTitleOrDescription);
			}
			catch (NonExistentEntityException e)
			{
				LOG.log(Level.INFO, "Capability [{0}] not found. Creating...", capabilityTitleOrDescription);
//				ApplicationVO applicationVO = new ApplicationVOBuilder()
//						.setId(requestedAccessRight.getApplicationID())
//						.createApplicationVO();
				CapabilityVO capabilityVO = new CapabilityVOBuilder()
						.setTitle(capabilityTitleOrDescription)
						.setDescription(capabilityTitleOrDescription)
						.setApplication(application)
						.createCapabilityVO();
				capabilityManager.createNewCapability(capabilityVO);
			}
		}
		catch (CapabilityManagerException ex)
		{
			LOG.log(Level.WARNING, "Capability [{0}] not found. Failed to create it...", capabilityTitleOrDescription);
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

	private void verifyPassword(UserSecurityProfileVO userSecurityProfileVO,
			String password) throws SecurityManagerException
	{
		if (null != userSecurityProfileVO)
		{
			String encrypted = null;
			try
			{
				encrypted = EncryptionUtil.encryptWithPassword(password, password);
			}
			catch (Exception ex)
			{
				LOG.log(Level.WARNING, ex.getMessage(), ex);
			}
			if (!userSecurityProfileVO.getPassword().equals(encrypted))
			{
				throw new SecurityAuthenticationException("Invalid Credentials");
			}
		}
		else
		{
			throw new SecurityAuthenticationException("Invalid Credentials");
		}

	}

	private ApplicationVO ensureRequestedApplicationIsTrusted(
			final AccessRightsVO requestedAccessRight) throws
			ApplicationManagerException
	{
		if (null == requestedAccessRight.getApplicationID() || null == requestedAccessRight.getApplicationToken()
				|| requestedAccessRight.getApplicationToken().isEmpty())
		{
			LOG.log(Level.WARNING, "requestedAccessRight.getApplicationID() = {0}", requestedAccessRight.getApplicationID());
			LOG.log(Level.WARNING, "requestedAccessRight.getApplicationToken() = {0}", requestedAccessRight.getApplicationToken());
			throw new ApplicationNotTrustedException();
		}
		ApplicationVO trustedApplication = applicationManager.loadApplicationWithID(requestedAccessRight.getApplicationID());
		if (null == trustedApplication || !trustedApplication.getToken().equals(requestedAccessRight.getApplicationToken()))
		{
			LOG.log(Level.FINE, "token mismatch");
			throw new ApplicationNotTrustedException();
		}
		ensureThatHostIsTrusted(requestedAccessRight, trustedApplication);
		return trustedApplication;
	}

	private List<CapabilityVO> retrieveAndConsolidateUserCapabilities(
			final AccessRightsVO requestedAccessRight) throws
			DataAccessException
	{
		List<CapabilityVO> capabilities = userSecurityProfileManager.retrieveCapabilitiesOfUser(requestedAccessRight);
		List<CapabilityVO> roleCapabilities = userSecurityProfileManager.retrieveCapabilitiesOfUserBasedOnRoles(requestedAccessRight);
		List<CapabilityVO> groupCapabilities = userSecurityProfileManager.retrieveCapabilitiesOfUserBasedOnGroups(requestedAccessRight);
		capabilities.addAll(roleCapabilities);
		capabilities.addAll(groupCapabilities);
		return capabilities;
	}

	private Set<AccessRightsVO> consolidateAccessRightsFromAllCapabilities(
			List<CapabilityVO> capabilities, ApplicationVO trustedApplication,
			final UserSecurityProfileVO userProfile)
	{
		final Set<AccessRightsVO> accessRightsSet = new HashSet<AccessRightsVO>();
		for (CapabilityVO capability : capabilities)
		{
			if (trustedApplication.equals(capability.getApplicationVO()))
			{
				for (CapabilityActionVO action : capability.getActions())
				{
					AccessRightsVO rights = new AccessRightsVOBuilder()
							.setActionVO(action.getActionVO())
							.setResourceVO(capability.getResource())
							.setUserProfileVO(userProfile).createAccessRightsVO();
					accessRightsSet.add(rights);
				}
			}
		}
		return accessRightsSet;
	}

	private void ensureThatHostIsTrusted(
			final AccessRightsVO requestedAccessRight,
			ApplicationVO trustedApplication) throws HostNotTrustedException
	{
		ApplicationHostVO applicationHostVO = new ApplicationHostVO();
		applicationHostVO.setHostNameOrIpAddress(requestedAccessRight.getHostNameOrIpAddress());
		try
		{
			if (trustedApplication.getHosts().contains(applicationHostVO))
			{
				LOG.log(Level.FINE, "host {0} is trusted", requestedAccessRight.getHostNameOrIpAddress());
			}
			else
			{
				throw new HostNotTrustedException(new StringBuilder("host [").append(requestedAccessRight.getHostNameOrIpAddress()).append("] is not trusted").toString());
			}
		}
		catch (Exception e)
		{
			throw new HostNotTrustedException(e);
		}
	}
}
