/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 16-May-2013
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
import com.rdonasco.security.dao.SecurityGroupDAO;
import com.rdonasco.security.dao.SecurityGroupRoleDAO;
import com.rdonasco.security.model.SecurityGroup;
import com.rdonasco.security.model.SecurityGroupRole;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.vo.SecurityGroupVO;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class SecurityGroupDataManager implements SecurityGroupDataManagerLocal
{

	@Inject
	private SecurityGroupDAO securityGroupDAO;

	@Inject
	private SecurityGroupRoleDAO securityGroupRoleDAO;

	public void setSecurityGroupDAO(SecurityGroupDAO securityGroupDAO)
	{
		this.securityGroupDAO = securityGroupDAO;
	}

	public void setSecurityGroupRoleDAO(
			SecurityGroupRoleDAO securityGroupRoleDAO)
	{
		this.securityGroupRoleDAO = securityGroupRoleDAO;
	}


	@Override
	public void deleteData(SecurityGroupVO data) throws DataAccessException
	{
		try
		{
			securityGroupDAO.delete(data.getId());
		}
		catch (Exception ex)
		{
			throw new DataAccessException(ex);
		}
	}

	@Override
	public SecurityGroupVO loadData(SecurityGroupVO data) throws
			DataAccessException
	{
		SecurityGroupVO securityGroupVO = null;
		try
		{
			SecurityGroup loadedData = securityGroupDAO.findData(data.getId());
			if (null == loadedData)
			{
				throw new DataAccessException("Record not found");
			}
			securityGroupVO = SecurityEntityValueObjectConverter.toSecurityGroupVO(loadedData);
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}

		return securityGroupVO;
	}

	@Override
	public List<SecurityGroupVO> retrieveAllData() throws DataAccessException
	{
		List<SecurityGroupVO> allGroupVO;
		try
		{
			List<SecurityGroup> allGroup = securityGroupDAO.findAllData();
			allGroupVO = SecurityEntityValueObjectConverter.toSecurityGroupVOList(allGroup);
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return allGroupVO;
	}

	@Override
	public SecurityGroupVO saveData(SecurityGroupVO dataToSave) throws
			DataAccessException
	{
		SecurityGroupVO savedData = null;
		try
		{
			SecurityGroup securityGroup = SecurityEntityValueObjectConverter.toSecurityGroup(dataToSave);
			securityGroupDAO.create(securityGroup);
			savedData = SecurityEntityValueObjectConverter.toSecurityGroupVO(securityGroup);
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return savedData;
	}

	@Override
	public void updateData(SecurityGroupVO dataToUpdate) throws
			DataAccessException
	{
		try
		{
			SecurityGroup securityGroupWithUpdatedDetails = SecurityEntityValueObjectConverter.toSecurityGroup(dataToUpdate);
			SecurityGroup securityGroupToUpdate = securityGroupDAO.findData(securityGroupWithUpdatedDetails.getId());
			securityGroupToUpdate.setName(securityGroupWithUpdatedDetails.getName());
			securityGroupToUpdate.setGroupRoles(CollectionsUtility.updateCollection(
					securityGroupWithUpdatedDetails.getGroupRoles(),
					securityGroupToUpdate.getGroupRoles(), new CollectionsUtility.CollectionItemDeleteStrategy<SecurityGroupRole>()
			{
				@Override
				public void delete(SecurityGroupRole itemToDelete) throws
						CollectionMergeException
				{
					try
					{
						securityGroupRoleDAO.delete(itemToDelete.getId());
					}
					catch (Exception e)
					{
						throw new CollectionMergeException(e);
					}

				}
			}, new CollectionsUtility.CollectionItemUpdateStrategy<SecurityGroupRole>()
			{
				@Override
				public void update(SecurityGroupRole itemToUpdate,
						SecurityGroupRole itemToCopy) throws
						CollectionMergeException
				{
					itemToUpdate.setRole(itemToCopy.getRole());
					itemToUpdate.setSecurityGroup(itemToCopy.getSecurityGroup());
				}
			}));

			securityGroupDAO.update(securityGroupToUpdate);
		}
		catch (Exception ex)
		{
			throw new DataAccessException(ex);
		}
	}
}
