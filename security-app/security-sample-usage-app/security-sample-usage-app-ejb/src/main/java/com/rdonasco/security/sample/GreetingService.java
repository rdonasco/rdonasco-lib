/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.sample;

import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class GreetingService implements GreetingServiceLocal
{
	private static final Logger LOG = Logger.getLogger(GreetingService.class.getName());

	@Override
	public String getGreetingMessage()
	{
		return "Hello from EJB";
	}
}
