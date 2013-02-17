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
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object)
	{
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof UserCapabilityVO))
		{
			return false;
		}
		UserCapabilityVO other = (UserCapabilityVO) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "UserCapabilityVO{" + "id=" + id + ", userProfile.loginId=" + userProfile.getLoginId() + ", capability=" + capability + '}';
	}


}
