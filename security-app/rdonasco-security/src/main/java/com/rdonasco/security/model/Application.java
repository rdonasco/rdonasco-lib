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
import java.util.Collection;
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

/**
 *
 * @author Roy F. Donasco
 */
@Entity
@Table(name = "application_data")
@NamedQueries(
		{
	@NamedQuery(name = Application.NAMED_QUERY_FIND_BY_NAME_AND_TOKEN,
			query = "SELECT app FROM Application app WHERE app.name = :name AND app.token = :token")
})
public class Application implements Serializable
{

	private static final long serialVersionUID = 1L;

	private static final String GENERATOR_KEY = "APP_ID_IDGEN";

	public static final String NAMED_QUERY_FIND_BY_NAME_AND_TOKEN = "Application.findByName";

	public static final String QUERY_PARAM_NAME = "name";

	public static final String QUERY_PARAM_TOKEN = "token";

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = GENERATOR_KEY)
	@TableGenerator(name = GENERATOR_KEY, table = SecurityConstants.TABLE_SEQUENCE)
	@Column(name = "id", nullable = false)
	private Long id;

	@Basic(optional = false)
	@Column(name = "name", nullable = false, length = 256, unique = true)
	private String name;

	@Basic(optional = false)
	@Column(name = "token", nullable = false, length = 256)
	private String token;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "application", fetch = FetchType.LAZY)
	private Collection<ApplicationHost> hosts;

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

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public Collection<ApplicationHost> getHosts()
	{
		return hosts;
	}

	public void setHosts(
			Collection<ApplicationHost> hosts)
	{
		this.hosts = hosts;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
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
			final Application other = (Application) obj;
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
		return "Application{" + "id=" + id + ", name=" + name + ", token=" + token + '}';
	}
}
