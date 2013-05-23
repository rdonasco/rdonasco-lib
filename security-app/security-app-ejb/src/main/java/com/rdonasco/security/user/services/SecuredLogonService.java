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
package com.rdonasco.security.user.services;

import com.rdonasco.security.exceptions.SecurityAuthenticationException;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.exceptions.SecurityProfileNotFoundException;
import com.rdonasco.security.services.SystemSecurityManagerLocal;
import com.rdonasco.security.utils.EncryptionUtil;
import com.rdonasco.security.vo.LogonVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class SecuredLogonService implements SecuredLogonServiceLocal
{

	private static final Logger LOG = Logger.getLogger(SecuredLogonService.class.getName());
	@EJB
	private SystemSecurityManagerLocal systemSecurityManager;

	@Override
	public String getServiceID()
	{
		return this.getClass().getName();
	}

	@Override
	public UserSecurityProfileVO logon(LogonVO logonVO) throws
			SecurityAuthenticationException
	{
		LOG.log(Level.FINE, "called logon()");
		String password = logonVO.getPassword();
		String userID = logonVO.getLogonID();
		UserSecurityProfileVO userSecurityProfile = null;
		try
		{
			userSecurityProfile = systemSecurityManager.findSecurityProfileWithLogonID(userID);
			if (null == userSecurityProfile)
			{
				throw new SecurityProfileNotFoundException("Security Profile not found");
			}
			if (null == password)
			{
				throw new SecurityAuthenticationException("password cannot be empty");
			}
			String encryptedPassword = EncryptionUtil.encryptWithPassword(password, password);
			if (!encryptedPassword.equals(userSecurityProfile.getPassword()))
			{
				throw new SecurityAuthenticationException("Authentication failed for user:" + userID);
			}
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
		catch (Exception ex)
		{
			throw new SecurityAuthenticationException(ex);
		}
		finally
		{
			LOG.log(Level.FINE, "ended logon()");
		}
		return userSecurityProfile;
	}

	void setSystemSecurityManager(
			SystemSecurityManagerLocal systemSecurityManager)
	{
		this.systemSecurityManager = systemSecurityManager;
	}
}
