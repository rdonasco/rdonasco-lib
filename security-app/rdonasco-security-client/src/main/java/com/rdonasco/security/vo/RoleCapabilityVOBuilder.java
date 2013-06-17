/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 03-May-2013
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


public class RoleCapabilityVOBuilder 
{
	private RoleVO roleVO;

	private CapabilityVO capabilityVO;

	private Long id;

	public RoleCapabilityVOBuilder()
	{
	}

	public RoleCapabilityVOBuilder setId(Long id)
	{
		this.id = id;
		return this;
	}

	public RoleCapabilityVOBuilder setRoleVO(RoleVO roleVO)
	{
		this.roleVO = roleVO;
		return this;
	}

	public RoleCapabilityVOBuilder setCapabilityVO(CapabilityVO capabilityVO)
	{
		this.capabilityVO = capabilityVO;
		return this;
	}

	public RoleCapabilityVO createRoleCapabilityVO()
	{
		return new RoleCapabilityVO(id, roleVO, capabilityVO);
	}

}
