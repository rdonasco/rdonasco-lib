/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 18-May-2013
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

import com.rdonasco.datamanager.listeditor.view.ListEditorItem;
import com.rdonasco.security.vo.SecurityGroupRoleVO;
import com.vaadin.ui.Embedded;
import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco
 */
public class GroupRoleItemVO implements Serializable, ListEditorItem
{

	private static final long serialVersionUID = 1L;

	private Embedded icon;

	private SecurityGroupRoleVO securityGroupRoleVO;

	GroupRoleItemVO(Embedded icon, SecurityGroupRoleVO securityGroupRoleVO)
	{
		this.icon = icon;
		this.securityGroupRoleVO = securityGroupRoleVO;
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

	public SecurityGroupRoleVO getSecurityGroupRoleVO()
	{
		return securityGroupRoleVO;
	}

	public void setSecurityGroupRoleVO(SecurityGroupRoleVO securityGroupRoleVO)
	{
		this.securityGroupRoleVO = securityGroupRoleVO;
	}

	public String getRoleName()
	{
		return securityGroupRoleVO.getRoleName();
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 67 * hash + (this.securityGroupRoleVO != null ? this.securityGroupRoleVO.hashCode() : 0);
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
			final GroupRoleItemVO other = (GroupRoleItemVO) obj;
			if (this.securityGroupRoleVO != other.securityGroupRoleVO && (this.securityGroupRoleVO == null || !this.securityGroupRoleVO.equals(other.securityGroupRoleVO)))
			{
				isEqual = false;
			}
		}
		return isEqual;
	}
}
