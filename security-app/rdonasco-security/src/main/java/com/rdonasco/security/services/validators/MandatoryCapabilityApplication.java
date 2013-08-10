/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 10-Aug-2013
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

package com.rdonasco.security.services.validators;

import com.rdonasco.security.model.Capability;

/**
 *
 * @author Roy F. Donasco
 */
public class MandatoryCapabilityApplication extends CapabilityValidator
{

	private static final String APPLICATION_IS_MANDATORY = "Application is required";
	@Override
	public boolean isValid(Capability capability)
	{
		return (capability.getApplication() != null && capability.getApplication().getId() != null);
	}

	@Override
	public String getInvalidValueMessage()
	{
		return APPLICATION_IS_MANDATORY;
	}
}
