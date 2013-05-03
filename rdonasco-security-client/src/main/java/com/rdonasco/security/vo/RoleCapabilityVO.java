/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 03-May-2013
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
public class RoleCapabilityVO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private Long id;

	private RoleVO roleVO;

	private CapabilityVO capabilityVO;

	RoleCapabilityVO(Long id, RoleVO roleVO, CapabilityVO capabilityVO)
	{
		this.id = id;
		this.roleVO = roleVO;
		this.capabilityVO = capabilityVO;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public RoleVO getRoleVO()
	{
		return roleVO;
	}

	public void setRoleVO(RoleVO roleVO)
	{
		this.roleVO = roleVO;
	}

	public CapabilityVO getCapabilityVO()
	{
		return capabilityVO;
	}

	public void setCapabilityVO(CapabilityVO capabilityVO)
	{
		this.capabilityVO = capabilityVO;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 29 * hash + (this.roleVO != null ? this.roleVO.hashCode() : 0);
		hash = 29 * hash + (this.capabilityVO != null ? this.capabilityVO.hashCode() : 0);
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
			final RoleCapabilityVO other = (RoleCapabilityVO) obj;
			if (this.roleVO != other.roleVO && (this.roleVO == null || !this.roleVO.equals(other.roleVO)))
			{
				isEqual = false;
			}
			else if (this.capabilityVO != other.capabilityVO && (this.capabilityVO == null || !this.capabilityVO.equals(other.capabilityVO)))
			{
				isEqual = false;
			}
		}
		return isEqual;
	}
}
