/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.vo;


public class CapabilityActionVOBuilder 
{
	private Long id;
	private CapabilityVO capabilityVO;
	private ActionVO actionVO;

	public CapabilityActionVOBuilder()
	{
	}

	public CapabilityActionVOBuilder setId(Long id)
	{
		this.id = id;
		return this;
	}

	public CapabilityActionVOBuilder setCapabilityVO(CapabilityVO capabilityVO)
	{
		this.capabilityVO = capabilityVO;
		return this;
	}

	public CapabilityActionVOBuilder setActionVO(ActionVO actionVO)
	{
		this.actionVO = actionVO;
		return this;
	}

	public CapabilityActionVO createCapabilityActionVO()
	{
		return new CapabilityActionVO(id, capabilityVO, actionVO);
	}

}
