/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 24-Feb-2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rdonasco.security.exceptions;

/**
 *
 * @author Roy F. Donasco
 */
public class SystemSecurityInitializationException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of
	 * <code>SystemSecurityInitializationException</code> without detail
	 * message.
	 */
	public SystemSecurityInitializationException()
	{
	}

	/**
	 * Constructs an instance of
	 * <code>SystemSecurityInitializationException</code> with the specified
	 * detail message.
	 *
	 * @param msg the detail message.
	 */
	public SystemSecurityInitializationException(String msg)
	{
		super(msg);
	}

	public SystemSecurityInitializationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SystemSecurityInitializationException(Throwable cause)
	{
		super(cause);
	}
	
}
