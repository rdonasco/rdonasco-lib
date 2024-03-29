/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 19-May-2013
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

import com.rdonasco.common.dao.DataAccess;
import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.security.model.SecurityGroup;
import com.rdonasco.security.model.UserGroup;
import com.rdonasco.security.model.UserSecurityProfile;
import java.util.List;

/**
 *
 * @author Roy F. Donasco
 */
public interface UserGroupDAO extends DataAccess<UserGroup>
{

	List<SecurityGroup> loadGroupsOf(UserSecurityProfile user) throws
			DataAccessException;
}
