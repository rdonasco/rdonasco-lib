/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.sample;

import com.rdonasco.security.authorization.interceptors.Secured;
import com.rdonasco.security.authorization.interceptors.SecuredCapability;
import javax.ejb.Local;

/**
 *
 * @author Roy F. Donasco
 */
@Local
public interface GreetingServiceLocal
{
	@Secured
	@SecuredCapability(action = "get", resource = "greeting")
	String getGreetingMessage();
}
