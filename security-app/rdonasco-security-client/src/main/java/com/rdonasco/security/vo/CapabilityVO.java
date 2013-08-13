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
import java.util.HashMap;
import java.util.Map;

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
	private ApplicationVO applicationVO;
	private Collection<CapabilityActionVO> actions;
	private Map<String, CapabilityActionVO> actionsMap = new HashMap<String, CapabilityActionVO>();

	public CapabilityVO(Long id, String title, String description,
			ResourceVO resource,
			Collection<CapabilityActionVO> actions, ApplicationVO applicationVO)
	{
		this.id = id;
		this.title = title;
		this.description = description;
		this.resource = resource;
		this.actions = actions;
		this.applicationVO = applicationVO;
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

	public ApplicationVO getApplicationVO()
	{
		return applicationVO;
	}

	public void setApplicationVO(ApplicationVO applicationVO)
	{
		this.applicationVO = applicationVO;
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
		actionsMap.clear();
		this.actions = actions;
		populateActionMap();
	}

	public CapabilityActionVO findActionNamed(String actionName)
	{
		return actionsMap.get(actionName);
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
		boolean areEqual = true;
		if (!(object instanceof CapabilityVO))
		{
			areEqual = false;
		}
		CapabilityVO other = (CapabilityVO) object;
		try
		{
			if (areEqual && (this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
			{
				areEqual = false;
			}
		}
		catch (NullPointerException e)
		{
			areEqual = false;
		}
		return areEqual;
	}

	@Override
	public String toString()
	{
		return "CapabilityVO{" + "id=" + id + ", applicationVO=" + applicationVO + ", title=" + title + '}';
	}

	void populateActionMap()
	{
		for (CapabilityActionVO action : actions)
		{
			actionsMap.put(action.getActionVO().getName(), action);
		}
	}
}
