/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 20-Jul-2013
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


public class ApplicationVOBuilder 
{
	private Long id;

	private String name;

	private String token;

	public ApplicationVOBuilder()
	{
	}

	public ApplicationVOBuilder setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ApplicationVOBuilder setName(String name)
	{
		this.name = name;
		return this;
	}

	public ApplicationVOBuilder setToken(String token)
	{
		this.token = token;
		return this;
	}

	public ApplicationVO createApplicationVO()
	{
		return new ApplicationVO(id, name, token);
	}

}
