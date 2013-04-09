/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.vo;

import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityActionVO;
import com.rdonasco.security.vo.CapabilityActionVOBuilder;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.ResourceVO;
import com.vaadin.ui.Embedded;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityItemVO implements Serializable
{

	private static final long serialVersionUID = 1L;
	private CapabilityVO capabilityVO;
	private Embedded embeddedIcon;
	private String captcha;

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

	public Collection<ActionVO> getActions()
	{
		List<ActionVO> actions = new ArrayList<ActionVO>();
		for (CapabilityActionVO capabilityAction : capabilityVO.getActions())
		{
			actions.add(capabilityAction.getActionVO());
		}
		return actions;
	}

	public void setActions(
			Collection<ActionVO> actions)
	{
		capabilityVO.getActions().clear();
		List<CapabilityActionVO> capabilityActions = new ArrayList<CapabilityActionVO>(actions.size());
		for (ActionVO action : actions)
		{
			CapabilityActionVO capabilityAction = new CapabilityActionVOBuilder()
					.setActionVO(action)
					.setCapabilityVO(capabilityVO)
					.createCapabilityActionVO();
			capabilityActions.add(capabilityAction);
		}
		capabilityVO.getActions().addAll(capabilityActions);
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
		else if (getClass() != obj.getClass())
		{
			areEqual = false;
		}
		else
		{
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
		}
		return areEqual;
	}

	@Override
	public String toString()
	{
		return capabilityVO.toString();
	}

	public String getCaptcha()
	{
		return captcha;
	}

	public void setCaptcha(String captcha)
	{
		this.captcha = captcha;
	}
}
