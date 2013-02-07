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
package com.rdonasco.security.vo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

/**
 *
 * @author Roy F. Donasco
 */
@Entity
public class ResourceVO implements Serializable
{
	public static final String NAMED_QUERY_FIND_RESOURCE_BY_NAME = "findResourceByName";
	public static final String QUERY_PARAM_RESOURCE_NAME = "resourceName";

	private static final long serialVersionUID = 1L;
	private static final String GENERATOR_KEY = "RESOURCE_IDGEN";
	private static final String GENERATOR_TABLE = "SEQUENCE";
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator =  GENERATOR_KEY)
	@TableGenerator(name = GENERATOR_KEY, table = GENERATOR_TABLE)
	@Column(name = "id", nullable = false)
	private Long id;
	@Basic(optional = false)
	@Column(name = "resource_name", nullable = false, length = 256, unique=true)
	private String name;
	@Basic(optional = true)
	@Column(name = "resource_description", nullable = false, length = 256)
	private String description;	

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
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
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof ResourceVO))
		{
			return false;
		}
		ResourceVO other = (ResourceVO) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "com.rdonasco.security.model.Resource[ id=" + id + " ]";
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
