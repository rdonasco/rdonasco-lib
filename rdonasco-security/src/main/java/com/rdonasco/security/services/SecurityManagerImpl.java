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
import com.rdonasco.security.dao.CapabilityDAO;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.SecuredAction;
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
			List<Capability> capabilities = retrieveCapabilitiesOfUser(accessRights);
			Set<AccessRightsVO> accessRightsSet = new HashSet<AccessRightsVO>();
			for (Capability capability : capabilities)
			{
				for (SecuredAction action : capability.getActions())
				{
					AccessRightsVO rights = new AccessRightsVOBuilder()
							.setAction(action)
							.setResource(capability.getResource())
							.setUserProfile(accessRights.getUserProfile()).createAccessRightsVO();
					accessRightsSet.add(rights);			
				}
				if(!accessRightsSet.contains(accessRights))
				{
					throw new SecurityException("Access Denied!");
				}
			}
		}
		catch (Exception e)
		{
			LOG.log(Level.FINE, e.getMessage(), e);
			throw new SecurityException(e);
		}
	}

	@Override
	public void setSecurityDAO(CapabilityDAO securityDAO)
	{
		this.capabilityDAO = securityDAO;
	}

	private List<Capability> retrieveCapabilitiesOfUser(
			AccessRightsVO accessRights)
			throws DataAccessException
	{
		return capabilityDAO
				.loadCapabilitiesOf(accessRights.getUserProfile());
	}
}
