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

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import com.rdonasco.config.data.ConfigElement;
import com.rdonasco.common.exceptions.DataAccessException;

/**
 *
 * @author Roy F. Donasco
 */
public class ConfigElementJpaDAO extends AbstractConfigDAO<ConfigElement>
		implements ConfigElementDAO
{

    @Override
    public Class<ConfigElement> getDataClass()
    {
        return ConfigElement.class;
    }

    @Override
    public ConfigElement findConfigElementByName(String name) throws DataAccessException
    {
        ConfigElement element = null;
        try
        {
           EntityManager em = getEntityManager();
            TypedQuery<ConfigElement> query = em.createNamedQuery(ConfigElement.NAMED_QUERY_FIND_BY_NAME,ConfigElement.class);
            query.setParameter(ConfigElement.QUERY_PARAM_NAME, name);
            element = query.getSingleResult();
        }
        catch(Exception e)
        {
            throw new DataAccessException(e);
        }
        return element;
    }
    
}
