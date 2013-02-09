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
import com.rdonasco.common.exceptions.PreexistingEntityException;
import com.rdonasco.security.dao.ActionDAO;
import com.rdonasco.security.dao.CapabilityDAO;
import com.rdonasco.security.dao.ResourceDAO;
import com.rdonasco.security.exceptions.NotSecuredResourceException;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
	private CapabilityDAO capabilityDAO;
	@Inject
	private ResourceDAO resourceDAO;
	@Inject
	private ActionDAO actionDAO;

	@Override
	public void checkAccessRights(AccessRightsVO accessRights) throws
			SecurityException
	{
		if (null == accessRights)
		{
			throw new SecurityException("accessRights is null. Please provide valid access rights to check");
		}
		try
		{
			List<CapabilityVO> capabilities = retrieveCapabilitiesOfUser(accessRights);
			Set<AccessRightsVO> accessRightsSet = new HashSet<AccessRightsVO>();
			boolean capabilitiesNotFound = (capabilities == null || capabilities.isEmpty());
			if (capabilitiesNotFound)
			{
				findOrAddActionNamedAs(accessRights.getAction().getName());
				findOrAddSecuredResourceNamedAs(accessRights.getResource().getName());				
			}
			else
			{
				for (CapabilityVO capability : capabilities)
				{
					for (ActionVO action : capability.getActions())
					{
						AccessRightsVO rights = new AccessRightsVOBuilder()
								.setActionVO(action)
								.setResourceVO(capability.getResource())
								.setUserProfileVO(accessRights.getUserProfile()).createAccessRightsVO();
						accessRightsSet.add(rights);
					}
					if (!accessRightsSet.contains(accessRights))
					{
						throw new SecurityException("Access Denied!");
					}
				}
			}

		}
		catch (NotSecuredResourceException e)
		{
			LOG.info(e.getMessage());
		}
		catch (Exception e)
		{
			LOG.log(Level.FINE, e.getMessage(), e);
			throw new SecurityException(e);
		}
	}

	public void setActionDAO(ActionDAO actionDAO)
	{
		this.actionDAO = actionDAO;
	}

	public void setCapabilityDAO(CapabilityDAO capabilityDAO)
	{
		this.capabilityDAO = capabilityDAO;
	}

	private List<CapabilityVO> retrieveCapabilitiesOfUser(
			AccessRightsVO accessRights)
			throws DataAccessException
	{
		List<CapabilityVO> capabilityVOList = null;
		try
		{

			UserSecurityProfile userProfile = SecurityEntityValueObjectConverter.toUserProfile(accessRights.getUserProfile());
			List<Capability> capabilities = capabilityDAO
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
	public ResourceVO addResource(ResourceVO resourceVO) throws
			SecurityManagerException
	{
		try
		{
			Resource resource = SecurityEntityValueObjectConverter.toResource(resourceVO);
			if (null == resourceVO.getId())
			{
				resource.setId(null);
			}
			resourceDAO.create(resource);
			resourceVO = SecurityEntityValueObjectConverter.toResourceVO(resource);
		}
		catch (Exception e)
		{
			throw new SecurityManagerException(e);
		}
		return resourceVO;
	}

	@Override
	public void removeResource(ResourceVO resource) throws
			SecurityManagerException
	{
		try
		{
			resourceDAO.delete(Resource.class, resource.getId());
		}
		catch (Exception e)
		{
			throw new SecurityManagerException(e);
		}
	}

	@Override
	public ResourceVO findResourceNamedAs(String resourceName) throws
			SecurityManagerException, NonExistentEntityException
	{
		ResourceVO resourceVO = null;
		try
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(Resource.QUERY_PARAM_RESOURCE_NAME, resourceName);
			Resource resource = resourceDAO.findUniqueDataUsingNamedQuery(Resource.NAMED_QUERY_FIND_RESOURCE_BY_NAME, parameters);
			resourceVO = SecurityEntityValueObjectConverter.toResourceVO(resource);
		}
		catch (NonExistentEntityException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SecurityManagerException(e);
		}
		return resourceVO;
	}

	@Override
	public ResourceVO findOrAddSecuredResourceNamedAs(String resourceName)
			throws
			SecurityManagerException
	{
		Resource securedResource = null;
		ResourceVO securedResourceVO = null;
		try
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(Capability.QUERY_PARAM_RESOURCE, resourceName);
			List<Capability> capabilities = capabilityDAO.findAllDataUsingNamedQuery(Capability.NAMED_QUERY_FIND_BY_RESOURCE_NAME, parameters);
			if (null != capabilities && !capabilities.isEmpty())
			{
				securedResource = capabilities.get(0).getResource();
			}
			else
			{
				try
				{
					findResourceNamedAs(resourceName);
				}
				catch (NonExistentEntityException e)
				{
					ResourceVO resourceToAdd = new ResourceVO();
					resourceToAdd.setDescription(resourceName);
					resourceToAdd.setName(resourceName);
					addResource(resourceToAdd);
				}
			}
			if (null == securedResource)
			{
				throw new NotSecuredResourceException("Not Secured Resource. Can be accessed by anyone.");
			}
			securedResourceVO = SecurityEntityValueObjectConverter.toResourceVO(securedResource);
		}
		catch (NotSecuredResourceException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SecurityManagerException(e);
		}

		return securedResourceVO;
	}

	public void setResourceDAO(ResourceDAO resourceDAO)
	{
		this.resourceDAO = resourceDAO;
	}

	@Override
	public UserSecurityProfileVO createNewSecurityProfile(
			UserSecurityProfileVO userSecurityProfile)
			throws SecurityManagerException
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ActionVO findOrAddActionNamedAs(String name) throws
			SecurityManagerException
	{
		ActionVO actionVO = null;
		Action action = null;
		try
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(Action.QUERY_PARAM_ACTION, name);
			action = actionDAO.findUniqueDataUsingNamedQuery(Action.NAMED_QUERY_FIND_ACTION_BY_NAME, parameters);
		}
		catch (NonExistentEntityException e)
		{
			LOG.log(Level.WARNING, "Action {0} not found. Creating one", name);
			LOG.log(Level.FINE, e.getMessage(), e);
			action = new Action();
			action.setName(name);
			action.setDescription(name);
			try
			{
				actionDAO.create(action);
			}
			catch (Exception ex)
			{
				throw new SecurityManagerException(ex);
			}

		}
		catch (Exception e)
		{
			throw new SecurityManagerException(e);
		}
		try
		{
			actionVO = SecurityEntityValueObjectConverter.toActionVO(action);
		}
		catch (Exception ex)
		{
			throw new SecurityManagerException(ex);
		}
		return actionVO;
	}
}
