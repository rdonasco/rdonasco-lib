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
import com.rdonasco.security.dao.UserRoleDAO;
import com.rdonasco.security.model.UserRole;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.vo.UserRoleVO;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class UserRoleManager implements UserRoleManagerLocal
{

	@Inject
	private UserRoleDAO userRoleDAO;

	public void setUserRoleVODAO(UserRoleDAO userRoleDAO)
	{
		this.userRoleDAO = userRoleDAO;
	}

	@Override
	public void deleteData(UserRoleVO data) throws DataAccessException
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
	public UserRoleVO loadData(UserRoleVO data) throws DataAccessException
	{
		UserRoleVO loadedUserRoleVO = null;
		try
		{
			UserRole foundData = userRoleDAO.findData(data.getId());
			if (null != foundData)
			{
				loadedUserRoleVO = SecurityEntityValueObjectConverter.toUserRoleVO(foundData);
			}		
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return loadedUserRoleVO;
	}

	@Override
	public List<UserRoleVO> retrieveAllData() throws DataAccessException
	{
		List<UserRoleVO> userRoles = null;
		try
		{
			userRoles = SecurityEntityValueObjectConverter.toUserRoleVOList(userRoleDAO.findAllData());
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return userRoles;
	}

	@Override
	public UserRoleVO saveData(UserRoleVO userRoleVO) throws DataAccessException
	{
		try
		{
			final UserRole userRole = SecurityEntityValueObjectConverter.toUserRole(userRoleVO);			
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
	public void updateData(UserRoleVO data) throws DataAccessException
	{
		try
		{
			userRoleDAO.update(SecurityEntityValueObjectConverter.toUserRole(data));
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
	}
}
