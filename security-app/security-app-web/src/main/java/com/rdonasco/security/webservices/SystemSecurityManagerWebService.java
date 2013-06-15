/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 09-Jun-2013
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

import com.rdonasco.security.authentication.services.SystemSecurityManagerDecorator;
import com.rdonasco.security.authorization.utils.AuthConstants;
import com.rdonasco.security.exceptions.SecurityAuthenticationException;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.exceptions.SecurityProfileNotFoundException;
import com.rdonasco.security.utils.EncryptionUtil;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author Roy F. Donasco
 */
@WebService(serviceName = "SystemSecurityManager")
public class SystemSecurityManagerWebService
{

	private static final Logger LOG = Logger.getLogger(SystemSecurityManagerWebService.class.getName());

	@Inject
	private SystemSecurityManagerDecorator securityManager;

	@Resource
	private WebServiceContext webServiceContext;

	@WebMethod(operationName = "checkUserCapability")
	public void checkCapabilityOfUserTo(
			@WebParam(name = "profile") UserSecurityProfileVO userSecurityProfileVO,
			@WebParam(name = "action") String action,
			@WebParam(name = "resource") String resource) throws
			SecurityAuthenticationException
	{
		try
		{
			LOG.log(Level.INFO, "checkUserCapability request from remote address {0}", getRemoteAddress());

			AccessRightsVO accessRights = new AccessRightsVOBuilder()
					.setActionAsString(action)
					.setResourceAsString(resource)
					.setUserProfileVO(userSecurityProfileVO)
					.createAccessRightsVO();
			securityManager.checkAccessRights(accessRights);
		}
		catch (Exception e)
		{
			throw new SecurityAuthenticationException(e);
		}
	}

	/**
	 * Web service operation
	 */
	@WebMethod(operationName = "logon")
	public UserSecurityProfileVO logon(
			@WebParam(name = "logonID") String logonID,
			@WebParam(name = "password") String password) throws
			SecurityAuthenticationException
	{
		UserSecurityProfileVO userSecurityProfile = null;
		try
		{
			LOG.log(Level.INFO, "logon request from remote address {0}", getRemoteAddress());
			LOG.log(Level.FINE, "finding logonID = {0}", logonID);
			userSecurityProfile = securityManager.findSecurityProfileWithLogonID(logonID);
			clearDataRelation(userSecurityProfile); // prevent cyclic reference on web service data
			if (null == userSecurityProfile)
			{
				throw new SecurityProfileNotFoundException("Security Profile not found");
			}
			String encryptedPassword = EncryptionUtil.encryptWithPassword(password, password);
			if (!encryptedPassword.equals(userSecurityProfile.getPassword()))
			{
				throw new SecurityAuthenticationException("Authentication failed for user with logon ID:" + logonID);
			}
			checkCapabilityOfUserTo(userSecurityProfile, AuthConstants.ACTION_LOGON, AuthConstants.RESOURCE_SYSTEM);
		}
		catch (SecurityAuthenticationException ex)
		{
			LOG.log(Level.WARNING, ex.getMessage(), ex);
			throw ex;
		}
		catch (SecurityProfileNotFoundException ex)
		{
			throw new SecurityAuthenticationException(ex);
		}
		catch (SecurityManagerException ex)
		{
			throw new SecurityAuthenticationException(ex);
		}
		catch (Exception e)
		{
			throw new SecurityAuthenticationException(e);
		}
		return userSecurityProfile;
	}

	private void clearDataRelation(UserSecurityProfileVO userSecurityProfileVO)
	{
		userSecurityProfileVO.setCapabilities(null);
		userSecurityProfileVO.setGroups(null);
		userSecurityProfileVO.setRoles(null);
	}

	private String getRemoteAddress()
	{
		MessageContext mc = webServiceContext.getMessageContext();
		HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
		return req.getRemoteAddr();
	}
}
