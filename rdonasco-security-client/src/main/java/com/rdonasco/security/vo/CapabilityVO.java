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
import java.util.Collection;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityVO implements Serializable
{	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String title;
	private String description;
	private ResourceVO resource;
	private Collection<CapabilityActionVO> actions;

	public CapabilityVO(Long id, String title, String description,
			ResourceVO resource,
			Collection<CapabilityActionVO> actions)
	{
		this.id = id;
		this.title = title;
		this.description = description;
		this.resource = resource;
		this.actions = actions;
	}	
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public ResourceVO getResource()
	{
		return resource;
	}

	public void setResource(ResourceVO resource)
	{
		this.resource = resource;
	}

	public Collection<CapabilityActionVO> getActions()
	{
		return actions;
	}

	public void setActions(Collection<CapabilityActionVO> actions)
	{
		this.actions = actions;
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
		if (!(object instanceof CapabilityVO))
		{
			return false;
		}
		CapabilityVO other = (CapabilityVO) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "CapabilityVO{" + "id=" + id + ", title=" + title + '}';
	}


}
