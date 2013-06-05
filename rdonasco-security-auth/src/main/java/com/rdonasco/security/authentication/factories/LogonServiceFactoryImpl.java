/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 04-Mar-2013
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
package com.rdonasco.security.authentication.factories;

import com.rdonasco.security.exceptions.LogonServiceNotFoundException;
import com.rdonasco.security.authentication.services.LogonService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class LogonServiceFactoryImpl implements LogonServiceFactory,
		Serializable
{

	private static final Logger LOG = Logger.getLogger(LogonServiceFactoryImpl.class.getName());

	private static final long serialVersionUID = 1L;

	@Inject
	private Instance<LogonService> logonServiceInstances;
	private Map<String, LogonService> instanceMap = new HashMap<String, LogonService>();

	@PostConstruct
	void initLogonServiceIDs()
	{
		for (LogonService service : logonServiceInstances)
		{
			instanceMap.put(service.getServiceID(), service);
			LOG.log(Level.INFO, "loaded LogonService.serviceID={0}", service.getServiceID());
		}
	}

	@Override
	public LogonService createLogonService(String serviceID) throws
			LogonServiceNotFoundException
	{
		LogonService logonService = instanceMap.get(serviceID);
		if (null == logonService)
		{
			throw new LogonServiceNotFoundException();
		}
		return logonService;
	}

	public Instance<LogonService> getLogonServiceInstances()
	{
		return logonServiceInstances;
	}
}
