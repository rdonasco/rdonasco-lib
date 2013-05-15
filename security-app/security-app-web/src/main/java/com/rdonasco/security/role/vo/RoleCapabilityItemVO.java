/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 15-May-2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rdonasco.security.role.vo;

import com.rdonasco.datamanager.listeditor.view.ListEditorItem;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.RoleCapabilityVO;
import com.rdonasco.security.vo.RoleVO;
import com.vaadin.ui.Embedded;
import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco
 */
public class RoleCapabilityItemVO implements Serializable, ListEditorItem
{

	private static final long serialVersionUID = 1L;

	private Embedded icon;

	private RoleCapabilityVO roleCapabilityVO;

	RoleCapabilityItemVO(RoleCapabilityVO roleCapabilityVO)
	{
		this.roleCapabilityVO = roleCapabilityVO;
	}

	@Override
	public Embedded getIcon()
	{
		return icon;
	}

	@Override
	public void setIcon(Embedded icon)
	{
		this.icon = icon;
	}

	public Long getId()
	{
		return roleCapabilityVO.getId();
	}

	public void setId(Long id)
	{
		roleCapabilityVO.setId(id);
	}

	public RoleVO getRole()
	{
		return roleCapabilityVO.getRoleVO();
	}

	public void setRole(RoleVO roleVO)
	{
		roleCapabilityVO.setRoleVO(roleVO);
	}

	public CapabilityVO getCapability()
	{
		return roleCapabilityVO.getCapabilityVO();
	}

	public void setCapability(CapabilityVO capabilityVO)
	{
		roleCapabilityVO.setCapabilityVO(capabilityVO);
	}

	@Override
	public int hashCode()
	{
		return roleCapabilityVO.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean isEqual = true;
		if (obj == null)
		{
			isEqual = false;
		}
		else if (getClass() != obj.getClass())
		{
			isEqual = false;
		}
		else
		{
			final RoleCapabilityItemVO other = (RoleCapabilityItemVO) obj;
			if (this.roleCapabilityVO != other.roleCapabilityVO && (this.roleCapabilityVO == null || !this.roleCapabilityVO.equals(other.roleCapabilityVO)))
			{
				isEqual = false;
			}
		}
		return isEqual;
	}
}
