/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.vo;

import com.rdonasco.security.vo.ActionVO;
import com.vaadin.ui.Embedded;

/**
 *
 * @author Roy F. Donasco
 */
public class ActionItemVO 
{
	private Embedded icon;
	private ActionVO action;

	public ActionItemVO(Embedded icon, ActionVO action)
	{
		this.icon = icon;
		this.action = action;
	}

	public Embedded getIcon()
	{
		return icon;
	}

	public Long getId()
	{
		return action.getId();
	}

	public void setId(Long id)
	{
		action.setId(id);
	}

	@Override
	public int hashCode()
	{
		return action.hashCode();
	}

	@Override
	public boolean equals(Object object)
	{
		return action.equals(object);
	}

	@Override
	public String toString()
	{
		return action.toString();
	}

	public String getName()
	{
		return action.getName();
	}

	public void setName(String name)
	{
		action.setName(name);
	}

	public String getDescription()
	{
		return action.getDescription();
	}

	public void setDescription(String description)
	{
		action.setDescription(description);
	}
}
