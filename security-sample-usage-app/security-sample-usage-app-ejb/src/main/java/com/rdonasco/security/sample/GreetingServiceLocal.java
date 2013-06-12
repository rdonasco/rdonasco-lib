/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.sample;

import javax.ejb.Local;

/**
 *
 * @author Roy F. Donasco
 */
@Local
public interface GreetingServiceLocal
{

	String getGreetingMessage();
}
