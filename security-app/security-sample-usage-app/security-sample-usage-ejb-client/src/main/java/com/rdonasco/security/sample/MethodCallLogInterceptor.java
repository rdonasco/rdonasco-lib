/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.sample;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 *
 * @author Roy F. Donasco
 */
public class MethodCallLogInterceptor
{

	private static final Logger LOG = Logger.getLogger(MethodCallLogInterceptor.class.getName());

	@Resource
	private SessionContext context;

	@AroundInvoke
	public Object logMethodCall(InvocationContext invocationContext) throws
			Exception
	{
		if (context != null)
		{
			LOG.log(Level.FINE, "contextData={0}", context.getContextData().get("data"));
		}
		StringBuilder logBuilder = new StringBuilder(invocationContext.getTarget().getClass().getName())
				.append(".").append(invocationContext.getMethod().getName());
		if (invocationContext.getParameters() != null && invocationContext.getParameters().length > 0)
		{
			logBuilder.append("(");
			int parameterCount = 0;
			for (Object parameter : invocationContext.getParameters())
			{
				if (parameterCount++ > 0)
				{
					logBuilder.append(",");
				}
				logBuilder.append(parameter.getClass().getSimpleName()).append("=").append(parameter);
			}
			logBuilder.append(")");
		}
		LOG.log(Level.FINE, logBuilder.toString());
		if (context != null)
		{
			context.getContextData().put("data", "my context");
		}
		return invocationContext.proceed();
	}
}
