/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 07-Mar-2013
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
package com.rdonasco.security.interceptors;

import com.rdonasco.security.exceptions.SecurityAuthorizationException;
import com.rdonasco.security.services.LoggedOnSession;
import com.rdonasco.security.services.SystemSecurityManager;
import com.rdonasco.security.services.SystemSecurityManagerRemote;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 *
 * @author Roy F. Donasco
 */
@Secured
@Interceptor
public class SecuredInterceptor
{

	@Inject
	private SystemSecurityManager securityManager;
	@Inject
	private LoggedOnSession loggedOnSession;

	@EJB
	void setSystemSecurityManager(
			SystemSecurityManagerRemote systemSecurityManager)
	{
		this.securityManager = systemSecurityManager;
	}

	@AroundInvoke
	public Object checkSecuredInvocation(InvocationContext joinPoint) throws
			Exception
	{
		String action;
		String resource = getResourceFromType(joinPoint);

		if (resource.isEmpty())
		{
			resource = getResourceFromMethod(joinPoint);
			action = getActionFromMethod(joinPoint);
		}
		else
		{
			action = getActionFromType(joinPoint);
		}

		if (!resource.isEmpty())
		{
			if (securityManager.isSecuredResource(resource) && !loggedOnSession.isLoggedOn())
			{
				throw new SecurityAuthorizationException("Logon Required");
			}
			else if (securityManager.isSecuredResource(resource))
			{
				AccessRightsVO accessRightsVO = new AccessRightsVOBuilder()
						.setResourceAsString(resource)
						.setActionAsString(action)
						.setUserProfileVO(securityManager.findSecurityProfileWithLogonID(loggedOnSession.getLoggedOnUser().getLogonId()))
						.createAccessRightsVO();
				securityManager.checkAccessRights(accessRightsVO);
			}
		}

		return joinPoint.proceed();
	}

	void setLoggedOnSession(LoggedOnSession loggedOnSession)
	{
		this.loggedOnSession = loggedOnSession;
	}

	String getResourceFromType(InvocationContext joinPoint)
	{
		// get resource from the annotation
		String resource = "";
		Secured typeAnnotation = joinPoint.getTarget().getClass().getAnnotation(Secured.class);
		if (null != typeAnnotation)
		{
			resource = typeAnnotation.resource();
		}
		return resource;
	}

	private String getResourceFromMethod(InvocationContext joinPoint)
	{
		String resource = "";
		Secured methodAnnotation = joinPoint.getMethod().getAnnotation(Secured.class);
		if (null != methodAnnotation)
		{
			resource = methodAnnotation.resource();
		}
		return resource;
	}

	private String getActionFromMethod(InvocationContext joinPoint)
	{
		String action = "";
		Secured methodAnnotation = joinPoint.getMethod().getAnnotation(Secured.class);
		if (null != methodAnnotation)
		{
			action = methodAnnotation.action();
		}
		return action;
	}

	private String getActionFromType(InvocationContext joinPoint)
	{
		String action = "";
		Secured typeAnnotation = joinPoint.getTarget().getClass().getAnnotation(Secured.class);
		if (null != typeAnnotation)
		{
			action = typeAnnotation.action();
		}
		return action;
	}
}
