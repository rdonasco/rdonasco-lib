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
import javax.persistence.Basic;
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
@Table(name = "application_host", catalog = "", schema = "",
		uniqueConstraints =
		@UniqueConstraint(
		columnNames =
{
	"application_id", "host_id"
}, name = "unique_application_host"))
public class ApplicationHost implements Serializable
{

	private static final long serialVersionUID = 1L;

	private static final String GENERATOR_KEY = "HOST_IDGEN";

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = GENERATOR_KEY)
	@TableGenerator(name = GENERATOR_KEY, table = SecurityConstants.TABLE_SEQUENCE)
	@Column(name = "id", nullable = false)
	private Long id;

	@JoinColumn(name = "application_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Application application;

	@Basic(optional = false)
	@Column(name = "hostNameOrIpAddress", nullable = false, length = 128, unique = true)
	private String hostNameOrIpAddress;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Application getApplication()
	{
		return application;
	}

	public void setApplication(Application application)
	{
		this.application = application;
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
		int hash = 5;
		hash = 71 * hash
				+ (this.application == null ? 0 : this.application.hashCode());
		hash = 71 * hash
				+ (this.hostNameOrIpAddress == null ? 0 : this.hostNameOrIpAddress.hashCode());
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean isEqual;
		if (obj == null)
		{
			isEqual = false;
		}
		else if (getClass() == obj.getClass())
		{
			final ApplicationHost other = (ApplicationHost) obj;
			if (this.application != other.application && (this.application == null || !this.application.equals(other.application)))
			{
				isEqual = false;
			}
			else if ((this.hostNameOrIpAddress == null)
					? other.hostNameOrIpAddress != null : !this.hostNameOrIpAddress.equals(other.hostNameOrIpAddress))
			{
				isEqual = false;
			}
			else
			{
				isEqual = true;
			}
		}
		else
		{
			isEqual = false;
		}
		return isEqual;
	}
}
