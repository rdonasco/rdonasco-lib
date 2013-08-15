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
	"application_id", "hostNameOrIpAddress"
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
	@Column(name = "hostNameOrIpAddress", nullable = false, length = 128, unique = false)
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
		hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
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
		final ApplicationHost other = (ApplicationHost) obj;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id)))
		{
			return false;
		}
		return true;
	}
}
