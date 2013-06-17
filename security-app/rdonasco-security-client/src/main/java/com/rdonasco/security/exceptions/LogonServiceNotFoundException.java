/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.exceptions;

/**
 *
 * @author Roy F. Donasco
 */
public class LogonServiceNotFoundException extends SecurityManagerException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of
	 * <code>SecurityManagerException</code> without detail message.
	 */
	public LogonServiceNotFoundException()
	{
		super("LogonService not found");
	}

	/**
	 * Constructs an instance of
	 * <code>SecurityManagerException</code> with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public LogonServiceNotFoundException(String msg)
	{
		super(msg);
	}

	public LogonServiceNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public LogonServiceNotFoundException(Throwable cause)
	{
		super(cause);
	}
	
	
}
