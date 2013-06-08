/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 08-Jun-2013
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
package com.rdonasco.security.webservices;

import com.rdonasco.security.exceptions.SecurityAuthorizationException;
import com.rdonasco.security.services.SystemSecurityManagerLocal;
import com.rdonasco.security.vo.AccessRightsVO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author Roy F. Donasco
 */
@WebService(serviceName = "SystemSecurityManagerWebService")
@Stateless()
public class SystemSecurityManagerWebService
{

	private static final Logger LOG = Logger.getLogger(SystemSecurityManagerWebService.class.getName());

	@EJB
	private SystemSecurityManagerLocal securityManager;

	/**
	 * Web service operation
	 */
	@WebMethod(operationName = "hasAccessRights")
	public Boolean hasAccessRights(
			@WebParam(name = "accessRight") final AccessRightsVO accessRight)
	{
		Boolean itHasAccessRights;
		LOG.log(Level.FINE, "checking access right : {0}", accessRight);
		try
		{
			securityManager.checkAccessRights(accessRight);
			itHasAccessRights = Boolean.TRUE;
		}
		catch (SecurityAuthorizationException e)
		{
			itHasAccessRights = Boolean.FALSE;
		}

		return itHasAccessRights;
	}

}
