/*
 * Copyright 2011 Roy F. Donasco.
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
package com.rdonasco.config.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import com.rdonasco.config.data.ConfigAttribute;
import com.rdonasco.common.exceptions.DataAccessException;

/**
 *
 * @author Roy F. Donasco
 */
public class ConfigAttributeJpaDAO extends AbstractConfigDAO<ConfigAttribute>
		implements ConfigAttributeDAO
{

	@Override
	public Class<ConfigAttribute> getDataClass()
	{
		return ConfigAttribute.class;
	}

	@Override
	public ConfigAttribute findConfigAttributeByElementNameAndAttributeName(
			String elementName, String attributeName)
			throws DataAccessException
	{
		ConfigAttribute attribute;

		EntityManager em = getEntityManager();
		TypedQuery<ConfigAttribute> query = em.createNamedQuery(
				ConfigAttribute.NAMED_QUERY_FIND_BY_PARENT_ELEMENT_NAME_AND_ATTRIBUTE_NAME, ConfigAttribute.class);
		query.setParameter(ConfigAttribute.QUERY_PARAM_ELEMENT_NAME, elementName);
		query.setParameter(ConfigAttribute.QUERY_PARAM_ATTRIBUTE_NAME, elementName);
		attribute = query.getSingleResult();

		return attribute;
	}

	@Override
	public List<ConfigAttribute> findAllConfigAttributesByElementNameAndAttributeName(
			String elementName, String attributeName)
			throws DataAccessException
	{
		List<ConfigAttribute> attributes = null;
		EntityManager em = getEntityManager();
		TypedQuery<ConfigAttribute> query = em.createNamedQuery(
				ConfigAttribute.NAMED_QUERY_FIND_BY_PARENT_ELEMENT_NAME_AND_ATTRIBUTE_NAME, ConfigAttribute.class);
		query.setParameter(ConfigAttribute.QUERY_PARAM_ELEMENT_NAME, elementName);
		query.setParameter(ConfigAttribute.QUERY_PARAM_ATTRIBUTE_NAME, attributeName);
		attributes = query.getResultList();
		return attributes;
	}
}
