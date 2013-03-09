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

package com.rdonasco.security.services;

import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco
 */
public class LoggedOnSession implements Serializable
{
	private static ThreadLocal<UserSecurityProfileVO> sessionToken 
			= new ThreadLocal<UserSecurityProfileVO>();
	private static final long serialVersionUID = 1L;
	public boolean isLoggedOn()
	{
		return (null != sessionToken.get());
	}
	
	public void setLoggedOnUser(UserSecurityProfileVO loggedOnUser)
	{
		sessionToken.set(loggedOnUser);
	}
	
	public UserSecurityProfileVO getLoggedOnUser()
	{
		return sessionToken.get();
	}

	public void clear()
	{
		sessionToken.set(null);
	}
}
