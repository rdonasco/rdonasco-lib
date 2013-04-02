/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.vo;

import com.rdonasco.security.vo.ResourceVO;
import com.vaadin.ui.Embedded;

public class ResourceItemVOBuilder 
{
	private Embedded icon;
	private ResourceVO resource;

	public ResourceItemVOBuilder()
	{
	}

	public ResourceItemVOBuilder setIcon(Embedded icon)
	{
		this.icon = icon;
		return this;
	}

	public ResourceItemVOBuilder setResource(ResourceVO resource)
	{
		this.resource = resource;
		return this;
	}

	public ResourceItemVO createResourceItemVO()
	{
		return new ResourceItemVO(icon, resource);
	}

}
