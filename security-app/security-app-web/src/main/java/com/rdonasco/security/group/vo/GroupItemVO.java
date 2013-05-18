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
package com.rdonasco.security.group.vo;

import com.rdonasco.security.role.vo.*;
import com.rdonasco.datamanager.listeditor.view.ListEditorItem;
import com.rdonasco.security.vo.RoleCapabilityVO;
import com.rdonasco.security.vo.SecurityGroupRoleVO;
import com.rdonasco.security.vo.SecurityGroupVO;
import com.vaadin.ui.Embedded;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Roy F. Donasco
 */
public class GroupItemVO implements Serializable, ListEditorItem
{

	private static final long serialVersionUID = 1L;

	private Embedded icon;

	private SecurityGroupVO securityGroupVO;

	GroupItemVO(Embedded icon, SecurityGroupVO roleVO)
	{
		this.icon = icon;
		this.securityGroupVO = roleVO;
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

	public SecurityGroupVO getSecurityGroupVO()
	{
		return securityGroupVO;
	}

	public void setSecurityGroupVO(SecurityGroupVO roleVO)
	{
		this.securityGroupVO = roleVO;
	}

	public Long getId()
	{
		return securityGroupVO.getId();
	}

	public void setId(Long id)
	{
		securityGroupVO.setId(id);
	}

	public String getName()
	{
		return securityGroupVO.getName();
	}

	public void setName(String name)
	{
		securityGroupVO.setName(name);
	}

	public List<SecurityGroupRoleVO> getGroupRoles()
	{
		return securityGroupVO.getGroupRoleVOs();
	}

	public void setGroupRoles(
			List<SecurityGroupRoleVO> roles)
	{
		securityGroupVO.setGroupRoleVOs(roles);
	}

	@Override
	public String toString()
	{
		return securityGroupVO.toString();
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 41 * hash + (this.securityGroupVO != null ? this.securityGroupVO.hashCode() : 0);
		return hash;
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
			final GroupItemVO other = (GroupItemVO) obj;
			if (this.securityGroupVO != other.securityGroupVO && (this.securityGroupVO == null || !this.securityGroupVO.equals(other.securityGroupVO)))
			{
				isEqual = false;
			}
		}
		return isEqual;
	}
}
