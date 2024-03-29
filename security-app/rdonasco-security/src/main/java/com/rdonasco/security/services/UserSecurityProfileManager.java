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
package com.rdonasco.security.services;

import com.rdonasco.common.exceptions.CollectionMergeException;
import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.common.utils.CollectionsUtility;
import com.rdonasco.security.dao.UserCapabilityDAO;
import com.rdonasco.security.dao.UserGroupDAO;
import com.rdonasco.security.dao.UserRoleDAO;
import com.rdonasco.security.dao.UserSecurityProfileDAO;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.exceptions.SecurityProfileNotFoundException;
import com.rdonasco.security.model.Application;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.Role;
import com.rdonasco.security.model.RoleCapability;
import com.rdonasco.security.model.SecurityGroup;
import com.rdonasco.security.model.SecurityGroupRole;
import com.rdonasco.security.model.UserCapability;
import com.rdonasco.security.model.UserGroup;
import com.rdonasco.security.model.UserRole;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.utils.EncryptionUtil;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.RoleCapabilityVO;
import com.rdonasco.security.vo.RoleVO;
import com.rdonasco.security.vo.SecurityGroupVO;
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserCapabilityVOBuilder;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
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
public class UserSecurityProfileManager implements
		UserSecurityProfileManagerLocal
{

	private static final Logger LOG = Logger.getLogger(UserSecurityProfileManager.class.getName());

	@Inject
	private UserSecurityProfileDAO userSecurityProfileDAO;

	@Inject
	private UserCapabilityDAO userCapabilityDAO;

	@Inject
	private UserRoleDAO userRoleDAO;

	@Inject
	private UserGroupDAO userGroupDAO;

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

	public void setUserRoleDAO(UserRoleDAO userRoleDAO)
	{
		this.userRoleDAO = userRoleDAO;
	}

	public void setUserGroupDAO(UserGroupDAO userGroupDAO)
	{
		this.userGroupDAO = userGroupDAO;
	}

	public void setCapabilityManager(CapabilityManagerLocal capabilityManager)
	{
		this.capabilityManager = capabilityManager;
	}

	@Override
	public UserSecurityProfileVO createNewUserSecurityProfile(
			final UserSecurityProfileVO userSecurityProfile) throws
			SecurityManagerException
	{
		UserSecurityProfileVO createdProfile = null;
		try
		{
			if (null != userSecurityProfile.getPassword() && !userSecurityProfile.getPassword().isEmpty())
			{
				userSecurityProfile.setPassword(EncryptionUtil.encryptWithPassword(
						userSecurityProfile.getPassword(), userSecurityProfile.getPassword()));
			}
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
	public List<CapabilityVO> retrieveCapabilitiesOfUser(
			AccessRightsVO accessRights) throws DataAccessException
	{
		List<CapabilityVO> capabilityVOList = null;
		try
		{

			UserSecurityProfile userProfile = SecurityEntityValueObjectConverter.toUserProfile(accessRights.getUserProfile());
			Application application = new Application();
			application.setId(accessRights.getApplicationID());
			application.setToken(accessRights.getApplicationToken());
			List<Capability> capabilities = userCapabilityDAO
					.loadCapabilitiesOnApplicationOf(userProfile,application);
			capabilityVOList = new ArrayList<CapabilityVO>(capabilities.size());
			CapabilityVO capabilityVO;
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
	public List<CapabilityVO> retrieveCapabilitiesOfUserBasedOnRoles(
			AccessRightsVO accessRights) throws DataAccessException
	{
		return extractCapabilitiesFromRoles(retrieveRolesOfUser(accessRights.getUserProfile()));
	}

	@Override
	public List<CapabilityVO> retrieveCapabilitiesOfUserBasedOnGroups(
			AccessRightsVO accessRights) throws DataAccessException
	{
		List<CapabilityVO> capabilityVOs = new ArrayList<CapabilityVO>();
		try
		{
			Set<CapabilityVO> capabilityVOset = new HashSet<CapabilityVO>();
			List<SecurityGroup> groups = userGroupDAO.loadGroupsOf(
					SecurityEntityValueObjectConverter.toUserProfile(accessRights.getUserProfile()));
			for (SecurityGroup group : groups)
			{
				capabilityVOset.addAll(extractCapabilityVOsFromSecurityGroup(group));
			}
			capabilityVOs.addAll(capabilityVOset);
		}
		catch (Exception ex)
		{
			throw new DataAccessException(ex);
		}

		return capabilityVOs;
	}

	@Override
	public List<RoleVO> retrieveRolesOfUser(
			UserSecurityProfileVO userSecurityProfileVO) throws
			DataAccessException
	{
		List<RoleVO> roleVOList = null;
		try
		{
			UserSecurityProfile userProfile = SecurityEntityValueObjectConverter.toUserProfile(userSecurityProfileVO);
			List<Role> roles = userRoleDAO.loadRolesOf(userProfile);
			roleVOList = new ArrayList<RoleVO>();
			roleVOList = SecurityEntityValueObjectConverter.toRoleVOList(roles);
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return roleVOList;
	}

	@Override
	public List<SecurityGroupVO> retrieveGroupsOfUser(
			UserSecurityProfileVO userSecurityProfileVO) throws
			DataAccessException
	{
		List<SecurityGroupVO> groupVOList = null;
		try
		{
			UserSecurityProfile userProfile = SecurityEntityValueObjectConverter.toUserProfile(userSecurityProfileVO);
			List<SecurityGroup> groupsOfUser = userGroupDAO.loadGroupsOf(userProfile);
			groupVOList = SecurityEntityValueObjectConverter.toSecurityGroupVOList(groupsOfUser);

		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return groupVOList;
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
			userSecurityProfile = userSecurityProfileDAO.findFreshData(userSecurityProfile.getId());
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
	public List<UserSecurityProfileVO> findAllProfiles() throws
			SecurityManagerException
	{
		List<UserSecurityProfileVO> allProfileVOList;
		try
		{
			List<UserSecurityProfile> allProfiles = userSecurityProfileDAO.findAllData();
			allProfileVOList = SecurityEntityValueObjectConverter.toUserProfileVOList(allProfiles);
		}
		catch (Exception e)
		{
			throw new SecurityManagerException(e);
		}
		return allProfileVOList;
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

	@Override
	public void removeUserSecurityProfile(
			UserSecurityProfileVO securityProfileToRemove) throws
			SecurityManagerException
	{
		try
		{
			this.userSecurityProfileDAO.delete(securityProfileToRemove.getId());
			LOG.log(Level.INFO, "Security profile {0} removed", securityProfileToRemove.toString());
		}
		catch (Exception e)
		{
			throw new SecurityManagerException(e);
		}
	}

	@Override
	public void updateUserSecurityProfile(
			UserSecurityProfileVO userSecurityProfile) throws
			SecurityManagerException
	{
		try
		{
			UserSecurityProfile profileUpdateDetails = SecurityEntityValueObjectConverter.toUserProfile(userSecurityProfile);
			UserSecurityProfile profileToUpdate = userSecurityProfileDAO.findData(profileUpdateDetails.getId());
			profileToUpdate.setLogonId(profileUpdateDetails.getLogonId());
			profileToUpdate.setRegistrationToken(profileUpdateDetails.getRegistrationToken());
			profileToUpdate.setRegistrationTokenExpiration(profileUpdateDetails.getRegistrationTokenExpiration());
			if (profileUpdateDetails.getPassword() != null && !profileUpdateDetails.getPassword().isEmpty())
			{
				String password = profileUpdateDetails.getPassword();
				profileUpdateDetails.setPassword(EncryptionUtil.encryptWithPassword(password, password));
				profileToUpdate.setPassword(profileUpdateDetails.getPassword());
			}

			mergeCapabilities(profileToUpdate, profileUpdateDetails);
			mergeRoles(profileToUpdate, profileUpdateDetails);
			mergeGroups(profileToUpdate, profileUpdateDetails);

			userSecurityProfileDAO.update(profileToUpdate);
		}
		catch (Exception ex)
		{
			throw new SecurityManagerException(ex);
		}
	}

	@Override
	public int removeAllAssignedUserCapability(CapabilityVO capability) throws
			DataAccessException
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(UserCapability.QUERY_PARAM_CAPABILITY_ID, capability.getId());
		return userSecurityProfileDAO.executeUpdateUsingNamedQuery(UserCapability.NAMED_QUERY_DELETE_CAPABILITY_WITH_ID, parameters);
	}

	private void mergeCapabilities(UserSecurityProfile profileToUpdate,
			UserSecurityProfile profileUpdateDetails) throws
			CollectionMergeException
	{
		profileToUpdate.setCapabilities(CollectionsUtility.updateCollection(
				profileUpdateDetails.getCapabilities(),
				profileToUpdate.getCapabilities(),
				new CollectionsUtility.CollectionItemDeleteStrategy<UserCapability>()
		{
			@Override
			public void delete(UserCapability itemToDelete) throws
					CollectionMergeException
			{
				try
				{
					userCapabilityDAO.delete(itemToDelete.getId());
				}
				catch (Exception e)
				{
					throw new CollectionMergeException(e);
				}
			}
		}, new CollectionsUtility.CollectionItemUpdateStrategy<UserCapability>()
		{
			@Override
			public void update(UserCapability itemToUpdate,
					UserCapability itemToCopy) throws
					CollectionMergeException
			{
				itemToUpdate.setCapability(itemToCopy.getCapability());
				itemToUpdate.setUserProfile(itemToCopy.getUserProfile());
			}
		}));
	}

	private void mergeRoles(UserSecurityProfile profileToUpdate,
			UserSecurityProfile profileUpdateDetails) throws
			CollectionMergeException
	{
		profileToUpdate.setRoles(CollectionsUtility.updateCollection(
				profileUpdateDetails.getRoles(),
				profileToUpdate.getRoles(),
				new CollectionsUtility.CollectionItemDeleteStrategy<UserRole>()
		{
			@Override
			public void delete(UserRole itemToDelete) throws
					CollectionMergeException
			{
				try
				{
					userRoleDAO.delete(itemToDelete.getId());
				}
				catch (Exception e)
				{
					throw new CollectionMergeException(e);
				}
			}
		}, new CollectionsUtility.CollectionItemUpdateStrategy<UserRole>()
		{
			@Override
			public void update(UserRole itemToUpdate,
					UserRole itemToCopy) throws
					CollectionMergeException
			{
				itemToUpdate.setRole(itemToCopy.getRole());
				itemToUpdate.setUserProfile(itemToCopy.getUserProfile());
			}
		}));
	}

	private void mergeGroups(UserSecurityProfile profileToUpdate,
			UserSecurityProfile profileUpdateDetails) throws
			CollectionMergeException
	{
		profileToUpdate.setGroups(CollectionsUtility.updateCollection(
				profileUpdateDetails.getGroups(),
				profileToUpdate.getGroups(),
				new CollectionsUtility.CollectionItemDeleteStrategy<UserGroup>()
		{
			@Override
			public void delete(UserGroup itemToDelete) throws
					CollectionMergeException
			{
				try
				{
					userGroupDAO.delete(itemToDelete.getId());
				}
				catch (Exception e)
				{
					throw new CollectionMergeException(e);
				}
			}
		}, new CollectionsUtility.CollectionItemUpdateStrategy<UserGroup>()
		{
			@Override
			public void update(UserGroup itemToUpdate,
					UserGroup itemToCopy) throws
					CollectionMergeException
			{
				itemToUpdate.setGroup(itemToCopy.getGroup());
				itemToUpdate.setUserProfile(itemToCopy.getUserProfile());
			}
		}));
	}

	private List<CapabilityVO> extractCapabilitiesFromRoles(
			List<RoleVO> roles)
	{
		List<CapabilityVO> capabilities = new ArrayList<CapabilityVO>();
		if (null != roles)
		{
			Set<CapabilityVO> uniqueCapabilities = new HashSet<CapabilityVO>();
			for (RoleVO role : roles)
			{
				for (RoleCapabilityVO roleCapability : role.getRoleCapabilities())
				{
					uniqueCapabilities.add(roleCapability.getCapabilityVO());
				}
			}
			capabilities.addAll(uniqueCapabilities);
		}
		return capabilities;
	}

	private Collection<? extends CapabilityVO> extractCapabilityVOsFromSecurityGroup(
			SecurityGroup group) throws IllegalAccessException,
			InvocationTargetException
	{
		Set<CapabilityVO> uniqueCapabilities = new HashSet<CapabilityVO>();
		List<CapabilityVO> capabilities = new ArrayList<CapabilityVO>();
		for (SecurityGroupRole groupRole : group.getGroupRoles())
		{
			for (RoleCapability roleCapability : groupRole.getRole().getCapabilities())
			{
				capabilities.add(SecurityEntityValueObjectConverter.toCapabilityVO(roleCapability.getCapability()));
			}
		}
		uniqueCapabilities.addAll(capabilities);
		return uniqueCapabilities;
	}
}
