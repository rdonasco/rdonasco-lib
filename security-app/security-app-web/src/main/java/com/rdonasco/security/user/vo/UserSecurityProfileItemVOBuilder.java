/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 17-Apr-2013
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

import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.vaadin.ui.Embedded;


public class UserSecurityProfileItemVOBuilder
{
	private Embedded icon;
	private UserSecurityProfileVO userSecurityProfileVO;
	private boolean requirePasswordChange;

	public UserSecurityProfileItemVOBuilder()
	{
	}

	public UserSecurityProfileItemVOBuilder setIcon(Embedded icon)
	{
		this.icon = icon;
		return this;
	}

	public UserSecurityProfileItemVOBuilder setUserSecurityProfileVO(
			UserSecurityProfileVO userSecurityProfileVO)
	{
		this.userSecurityProfileVO = userSecurityProfileVO;
		return this;
	}

	public UserSecurityProfileItemVOBuilder setRequirePasswordChange(
			boolean requirePasswordChange)
	{
		this.requirePasswordChange = requirePasswordChange;
		return this;
	}

	public UserSecurityProfileItemVO createUserSecurityProfileItemVO()
	{
		return new UserSecurityProfileItemVO(icon, userSecurityProfileVO, requirePasswordChange);
	}

}
