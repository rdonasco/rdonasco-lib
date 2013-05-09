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

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.RoleVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Roy F. Donasco
 */
@Local
public interface UserSecurityProfileManagerLocal
{

	UserSecurityProfileVO createNewUserSecurityProfile(
			final UserSecurityProfileVO userSecurityProfile) throws
			SecurityManagerException;

	List<CapabilityVO> retrieveCapabilitiesOfUser(
			AccessRightsVO accessRights) throws DataAccessException;

	List<CapabilityVO> retrieveCapabilitiesOfUserBasedOnRoles(
			AccessRightsVO accessRights) throws DataAccessException;

	List<RoleVO> retrieveRolesOfUser(UserSecurityProfileVO userSecurityProfile)
			throws DataAccessException;

	UserSecurityProfileVO findSecurityProfileWithLogonID(String logonId)
			throws SecurityManagerException;

	List<UserSecurityProfileVO> findAllProfiles() throws
			SecurityManagerException;

	void setupDefaultCapabilitiesForUser(
			UserSecurityProfileVO userSecurityProfile) throws
			SecurityManagerException;

	void addCapabilityForUser(UserSecurityProfileVO userSecurityProfileVO,
			CapabilityVO capability) throws SecurityManagerException;

	void removeUserSecurityProfile(
			UserSecurityProfileVO securityProfileToRemove) throws
			SecurityManagerException;

	void updateUserSecurityProfile(UserSecurityProfileVO userSecurityProfile)
			throws SecurityManagerException;

	int removeAllAssignedUserCapability(CapabilityVO capability) throws
			DataAccessException;
}
