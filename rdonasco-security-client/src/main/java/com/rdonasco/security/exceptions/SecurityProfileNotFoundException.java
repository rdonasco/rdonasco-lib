/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.exceptions;

/**
 *
 * @author Roy F. Donasco
 */
public class SecurityProfileNotFoundException extends SecurityManagerException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of
	 * <code>SecurityManagerException</code> without detail message.
	 */
	public SecurityProfileNotFoundException()
	{
	}

	/**
	 * Constructs an instance of
	 * <code>SecurityManagerException</code> with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public SecurityProfileNotFoundException(String msg)
	{
		super(msg);
	}

	public SecurityProfileNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SecurityProfileNotFoundException(Throwable cause)
	{
		super(cause);
	}
	
	
}
