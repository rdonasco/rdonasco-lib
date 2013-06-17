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
@Table(name = "user_group", catalog = "", schema = "", uniqueConstraints =
{
	@UniqueConstraint(columnNames =
	{
		"user_profile_id", "group_id"
	}, name = "unique_user_group")
})
@NamedQueries(
{
	@NamedQuery(name = UserGroup.NAMED_QUERY_FIND_GROUP_BY_USER,
			query = "SELECT ugroup from UserGroup ugroup where ugroup.userProfile = :user")
})
public class UserGroup implements Serializable
{

	private static final long serialVersionUID = 1L;

	private static final String GENERATOR_KEY = "USER_GROUP_IDGEN";

	public static final String NAMED_QUERY_FIND_GROUP_BY_USER = "findGroupByUser";

	public static final String QUERY_PARAM_USER = "user";

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = GENERATOR_KEY)
	@TableGenerator(name = GENERATOR_KEY, table = SecurityConstants.TABLE_SEQUENCE)
	@Column(name = "id", nullable = false)
	private Long id;

	@JoinColumn(name = "user_profile_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	private UserSecurityProfile userProfile;

	@JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	private SecurityGroup group;

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

	public SecurityGroup getGroup()
	{
		return group;
	}

	public void setGroup(SecurityGroup group)
	{
		this.group = group;
	}

	@Override
	public int hashCode()
	{
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object)
	{
		boolean isEqual = true;
		if (!(object instanceof UserGroup))
		{
			isEqual = false;
		}
		else
		{
			UserGroup other = (UserGroup) object;
			if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
			{
				isEqual = false;
			}
		}
		return isEqual;
	}

	@Override
	public String toString()
	{
		return "UserGroup{" + "id=" + id + ", userProfile=" + userProfile + ", group=" + group + '}';
	}
}
