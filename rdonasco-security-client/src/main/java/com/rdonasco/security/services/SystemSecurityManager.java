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

import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.List;

/**
 *
 * @author Roy F. Donasco
 */
public interface SystemSecurityManager
{

	void checkAccessRights(AccessRightsVO accessRights);

	UserSecurityProfileVO createNewSecurityProfile(
			UserSecurityProfileVO userSecurityProfile) throws
			SecurityManagerException;

	void updateSecurityProfile(UserSecurityProfileVO userSecurityProfile) throws
			SecurityManagerException;

	void setupDefaultCapabilitiesForUser(
			UserSecurityProfileVO userSecurityProfile)
			throws SecurityManagerException;

	void addCapabilityForUser(UserSecurityProfileVO userSecurityProfile,
			CapabilityVO capability) throws SecurityManagerException;

	void removeSecurityProfile(UserSecurityProfileVO securityProfileToRemove) throws SecurityManagerException;
	UserSecurityProfileVO findSecurityProfileWithLogonID(String logonID) throws SecurityManagerException;
	boolean isSecuredResource(String resource);
	List<UserSecurityProfileVO> findAllProfiles() throws SecurityManagerException;
}
