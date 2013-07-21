/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 21-Jul-2013
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
public class ApplicationHostVO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private Long id;

	private String hostNameOrIpAddress;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getHostNameOrIpAddress()
	{
		return hostNameOrIpAddress;
	}

	public void setHostNameOrIpAddress(String hostNameOrIpAddress)
	{
		this.hostNameOrIpAddress = hostNameOrIpAddress;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 59 * hash + (this.hostNameOrIpAddress != null ? this.hostNameOrIpAddress.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean isEqual = true;
		if (obj == null)
		{
			isEqual = false;
		}
		else if (getClass() != obj.getClass())
		{
			isEqual = false;
		}
		else
		{
			final ApplicationHostVO other = (ApplicationHostVO) obj;
			if ((this.hostNameOrIpAddress == null) ? (other.hostNameOrIpAddress != null) : !this.hostNameOrIpAddress.equals(other.hostNameOrIpAddress))
			{
				isEqual = false;
			}
		}
		return isEqual;
	}
}
