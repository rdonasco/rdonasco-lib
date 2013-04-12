/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.vo;

import com.rdonasco.datamanager.listeditor.view.ListEditorItem;
import com.rdonasco.security.vo.ResourceVO;
import com.vaadin.ui.Embedded;

/**
 *
 * @author Roy F. Donasco
 */
public class ResourceItemVO implements ListEditorItem
{

	Embedded icon;
	private ResourceVO resource;

	public ResourceItemVO(Embedded icon, ResourceVO resource)
	{
		this.icon = icon;
		this.resource = resource;
	}

	@Override
	public Embedded getIcon()
	{
		return icon;
	}

	@Override
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

	@Override
	public int hashCode()
	{
		return resource.hashCode();
	}

	@Override
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
		boolean isEqual;
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
