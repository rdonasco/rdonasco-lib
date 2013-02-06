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
import com.rdonasco.security.dao.CapabilityDAO;
import com.rdonasco.security.dao.ResourceDAO;
import com.rdonasco.security.exceptions.NotSecuredResourceException;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
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
public class SecurityManagerImpl implements SecurityManager
{

	private static final Logger LOG = Logger.getLogger(SecurityManagerImpl.class.getName());
	@Inject
	private CapabilityDAO capabilityDAO;
	@Inject
	private ResourceDAO resourceDAO;

	@Override
	public void checkAccessRights(AccessRightsVO accessRights) throws
			SecurityException, NotSecuredResourceException
	{
		if (null == accessRights)
		{
			throw new SecurityException("accessRights is null. Please provide valid access rights to check");
		}
		try
		{
			List<Capability> capabilities = retrieveCapabilitiesOfUser(accessRights);
			Set<AccessRightsVO> accessRightsSet = new HashSet<AccessRightsVO>();
			boolean capabilitiesNotFound = (capabilities == null || capabilities.isEmpty());
			if (capabilitiesNotFound)
			{
				findOrAddSecuredResourceNamedAs(accessRights.getResource().getName());

			}
			else
			{
				for (Capability capability : capabilities)
				{
					for (Action action : capability.getActions())
					{
						AccessRightsVO rights = new AccessRightsVOBuilder()
								.setAction(action)
								.setResource(capability.getResource())
								.setUserProfile(accessRights.getUserProfile()).createAccessRightsVO();
						accessRightsSet.add(rights);
					}
					if (!accessRightsSet.contains(accessRights))
					{
						throw new SecurityException("Access Denied!");
					}
				}
			}

		}
		catch(NotSecuredResourceException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			LOG.log(Level.FINE, e.getMessage(), e);
			throw new SecurityException(e);
		}
	}

	public void setCapabilityDAO(CapabilityDAO capabilityDAO)
	{
		this.capabilityDAO = capabilityDAO;
	}

	private List<Capability> retrieveCapabilitiesOfUser(
			AccessRightsVO accessRights)
			throws DataAccessException
	{
		return capabilityDAO
				.loadCapabilitiesOf(accessRights.getUserProfile());
	}

	@Override
	public Resource addResource(Resource resource) throws
			SecurityManagerException
	{
		try
		{
			resourceDAO.create(resource);
		}
		catch (Exception e)
		{
			throw new SecurityManagerException(e);
		}
		return resource;
	}

	@Override
	public void removeResource(Resource resource) throws
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
	public Resource findResourceNamedAs(String resourceName) throws
			SecurityManagerException, NonExistentEntityException
	{
		Resource resource = null;
		try
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(Resource.QUERY_PARAM_RESOURCE_NAME, resourceName);
			resource = resourceDAO.findUniqueDataUsingNamedQuery(Resource.NAMED_QUERY_FIND_RESOURCE_BY_NAME, parameters);
		}
		catch (NonExistentEntityException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SecurityManagerException(e);
		}
		return resource;
	}

	@Override
	public Resource findOrAddSecuredResourceNamedAs(String resourceName) throws
			SecurityManagerException
	{
		Resource securedResource = null;
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
					Resource resourceToAdd = new Resource();
					resourceToAdd.setDescription(resourceName);
					resourceToAdd.setName(resourceName);
					addResource(resourceToAdd);
				}
			}
			if (null == securedResource)
			{
				throw new NotSecuredResourceException("Not Secured Resource. Can be accessed by anyone.");
			}
		}
		catch(NotSecuredResourceException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SecurityManagerException(e);
		}
		return securedResource;
	}

	public void setResourceDAO(ResourceDAO resourceDAO)
	{
		this.resourceDAO = resourceDAO;
	}
}
