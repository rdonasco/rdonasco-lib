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

import com.rdonasco.security.vo.RoleCapabilityVO;
import com.vaadin.ui.Embedded;

public class RoleCapabilityItemVOBuilder 
{
	private RoleCapabilityVO roleCapabilityVO;

	private Embedded icon;

	public RoleCapabilityItemVOBuilder()
	{
	}

	public RoleCapabilityItemVOBuilder setIcon(Embedded icon)
	{
		this.icon = icon;
		return this;
	}

	public RoleCapabilityItemVOBuilder setRoleCapabilityVO(
			RoleCapabilityVO roleCapabilityVO)
	{
		this.roleCapabilityVO = roleCapabilityVO;
		return this;
	}

	public RoleCapabilityItemVO createRoleCapabilityItemVO()
	{
		return new RoleCapabilityItemVO(icon, roleCapabilityVO);
	}

}
