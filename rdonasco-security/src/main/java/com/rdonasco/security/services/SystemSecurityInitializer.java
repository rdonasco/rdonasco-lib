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
package com.rdonasco.security.services;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.config.services.ConfigDataManagerProxyRemote;
import com.rdonasco.config.vo.ConfigElementVO;
import com.rdonasco.security.exceptions.SystemSecurityInitializationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class SystemSecurityInitializer implements SystemSecurityInitializerLocal
{
	private static final Logger LOG = Logger.getLogger(SystemSecurityInitializer.class.getName());
	
	@EJB 
	private ConfigDataManagerProxyRemote configDataManager;
			
	@EJB
	private CapabilityManagerLocal capabilityManager;

	@Override
	public void initializeDefaultSystemAccessCapabilities() throws
			SystemSecurityInitializationException
	{
		try
		{
			List<ConfigElementVO> configElements = configDataManager.findConfigElementsWithXpath(DEFAULT_CAPABILITIES);			
			for(ConfigElementVO configElementVO : configElements)
			{
//				String capabilityTitle = configElementVO.get
//				capabilityManager.findCapabilityWithTitle()
			}
		}
		catch (DataAccessException ex)
		{
			throw new SystemSecurityInitializationException(ex);
		}
	}	
}
