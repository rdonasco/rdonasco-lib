/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 27-Jan-2013
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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Roy F. Donasco
 */
@Entity
@Table(name = "role_capability", catalog = "", schema = "",
		uniqueConstraints =
		@UniqueConstraint(
		columnNames =
{
	"role_id", "capability_id"
}, name = "unique_role_capabilty"))
public class RoleCapability implements Serializable
{

	private static final long serialVersionUID = 1L;

	private static final String GENERATOR_KEY = "ROLE_CAPABILITY_IDGEN";

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = GENERATOR_KEY)
	@TableGenerator(name = GENERATOR_KEY, table = SecurityConstants.TABLE_SEQUENCE)
	@Column(name = "id", nullable = false)
	private Long id;

	@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Role role;

	@JoinColumn(name = "capability_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(cascade = CascadeType.REFRESH, optional = false, fetch = FetchType.EAGER)
	private Capability capability;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Capability getCapability()
	{
		return capability;
	}

	public void setCapability(Capability capability)
	{
		this.capability = capability;
	}

	public Role getRole()
	{
		return role;
	}

	public void setRole(Role role)
	{
		this.role = role;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 31 * hash + (this.role != null ? this.role.hashCode() : 0);
		hash = 31 * hash + (this.capability != null ? this.capability.hashCode() : 0);
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
			final RoleCapability other = (RoleCapability) obj;
			if (this.role != other.role && (this.role == null || !this.role.equals(other.role)))
			{
				isEqual = false;
			}
			else if (this.capability != other.capability && (this.capability == null || !this.capability.equals(other.capability)))
			{
				isEqual = false;
			}
		}
		return isEqual;
	}
}
