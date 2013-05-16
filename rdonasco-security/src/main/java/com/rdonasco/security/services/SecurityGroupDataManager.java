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

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.security.dao.SecurityGroupDAO;
import com.rdonasco.security.model.SecurityGroup;
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

	public void setSecurityGroupDAO(SecurityGroupDAO securityGroupDAO)
	{
		this.securityGroupDAO = securityGroupDAO;
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
		SecurityGroup loadedData = securityGroupDAO.findData(data.getId());
		if (null == loadedData)
		{
			throw new DataAccessException("Record not found");
		}
		SecurityGroupVO securityGroupVO = SecurityEntityValueObjectConverter.toSecurityGroupVO(loadedData);
		return securityGroupVO;
	}

	@Override
	public List<SecurityGroupVO> retrieveAllData() throws DataAccessException
	{
		List<SecurityGroupVO> allGroupVO;
		List<SecurityGroup> allGroup = securityGroupDAO.findAllData();
		allGroupVO = SecurityEntityValueObjectConverter.toSecurityGroupVOList(allGroup);
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
			SecurityGroup securityGroup = SecurityEntityValueObjectConverter.toSecurityGroup(dataToUpdate);
			securityGroupDAO.update(securityGroup);
		}
		catch (Exception ex)
		{
			throw new DataAccessException(ex);
		}
	}
}
