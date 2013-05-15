/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 02-May-2013
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
import com.rdonasco.common.utils.CollectionsUtility;
import com.rdonasco.security.dao.RoleCapabilityDAO;
import com.rdonasco.security.dao.RoleDAO;
import com.rdonasco.security.model.Role;
import com.rdonasco.security.model.RoleCapability;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.vo.RoleCapabilityVO;
import com.rdonasco.security.vo.RoleVO;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class RoleManager implements RoleManagerLocal
{

	@Inject
	private RoleDAO roleDao;

	@Inject
	private RoleCapabilityDAO roleCapabilityDAO;

	public void setRoleDAO(RoleDAO userRoleDAO)
	{
		this.roleDao = userRoleDAO;
	}

	@Override
	public void deleteData(RoleVO data) throws DataAccessException
	{
		try
		{
			roleDao.delete(data.getId());
		}
		catch (Exception ex)
		{
			throw new DataAccessException(ex);
		}

	}

	@Override
	public RoleVO loadData(RoleVO data) throws DataAccessException
	{
		RoleVO loadedUserRoleVO = null;
		try
		{
			Role foundData = roleDao.findData(data.getId());
			if (null != foundData)
			{
				loadedUserRoleVO = SecurityEntityValueObjectConverter.toRoleVO(foundData);
			}		
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return loadedUserRoleVO;
	}

	@Override
	public List<RoleVO> retrieveAllData() throws DataAccessException
	{
		List<RoleVO> userRoles = null;
		try
		{
			userRoles = SecurityEntityValueObjectConverter.toRoleVOList(roleDao.findAllData());
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return userRoles;
	}

	@Override
	public RoleVO saveData(RoleVO userRoleVO) throws DataAccessException
	{
		try
		{
			final Role userRole = SecurityEntityValueObjectConverter.toRole(userRoleVO);			
			roleDao.create(userRole);
			userRoleVO.setId(userRole.getId());
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return userRoleVO;
	}

	@Override
	public void updateData(RoleVO data) throws DataAccessException
	{
		try
		{
			ensureThatCapabilitiesAreAssignedToThisRole(data);
			Role updatedRoleDetails = SecurityEntityValueObjectConverter.toRole(data);
			Role roleToUpdate = roleDao.findData(updatedRoleDetails.getId());
			roleToUpdate.setName(updatedRoleDetails.getName());
			roleToUpdate.setCapabilities(CollectionsUtility.updateCollection(
					updatedRoleDetails.getCapabilities(),
					roleToUpdate.getCapabilities(),
					new CollectionsUtility.CollectionItemDeleteStrategy<RoleCapability>()
			{
				@Override
				public void delete(RoleCapability itemToDelete) throws
						CollectionMergeException
				{
					try
					{
						roleCapabilityDAO.delete(itemToDelete.getId());
					}
					catch (Exception e)
					{
						throw new CollectionMergeException(e);
					}
				}
			}, new CollectionsUtility.CollectionItemUpdateStrategy<RoleCapability>()
			{
				@Override
				public void update(RoleCapability itemToUpdate,
						RoleCapability itemToCopy) throws
						CollectionMergeException
				{
					itemToUpdate.setCapability(itemToCopy.getCapability());
					itemToUpdate.setRole(itemToCopy.getRole());
				}
			}));
			roleDao.update(roleToUpdate);
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
	}

	private void ensureThatCapabilitiesAreAssignedToThisRole(RoleVO roleVO)
	{
		for (RoleCapabilityVO roleCapabilityVO : roleVO.getRoleCapabilities())
		{
			roleCapabilityVO.setRoleVO(roleVO);
		}
	}
}
