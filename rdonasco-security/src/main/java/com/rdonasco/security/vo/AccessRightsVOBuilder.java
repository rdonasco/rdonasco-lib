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

import com.rdonasco.security.model.Action;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.model.UserSecurityProfile;
import java.util.logging.Logger;


public class AccessRightsVOBuilder 
{
	private static final Logger LOG = Logger.getLogger(AccessRightsVOBuilder.class.getName());
	
	
	private Resource resource;
	private UserSecurityProfile userProfile;
	private Action action;

	public AccessRightsVOBuilder()
	{
	}

	public AccessRightsVOBuilder setResource(Resource resource)
	{
		this.resource = resource;
		return this;
	}
	
	public AccessRightsVOBuilder setResourceAsString(String stringResource)
	{
		Resource securedResource = new Resource();
		securedResource.setName(stringResource);
		this.resource = securedResource;
		return this;
	}
	
	public AccessRightsVOBuilder setResourceID(Long resourceID)
	{
		if(null == resource)
		{
			LOG.warning("resource not set, ignoring resourceID parameter.");
		}
		else
		{
			resource.setId(resourceID);
		}
		return this;
	}

	public AccessRightsVOBuilder setUserProfile(UserSecurityProfile userProfile)
	{
		this.userProfile = userProfile;
		return this;
	}

	public AccessRightsVOBuilder setAction(Action action)
	{
		this.action = action;
		return this;
	}
	
	public AccessRightsVOBuilder setActionAsString(String action)
	{
		Action newAction = Action.createWithName(action);
		newAction.setName(action);
		this.action = newAction;
		return this;
	}
	
	public AccessRightsVOBuilder setActionID(Long actionID)
	{
		if(action == null)
		{
			LOG.warning("Action not set, ignoring actionID parameter");
		}
		else
		{
			action.setId(actionID);
		}
		return this;
	}	

	public AccessRightsVO createAccessRightsVO()
	{
		return new AccessRightsVO(resource, userProfile, action);
	}

}
