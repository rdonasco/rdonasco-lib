/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 11-May-2013
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
import com.rdonasco.security.vo.RoleCapabilityVO;
import com.rdonasco.security.vo.RoleVO;
import com.vaadin.ui.Embedded;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Roy F. Donasco
 */
public class RoleItemVO implements Serializable, ListEditorItem
{
	private static final long serialVersionUID = 1L;

	private Embedded icon;

	private RoleVO roleVO;

	RoleItemVO(Embedded icon, RoleVO roleVO)
	{
		this.icon = icon;
		this.roleVO = roleVO;
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

	public RoleVO getRoleVO()
	{
		return roleVO;
	}

	public void setRoleVO(RoleVO roleVO)
	{
		this.roleVO = roleVO;
	}

	public Long getId()
	{
		return roleVO.getId();
	}

	public void setId(Long id)
	{
		roleVO.setId(id);
	}

	public String getName()
	{
		return roleVO.getName();
	}

	public void setName(String name)
	{
		roleVO.setName(name);
	}

	public List<RoleCapabilityVO> getRoleCapabilities()
	{
		return roleVO.getRoleCapabilities();
	}

	public void setRoleCapabilities(
			List<RoleCapabilityVO> roleCapabilities)
	{
		roleVO.setRoleCapabilities(roleCapabilities);
	}

	@Override
	public String toString()
	{
		return roleVO.toString();
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 89 * hash + (this.roleVO != null ? this.roleVO.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final RoleItemVO other = (RoleItemVO) obj;
		if (this.roleVO != other.roleVO && (this.roleVO == null || !this.roleVO.equals(other.roleVO)))
		{
			return false;
		}
		return true;
	}
}
