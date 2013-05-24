/*
 *  Copyright 2010 Roy F. Donasco.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.rdonasco.config.services;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import com.rdonasco.config.dao.ConfigAttributeDAO;
import com.rdonasco.config.dao.ConfigElementDAO;
import com.rdonasco.config.data.ConfigAttribute;
import com.rdonasco.config.data.ConfigData;
import com.rdonasco.config.data.ConfigElement;
import com.rdonasco.config.exceptions.ConfigXPathException;
import com.rdonasco.config.exceptions.LoadValueException;
import com.rdonasco.config.parsers.ValueParser;
import com.rdonasco.config.util.XpathToConfigTransformer;
import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.datamanager.utils.CommonConstants;
import com.rdonasco.common.i18.I18NResource;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class ConfigDataManager implements ConfigDataManagerLocal
{

	private static final Logger LOGGER = Logger.getLogger(ConfigDataManager.class.
			getName());
	@Inject
	private ConfigElementDAO configElementDAO;
	@Inject
	private ConfigAttributeDAO configAttributeDAO;
	private List<ValueParser> valueParsers = new ArrayList<ValueParser>();

	@Inject
	void initParsers(@Any Instance<ValueParser> parsers)
	{
		for (ValueParser parser : parsers)
		{
			valueParsers.add(parser);
		}
	}

	@Override
	public ConfigElementDAO getConfigElementDAO()
	{
		return configElementDAO;
	}

	@Override
	public void setConfigElementDAO(ConfigElementDAO configElementDAO)
	{
		this.configElementDAO = configElementDAO;
	}

	public ConfigAttributeDAO getConfigAttributeDAO()
	{
		return configAttributeDAO;
	}

	public void setConfigAttributeDAO(ConfigAttributeDAO configAttributeDAO)
	{
		this.configAttributeDAO = configAttributeDAO;
	}

	@Override
	public List<ConfigElement> retrieveAllData() throws DataAccessException
	{
		List<ConfigElement> configElement = null;
		try
		{
			configElement = getConfigElementDAO().findAllDataUsingNamedQuery(
					ConfigElement.NAMED_QUERY_FIND_ROOT_ELEMENTS, null);
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return configElement;
	}

	@Override
	public ConfigElement loadData(ConfigElement configElement) throws
			DataAccessException
	{
		try
		{
			return getConfigElementDAO().findFreshData(configElement.getId());
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
	}

	@Override
	public ConfigElement saveData(ConfigElement configElement) throws
			DataAccessException
	{
		try
		{			
			configureXpath(configElement);
			getConfigElementDAO().create(configElement);
			ConfigElement parent = (ConfigElement) configElement.getParentConfig();
			if (parent != null)
			{
				parent = getConfigElementDAO().findData(parent.getId());
				parent.getSubConfigElements().add(configElement);
				getConfigElementDAO().update(parent);
			}

		}
		catch (Exception e)
		{
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new DataAccessException("Unable to save Record" + configElement, e);
		}
		return configElement;
	}

	@Override
	public void updateData(ConfigElement configElement) throws
			DataAccessException
	{
		try
		{
			configureXpath(configElement);
			getConfigElementDAO().update(configElement);
		}
		catch (Exception e)
		{
			throw new DataAccessException(
					I18NResource.localizeWithParameter(
					CommonConstants.UNABLE_TO_UPDATE_RECORD__, configElement), e);
		}
	}

	@Override
	public void deleteData(ConfigElement configElement) throws
			DataAccessException
	{
		try
		{
			getConfigElementDAO().delete(configElement.getId());
		}
		catch (Exception e)
		{
			throw new DataAccessException(
					I18NResource.localizeWithParameter(
					CommonConstants.UNABLE_TO_DELETE_RECORD__, configElement), e);
		}
	}

	@Override
	public ConfigAttribute saveAttribute(ConfigAttribute attribute) throws
			DataAccessException
	{
		try
		{
			ConfigElement configElement = getConfigElementDAO().findData(attribute.getParentConfig().getId());
			configElement.addConfigAttribute(attribute);
			configureXpath(attribute);
			getConfigAttributeDAO().create(attribute);
			getConfigElementDAO().update(configElement);
		}
		catch (Exception e)
		{
			throw new DataAccessException("Unable to save attribute " + attribute, e);
		}

		return attribute;
	}

	@Override
	public void updateAttribute(ConfigAttribute attribute) throws
			DataAccessException
	{
		try
		{
			configureXpath(attribute);
			getConfigAttributeDAO().update(attribute);
		}
		catch (Exception e)
		{
			throw new DataAccessException(I18NResource.localizeWithParameter(
					CommonConstants.UNABLE_TO_UPDATE_RECORD__, attribute), e);
		}
	}

	@Override
	public void deleteAttribute(ConfigAttribute configAttribute) throws
			DataAccessException
	{
		try
		{
			getConfigAttributeDAO().delete(configAttribute.getId());
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
	}

	@Override
	public ConfigData configureXpath(ConfigData configData) throws
			ConfigXPathException
	{
		try
		{
			StringBuilder xPathBuilder = new StringBuilder();
			if (null != configData && null != configData.getParentConfig())
			{
				if (null == configData.getParentXpath() || !configData.getParentXpath().startsWith("/"))
				{
					configureXpath(configData.getParentConfig());
				}
				xPathBuilder.append(configData.getParentXpath());
			}
			if (null != configData)
			{
				xPathBuilder.append("/").append(configData.getName());
				configData.setXpath(xPathBuilder.toString());
			}
		}
		catch (Exception e)
		{
			throw new ConfigXPathException(e);
		}
		return configData;
	}

	@Override
	public ConfigAttribute findConfigAttributeWithXpath(String xpath) throws
			DataAccessException, NonExistentEntityException
	{
		ConfigAttribute attribute = null;
		try
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ConfigAttribute.QUERY_PARAM_XPATH, xpath);
			attribute = getConfigAttributeDAO().findUniqueDataUsingNamedQuery(
					ConfigAttribute.NAMED_QUERY_FIND_BY_XPATH, parameters);
		}
		catch (NonExistentEntityException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return attribute;
	}

	@Override
	public ConfigElement findConfigElementWithXpath(String xpath) throws
			DataAccessException, NonExistentEntityException
	{
		ConfigElement configElement = null;
		try
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ConfigElement.QUERY_PARAM_XPATH, xpath);
			configElement = getConfigElementDAO().findUniqueDataUsingNamedQuery(
					ConfigElement.NAMED_QUERY_FIND_BY_XPATH, parameters);
		}
		catch (NonExistentEntityException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return configElement;
	}

	@Override
	public List<ConfigAttribute> findConfigAttributesWithXpath(String xpath)
			throws DataAccessException
	{
		List<ConfigAttribute> attributes = null;
		try
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ConfigAttribute.QUERY_PARAM_XPATH, xpath);
			attributes = getConfigAttributeDAO().findAllDataUsingNamedQuery(
					ConfigAttribute.NAMED_QUERY_FIND_BY_XPATH, parameters);
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return attributes;
	}

	@Override
	public List<ConfigElement> findConfigElementsWithXpath(String xpath) throws
			DataAccessException
	{
		List<ConfigElement> configElements = null;
		try
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ConfigElement.QUERY_PARAM_XPATH, xpath);
			configElements = getConfigElementDAO().findAllDataUsingNamedQuery(
					ConfigElement.NAMED_QUERY_FIND_BY_XPATH, parameters);
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return configElements;
	}

	@Override
	public <T> T loadValue(String xpath, Class<T> t) throws LoadValueException
	{
		T value = null;
		try
		{
			ConfigAttribute attribute = findConfigAttributeWithXpath(xpath);
			Constructor<T> constructor = t.getConstructor(new Class[]
			{
				String.class
			});

			for (ValueParser parser : valueParsers)
			{
				try
				{
					value = (T) parser.parse(t, attribute.getValue());
				}
				catch (Exception ex)
				{
					Logger.getLogger(this.getClass().getName()).
							log(Level.WARNING, ex.getMessage(), ex);
				}
				if (value != null && value.getClass().isAssignableFrom(t))
				{
					break;
				}
				value = null;
			}
			if (value == null)
			{
				value = constructor.newInstance(attribute.getValue());
			}

		}
		catch (Exception ex)
		{
			throw new LoadValueException(ex);
		}
		return value;
	}

	@Override
	public <T> T loadValue(String xpath, Class<T> t, T defaultValue)
	{
		T value = null;
		try
		{
			value = loadValue(xpath, t);
		}
		catch (LoadValueException e)
		{
			try
			{
				createAttributeFromXpath(xpath, defaultValue);
			}
			catch (Exception ex)
			{
				Logger.getLogger(ConfigDataManager.class.getName()).
						log(Level.WARNING, null, ex);
			}
			value = defaultValue;
		}

		return value;
	}

	@Override
	public ConfigAttribute createOrUpdateAttributeFromXpath(String xpath,
			Object value) throws DataAccessException, ConfigXPathException
	{
		return privatelyCreateOrUpdateAttributeFromXpath(xpath, value, true);
	}

	@Override
	public ConfigAttribute createAttributeFromXpath(String xpath, Object value)
			throws DataAccessException, ConfigXPathException
	{
		return privatelyCreateOrUpdateAttributeFromXpath(xpath, value, false);
	}

	private ConfigAttribute privatelyCreateOrUpdateAttributeFromXpath(
			String xpath, Object value, boolean updateWhenAble)
			throws DataAccessException, ConfigXPathException
	{
		ConfigAttribute attribute = null;
		List<ConfigData> configList = XpathToConfigTransformer.transform(
				xpath);
		int elementCount = configList.size() - 1;
		ConfigElement element = null;
		for (ConfigData configData : configList)
		{
			ConfigElement parent = element;
			element = null;
			elementCount--;
			configData = configureXpath(configData);
			if (elementCount < 0)
			{
				attribute = (ConfigAttribute) configData;
				attribute.setParentConfig(parent);
				attribute.setValue(value.toString());
				break;
			}
			try
			{
				element = findConfigElementWithXpath(configData.getXpath());
			}
			catch (Exception e)
			{
				LOGGER.log(Level.FINER, e.getMessage(), e);
			}
			if (element == null)
			{
				if (parent != null && !parent.equals(configData.getParentConfig()))
				{
					configData.setParentConfig(parent);
				}
				element = saveData((ConfigElement) configData);
			}
			if (parent != null && !parent.equals(element.getParentConfig()))
			{
				element.setParentConfig(parent);
				updateData(element);
			}

		}

		if (updateWhenAble)
		{
			try
			{
				ConfigAttribute oldAttribute = findConfigAttributeWithXpath(attribute.getXpath());
				oldAttribute.setValue(attribute.getValue());
				updateAttribute(oldAttribute);
				attribute = oldAttribute;
			}
			catch (Exception e)
			{
				saveAttribute(attribute);
			}
		}
		else
		{
			saveAttribute(attribute);
		}

		return attribute;
	}
}
