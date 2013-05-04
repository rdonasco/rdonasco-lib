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
package com.rdonasco.security.vo;

import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco
 */
public class UserRoleVO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private Long id;

	private UserSecurityProfileVO userProfile;

	private RoleVO role;

	UserRoleVO(Long id, UserSecurityProfileVO userProfile, RoleVO role)
	{
		this.id = id;
		this.userProfile = userProfile;
		this.role = role;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public UserSecurityProfileVO getUserProfile()
	{
		return userProfile;
	}

	public void setUserProfile(UserSecurityProfileVO userProfile)
	{
		this.userProfile = userProfile;
	}

	public RoleVO getRole()
	{
		return role;
	}

	public void setRole(RoleVO role)
	{
		this.role = role;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 11 * hash + (this.userProfile != null ? this.userProfile.hashCode() : 0);
		hash = 11 * hash + (this.role != null ? this.role.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean isEqual = true;
		if (obj == null)
		{
			isEqual = false;
		}
		else if (getClass() != obj.getClass())
		{
			isEqual = false;
		}
		else
		{
			final UserRoleVO other = (UserRoleVO) obj;
			if (this.userProfile != other.userProfile && (this.userProfile == null || !this.userProfile.equals(other.userProfile)))
			{
				isEqual = false;
			}
			else if (this.role != other.role && (this.role == null || !this.role.equals(other.role)))
			{
				isEqual = false;
			}
		}
		return isEqual;
	}
}
