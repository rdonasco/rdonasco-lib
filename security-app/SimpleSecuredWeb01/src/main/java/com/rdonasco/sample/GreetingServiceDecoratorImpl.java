/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.sample;

import com.rdonasco.security.authorization.interceptors.Secured;
import com.rdonasco.security.authorization.interceptors.SecuredCapability;
import java.io.Serializable;
import javax.ejb.EJB;

/**
 *
 * @author Roy F. Donasco
 */
@Secured
public class GreetingServiceDecoratorImpl implements GreetingServiceDecorator,
		Serializable
{
	private static final long serialVersionUID = 1L;

	@EJB
	private GreetingHelloServiceLocal greetingServiceLocal;

	@Override
	@SecuredCapability(action = "get", resource = "greetingMessage")
	public String getGreetingMessage(String messageKey)
	{
		return greetingServiceLocal.getGreetingMessage();
	}
}
