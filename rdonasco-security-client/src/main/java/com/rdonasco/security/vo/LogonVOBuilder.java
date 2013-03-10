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


public class LogonVOBuilder 
{
	private String logonID;
	private String password;

	public LogonVOBuilder()
	{
	}

	public LogonVOBuilder setLogonID(String logonID)
	{
		this.logonID = logonID;
		return this;
	}

	public LogonVOBuilder setPassword(String password)
	{
		this.password = password;
		return this;
	}

	public LogonVO createLogonVO()
	{
		return new LogonVO(logonID, password);
	}

}
