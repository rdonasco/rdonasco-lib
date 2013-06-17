/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 02-May-2013
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
import java.util.List;

/**
 *
 * @author Roy F. Donasco
 */
public class RoleVO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private List<RoleCapabilityVO> roleCapabilities;

	RoleVO(Long id, String name, List<RoleCapabilityVO> roleCapabilities)
	{
		this.id = id;
		this.name = name;
		this.roleCapabilities = roleCapabilities;
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

	public List<RoleCapabilityVO> getRoleCapabilities()
	{
		return roleCapabilities;
	}

	public void setRoleCapabilities(
			List<RoleCapabilityVO> roleCapabilities)
	{
		this.roleCapabilities = roleCapabilities;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
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
			final RoleVO other = (RoleVO) obj;

			if (this.id != other.id && (this.id == null || !this.id.equals(other.id)))
			{
				isEqual = false;
			}
		}
		return isEqual;
	}

	@Override
	public String toString()
	{
		return "UserRoleVO{" + "id=" + id + ", name=" + name + '}';
	}
}
