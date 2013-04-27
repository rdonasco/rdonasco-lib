/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 15-Apr-2013
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
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.vaadin.ui.Embedded;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Roy F. Donasco
 */
public class UserSecurityProfileItemVO implements ListEditorItem, Serializable
{
	private static final long serialVersionUID = 1L;
	private Embedded icon;
	private UserSecurityProfileVO userSecurityProfileVO;
	private String retypedPassword;
	private boolean requirePasswordChange;

	public UserSecurityProfileItemVO(Embedded icon,
			UserSecurityProfileVO userSecurityProfileVO,
			boolean requirePasswordChange)
	{
		this.icon = icon;
		this.userSecurityProfileVO = userSecurityProfileVO;
		this.requirePasswordChange = requirePasswordChange;
	}

	public UserSecurityProfileVO getUserSecurityProfileVO()
	{
		return userSecurityProfileVO;
	}

	public Long getId()
	{
		return userSecurityProfileVO.getId();
	}

	public void setId(Long id)
	{
		userSecurityProfileVO.setId(id);
	}

	public String getLogonId()
	{
		return userSecurityProfileVO.getLogonId();
	}

	public void setLogonId(String loginId)
	{
		userSecurityProfileVO.setLogonId(loginId);
	}

	public String getPassword()
	{
		return userSecurityProfileVO.getPassword();
	}

	public void setPassword(String password)
	{
		userSecurityProfileVO.setPassword(password);
	}

	public String getRegistrationToken()
	{
		return userSecurityProfileVO.getRegistrationToken();
	}

	public void setRegistrationToken(String registrationToken)
	{
		userSecurityProfileVO.setRegistrationToken(registrationToken);
	}

	public Date getRegistrationTokenExpiration()
	{
		return userSecurityProfileVO.getRegistrationTokenExpiration();
	}

	public void setRegistrationTokenExpiration(Date registrationTokenExpiration)
	{
		userSecurityProfileVO.setRegistrationTokenExpiration(registrationTokenExpiration);
	}

	public Collection<UserCapabilityVO> getCapabilities()
	{
		return userSecurityProfileVO.getCapabilities();
	}

	public void setCapabilities(
			Collection<UserCapabilityVO> capabilities)
	{
		userSecurityProfileVO.setCapabilities(capabilities);
	}

	public boolean isTokenExpired()
	{
		return userSecurityProfileVO.isTokenExpired();
	}

	@Override
	public String toString()
	{
		return userSecurityProfileVO.toString();
	}

	public void addCapbility(UserCapabilityVO userCapabilityVO)
	{
		userSecurityProfileVO.addCapbility(userCapabilityVO);
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

	public String getRetypedPassword()
	{
		return retypedPassword;
	}

	public void setRetypedPassword(String retypedPassword)
	{
		this.retypedPassword = retypedPassword;
	}

	public boolean isRequirePasswordChange()
	{
		return requirePasswordChange;
	}

	public void setRequirePasswordChange(boolean requirePasswordChange)
	{
		this.requirePasswordChange = requirePasswordChange;
	}

	@Override
	public int hashCode()
	{
		return userSecurityProfileVO.hashCode();
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
			final UserSecurityProfileItemVO other = (UserSecurityProfileItemVO) obj;
			UserSecurityProfileVO otherProfile = other.userSecurityProfileVO;
			UserSecurityProfileVO thisProfile = userSecurityProfileVO;
			if (null != thisProfile)
			{
				isEqual = thisProfile.equals(otherProfile);
			}
		}
		return isEqual;
	}
}
