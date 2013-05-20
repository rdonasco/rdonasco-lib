/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 20-May-2013
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
import com.rdonasco.security.vo.SecurityGroupVO;
import com.rdonasco.security.vo.UserGroupVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.vaadin.ui.Embedded;
import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco
 */
public class UserGroupItemVO implements Serializable, ListEditorItem
{
	private static final long serialVersionUID = 1L;
	private Embedded icon;
	private UserGroupVO userGroupVO;

	UserGroupItemVO(Embedded icon, UserGroupVO userGroupVO)
	{
		this.icon = icon;
		this.userGroupVO = userGroupVO;
	}

	public UserGroupVO getUserGroupVO()
	{
		return userGroupVO;
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
		return userGroupVO.getId();
	}

	public void setId(Long id)
	{
		userGroupVO.setId(id);
	}

	public UserSecurityProfileVO getUserProfile()
	{
		return userGroupVO.getUserProfile();
	}

	public void setUserProfile(UserSecurityProfileVO userProfile)
	{
		userGroupVO.setUserProfile(userProfile);
	}

	public SecurityGroupVO getGroup()
	{
		return userGroupVO.getGroup();
	}

	public void setGroup(SecurityGroupVO group)
	{
		userGroupVO.setGroup(group);
	}


	@Override
	public int hashCode()
	{
		return userGroupVO.hashCode();
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
			final UserGroupItemVO otherItem = (UserGroupItemVO) obj;
			if (userGroupVO != null)
			{
				isEqual = userGroupVO.equals(otherItem.userGroupVO);
			}

		}
		return isEqual;
	}
}
