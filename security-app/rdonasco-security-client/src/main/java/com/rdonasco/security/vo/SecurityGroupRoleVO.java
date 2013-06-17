/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 16-May-2013
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
public class SecurityGroupRoleVO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private SecurityGroupVO securityGroup;

	private RoleVO role;

	private Long id;

	SecurityGroupRoleVO(Long id, SecurityGroupVO securityGroup, RoleVO role)
	{
		this.id = id;
		this.securityGroup = securityGroup;
		this.role = role;
	}

	public Long getId()
	{
		return id;
	}

	public String getRoleName()
	{
		return role.getName();
	}

	public RoleVO getRoleVO()
	{
		return role;
	}

	void setSecurityGroup(SecurityGroupVO parentSecurityGroup)
	{
		securityGroup = parentSecurityGroup;
	}

	public SecurityGroupVO getSecurityGroup()
	{
		return securityGroup;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 97 * hash + (this.securityGroup != null ? this.securityGroup.hashCode() : 0);
		hash = 97 * hash + (this.role != null ? this.role.hashCode() : 0);
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
			final SecurityGroupRoleVO other = (SecurityGroupRoleVO) obj;
			if (this.securityGroup != other.securityGroup && (this.securityGroup == null || !this.securityGroup.equals(other.securityGroup)))
			{
				isEqual = false;
			}
			else if (this.role != other.role && (this.role == null || !this.role.equals(other.role)))
			{
				isEqual = false;
			}
		}
		return isEqual;
	}

	@Override
	public String toString()
	{
		return "SecurityGroupRoleVO{" + getRoleName() + '}';
	}

}
