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
package com.rdonasco.security.vo;

import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco
 */
public class UserCapabilityVO implements Serializable
{

	private static final long serialVersionUID = 1L;
	private Long id;
	private UserSecurityProfileVO userProfile;
	private CapabilityVO capability;

	public UserCapabilityVO(Long id, UserSecurityProfileVO userProfile,
			CapabilityVO capability)
	{
		this.id = id;
		this.userProfile = userProfile;
		this.capability = capability;
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

	public CapabilityVO getCapability()
	{
		return capability;
	}

	public void setCapability(CapabilityVO capability)
	{
		this.capability = capability;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 59 * hash + (this.userProfile != null ? this.userProfile.hashCode() : 0);
		hash = 59 * hash + (this.capability != null ? this.capability.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final UserCapabilityVO other = (UserCapabilityVO) obj;
		if (this.userProfile != other.userProfile && (this.userProfile == null || !this.userProfile.equals(other.userProfile)))
		{
			return false;
		}
		if (this.capability != other.capability && (this.capability == null || !this.capability.equals(other.capability)))
		{
			return false;
		}
		return true;
	}


	

	@Override
	public String toString()
	{
		return "UserCapabilityVO{" + "id=" + id + ", userProfile.loginId=" + userProfile.getLogonId() + ", capability=" + capability + '}';
	}


}
