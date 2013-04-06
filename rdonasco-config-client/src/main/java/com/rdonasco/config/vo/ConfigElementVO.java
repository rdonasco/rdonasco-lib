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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Roy F. Donasco
 */
public class ConfigElementVO implements ConfigDataVO
{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private ConfigElementVO parentConfig;
	private String value;
	private String xpath;
	private boolean root;
	private List<ConfigElementVO> subConfigElementVOList;
	private List<ConfigAttributeVO> attributeVOList;
	private int version;
	private Map<String, List<ConfigAttributeVO>> attributeMap = new HashMap<String, List<ConfigAttributeVO>>();
	private Map<String, List<ConfigElementVO>> subElementsMap = new HashMap<String, List<ConfigElementVO>>();

	ConfigElementVO(Long id, String name, ConfigElementVO parentConfig,
			String value, String xpath, boolean root,
			List<ConfigElementVO> subConfigElementVOList,
			List<ConfigAttributeVO> attributeVOList, int version)
	{
		this.id = id;
		this.name = name;
		this.parentConfig = parentConfig;
		this.value = value;
		this.xpath = xpath;
		this.root = root;
		this.subConfigElementVOList = subConfigElementVOList;
		this.attributeVOList = attributeVOList;
		this.version = version;
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
	public ConfigElementVO getParentConfig()
	{
		return parentConfig;
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
	public boolean isRoot()
	{
		return root;
	}

	public void setRoot(boolean root)
	{
		this.root = root;
	}

	public List<ConfigElementVO> getSubConfigElementVOList()
	{
		return subConfigElementVOList;
	}

	public void setSubConfigElementVOList(
			List<ConfigElementVO> subConfigElementVOList)
	{
		this.subConfigElementVOList = subConfigElementVOList;
		organizeSubConfigElementsByName();
	}

	public List<ConfigAttributeVO> getAttributeVOList()
	{
		return attributeVOList;
	}

	public void setAttributeVOList(
			List<ConfigAttributeVO> attributeVOList)
	{
		this.attributeVOList = attributeVOList;
		organizeAttributesByName();
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
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
		final ConfigElementVO other = (ConfigElementVO) obj;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id)))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "ConfigElementVO{" + "id=" + id + ", xpath=" + xpath + '}';
	}

	public int getVersion()
	{
		return this.version;
	}

	private void organizeAttributesByName()
	{
		attributeMap.clear();
		if (null != attributeVOList)
		{
			Comparator<ConfigAttributeVO> attributeNameComparator = new Comparator<ConfigAttributeVO>()
			{
				public int compare(ConfigAttributeVO o1, ConfigAttributeVO o2)
				{
					return o1.getName().compareTo(o2.getName());
				}
			};
			Collections.sort(attributeVOList, attributeNameComparator);
			List<ConfigAttributeVO> attributes;
			for (ConfigAttributeVO attribute : attributeVOList)
			{
				attributes = attributeMap.get(attribute.getName());
				if (null == attributes)
				{
					attributes = new ArrayList<ConfigAttributeVO>();
					attributeMap.put(attribute.getName(), attributes);
				}
				attributes.add(attribute);
			}
		}
	}

	public List<ConfigAttributeVO> getAttributesNamed(String attributeName)
	{
		return attributeMap.get(attributeName);
	}

	public List<ConfigElementVO> getSubElementsNamed(String elementName)
	{
		return subElementsMap.get(elementName);
	}

	private void organizeSubConfigElementsByName()
	{
		subElementsMap.clear();
		Comparator<ConfigElementVO> elementComparator = new Comparator<ConfigElementVO>()
		{
			@Override
			public int compare(ConfigElementVO elementA,
					ConfigElementVO elementB)
			{
				return elementA.getName().compareTo(elementB.getName());
			}
		};
		Collections.sort(subConfigElementVOList, elementComparator);
		List<ConfigElementVO> subElements;
		for (ConfigElementVO subElement : subConfigElementVOList)
		{
			subElements = subElementsMap.get(subElement.getName());
			if (null == subElements)
			{
				subElements = new ArrayList<ConfigElementVO>();
				subElementsMap.put(subElement.getName(), subElements);
			}
			subElements.add(subElement);
		}


	}

	@Override
	public void setParentConfig(ConfigDataVO config)
	{
		this.parentConfig = (ConfigElementVO) config;
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
}
