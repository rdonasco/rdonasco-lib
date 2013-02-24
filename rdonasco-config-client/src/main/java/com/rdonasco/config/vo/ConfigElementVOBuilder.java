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

import java.util.ArrayList;
import java.util.List;


public class ConfigElementVOBuilder 
{
	private Long id;
	private String name;
	private ConfigElementVO parentConfig = null;
	private String value;
	private String xpath;
	private boolean root = false;
	private List<ConfigElementVO> subConfigElementVOList = new ArrayList<ConfigElementVO>();
	private List<ConfigAttributeVO> attributeVOList = new ArrayList<ConfigAttributeVO>();

	public ConfigElementVOBuilder()
	{
	}

	public ConfigElementVOBuilder setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ConfigElementVOBuilder setName(String name)
	{
		this.name = name;
		return this;
	}

	public ConfigElementVOBuilder setParentConfig(ConfigElementVO parentConfig)
	{
		this.parentConfig = parentConfig;
		return this;
	}

	public ConfigElementVOBuilder setValue(String value)
	{
		this.value = value;
		return this;
	}

	public ConfigElementVOBuilder setXpath(String xpath)
	{
		this.xpath = xpath;
		return this;
	}

	public ConfigElementVOBuilder setRoot(boolean root)
	{
		this.root = root;
		return this;
	}

	public ConfigElementVOBuilder setSubConfigElementVOList(
			List<ConfigElementVO> subConfigElementVOList)
	{
		this.subConfigElementVOList = subConfigElementVOList;
		return this;
	}

	public ConfigElementVOBuilder setAttributeVOList(
			List<ConfigAttributeVO> attributeVOList)
	{
		this.attributeVOList = attributeVOList;
		return this;
	}

	public ConfigElementVO createConfigElementVO()
	{
		return new ConfigElementVO(id, name, parentConfig, value, xpath, root, subConfigElementVOList, attributeVOList);
	}

}
