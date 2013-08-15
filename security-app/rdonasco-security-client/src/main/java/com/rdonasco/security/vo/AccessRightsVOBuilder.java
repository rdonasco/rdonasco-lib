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

import java.util.logging.Logger;

public class AccessRightsVOBuilder
{

	private static final Logger LOG = Logger.getLogger(AccessRightsVOBuilder.class.getName());
	private ResourceVO resource;
	private UserSecurityProfileVO userProfile;
	private ActionVO action;
	private Long applicationID;
	private String applicationToken;
	private String hostNameOrIpAddress;

	public AccessRightsVOBuilder()
	{
	}

	public AccessRightsVOBuilder setResourceVO(ResourceVO resource)
	{
		this.resource = resource;
		return this;
	}

	public AccessRightsVOBuilder setResourceAsString(String stringResource)
	{
		ResourceVO securedResource = new ResourceVOBuilder()
				.setName(stringResource)
				.createResourceVO();
		this.resource = securedResource;
		return this;
	}

	public AccessRightsVOBuilder setResourceID(Long resourceID)
	{
		if (null == resource)
		{
			LOG.warning("resource not set, ignoring resourceID parameter.");
		}
		else
		{
			resource.setId(resourceID);
		}
		return this;
	}

	public AccessRightsVOBuilder setUserProfileVO(
			UserSecurityProfileVO userProfile)
	{
		this.userProfile = userProfile;
		return this;
	}

	public AccessRightsVOBuilder setActionVO(ActionVO action)
	{
		this.action = action;
		return this;
	}

	public AccessRightsVOBuilder setActionAsString(String action)
	{
		ActionVO newAction = ActionVO.createWithName(action);
		newAction.setName(action);
		this.action = newAction;
		return this;
	}

	public AccessRightsVOBuilder setActionID(Long actionID)
	{
		if (action == null)
		{
			LOG.warning("Action not set, ignoring actionID parameter");
		}
		else
		{
			action.setId(actionID);
		}
		return this;
	}

	public AccessRightsVOBuilder setApplicationID(Long applicationID)
	{
		this.applicationID = applicationID;
		return this;
	}

	public AccessRightsVOBuilder setApplicationToken(String applicationToken)
	{
		this.applicationToken = applicationToken;
		return this;
	}
	
	public AccessRightsVOBuilder setHostNameOrIpAddress(String hostNameOrIpAddress)
	{
		this.hostNameOrIpAddress = hostNameOrIpAddress;
		return this;
	}	

	public AccessRightsVO createAccessRightsVO()
	{
		return new AccessRightsVO(applicationID, applicationToken, hostNameOrIpAddress, resource, userProfile, action);
	}
}
