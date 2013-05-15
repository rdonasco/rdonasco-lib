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
package com.rdonasco.security.user.vo;

import com.rdonasco.datamanager.listeditor.view.ListEditorItem;
import com.rdonasco.security.vo.RoleVO;
import com.rdonasco.security.vo.UserRoleVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.vaadin.ui.Embedded;
import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco
 */
public class UserRoleItemVO implements Serializable, ListEditorItem
{
	private static final long serialVersionUID = 1L;
	private Embedded icon;
	private UserRoleVO userRoleVO;

	UserRoleItemVO(Embedded icon, UserRoleVO userRoleVO)
	{
		this.icon = icon;
		this.userRoleVO = userRoleVO;
	}

	public UserRoleVO getUserRoleVO()
	{
		return userRoleVO;
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
		return userRoleVO.getId();
	}

	public void setId(Long id)
	{
		userRoleVO.setId(id);
	}

	public UserSecurityProfileVO getUserProfile()
	{
		return userRoleVO.getUserProfile();
	}

	public void setUserProfile(UserSecurityProfileVO userProfile)
	{
		userRoleVO.setUserProfile(userProfile);
	}

	public RoleVO getRole()
	{
		return userRoleVO.getRole();
	}

	public void setRole(RoleVO role)
	{
		userRoleVO.setRole(role);
	}

	@Override
	public int hashCode()
	{
		return userRoleVO.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean isEqual = true;
		if (obj == null)
		{
			isEqual = false;
		}
		else if (!getClass().equals(obj.getClass()))
		{
			isEqual = false;
		}
		else
		{
			final UserRoleItemVO otherItem = (UserRoleItemVO) obj;
			if (userRoleVO != null)
			{
				isEqual = userRoleVO.equals(otherItem.userRoleVO);
			}

		}
		return isEqual;
	}
}
