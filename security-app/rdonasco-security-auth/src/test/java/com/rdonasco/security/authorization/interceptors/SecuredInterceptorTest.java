/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 07-Mar-2013
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
package com.rdonasco.security.authorization.interceptors;

import com.rdonasco.config.services.ConfigDataManagerVODecoratorRemote;
import com.rdonasco.security.exceptions.SecurityAuthorizationException;
import com.rdonasco.security.authentication.services.LoggedOnSession;
import com.rdonasco.security.authentication.services.LoggedOnSessionProvider;
import com.rdonasco.security.services.SystemSecurityManagerRemote;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.rdonasco.security.vo.UserSecurityProfileVOBuilder;
import java.lang.reflect.Method;
import javax.interceptor.InvocationContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Roy F. Donasco
 */
public class SecuredInterceptorTest
{

	InvocationContext joinPointMock = mock(InvocationContext.class);

	SystemSecurityManagerRemote systemSecurityManagerMock = mock(SystemSecurityManagerRemote.class);

	LoggedOnSessionProvider loggedOnSessionProviderMock = mock(LoggedOnSessionProvider.class);

	LoggedOnSession loggedOnSessionMock = mock(LoggedOnSession.class);

	ConfigDataManagerVODecoratorRemote configDataManager = mock(ConfigDataManagerVODecoratorRemote.class);

	public SecuredInterceptorTest()
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
	public void setUp() throws Exception
	{
		reset(joinPointMock);
		reset(systemSecurityManagerMock);
		reset(loggedOnSessionProviderMock);
		reset(loggedOnSessionMock);
		when(loggedOnSessionProviderMock.getLoggedOnSession()).thenReturn(loggedOnSessionMock);
	}

	@After
	public void tearDown()
	{
	}

	/**
	 * Test of checkSecuredInvocation method, of class SecuredInterceptor.
	 */
	@Test
	public void testCheckSecuredInvocationAsAllowed() throws Exception
	{
		System.out.println("checkSecuredInvocationAsAllowed");
		SecuredInterceptor instance = new SecuredInterceptor();
		setupConditionsForAllowedLogon(instance);
		Object result = instance.interceptSecuredMethodCall(joinPointMock);
		assertEquals("Hello", result);
	}

	@Test
	public void testCheckNotSecuredInvocationAsAllowed() throws Exception
	{
		System.out.println("checkNotSecuredInvocationAsAllowed");
		SecuredInterceptor instance = new SecuredInterceptor();
		setupConditionsForNotSecuredCall(instance);
		Object result = instance.interceptSecuredMethodCall(joinPointMock);
		assertEquals("HelloNotSecured", result);
	}

	@Test(expected = SecurityAuthorizationException.class)
	public void testNotAllowedAccess() throws Exception
	{
		System.out.println("notAllowedAccess");
		SecuredInterceptor instance = new SecuredInterceptor();
		setupConditionsForAllowedLogon(instance);
		doAnswer(new Answer()
		{
			@Override
			public Object answer(InvocationOnMock invocation)
			{
				Object[] args = invocation.getArguments();
				throw new SecurityAuthorizationException();
			}
		}).when(systemSecurityManagerMock).checkAccessRights(any(AccessRightsVO.class));
		instance.interceptSecuredMethodCall(joinPointMock);
	}

	@Secured
	@SecuredCapability(action = "greet", resource = "person")
	public String getGreeting()
	{
		return "Hello";
	}

	@Secured
	public String getNonSecuredGreeting()
	{
		return "HelloNotSecured";
	}

	private UserSecurityProfileVO createTestDataLoggedOnUser()
	{
		return new UserSecurityProfileVOBuilder()
				.setId(Long.MIN_VALUE)
				.setLoginId("user")
				.setPassword("pasword")
				.createUserSecurityProfileVO();
	}

	private void setupConditionsForAllowedLogon(SecuredInterceptor interceptor)
			throws NoSuchMethodException, Exception,
			SecurityException
	{
		Class[] parameterTypes = null;
		Object[] parameters = null;
		interceptor.setSystemSecurityManager(systemSecurityManagerMock);
		interceptor.setLoggedOnSession(loggedOnSessionProviderMock);

		Method method = this.getClass().getMethod("getGreeting", parameterTypes);
		when(joinPointMock.getMethod()).thenReturn(method);
		when(joinPointMock.proceed()).thenReturn(method.invoke(this, parameters));
		when(joinPointMock.getTarget()).thenReturn(this);
		when(systemSecurityManagerMock.isSecuredResource("person")).thenReturn(Boolean.TRUE);
		when(loggedOnSessionProviderMock.getLoggedOnSession().isLoggedOn()).thenReturn(true);
		when(loggedOnSessionProviderMock.getLoggedOnSession().getLoggedOnUser()).thenReturn(createTestDataLoggedOnUser());
	}

	private void setupConditionsForNotSecuredCall(SecuredInterceptor interceptor)
			throws NoSuchMethodException, Exception,
			SecurityException
	{
		Class[] parameterTypes = null;
		Object[] parameters = null;
		interceptor.setSystemSecurityManager(systemSecurityManagerMock);
		interceptor.setLoggedOnSession(loggedOnSessionProviderMock);

		Method method = this.getClass().getMethod("getNonSecuredGreeting", parameterTypes);
		when(joinPointMock.getMethod()).thenReturn(method);
		when(joinPointMock.proceed()).thenReturn(method.invoke(this, parameters));
		when(joinPointMock.getTarget()).thenReturn(this);
	}
}