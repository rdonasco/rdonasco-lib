/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.vo;

import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.vaadin.ui.Embedded;
import java.util.ArrayList;
import java.util.List;

public class CapabilityItemVOBuilder
{
	private CapabilityVO capabilityVO;
	private Embedded icon;

	public CapabilityItemVOBuilder()
	{
	}

	public CapabilityItemVOBuilder setCapabilityVO(CapabilityVO capabilityVO)
	{
		this.capabilityVO = capabilityVO;
		return this;
	}

	public CapabilityItemVOBuilder setIcon(Embedded icon)
	{
		this.icon = icon;
		return this;
	}

	public CapabilityItemVO createCapabilityItemVO()
	{
		List<ActionVO> actions = new ArrayList<ActionVO>();
		for (CapabilityActionVO action : capabilityVO.getActions())
		{
			actions.add(action.getActionVO());
		}
		CapabilityItemVO capabilityItemVO = new CapabilityItemVO(capabilityVO, icon);
		capabilityItemVO.setActions(actions);
		return capabilityItemVO;
	}

}
