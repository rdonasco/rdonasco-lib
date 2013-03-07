/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.exceptions;

/**
 *
 * @author Roy F. Donasco
 */
public class SecurityAuthorizationException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of
	 * <code>SecurityManagerException</code> without detail message.
	 */
	public SecurityAuthorizationException()
	{
	}

	/**
	 * Constructs an instance of
	 * <code>SecurityManagerException</code> with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public SecurityAuthorizationException(String msg)
	{
		super(msg);
	}

	public SecurityAuthorizationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SecurityAuthorizationException(Throwable cause)
	{
		super(cause);
	}
	
	
}
