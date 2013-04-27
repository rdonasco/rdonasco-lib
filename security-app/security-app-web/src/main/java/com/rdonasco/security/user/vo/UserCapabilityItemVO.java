/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 25-Apr-2013
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
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.vaadin.ui.Embedded;
import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco
 */
public class UserCapabilityItemVO implements Serializable, ListEditorItem
{
	private static final long serialVersionUID = 1L;
	private Embedded icon;
	private UserCapabilityVO userCapabilityVO;

	UserCapabilityItemVO(Embedded icon, UserCapabilityVO userCapabilityVO)
	{
		this.icon = icon;
		this.userCapabilityVO = userCapabilityVO;
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
		return userCapabilityVO.getId();
	}

	public void setId(Long id)
	{
		userCapabilityVO.setId(id);
	}

	public UserSecurityProfileVO getUserProfile()
	{
		return userCapabilityVO.getUserProfile();
	}

	public void setUserProfile(UserSecurityProfileVO userProfile)
	{
		userCapabilityVO.setUserProfile(userProfile);
	}

	public CapabilityVO getCapability()
	{
		return userCapabilityVO.getCapability();
	}

	public void setCapability(CapabilityVO capability)
	{
		userCapabilityVO.setCapability(capability);
	}

	@Override
	public int hashCode()
	{
		return userCapabilityVO.hashCode();
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
			final UserCapabilityItemVO otherItem = (UserCapabilityItemVO) obj;
			if (userCapabilityVO != null)
			{
				isEqual = userCapabilityVO.equals(otherItem.userCapabilityVO);
			}

		}
		return isEqual;
	}
}
