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
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.services.LoggedOnSessionProvider;
import com.rdonasco.security.services.SystemSecurityManager;
import com.rdonasco.security.services.SystemSecurityManagerRemote;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.logging.Level;
import java.util.logging.Logger;
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

	private static final Logger LOG = Logger.getLogger(SecuredInterceptor.class.getName());
	private SystemSecurityManager systemSecurityManager;
	private LoggedOnSessionProvider loggedOnSessionProvider;
	private SecurityExceptionHandler securityExceptionHandler;

	public SecuredInterceptor()
	{
		LOG.info("SecuredInterceptor instantiated");
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
		Object returnValue = null;
		InvocationEventType invocationEventType = getInvocationEventTypeFromMethod(joinPoint);
		try
		{
			if (InvocationEventType.BEFORE == invocationEventType)
			{
				LOG.log(Level.INFO, "executing doTheInvocationCheck before method {0}", joinPoint.getMethod().getName());
				doTheInvocationCheck(resource, action);
				returnValue = joinPoint.proceed();
			}
			else
			{
				returnValue = joinPoint.proceed();
				LOG.log(Level.INFO, "executing doTheInvocationCheck after method {0}", joinPoint.getMethod().getName());
				doTheInvocationCheck(resource, action);
			}
		}
		catch (Exception e)
		{
			if (null != securityExceptionHandler)
			{
				Throwable cause = e.getCause();
				if (null != cause)
				{
					securityExceptionHandler.handleSecurityException(cause);
				}
				else
				{
					securityExceptionHandler.handleSecurityException(e);
				}
			}
			else
			{
				throw e;
			}
		}
		return returnValue;
	}

	String getResourceFromType(InvocationContext joinPoint)
	{
		// get resource from the annotation
		String resource = "";
		SecuredCapability typeAnnotation = joinPoint.getTarget().getClass().getAnnotation(SecuredCapability.class);
		if (null != typeAnnotation)
		{
			resource = typeAnnotation.resource();
		}
		return resource;
	}

	private String getResourceFromMethod(InvocationContext joinPoint)
	{
		String resource = "";
		SecuredCapability methodAnnotation = joinPoint.getMethod().getAnnotation(SecuredCapability.class);
		if (null != methodAnnotation)
		{
			resource = methodAnnotation.resource();
		}
		return resource;
	}

	private InvocationEventType getInvocationEventTypeFromMethod(
			InvocationContext joinPoint)
	{
		InvocationEventType invocationEventType = InvocationEventType.BEFORE;
		SecuredCapability methodAnnotation = joinPoint.getMethod().getAnnotation(SecuredCapability.class);
		if (null != methodAnnotation)
		{
			invocationEventType = methodAnnotation.invocationEventType();
		}
		return invocationEventType;
	}

	private String getActionFromMethod(InvocationContext joinPoint)
	{
		String action = "";
		SecuredCapability methodAnnotation = joinPoint.getMethod().getAnnotation(SecuredCapability.class);
		if (null != methodAnnotation)
		{
			action = methodAnnotation.action();
		}
		return action;
	}

	private String getActionFromType(InvocationContext joinPoint)
	{
		String action = "";
		SecuredCapability typeAnnotation = joinPoint.getTarget().getClass().getAnnotation(SecuredCapability.class);
		if (null != typeAnnotation)
		{
			action = typeAnnotation.action();
		}
		return action;
	}

	private void doTheInvocationCheck(String resource, String action) throws
			SecurityManagerException, SecurityAuthorizationException
	{
		LOG.log(Level.INFO, "executing doTheInvocationCheck resource = {0}, action= {1}", new Object[]
		{
			resource, action
		});
		if (!resource.isEmpty())
		{
			UserSecurityProfileVO userSecurityProfileVO = loggedOnSessionProvider.getLoggedOnSession().getLoggedOnUser();
			AccessRightsVO accessRights = new AccessRightsVOBuilder()
					.setActionAsString(action)
					.setResourceAsString(resource)
					.setUserProfileVO(userSecurityProfileVO)
					.createAccessRightsVO();
			systemSecurityManager.checkAccessRights(accessRights);
		}
		else
		{
			throw new SecurityAuthorizationException("Resource is null, please provide a resource");
		}
	}

	@Inject
	void setLoggedOnSession(LoggedOnSessionProvider loggedOnSessionProvider)
	{
		this.loggedOnSessionProvider = loggedOnSessionProvider;
	}

	@Inject
	void setSecurityExceptionHandler(SecurityExceptionHandler exceptionHandler)
	{
		this.securityExceptionHandler = exceptionHandler;
	}

	@EJB
	void setSystemSecurityManager(
			SystemSecurityManagerRemote systemSecurityManager)
	{
		this.systemSecurityManager = systemSecurityManager;
	}
}
