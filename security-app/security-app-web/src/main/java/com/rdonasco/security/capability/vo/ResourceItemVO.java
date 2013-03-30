/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.vo;

import com.rdonasco.security.vo.ResourceVO;
import com.vaadin.ui.Embedded;

/**
 *
 * @author Roy F. Donasco
 */
public class ResourceItemVO
{

	Embedded icon;
	private ResourceVO resource;

	public ResourceItemVO(Embedded icon, ResourceVO resource)
	{
		this.icon = icon;
		this.resource = resource;
	}

	public Embedded getIcon()
	{
		return icon;
	}

	public void setIcon(Embedded icon)
	{
		this.icon = icon;
	}

	public Long getId()
	{
		return resource.getId();
	}

	public void setId(Long id)
	{
		resource.setId(id);
	}

	public int hashCode()
	{
		return resource.hashCode();
	}

	public String toString()
	{
		return resource.toString();
	}

	public String getName()
	{
		return resource.getName();
	}

	public void setName(String name)
	{
		resource.setName(name);
	}

	public String getDescription()
	{
		return resource.getDescription();
	}

	public void setDescription(String description)
	{
		resource.setDescription(description);
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
			ResourceItemVO otherItem = (ResourceItemVO) obj;
			isEqual = this.resource.equals(otherItem.resource);
		}
		return isEqual;
	}

	public ResourceVO getResource()
	{
		return resource;
	}
}
