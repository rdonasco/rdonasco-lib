/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 20-Jul-2013
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Roy F. Donasco
 */
public class ApplicationVO implements Serializable, Comparable<ApplicationVO>
{
	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private String token;

	private List<ApplicationHostVO> hosts = new ArrayList<ApplicationHostVO>();

	ApplicationVO(Long id, String name, String token)
	{
		this.id = id;
		this.name = name;
		this.token = token;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public List<ApplicationHostVO> getHosts()
	{
		return hosts;
	}

	public void setHosts(
			List<ApplicationHostVO> hosts)
	{
		this.hosts = hosts;
	}


	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final ApplicationVO other = (ApplicationVO) obj;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id)))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	public int compareTo(ApplicationVO o)
	{
		int compareValue;
		if(null == o)
		{
			compareValue = 1;
		}
		else if(getName() == null || o.getName() == null) 
		{
			compareValue = 0;
		}
		else
		{
			compareValue = getName().compareTo(o.getName());
		}
		return compareValue;
	}


}
