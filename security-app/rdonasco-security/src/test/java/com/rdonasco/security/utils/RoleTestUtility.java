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

package com.rdonasco.security.utils;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.security.services.RoleManagerLocal;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.RoleVO;
import com.rdonasco.security.vo.RoleVOBuilder;

/**
 *
 * @author Roy F. Donasco
 */
public class RoleTestUtility 
{

	private RoleManagerLocal userRoleManager;

	public RoleTestUtility(RoleManagerLocal userRoleManager)
	{
		this.userRoleManager = userRoleManager;
	}

	public RoleVO createRoleNamedAndWithCapability(String roleName,
			CapabilityVO capabilityVO) throws DataAccessException
	{
		RoleVO userRole = new RoleVOBuilder()
				.setName(roleName)
				.addCapability(capabilityVO)
				.createUserRoleVO();
		userRole = userRoleManager.saveData(userRole);
		return userRole;
	}

	public RoleVO createRoleWithNoCapability(String roleName) throws
			DataAccessException
	{
		return createRoleNamedAndWithCapability(roleName, null);
	}
}
