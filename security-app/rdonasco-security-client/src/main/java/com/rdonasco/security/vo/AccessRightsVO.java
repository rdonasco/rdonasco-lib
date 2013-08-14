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
	private String resourceName;
	private String actionName;
	private String applicationToken;
	private Long applicationID;
	private String hostNameOrIpAddress;

	AccessRightsVO(Long applicationID, String applicationToken, String hostNameOrIpAddress,
			ResourceVO resource,UserSecurityProfileVO userProfile, ActionVO action)
	{
		setResource(resource);
		setUserProfile(userProfile);
		setAction(action);
		setApplicationID(applicationID);
		setApplicationToken(applicationToken);
		setHostNameOrIpAddress(hostNameOrIpAddress);
	}

	private void setResource(ResourceVO resource)
	{
		this.resource = resource;
		if (null != resource && resource.getName() != null)
		{
			resourceName = resource.getName();
		}
	}

	private void setUserProfile(UserSecurityProfileVO userProfile)
	{
		this.userProfile = userProfile;
	}

	private void setAction(ActionVO action)
	{
		this.action = action;
		if (null != action && action.getName() != null)
		{
			actionName = action.getName().toLowerCase();
		}
	}

	private void setApplicationToken(String applicationToken)
	{
		this.applicationToken = applicationToken;
	}

	private void setApplicationID(Long applicationID)
	{
		this.applicationID = applicationID;
	}

	public String getApplicationToken()
	{
		return applicationToken;
	}

	public Long getApplicationID()
	{
		return applicationID;
	}	
		

	public ResourceVO getResource()
	{
		return resource;
	}

	public String getHostNameOrIpAddress()
	{
		return hostNameOrIpAddress;
	}

	private void setHostNameOrIpAddress(String hostNameOrIpAddress)
	{
		this.hostNameOrIpAddress = hostNameOrIpAddress;
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
		hash = 89 * hash + (getResourceName() != null ? getResourceName().hashCode() : 0);
		hash = 89 * hash + (this.userProfile != null ? this.userProfile.hashCode() : 0);
		hash = 89 * hash + (getActionName() != null ? getActionName().hashCode() : 0);
		return hash;
	}

	public Long getId()
	{
		return new Long(hashCode());
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean isEqual;
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
			final AccessRightsVO other = (AccessRightsVO) obj;
			if (this.getId() == null)
			{
				isEqual = false;
			}
			else
			{
				isEqual = this.getId().equals(other.getId());
			}
		}
		return isEqual;
	}

	@Override
	public String toString()
	{
		return "AccessRightsVO{" + "resource=" + getResourceName() + ", userProfile=" + userProfile.getId() + ", action=" + getActionName() + '}';
	}

	private String getResourceName()
	{
		return resourceName;
	}

	private String getActionName()
	{
		return actionName;
	}
}
