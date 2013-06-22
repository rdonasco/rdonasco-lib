/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.sample;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.Interceptor;
import javax.interceptor.Interceptors;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
@Interceptors(MethodCallLogInterceptor.class)
public class GreetingService implements GreetingServiceLocal
{
	private static final Logger LOG = Logger.getLogger(GreetingService.class.getName());

	@Resource
	private SessionContext context;

	@Override
	public String getGreetingMessage()
	{
		LOG.log(Level.INFO, "context.getContextData().get(message) = {0}", context.getContextData().get("message"));
		return "Hello from EJB";
	}
}
