/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.vo;

import com.rdonasco.security.vo.CapabilityActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.ResourceVO;
import com.vaadin.ui.Embedded;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityItemVO implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CapabilityVO capabilityVO;
	private Embedded embeddedIcon;

	public CapabilityItemVO(CapabilityVO capabilityVO, Embedded embeddedIcon)
	{
		this.capabilityVO = capabilityVO;
		this.embeddedIcon = embeddedIcon;
	}

	public CapabilityVO getCapabilityVO()
	{
		return capabilityVO;
	}

	public Embedded getEmbeddedIcon()
	{
		return embeddedIcon;
	}

	public void setEmbeddedIcon(Embedded embeddedIcon)
	{
		this.embeddedIcon = embeddedIcon;
	}

	public void setCapabilityVO(CapabilityVO capabilityVO)
	{
		this.capabilityVO = capabilityVO;
	}

	public Long getId()
	{
		return capabilityVO.getId();
	}

	public void setId(Long id)
	{
		capabilityVO.setId(id);
	}

	public String getTitle()
	{
		return capabilityVO.getTitle();
	}

	public void setTitle(String title)
	{
		capabilityVO.setTitle(title);
	}

	public String getDescription()
	{
		return capabilityVO.getDescription();
	}

	public void setDescription(String description)
	{
		capabilityVO.setDescription(description);
	}

	public ResourceVO getResource()
	{
		return capabilityVO.getResource();
	}

	public void setResource(ResourceVO resource)
	{
		capabilityVO.setResource(resource);
	}

	public Collection<CapabilityActionVO> getActions()
	{
		return capabilityVO.getActions();
	}

	public void setActions(
			Collection<CapabilityActionVO> actions)
	{
		capabilityVO.setActions(actions);
	}

	public CapabilityActionVO findActionNamed(String actionName)
	{
		return capabilityVO.findActionNamed(actionName);
	}

	@Override
	public int hashCode()
	{
		return capabilityVO.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean areEqual = true;
		if (obj == null)
		{
			areEqual = false;
		}
		if (null == obj || getClass() != obj.getClass())
		{
			areEqual = false;
		}
		final CapabilityItemVO other = (CapabilityItemVO) obj;
		try
		{
			if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId())))
			{
				areEqual = false;
			}
		}
		catch (NullPointerException e)
		{
			areEqual = false;
		}
		return areEqual;
	}

	@Override
	public String toString()
	{
		return capabilityVO.toString();
	}
}
