/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.sample;

import com.rdonasco.security.authorization.interceptors.Secured;
import com.rdonasco.security.authorization.interceptors.SecuredCapability;
import javax.ejb.Local;

/**
 *
 * @author Roy F. Donasco
 */
@Local
public interface GreetingHelloServiceLocal
{
	@Secured
	@SecuredCapability(action = "get", resource = "local greeting")
	String getGreetingMessage();
}
