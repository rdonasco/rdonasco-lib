/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.sample;

import com.rdonasco.security.authorization.interceptors.Secured;
import com.rdonasco.security.authorization.interceptors.SecuredCapability;
import java.io.Serializable;
import javax.ejb.EJB;

/**
 *
 * @author Roy F. Donasco
 */
public class GreetingServiceWrapperImpl implements GreetingServiceWrapper,
		Serializable
{
	private static final long serialVersionUID = 1L;

	@EJB
	private GreetingServiceLocal greetingServiceLocal;

	@Override
	@Secured
	@SecuredCapability(action = "get", resource = "greetingMessage")
	public String getGreetingMessage(String ss)
	{
		return greetingServiceLocal.getGreetingMessage();
	}
}
