/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.home.services;

import javax.ejb.Stateless;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class SecureWorld implements SecureWorldLocal
{

	@Override
	public String getMessage()
	{
		return "Secure World";
	}
	// Add business logic below. (Right-click in editor and choose
	// "Insert Code > Add Business Method")

}
