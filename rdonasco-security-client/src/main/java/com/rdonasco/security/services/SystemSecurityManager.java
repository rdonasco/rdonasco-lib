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

import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;

/**
 *
 * @author Roy F. Donasco
 */

public interface SystemSecurityManager
{
	void checkAccessRights(AccessRightsVO accessRights) throws SecurityException;
	ResourceVO addResource(ResourceVO resource) throws SecurityManagerException;
	void removeResource(ResourceVO resource) throws SecurityManagerException;
	ResourceVO findResourceNamedAs(String resourceName) throws SecurityManagerException, NonExistentEntityException;
	ResourceVO findOrAddSecuredResourceNamedAs(String resourceName) throws SecurityManagerException, NonExistentEntityException;
	public UserSecurityProfileVO createNewSecurityProfile(UserSecurityProfileVO userSecurityProfile) throws SecurityManagerException;
}