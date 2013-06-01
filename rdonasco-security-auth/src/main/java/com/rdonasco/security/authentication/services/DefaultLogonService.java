/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.authentication.services;

import com.rdonasco.security.exceptions.SecurityAuthenticationException;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.exceptions.SecurityProfileNotFoundException;
import com.rdonasco.security.utils.EncryptionUtil;
import com.rdonasco.security.vo.LogonVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class DefaultLogonService implements LogonService
{

	private static final Logger LOG = Logger.getLogger(DefaultLogonService.class.getName());

	public static final String SERVICE_ID = "DefaultLogonService";

	@Inject
	private SystemSecurityManagerDecorator systemSecurityManager;

	@Inject
	LoggedOnSessionProvider loggedOnSessionProvider;


	@Override
	public String getServiceID()
	{
		return SERVICE_ID;
	}

	@Override
	public UserSecurityProfileVO logon(LogonVO logonVO) throws
			SecurityAuthenticationException
	{
		LOG.log(Level.FINE, "called DefaultLogonService.logon()");
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
			if (null == password || password.isEmpty())
			{
				throw new SecurityAuthenticationException("password cannot be empty");
			}
			String encryptedPassword = EncryptionUtil.encryptWithPassword(password, password);
			if (!encryptedPassword.equals(userSecurityProfile.getPassword()))
			{
				throw new SecurityAuthenticationException("Authentication failed for user:" + userID);
			}
			loggedOnSessionProvider.getLoggedOnSession().setLoggedOnUser(userSecurityProfile);
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
			LOG.log(Level.FINE, "ended DefaultLogonService.logon()");
		}
		return userSecurityProfile;
	}
}
