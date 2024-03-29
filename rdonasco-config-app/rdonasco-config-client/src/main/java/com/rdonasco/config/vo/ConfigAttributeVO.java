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

/**
 *
 * @author Roy F. Donasco
 */
public class ConfigAttributeVO implements ConfigDataVO
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

	
	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public void setId(Long id)
	{
		this.id = id;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public ConfigDataVO getParentConfig()
	{
		return parentConfig;
	}

	@Override
	public void setParentConfig(ConfigDataVO parentConfig)
	{
		this.parentConfig = (ConfigElementVO) parentConfig;
	}

	@Override
	public String getValue()
	{
		return value;
	}

	@Override
	public void setValue(String value)
	{
		this.value = value;
	}

	@Override
	public String getXpath()
	{
		return xpath;
	}

	@Override
	public void setXpath(String xpath)
	{
		this.xpath = xpath;
	}

	@Override
	public String getParentXpath()
	{
		String parentXpath = null;
		if (!isRoot())
		{
			parentXpath = getParentConfig().getXpath();
		}
		return parentXpath;
	}

	@Override
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
