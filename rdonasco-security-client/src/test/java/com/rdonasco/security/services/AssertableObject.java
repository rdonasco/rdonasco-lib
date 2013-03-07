/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 07-Mar-2013
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

package com.rdonasco.security.services;

/**
 *
 * @author Roy F. Donasco
 */
public class AssertableObject<T> 
{
	private T value;
	
	private final Object lock = new Object();

	public T getValue()
	{
		T objectToReturn;
		synchronized(lock)
		{
			objectToReturn = value;
		}
		return objectToReturn;
	}

	public void setValue(T payload)
	{
		synchronized(lock)
		{
			this.value = payload;
		}
	}		
	
}
