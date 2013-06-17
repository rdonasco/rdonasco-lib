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
package com.rdonasco.security.model;

import com.rdonasco.security.utils.SecurityConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 *
 * @author Roy F. Donasco
 */
@Entity
@Table(name = "security_group")
public class SecurityGroup implements Serializable
{

	private static final long serialVersionUID = 1L;

	private static final String GENERATOR_KEY = "GROUP_IDGEN";

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = GENERATOR_KEY)
	@TableGenerator(name = GENERATOR_KEY, table = SecurityConstants.TABLE_SEQUENCE)
	@Column(name = "id", nullable = false)
	private Long id;

	@Basic(optional = false)
	@Column(name = "name", nullable = false, length = 256, unique = true)
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "securityGroup", fetch = FetchType.LAZY)
	private Collection<SecurityGroupRole> groupRoles;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "group", fetch = FetchType.LAZY)
	private Collection<UserGroup> userGroups;

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

	public Collection<SecurityGroupRole> getGroupRoles()
	{
		if (null == groupRoles)
		{
			groupRoles = new ArrayList<SecurityGroupRole>();
		}
		return groupRoles;
	}

	public void setGroupRoles(
			Collection<SecurityGroupRole> groupRoles)
	{
		this.groupRoles = groupRoles;
	}

	public Collection<UserGroup> getUserGroups()
	{
		if (null == userGroups)
		{
			userGroups = new ArrayList<UserGroup>();
		}
		return userGroups;
	}

	public void setUserGroups(
			Collection<UserGroup> userGroups)
	{
		this.userGroups = userGroups;
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
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
			final SecurityGroup other = (SecurityGroup) obj;
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
		return "UserGroup{" + "id=" + id + ", name=" + name + '}';
	}
}
