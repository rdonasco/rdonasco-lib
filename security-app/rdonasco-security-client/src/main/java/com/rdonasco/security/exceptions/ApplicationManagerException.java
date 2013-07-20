/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.exceptions;

/**
 *
 * @author Roy F. Donasco
 */
public class ApplicationManagerException extends Exception
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of
	 * <code>SecurityManagerException</code> without detail message.
	 */
	public ApplicationManagerException()
	{
	}

	/**
	 * Constructs an instance of
	 * <code>SecurityManagerException</code> with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public ApplicationManagerException(String msg)
	{
		super(msg);
	}

	public ApplicationManagerException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ApplicationManagerException(Throwable cause)
	{
		super(cause);
	}
	
	
}
