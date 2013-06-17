/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.authentication.services;

import com.rdonasco.security.exceptions.DefaultAdminSecurityProfileAlreadyExist;
import com.rdonasco.security.exceptions.SecurityAuthenticationException;
import com.rdonasco.security.utils.EncryptionUtil;
import com.rdonasco.security.vo.LogonVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.rdonasco.security.vo.UserSecurityProfileVOBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Roy F. Donasco
 */
public class DefaultLogonServiceTest
{

	public DefaultLogonServiceTest()
	{
	}

	@BeforeClass
	public static void setUpClass()
	{
	}

	@AfterClass
	public static void tearDownClass()
	{
	}

	@Before
	public void setUp()
	{
	}

	@After
	public void tearDown()
	{
	}

	/**
	 * Test of logon method, of class DefaultLogonService.
	 */
	@Test
	public void testLogon() throws Exception
	{
		System.out.println("logon");
		final String password = EncryptionUtil.encryptWithPassword("pass", "pass");
		LogonVO logonVO = new LogonVO("roy", "pass");
		DefaultLogonService instance = new DefaultLogonService();
		UserSecurityProfileVO expResult = new UserSecurityProfileVOBuilder()
				.setId(Long.MIN_VALUE)
				.setPassword(password)
				.createUserSecurityProfileVO();
		final LoggedOnSessionProvider loggedOnSessionProviderMock = mock(LoggedOnSessionProvider.class);
		final LoggedOnSession loggedOnSession = mock(LoggedOnSession.class);
		final SessionSecurityChecker sessionSecurityChecker = mock(SessionSecurityChecker.class);
		instance.setLoggedOnSessionProvider(loggedOnSessionProviderMock);
		final SystemSecurityManagerDecorator systemSecurityManager = mock(SystemSecurityManagerDecorator.class);
		instance.setSystemSecurityManager(systemSecurityManager);
		instance.setSessionSecurityChecker(sessionSecurityChecker);
		when(systemSecurityManager.findSecurityProfileWithLogonID(logonVO.getLogonID())).thenReturn(expResult);
		when(loggedOnSessionProviderMock.getLoggedOnSession()).thenReturn(loggedOnSession);
		UserSecurityProfileVO result = instance.logon(logonVO);
		assertEquals(expResult, result);

	}

	@Test(expected = SecurityAuthenticationException.class)
	public void testFailedLogonBecauseOfWrongPassword() throws Exception
	{
		System.out.println("FailedLogonBecauseOfWrongPassword");
		final String password = EncryptionUtil.encryptWithPassword("pass", "pass");
		LogonVO logonVO = new LogonVO("roy", "passed");
		DefaultLogonService instance = new DefaultLogonService();
		UserSecurityProfileVO expResult = new UserSecurityProfileVOBuilder()
				.setId(Long.MIN_VALUE)
				.setPassword(password)
				.createUserSecurityProfileVO();
		final LoggedOnSessionProvider loggedOnSessionProviderMock = mock(LoggedOnSessionProvider.class);
		final LoggedOnSession loggedOnSession = mock(LoggedOnSession.class);
		instance.setLoggedOnSessionProvider(loggedOnSessionProviderMock);
		final SystemSecurityManagerDecorator systemSecurityManager = mock(SystemSecurityManagerDecorator.class);
		instance.setSystemSecurityManager(systemSecurityManager);
		when(systemSecurityManager.findSecurityProfileWithLogonID(logonVO.getLogonID())).thenReturn(expResult);
		when(loggedOnSessionProviderMock.getLoggedOnSession()).thenReturn(loggedOnSession);
		instance.logon(logonVO);
	}

	@Test(expected = SecurityAuthenticationException.class)
	public void testFailedLogonBecauseUserIsNotFound() throws Exception
	{
		System.out.println("FailedLogonBecauseUserIsNotFound");
		LogonVO logonVO = new LogonVO("roy", "passed");
		DefaultLogonService instance = new DefaultLogonService();
		final LoggedOnSessionProvider loggedOnSessionProviderMock = mock(LoggedOnSessionProvider.class);
		final LoggedOnSession loggedOnSession = mock(LoggedOnSession.class);
		instance.setLoggedOnSessionProvider(loggedOnSessionProviderMock);
		final SystemSecurityManagerDecorator systemSecurityManager = mock(SystemSecurityManagerDecorator.class);
		instance.setSystemSecurityManager(systemSecurityManager);
		when(systemSecurityManager.findSecurityProfileWithLogonID(logonVO.getLogonID())).thenReturn(null);
		when(systemSecurityManager.createDefaultAdminSecurityProfile()).thenThrow(DefaultAdminSecurityProfileAlreadyExist.class);
		when(loggedOnSessionProviderMock.getLoggedOnSession()).thenReturn(loggedOnSession);
		instance.logon(logonVO);
	}	
}