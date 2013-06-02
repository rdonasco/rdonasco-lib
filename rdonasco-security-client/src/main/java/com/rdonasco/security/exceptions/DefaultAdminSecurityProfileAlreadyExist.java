package com.rdonasco.security.exceptions;

/**
 *
 * @author Roy F. Donasco
 */
public class DefaultAdminSecurityProfileAlreadyExist extends SecurityManagerException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of
	 * <code>SecurityManagerException</code> without detail message.
	 */
	public DefaultAdminSecurityProfileAlreadyExist()
	{
	}

	/**
	 * Constructs an instance of
	 * <code>SecurityManagerException</code> with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public DefaultAdminSecurityProfileAlreadyExist(String msg)
	{
		super(msg);
	}

	public DefaultAdminSecurityProfileAlreadyExist(String message, Throwable cause)
	{
		super(message, cause);
	}

	public DefaultAdminSecurityProfileAlreadyExist(Throwable cause)
	{
		super(cause);
	}
	
	
}
