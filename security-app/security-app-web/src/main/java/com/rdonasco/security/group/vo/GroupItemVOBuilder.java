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

import com.rdonasco.security.vo.SecurityGroupVO;
import com.vaadin.ui.Embedded;

public class GroupItemVOBuilder 
{
	private Embedded icon;

	private SecurityGroupVO roleVO;

	public GroupItemVOBuilder()
	{
	}

	public GroupItemVOBuilder setIcon(Embedded icon)
	{
		this.icon = icon;
		return this;
	}

	public GroupItemVOBuilder setRoleVO(SecurityGroupVO roleVO)
	{
		this.roleVO = roleVO;
		return this;
	}

	public GroupItemVO createGroupItemVO()
	{
		return new GroupItemVO(icon, roleVO);
	}

}
