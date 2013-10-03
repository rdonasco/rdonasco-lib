/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.sample;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class GreetingHelloService implements GreetingHelloServiceLocal
{
	private static final Logger LOG = Logger.getLogger(GreetingHelloService.class.getName());

	@Resource
	private SessionContext context;

	@Override
	public String getGreetingMessage()
	{
		LOG.log(Level.INFO, "context.getContextData().get(message) = {0}", context.getContextData().get("message"));
		return "Hello from EJB";
	}
}
