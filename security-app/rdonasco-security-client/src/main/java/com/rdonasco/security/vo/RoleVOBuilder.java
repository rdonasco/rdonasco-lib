/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 02-May-2013
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
package com.rdonasco.security.vo;

import java.util.ArrayList;
import java.util.List;

public class RoleVOBuilder
{

	private Long id;

	private String name;

	private List<RoleCapabilityVO> roleCapabilities;

	public RoleVOBuilder()
	{
	}

	public RoleVOBuilder setId(Long id)
	{
		this.id = id;
		return this;
	}

	public RoleVOBuilder setName(String name)
	{
		this.name = name;
		return this;
	}

	public RoleVOBuilder setRoleCapabilities(
			List<RoleCapabilityVO> roleCapabilities)
	{
		this.roleCapabilities = roleCapabilities;
		return this;
	}

	public RoleVOBuilder addRoleCapability(RoleCapabilityVO roleCapability)
	{
		getRoleCapabilities().add(roleCapability);
		return this;
	}

	public RoleVO createUserRoleVO()
	{
		RoleVO roleVO = new RoleVO(id, name, getRoleCapabilities());
		for (RoleCapabilityVO roleCapability : getRoleCapabilities())
		{
			roleCapability.setRoleVO(roleVO);
		}

		return roleVO;
	}

	public List<RoleCapabilityVO> getRoleCapabilities()
	{
		if (null == roleCapabilities)
		{
			roleCapabilities = new ArrayList<RoleCapabilityVO>();
		}
		return roleCapabilities;
	}

	public RoleVOBuilder addCapability(CapabilityVO capabilityVO)
	{
		if (null != capabilityVO)
		{
			RoleCapabilityVO roleCapability = new RoleCapabilityVOBuilder()
					.setCapabilityVO(capabilityVO)
					.createRoleCapabilityVO();
			getRoleCapabilities().add(roleCapability);
		}
		return this;
	}
}
