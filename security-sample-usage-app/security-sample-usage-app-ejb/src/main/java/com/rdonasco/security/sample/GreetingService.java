/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.sample;

import javax.ejb.Stateless;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class GreetingService implements GreetingServiceLocal
{

	@Override
	public String getGreetingMessage()
	{
		return "Hello from EJB";
	}
	// Add business logic below. (Right-click in editor and choose
	// "Insert Code > Add Business Method")

}
