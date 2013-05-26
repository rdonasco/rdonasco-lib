/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 21-Apr-2013
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
package com.rdonasco.security.authentication.services;

import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.security.exceptions.SecurityAuthorizationException;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class SessionSecurityChecker
{
	private static final Logger LOG = Logger.getLogger(SessionSecurityChecker.class.getName());

	@Inject
	private Instance<LoggedOnSessionProvider> loggedOnSessionInstances;
	private LoggedOnSessionProvider loggedOnSessionProvider;
	@Inject
	private SystemSecurityManagerDecorator securityManager;

	public void checkAccess(String resource, String action)
	{
		LOG.log(Level.INFO, "executing checkAccess(resource = {0}, action= {1})", new Object[]
		{
			resource, action
		});
		if (null != getLoggedOnSessionProvider())
		{
			UserSecurityProfileVO userSecurityProfileVO = getLoggedOnSessionProvider().getLoggedOnSession().getLoggedOnUser();
			AccessRightsVO accessRights = new AccessRightsVOBuilder()
					.setActionAsString(action)
					.setResourceAsString(resource)
					.setUserProfileVO(userSecurityProfileVO)
					.createAccessRightsVO();
			securityManager.checkAccessRights(accessRights);
		}
		else
		{
			throw new SecurityAuthorizationException(I18NResource.localize("User not logged on"));
		}
	}

	public boolean hasTheCapabilityTo(String action, String resource)
	{
		boolean hasTheCapability = false;
		try
		{
			checkAccess(resource, action);
			hasTheCapability = true;
		}
		catch (Exception e)
		{
			LOG.log(Level.FINE, e.getMessage(), e);
		}
		return hasTheCapability;
	}

	public LoggedOnSessionProvider getLoggedOnSessionProvider()
	{
		if (null == loggedOnSessionProvider && loggedOnSessionInstances != null)
		{
			loggedOnSessionProvider = loggedOnSessionInstances.get();
		}
		return loggedOnSessionProvider;
	}
}
