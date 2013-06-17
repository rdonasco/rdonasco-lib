/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.exceptions;

/**
 *
 * @author Roy F. Donasco
 */
public class SecurityAuthenticationException extends SecurityManagerException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of
	 * <code>SecurityManagerException</code> without detail message.
	 */
	public SecurityAuthenticationException()
	{
	}

	/**
	 * Constructs an instance of
	 * <code>SecurityManagerException</code> with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public SecurityAuthenticationException(String msg)
	{
		super(msg);
	}

	public SecurityAuthenticationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SecurityAuthenticationException(Throwable cause)
	{
		super(cause);
	}
	
	
}
