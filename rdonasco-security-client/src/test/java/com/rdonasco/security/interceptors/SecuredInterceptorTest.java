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
package com.rdonasco.security.interceptors;

import com.rdonasco.security.exceptions.SecurityAuthorizationException;
import com.rdonasco.security.services.LoggedOnSession;
import com.rdonasco.security.services.SystemSecurityManager;
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
	SystemSecurityManager systemSecurityManagerMock = mock(SystemSecurityManager.class);
	LoggedOnSession sessionMock = mock(LoggedOnSession.class);

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
	public void setUp()
	{
		reset(joinPointMock);
		reset(systemSecurityManagerMock);
		reset(sessionMock);
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
		Object result = instance.checkSecuredInvocation(joinPointMock);
		verify(systemSecurityManagerMock).checkAccessRights(any(AccessRightsVO.class));
		verify(sessionMock).isLoggedOn();
		assertEquals("Hello", result);
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
		instance.checkSecuredInvocation(joinPointMock);
	}

	@Secured(action = "greet", resource = "person")
	public String getGreeting()
	{
		return "Hello";
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
		interceptor.setLoggedOnSession(sessionMock);

		Method method = this.getClass().getMethod("getGreeting", parameterTypes);
		when(joinPointMock.getMethod()).thenReturn(method);
		when(joinPointMock.proceed()).thenReturn(method.invoke(this, parameters));
		when(joinPointMock.getTarget()).thenReturn(this);
		when(systemSecurityManagerMock.isSecuredResource("person")).thenReturn(Boolean.TRUE);
		when(sessionMock.isLoggedOn()).thenReturn(true);
		when(sessionMock.getLoggedOnUser()).thenReturn(createTestDataLoggedOnUser());
	}
}