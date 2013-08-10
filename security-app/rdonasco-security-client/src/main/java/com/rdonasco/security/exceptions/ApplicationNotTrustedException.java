/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.exceptions;

/**
 *
 * @author Roy F. Donasco
 */
public class ApplicationNotTrustedException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of
	 * <code>SecurityManagerException</code> without detail message.
	 */
	public ApplicationNotTrustedException()
	{
		super("Application not trusted");
	}

	/**
	 * Constructs an instance of
	 * <code>SecurityManagerException</code> with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public ApplicationNotTrustedException(String msg)
	{
		super(msg);
	}

	public ApplicationNotTrustedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ApplicationNotTrustedException(Throwable cause)
	{
		super(cause);
	}
	
	
}
