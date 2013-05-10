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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Roy F. Donasco
 */
@Entity
@Table(name = "user_capability", catalog = "", schema = "", uniqueConstraints =
{
	@UniqueConstraint(columnNames =
	{
		"user_profile_id", "capability_id"
	}, name = "unique_user_capability")
})
@NamedQueries(
{
	@NamedQuery(name = UserCapability.NAMED_QUERY_FIND_CAPABILITY_BY_USER,
			query = "SELECT uc from UserCapability uc where uc.userProfile = :user"),
	@NamedQuery(name = UserCapability.NAMED_QUERY_DELETE_CAPABILITY_WITH_ID,
			query = "DELETE from UserCapability uc where uc.capability.id = :capabilityID")
})
public class UserCapability implements Serializable
{

	private static final long serialVersionUID = 1L;

	private static final String GENERATOR_KEY = "USER_CAPABILITY_IDGEN";

	public static final String NAMED_QUERY_FIND_CAPABILITY_BY_USER = "UserCapability.findCapabilityByUser";

	public static final String NAMED_QUERY_DELETE_CAPABILITY_WITH_ID = "UserCapability.deleteCapabilityByID";

	public static final String QUERY_PARAM_CAPABILITY_ID = "capabilityID";

	public static final String QUERY_PARAM_USER = "user";

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = GENERATOR_KEY)
	@TableGenerator(name = GENERATOR_KEY, table = SecurityConstants.TABLE_SEQUENCE)
	@Column(name = "id", nullable = false)
	private Long id;

	@JoinColumn(name = "user_profile_id", referencedColumnName = "id", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	private UserSecurityProfile userProfile;

	@JoinColumn(name = "capability_id", referencedColumnName = "id", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	private Capability capability;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public UserSecurityProfile getUserProfile()
	{
		return userProfile;
	}

	public void setUserProfile(UserSecurityProfile userProfile)
	{
		this.userProfile = userProfile;
	}

	public Capability getCapability()
	{
		return capability;
	}

	public void setCapability(Capability capability)
	{
		this.capability = capability;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 79 * hash + (this.userProfile != null ? this.userProfile.hashCode() : 0);
		hash = 79 * hash + (this.capability != null ? this.capability.hashCode() : 0);
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
			final UserCapability other = (UserCapability) obj;
			if (this.userProfile != other.userProfile && (this.userProfile == null || !this.userProfile.equals(other.userProfile)))
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

	@Override
	public String toString()
	{
		return "UserCapability{" + "id=" + id + ", userProfile=" + userProfile + ", capability=" + capability + '}';
	}
}
