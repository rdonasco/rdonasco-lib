/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.exceptions;

/**
 *
 * @author Roy F. Donasco
 */
public class SecurityManagerException extends Exception
{

	/**
	 * Creates a new instance of
	 * <code>SecurityManagerException</code> without detail message.
	 */
	public SecurityManagerException()
	{
	}

	/**
	 * Constructs an instance of
	 * <code>SecurityManagerException</code> with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public SecurityManagerException(String msg)
	{
		super(msg);
	}

	public SecurityManagerException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SecurityManagerException(Throwable cause)
	{
		super(cause);
	}
	
	
}
