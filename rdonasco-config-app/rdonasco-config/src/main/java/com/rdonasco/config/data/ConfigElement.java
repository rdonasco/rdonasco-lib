/*
 * Copyright 2012 rdonasco.
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
package com.rdonasco.config.data;

import com.rdonasco.config.util.ConfigConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author rdonasco
 */
@Entity
@Table(name = "config_element", catalog = "", schema = "")
@NamedQueries(
		{
	@NamedQuery(name = ConfigElement.NAMED_QUERY_FIND_BY_XPATH, query = "SELECT c FROM ConfigElement c WHERE c.xpath = :xpath"),
	@NamedQuery(name = ConfigElement.NAMED_QUERY_FIND_BY_NAME, query = "SELECT c FROM ConfigElement c WHERE c.name = :name"),
	@NamedQuery(name = ConfigElement.NAMED_QUERY_FIND_ROOT_ELEMENTS, query = "SELECT c FROM ConfigElement c WHERE c.parentConfig is NULL")
})
public class ConfigElement implements Serializable, ConfigData
{

	public static final String NAMED_QUERY_FIND_ROOT_ELEMENTS = "ConfigElement.findRootElements";

	public static final String NAMED_QUERY_FIND_BY_NAME = "ConfigElement.findByName";

	public static final String NAMED_QUERY_FIND_BY_XPATH = "ConfigElement.findByXpath";

	public static final String QUERY_PARAM_NAME = "name";

	public static final String QUERY_PARAM_XPATH = "xpath";

	private static final String IDGEN = "CONFIG_DATA_IDGEN";

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = IDGEN)
	@TableGenerator(name = IDGEN, table = ConfigConstants.TABLE_SEQUENCE)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_config_id", referencedColumnName = "id", nullable = true)
	private ConfigElement parentConfig;

	@Column(name = "section_name", length = 256, unique = false)
	private String name;

	@Column(name = "xpath", length = 2048, unique = false, nullable = false)
	private String xpath;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parentConfig", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<ConfigElement> subConfigElements;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parentConfigElement", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<ConfigAttribute> attributes;

	@Version
	@Column(name = "VERSION")
	private int version;

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
	public ConfigData getParentConfig()
	{
		return parentConfig;
	}

	@Override
	public void setParentConfig(ConfigData parentConfig)
	{
		this.parentConfig = (ConfigElement) parentConfig;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
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

	public List<ConfigElement> getSubConfigElements()
	{
		if (subConfigElements == null)
		{
			subConfigElements = new ArrayList<ConfigElement>();
		}
		return subConfigElements;
	}

	public List<ConfigElement> getSortedSubConfigElements()
	{
		List<ConfigElement> sortedSubConfigElements = new ArrayList<ConfigElement>(getAttributes().size());
		sortedSubConfigElements.addAll(getSubConfigElements());
		Collections.sort(sortedSubConfigElements, ConfigConstants.CONFIG_DATA_COMPARATOR);
		return sortedSubConfigElements;
	}

	public void setSubConfigElements(List<ConfigElement> subElements)
	{
		this.subConfigElements = subElements;
	}

	public List<ConfigAttribute> getAttributes()
	{
		if (attributes == null)
		{
			attributes = new ArrayList<ConfigAttribute>();
		}
		return attributes;
	}

	public List<ConfigAttribute> getSortedAttributes()
	{
		List<ConfigAttribute> sortedConfigAttributes = new ArrayList<ConfigAttribute>(getAttributes().size());
		sortedConfigAttributes.addAll(getAttributes());
		Collections.sort(sortedConfigAttributes, ConfigConstants.CONFIG_DATA_COMPARATOR);
		return sortedConfigAttributes;
	}

	public void setAttributes(List<ConfigAttribute> attributes)
	{
		this.attributes = attributes;
	}

	@Override
	public String toString()
	{
		return "ConfigElement{" + "id=" + id + ", name=" + name + ", xpath = " + xpath + "}";
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean isEqual = true;
		if (obj == null)
		{
			isEqual = false;
		}
		else if (getClass() != obj.getClass())
		{
			isEqual = false;
		}
		else
		{
			final ConfigElement other = (ConfigElement) obj;
			if (this.id != other.id && (this.id == null || !this.id.equals(other.id)))
			{
				isEqual = false;
			}
		}
		return isEqual;
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}

	public void addConfigAttribute(ConfigAttribute attribute)
	{
		getAttributes().add(attribute);
		attribute.setParentConfig(this);
	}

	public void removeConfigAttribute(ConfigAttribute attribute)
	{
		getAttributes().remove(attribute);
		attribute.setParentConfig(null);
	}

	@Override
	public String getValue()
	{
		// not supported
		return null;
	}

	@Override
	public void setValue(String value)
	{
		// not supported
	}

	public int getVersion()
	{
		return version;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}

	@Override
	public boolean isRoot()
	{
		return (getParentConfig() == null);
	}

	public void addSubConfig(ConfigElement subElement)
	{
		getSubConfigElements().add(subElement);
		subElement.setParentConfig(this);
	}
}
