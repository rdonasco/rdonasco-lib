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

package com.rdonasco.common.utils;

/**
 *
 * @author Roy F. Donasco
 */
public final class NumberUtilities 
{
	private static long MIN_SEED = 1;
	private static long MAX_SEED = Long.MAX_VALUE;
	
	public static long generateRandomID()
	{
		long randomID = Math.round(Math.random() * (MAX_SEED - MIN_SEED) + MIN_SEED);
		return randomID;
	}
}
