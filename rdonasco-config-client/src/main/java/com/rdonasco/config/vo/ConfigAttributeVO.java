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

import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco
 */
public class ConfigAttributeVO implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private ConfigElementVO parentConfig;
	private String value;
	private String xpath;
	private boolean root;

	ConfigAttributeVO(Long id, String name, ConfigElementVO parentConfig,
			String value, String xpath, boolean root)
	{
		this.id = id;
		this.name = name;
		this.parentConfig = parentConfig;
		this.value = value;
		this.xpath = xpath;
		this.root = root;
	}

	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ConfigElementVO getParentConfig()
	{
		return parentConfig;
	}

	public void setParentConfig(ConfigElementVO parentConfig)
	{
		this.parentConfig = parentConfig;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getXpath()
	{
		return xpath;
	}

	public void setXpath(String xpath)
	{
		this.xpath = xpath;
	}

	public boolean isRoot()
	{
		return root;
	}

	public void setRoot(boolean root)
	{
		this.root = root;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final ConfigAttributeVO other = (ConfigAttributeVO) obj;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id)))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "ConfigAttributeVO{" + "id=" + id + ", xpath=" + xpath + ", value=" + value + '}';
	}

}
