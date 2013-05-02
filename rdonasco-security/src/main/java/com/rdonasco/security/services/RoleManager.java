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

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.security.dao.RoleDAO;
import com.rdonasco.security.model.Role;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
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
	private RoleDAO userRoleDAO;

	public void setUserRoleVODAO(RoleDAO userRoleDAO)
	{
		this.userRoleDAO = userRoleDAO;
	}

	@Override
	public void deleteData(RoleVO data) throws DataAccessException
	{
		try
		{
			userRoleDAO.delete(data.getId());
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
			Role foundData = userRoleDAO.findData(data.getId());
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
			userRoles = SecurityEntityValueObjectConverter.toRoleVOList(userRoleDAO.findAllData());
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
			userRoleDAO.create(userRole);
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
			userRoleDAO.update(SecurityEntityValueObjectConverter.toRole(data));
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
	}
}
