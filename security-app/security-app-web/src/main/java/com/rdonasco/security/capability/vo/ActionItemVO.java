/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.vo;

import com.rdonasco.security.vo.ActionVO;
import com.vaadin.ui.Embedded;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roy F. Donasco
 */
public class ActionItemVO 
{

	private static final Logger LOG = Logger.getLogger(ActionItemVO.class.getName());
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
	public boolean equals(Object obj)
	{
		boolean equalityTest = true;
		if (obj == null)
		{
			equalityTest = false;
		}
		if (equalityTest && getClass() != obj.getClass())
		{
			equalityTest = false;
		}
		final ActionItemVO other = (ActionItemVO) obj;
		if (equalityTest && (this.action != other.action && (this.action == null || !this.action.equals(other.action))))
		{
			equalityTest = false;
		}
		return equalityTest;
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
