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
public class AccessRightsVO implements Serializable
{

	private static final long serialVersionUID = 1L;
	private ResourceVO resource;
	private UserSecurityProfileVO userProfile;
	private ActionVO action;

	AccessRightsVO(ResourceVO resource,
			UserSecurityProfileVO userProfile, ActionVO action)
	{
		this.resource = resource;
		this.userProfile = userProfile;
		this.action = action;
	}

	void setResource(ResourceVO resource)
	{
		this.resource = resource;
	}

	void setUserProfile(UserSecurityProfileVO userProfile)
	{
		this.userProfile = userProfile;
	}

	void setAction(ActionVO action)
	{
		this.action = action;
	}

	public ResourceVO getResource()
	{
		return resource;
	}

	public static long getSerialVersionUID()
	{
		return serialVersionUID;
	}

	public UserSecurityProfileVO getUserProfile()
	{
		return userProfile;
	}

	public ActionVO getAction()
	{
		return action;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 89 * hash + (this.resource != null ? this.resource.hashCode() : 0);
		hash = 89 * hash + (this.userProfile != null ? this.userProfile.hashCode() : 0);
		hash = 89 * hash + (this.action != null ? this.action.hashCode() : 0);
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
		final AccessRightsVO other = (AccessRightsVO) obj;
		if (this.resource != other.resource && (this.resource == null || !this.resource.equals(other.resource)))
		{
			return false;
		}
		if (this.userProfile != other.userProfile && (this.userProfile == null || !this.userProfile.equals(other.userProfile)))
		{
			return false;
		}
		if (this.action != other.action && (this.action == null || !this.action.equals(other.action)))
		{
			return false;
		}
		return true;
	}
	
}
