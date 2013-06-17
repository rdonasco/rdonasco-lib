/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.common.vaadin.captcha.exceptions;

/**
 *
 * @author Roy F. Donasco
 */
public class InvalidCaptchaParameterException extends RuntimeException
{

	/**
	 * Creates a new instance of
	 * <code>InvalidCaptchaParameterException</code> without detail message.
	 */
	public InvalidCaptchaParameterException()
	{
	}

	/**
	 * Constructs an instance of
	 * <code>InvalidCaptchaParameterException</code> with the specified detail
	 * message.
	 *
	 * @param msg the detail message.
	 */
	public InvalidCaptchaParameterException(String msg)
	{
		super(msg);
	}

	public InvalidCaptchaParameterException(Throwable cause)
	{
		super(cause);
	}
}
