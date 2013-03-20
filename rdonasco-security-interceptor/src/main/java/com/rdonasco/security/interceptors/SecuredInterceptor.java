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

import java.util.logging.Logger;
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

	public SecuredInterceptor()
	{
		LOG.info("SecuredInterceptor instantiated");
	}

	@AroundInvoke
	public Object checkSecuredInvocation(InvocationContext joinPoint) throws
			Exception
	{
		Object returnValue;
		InvocationEventType invocationEventType = getInvocationEventTypeFromMethod(joinPoint);
		if (InvocationEventType.BEFORE == invocationEventType)
		{
			LOG.info("itIsBefore");
			doTheInvocationCheck();
			returnValue = joinPoint.proceed();
		}
		else
		{
			returnValue = joinPoint.proceed();
			doTheInvocationCheck();
			LOG.info("itIsAfter");
		}
		return returnValue;
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

	private InvocationEventType getInvocationEventTypeFromMethod(
			InvocationContext joinPoint)
	{
		InvocationEventType invocationEventType = InvocationEventType.BEFORE;
		Secured methodAnnotation = joinPoint.getMethod().getAnnotation(Secured.class);
		if (null != methodAnnotation)
		{
			invocationEventType = methodAnnotation.invocationEventType();
		}
		return invocationEventType;
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

	private void doTheInvocationCheck()
	{
		LOG.info("doTheInvocationCheck called");
	}
}
