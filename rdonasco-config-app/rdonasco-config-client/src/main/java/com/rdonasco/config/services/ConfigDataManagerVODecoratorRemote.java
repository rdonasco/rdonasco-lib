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
package com.rdonasco.config.services;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.config.exceptions.ConfigXPathException;
import com.rdonasco.config.exceptions.LoadValueException;
import com.rdonasco.config.vo.ConfigAttributeVO;
import com.rdonasco.config.vo.ConfigElementVO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Roy F. Donasco
 */
@Remote
public interface ConfigDataManagerVODecoratorRemote
{

	public ConfigAttributeVO saveAttribute(ConfigAttributeVO attribute) throws
			DataAccessException;

	public void updateAttribute(ConfigAttributeVO attribute) throws
			DataAccessException;

	public void deleteAttribute(ConfigAttributeVO configAttribute) throws
			DataAccessException;

	public ConfigElementVO findConfigElementWithXpath(String xpath) throws
			DataAccessException, NonExistentEntityException;

	public ConfigAttributeVO findConfigAttributeWithXpath(String xpath) throws
			DataAccessException, NonExistentEntityException;

	public List<ConfigElementVO> findConfigElementsWithXpath(String xpath)
			throws
			DataAccessException;

	public List<ConfigAttributeVO> findConfigAttributesWithXpath(String xpath)
			throws DataAccessException;

	public <T> T loadValue(String xpath, Class<T> t) throws LoadValueException;

	public <T> T loadValue(String xpath, Class<T> t, T defaultValue);

	public ConfigAttributeVO createAttributeFromXpath(String xpath, Object value)
			throws DataAccessException, ConfigXPathException;

	public ConfigElementVO saveConfigElement(ConfigElementVO configElement)
			throws DataAccessException;

	public ConfigElementVO loadConfigElement(ConfigElementVO savedParentConfig)
			throws DataAccessException;

	public void deleteConfigElement(ConfigElementVO configElementVO) throws
			DataAccessException;

	public List<ConfigElementVO> retrieveAllData() throws DataAccessException;

	public void updateData(ConfigElementVO configElementVO) throws
			DataAccessException;

	public ConfigElementVO saveData(ConfigElementVO configElement) throws
			DataAccessException;

	public void deleteData(ConfigElementVO configElement) throws
			DataAccessException;
}
