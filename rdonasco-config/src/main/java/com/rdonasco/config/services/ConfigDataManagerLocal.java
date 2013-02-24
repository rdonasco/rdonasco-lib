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
package com.rdonasco.config.services;

import com.rdonasco.config.exceptions.LoadValueException;
import java.util.List;
import com.rdonasco.config.dao.ConfigElementDAO;
import com.rdonasco.config.exceptions.ConfigXPathException;
import com.rdonasco.config.data.ConfigAttribute;
import com.rdonasco.config.data.ConfigData;
import com.rdonasco.config.data.ConfigElement;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.common.exceptions.DataAccessException;
import javax.ejb.Local;

/**
 *
 * @author Roy F. Donasco
 */
@Local
public interface ConfigDataManagerLocal extends DataManager<ConfigElement>
{
    ConfigElementDAO getConfigElementDAO();
    void setConfigElementDAO(ConfigElementDAO unitOfMeasureDAO);

    @Override
    public ConfigElement loadData(ConfigElement ConfigElement) throws DataAccessException;

    @Override
    public ConfigElement saveData(ConfigElement ConfigElement) throws DataAccessException;

    @Override
    public void updateData(ConfigElement ConfigElement) throws DataAccessException;

    @Override
    public void deleteData(ConfigElement ConfigElement) throws DataAccessException;
    
    public ConfigAttribute saveAttribute(ConfigAttribute attribute) throws DataAccessException;
    public void updateAttribute(ConfigAttribute attribute) throws DataAccessException;

    public void deleteAttribute(ConfigAttribute configAttribute) throws DataAccessException;
    
    public ConfigData configureXpath(ConfigData configData) throws ConfigXPathException;
    public ConfigElement findConfigElementWithXpath(String xpath) throws DataAccessException;
    public ConfigAttribute findConfigAttributeWithXpath(String xpath) throws DataAccessException;    
    public List<ConfigElement> findConfigElementsWithXpath(String xpath) throws DataAccessException;
    public List<ConfigAttribute> findConfigAttributesWithXpath(String xpath) throws DataAccessException;   
    public <T> T loadValue(String xpath,Class<T> t) throws LoadValueException;
    public <T> T loadValue(String xpath,Class<T> t, T defaultValue);
    public ConfigAttribute createAttributeFromXpath(String xpath, Object value) throws DataAccessException, ConfigXPathException;
}
