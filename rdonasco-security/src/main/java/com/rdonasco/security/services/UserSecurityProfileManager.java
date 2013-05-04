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

import com.rdonasco.security.dao.UserSecurityProfileDAO;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.vo.UserSecurityProfileVO;
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
	@Inject
	private UserSecurityProfileDAO userSecurityProfileDAO;

	public void setUserSecurityProfileDAO(
			UserSecurityProfileDAO userSecurityProfileDAO)
	{
		this.userSecurityProfileDAO = userSecurityProfileDAO;
	}

	@Override
	public UserSecurityProfileVO createNewUserSecurityProfile(
			final UserSecurityProfileVO userSecurityProfile) throws
			SecurityManagerException
	{
		UserSecurityProfileVO createdProfile = null;
		try
		{
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
	// Add business logic below. (Right-click in editor and choose
	// "Insert Code > Add Business Method")

}
