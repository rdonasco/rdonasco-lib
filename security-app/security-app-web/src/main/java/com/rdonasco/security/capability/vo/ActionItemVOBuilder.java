/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.vo;

import com.rdonasco.security.vo.ActionVO;
import com.vaadin.ui.Embedded;

public class ActionItemVOBuilder 
{
	private Embedded icon;
	private ActionVO action;

	public ActionItemVOBuilder()
	{
	}

	public ActionItemVOBuilder setIcon(Embedded icon)
	{
		this.icon = icon;
		return this;
	}

	public ActionItemVOBuilder setAction(ActionVO action)
	{
		this.action = action;
		return this;
	}

	public ActionItemVO createActionItemVO()
	{
		return new ActionItemVO(icon, action);
	}

}
