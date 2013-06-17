/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 10-Mar-2013
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

package com.rdonasco.security.vo;

import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco
 */
public class LogonVO implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String logonID;
	private String password;

	public LogonVO()
	{
	}

	public LogonVO(String logonID, String password)
	{
		this.logonID = logonID;
		this.password = password;
	}

	public String getLogonID()
	{
		return logonID;
	}

	public void setLogonID(String logonID)
	{
		this.logonID = logonID;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}
