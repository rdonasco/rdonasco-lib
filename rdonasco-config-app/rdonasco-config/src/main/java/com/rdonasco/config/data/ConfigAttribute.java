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
import javax.persistence.*;

/**
 *
 * @author rdonasco
 */
@Entity
@Table(name = "config_attribute", catalog = "", schema = "")
@NamedQueries(
{
    @NamedQuery(name = ConfigAttribute.NAMED_QUERY_FIND_BY_XPATH, query = "SELECT ca FROM ConfigAttribute ca WHERE ca.xpath = :xpath"),    
    @NamedQuery(name = ConfigAttribute.NAMED_QUERY_FIND_BY_PARENT_ELEMENT_NAME_AND_ATTRIBUTE_NAME,
    query = "SELECT ce FROM ConfigAttribute ce WHERE ce.name = :attributeName AND ce.parentConfigElement.name = :parentElementName ORDER BY ce.id")
})
public class ConfigAttribute implements Serializable, ConfigData
{
    public static final String NAMED_QUERY_FIND_BY_XPATH = "ConfigAttribute.findByXpath";
    public static final String NAMED_QUERY_FIND_BY_PARENT_ELEMENT_NAME_AND_ATTRIBUTE_NAME = "ConfigAttribute.findByParentElementNameAndAttributeName";
    public static final String QUERY_PARAM_ELEMENT_NAME = "parentElementName";
    public static final String QUERY_PARAM_ATTRIBUTE_NAME = "attributeName";
    public static final String QUERY_PARAM_XPATH = "xpath";
    private static final String IDGEN = "CONFIG_ATTRIBUTE_DATA_IDGEN";
	private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = IDGEN)
	@TableGenerator(name = IDGEN, table = ConfigConstants.TABLE_SEQUENCE)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_config_id", referencedColumnName = "id", nullable = true)
    private ConfigElement parentConfigElement;
    @Column(name = "attribute_name", length = 256, unique = false)
    private String name;
    @Column(name = "attribute_value", length = 256, unique = false)
    private String value;
    @Column(name = "xpath", length = 2048, unique = false, nullable = false)
    private String xpath;

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
    public ConfigData getParentConfig()
    {
        return parentConfigElement;
    }

    @Override
    public void setParentConfig(ConfigData parentConfig)
    {
        this.parentConfigElement = (ConfigElement) parentConfig;
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
		if(!isRoot())
		{
			parentXpath = getParentConfig().getXpath();
		}
		return parentXpath;
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
        final ConfigAttribute other = (ConfigAttribute) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 11 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString()
    {
        return "ConfigAttribute{" + "id=" + id + ", name=" + name + ", xpath = " + xpath +"}";
    }

    @Override
    public boolean isRoot()
    {
        return (getParentConfig() == null);
    }
}
