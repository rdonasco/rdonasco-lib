/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 01-Aug-2013
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

package com.rdonasco.security.application.utils;

import com.rdonasco.common.utils.RandomTextGenerator;
import com.rdonasco.config.services.ConfigDataManagerVODecoratorRemote;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Roy F. Donasco
 */
@Dependent
public class ApplicationTokenGenerator implements Serializable
{
	private static final long serialVersionUID = 1L;
	private ConfigDataManagerVODecoratorRemote configDataManager;

	@EJB
	public void setConfigDataManager(
			ConfigDataManagerVODecoratorRemote configDataManager)
	{
		this.configDataManager = configDataManager;
	}

	public String generate()
	{
		Integer tokenLength = configDataManager.loadValue(ApplicationConstants.XPATH_DEFAULT_TOKEN_LENGTH, Integer.class, 32);
		return RandomTextGenerator.generate(tokenLength);
	}
}
