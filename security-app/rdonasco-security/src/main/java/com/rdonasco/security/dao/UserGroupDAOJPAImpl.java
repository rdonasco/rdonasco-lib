/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 13-Mar-2013
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

package com.rdonasco.security.dao;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.security.model.Role;
import com.rdonasco.security.model.SecurityGroup;
import com.rdonasco.security.model.UserCapability;
import com.rdonasco.security.model.UserGroup;
import com.rdonasco.security.model.UserSecurityProfile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Roy F. Donasco
 */
public class UserGroupDAOJPAImpl extends AbstractSecurityDAO<UserGroup>
		implements UserGroupDAO
{

	@Override
	public Class<UserGroup> getDataClass()
	{
		return UserGroup.class;
	}

	@Override
	public List<SecurityGroup> loadGroupsOf(UserSecurityProfile user) throws
			DataAccessException
	{
		Map<String, Object> parameters = new HashMap<String, Object>(1);
		parameters.put(UserCapability.QUERY_PARAM_USER, user);
		List<UserGroup> userGroups = findAllDataUsingNamedQuery(UserGroup.NAMED_QUERY_FIND_GROUP_BY_USER, parameters);
		List<SecurityGroup> roles = new ArrayList<SecurityGroup>(userGroups.size());
		for (UserGroup userGroup : userGroups)
		{
			roles.add(userGroup.getGroup());
		}
		return roles;
	}
}
