/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.vo;

import java.util.ArrayList;
import java.util.Collection;


public class CapabilityVOBuilder 
{
	private Long id = null;
	private String title;
	private String description;
	private ResourceVO resource;
	private Collection<CapabilityActionVO> actions;

	public CapabilityVOBuilder()
	{
	}
	
	public CapabilityVOBuilder addCapabilityAction(CapabilityActionVO capabilityAction)
	{
		ensureActionsAreInitialized();
		actions.add(capabilityAction);
		return this;
	}
	
	public CapabilityVOBuilder addAction(ActionVO action)
	{
		ensureActionsAreInitialized();
		CapabilityActionVO capabilityAction = new CapabilityActionVOBuilder()
				.setActionVO(action)
				.createCapabilityActionVO();
		return addCapabilityAction(capabilityAction);
	}

	public CapabilityVOBuilder setId(Long id)
	{
		this.id = id;
		return this;
	}

	public CapabilityVOBuilder setTitle(String title)
	{
		this.title = title;
		return this;
	}

	public CapabilityVOBuilder setDescription(String description)
	{
		this.description = description;
		return this;
	}

	public CapabilityVOBuilder setResource(ResourceVO resource)
	{
		this.resource = resource;
		return this;
	}

	public CapabilityVOBuilder setActions(Collection<CapabilityActionVO> actions)
	{
		this.actions = actions;
		return this;
	}

	public CapabilityVO createCapabilityVO()
	{
		ensureActionsAreInitialized();
		CapabilityVO capabilityVO = new CapabilityVO(id, title, description, resource, actions);
		for(CapabilityActionVO action : actions)
		{
			action.setCapabilityVO(capabilityVO);
		}
		return capabilityVO;
	}

	private void ensureActionsAreInitialized()
	{
		if(null == actions)
		{
			actions = new ArrayList<CapabilityActionVO>();
		}
	}

}
