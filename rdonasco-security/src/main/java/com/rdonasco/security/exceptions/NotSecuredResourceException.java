/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.exceptions;

/**
 *
 * @author Roy F. Donasco
 */
public class NotSecuredResourceException extends SecurityManagerException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of
	 * <code>SecurityManagerException</code> without detail message.
	 */
	public NotSecuredResourceException()
	{
	}

	/**
	 * Constructs an instance of
	 * <code>SecurityManagerException</code> with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public NotSecuredResourceException(String msg)
	{
		super(msg);
	}

	public NotSecuredResourceException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public NotSecuredResourceException(Throwable cause)
	{
		super(cause);
	}
	
	
}
