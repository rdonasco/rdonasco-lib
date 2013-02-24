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
import com.rdonasco.config.exceptions.ConfigXPathException;
import com.rdonasco.config.exceptions.LoadValueException;
import com.rdonasco.config.vo.ConfigAttributeVO;
import com.rdonasco.config.vo.ConfigElementVO;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class ConfigDataManagerService implements ConfigDataManagerServiceRemote
{

	@Override
	public ConfigAttributeVO saveAttribute(ConfigAttributeVO attribute) throws
			DataAccessException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void updateAttribute(ConfigAttributeVO attribute) throws
			DataAccessException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void deleteAttribute(ConfigAttributeVO configAttribute) throws
			DataAccessException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public ConfigElementVO findConfigElementWithXpath(String xpath) throws
			DataAccessException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public ConfigAttributeVO findConfigAttributeWithXpath(String xpath) throws
			DataAccessException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<ConfigElementVO> findConfigElementsWithXpath(String xpath)
			throws DataAccessException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<ConfigAttributeVO> findConfigAttributesWithXpath(String xpath)
			throws DataAccessException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public <T> T loadValue(String xpath,
			Class<T> t) throws LoadValueException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public <T> T loadValue(String xpath,
			Class<T> t, T defaultValue)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public ConfigAttributeVO createAttributeFromXpath(String xpath, Object value)
			throws DataAccessException, ConfigXPathException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


}
