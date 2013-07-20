/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 20-Jul-2013
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
package com.rdonasco.security.services;

import com.rdonasco.security.dao.ApplicationDAO;
import com.rdonasco.security.exceptions.ApplicationManagerException;
import com.rdonasco.security.model.Application;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.vo.ApplicationVO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class ApplicationManager implements
		ApplicationManagerLocal
{
	private static final Logger LOG = Logger.getLogger(ApplicationManager.class.getName());

	private ApplicationDAO applicationDAO;

	@Inject
	public void setApplicationDAO(ApplicationDAO applicationDAO)
	{
		this.applicationDAO = applicationDAO;
	}

	@Override
	public ApplicationVO createNewApplication(ApplicationVO newApplicationVO)
			throws ApplicationManagerException
	{
		ApplicationVO savedApplicationVO = null;
		try
		{
			Application application = SecurityEntityValueObjectConverter.toApplication(newApplicationVO);
			applicationDAO.create(application);
			savedApplicationVO = SecurityEntityValueObjectConverter.toApplicationVO(application);
		}
		catch (Exception e)
		{
			throw new ApplicationManagerException(e);
		}
		return savedApplicationVO;
	}

	// Add business logic below. (Right-click in editor and choose
	// "Insert Code > Add Business Method")
	@Override
	public void updateApplication(ApplicationVO applicationToUpdate) throws
			ApplicationManagerException
	{
		try
		{
			Application application = SecurityEntityValueObjectConverter.toApplication(applicationToUpdate);
			applicationDAO.update(application);
		}
		catch (Exception e)
		{
			throw new ApplicationManagerException(e);
		}

	}

	@Override
	public ApplicationVO loadApplicationWithID(Long id) throws
			ApplicationManagerException
	{
		ApplicationVO loadedApplicationVO = null;
		try
		{
			Application loadedApplication = applicationDAO.findData(id);
			if (null == loadedApplication)
			{
				LOG.log(Level.FINE, "loadedApplication = {0}", loadedApplication);
			}
			else
			{
				loadedApplicationVO = SecurityEntityValueObjectConverter.toApplicationVO(loadedApplication);
			}			
		}
		catch (Exception e)
		{
			throw new ApplicationManagerException(e);
		}
		return loadedApplicationVO;
	}

	@Override
	public ApplicationVO loadApplicationByNameAndToken(String applicationName,
			String token) throws
			ApplicationManagerException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method loadApplicationByNameAndToken
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
