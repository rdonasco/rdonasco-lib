/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 24-Feb-2013
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

package com.rdonasco.config.vo;


public class ConfigAttributeVOBuilder 
{
	private Long id;
	private String name;
	private ConfigElementVO parentConfig = null;
	private String value;
	private String xpath;
	private boolean root = false;

	public ConfigAttributeVOBuilder()
	{
	}

	public ConfigAttributeVOBuilder setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ConfigAttributeVOBuilder setName(String name)
	{
		this.name = name;
		return this;
	}

	public ConfigAttributeVOBuilder setParentConfig(
			ConfigElementVO parentConfig)
	{
		this.parentConfig = parentConfig;
		return this;
	}

	public ConfigAttributeVOBuilder setValue(String value)
	{
		this.value = value;
		return this;
	}

	public ConfigAttributeVOBuilder setXpath(String xpath)
	{
		this.xpath = xpath;
		return this;
	}

	public ConfigAttributeVOBuilder setRoot(boolean root)
	{
		this.root = root;
		return this;
	}

	public ConfigAttributeVO createConfigAttributeVO()
	{
		return new ConfigAttributeVO(id, name, parentConfig, value, xpath, root);
	}

}
