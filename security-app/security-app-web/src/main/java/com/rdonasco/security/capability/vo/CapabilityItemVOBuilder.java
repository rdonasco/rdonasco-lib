/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.vo;

import com.rdonasco.security.vo.CapabilityVO;
import com.vaadin.ui.Embedded;

public class CapabilityItemVOBuilder 
{
	private CapabilityVO capabilityVO;
	private Embedded embeddedIcon;

	public CapabilityItemVOBuilder()
	{
	}

	public CapabilityItemVOBuilder setCapabilityVO(CapabilityVO capabilityVO)
	{
		this.capabilityVO = capabilityVO;
		return this;
	}

	public CapabilityItemVOBuilder setEmbeddedIcon(Embedded embeddedIcon)
	{
		this.embeddedIcon = embeddedIcon;
		return this;
	}

	public CapabilityItemVO createCapabilityItemVO()
	{
		return new CapabilityItemVO(capabilityVO, embeddedIcon);
	}

}
