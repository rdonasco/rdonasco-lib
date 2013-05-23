/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 05-Mar-2013
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
package com.rdonasco.security.authentication.services;

import com.rdonasco.security.exceptions.SecurityAuthenticationException;
import com.rdonasco.security.interceptors.InvocationEventType;
import com.rdonasco.security.interceptors.Secured;
import com.rdonasco.security.interceptors.SecuredCapability;
import com.rdonasco.security.services.LoggedOnSessionProvider;
import com.rdonasco.security.services.LogonService;
import com.rdonasco.security.user.services.SecuredLogonServiceLocal;
import com.rdonasco.security.vo.LogonVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class SecuredLogonServiceDecorator implements LogonService
{

	private static final Logger LOG = Logger.getLogger(SecuredLogonServiceDecorator.class.getName());
	public static final String LOGON_SERVICE = "SecuredLogonService";
    @EJB
	private SecuredLogonServiceLocal securedLogonService;
	@Inject
	LoggedOnSessionProvider loggedOnSessionProvider;

    @Override
    public String getServiceID()
    {
		return LOGON_SERVICE;
    }

	@Override
	@Secured
	@SecuredCapability(action = "logon", resource = "system", invocationEventType = InvocationEventType.AFTER)
	public UserSecurityProfileVO logon(LogonVO logonVO)
			throws SecurityAuthenticationException
	{
		UserSecurityProfileVO userSecurityProfileVO = null;
		LOG.log(Level.FINE, "logon() start");
		try
		{
			userSecurityProfileVO = securedLogonService.logon(logonVO);
			loggedOnSessionProvider.getLoggedOnSession().setLoggedOnUser(userSecurityProfileVO);
		}
		finally
		{
			LOG.log(Level.FINE, "logon() end");
		}
		return userSecurityProfileVO;
    }
}
