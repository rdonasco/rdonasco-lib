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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Roy F. Donasco
 */
@Entity
@Table(name = "user_security_profile", catalog = "", schema = "")
@NamedQueries(
		{
	@NamedQuery(name = UserSecurityProfile.NAMED_QUERY_FIND_SECURITY_PROFILE_BY_LOGON_ID,
			query = "SELECT u from UserSecurityProfile u where u.logonId = :logonId")
})
public class UserSecurityProfile implements Serializable
{

	private static final long serialVersionUID = 1L;

	private static final String GENERATOR_KEY = "SECURITY_PROFILE_IDGEN";

	public static final String NAMED_QUERY_FIND_SECURITY_PROFILE_BY_LOGON_ID = "UserSecurityProfile.findSecurityProfileByLogonId";

	public static final String QUERY_PARAM_LOGON_ID = "logonId";

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = GENERATOR_KEY)
	@TableGenerator(name = GENERATOR_KEY, table = SecurityConstants.TABLE_SEQUENCE)
	@Column(name = "id", nullable = false)
	private Long id;

	@Basic(optional = false)
	@Column(name = "logon_id", nullable = false, length = 128, unique = true)
	private String logonId;

	@Basic(optional = false)
	@Column(name = "password", nullable = false)
	private String password;

	@Basic(optional = true)
	@Column(name = "registrationToken")
	private String registrationToken;

	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tokenExpiration")
	private Date registrationTokenExpiration;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userProfile", fetch = FetchType.EAGER)
	private Collection<UserCapability> capabilities;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userProfile", fetch = FetchType.EAGER)
	private Collection<UserRole> roles;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getLogonId()
	{
		return logonId;
	}

	public void setLogonId(String loginId)
	{
		this.logonId = loginId;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getRegistrationToken()
	{
		return registrationToken;
	}

	public void setRegistrationToken(String registrationToken)
	{
		this.registrationToken = registrationToken;
	}

	public Date getRegistrationTokenExpiration()
	{
		return registrationTokenExpiration;
	}

	public void setRegistrationTokenExpiration(Date registrationTokenExpiration)
	{
		this.registrationTokenExpiration = registrationTokenExpiration;
	}

	public Collection<UserCapability> getCapabilities()
	{
		if (null == capabilities)
		{
			capabilities = new ArrayList<UserCapability>();
		}
		return capabilities;
	}

	public void setCapabilities(Collection<UserCapability> capabilities)
	{
		this.capabilities = capabilities;
		for (UserCapability capability : capabilities)
		{
			capability.setUserProfile(this);
		}
	}

	public Collection<UserRole> getRoles()
	{
		if (null == roles)
		{
			roles = new ArrayList<UserRole>();
		}
		return roles;
	}

	public void setRoles(Collection<UserRole> roles)
	{
		this.roles = roles;
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
		if (!(object instanceof UserSecurityProfile))
		{
			isEqual = false;
		}
		UserSecurityProfile other = (UserSecurityProfile) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
		{
			isEqual = false;
		}
		return isEqual;
	}

	@Override
	public String toString()
	{
		return "UserSecurityProfile{" + "id=" + id + ", logonId=" + logonId + ", capabilities=" + capabilities + ", roles=" + roles + '}';
	}
}
