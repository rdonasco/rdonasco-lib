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

import com.rdonasco.common.exceptions.CollectionMergeException;
import com.rdonasco.common.exceptions.IllegalOrphanException;
import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.common.utils.CollectionsUtility;
import com.rdonasco.security.dao.ApplicationDAO;
import com.rdonasco.security.dao.ApplicationHostDAO;
import com.rdonasco.security.exceptions.ApplicationManagerException;
import com.rdonasco.security.model.Application;
import com.rdonasco.security.model.ApplicationHost;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.vo.ApplicationVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private ApplicationHostDAO applicationHostDAO;

	@Inject
	public void setApplicationDAO(ApplicationDAO applicationDAO)
	{
		this.applicationDAO = applicationDAO;
	}

	@Inject
	public void setApplicationHostDAO(ApplicationHostDAO applicationHostDAO)
	{
		this.applicationHostDAO = applicationHostDAO;
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
	public void updateApplication(ApplicationVO updatedApplicationVO) throws
			ApplicationManagerException
	{
		try
		{
			Application applicationUpdate = SecurityEntityValueObjectConverter.toApplication(updatedApplicationVO);
			Application applicationToUpdate = applicationDAO.findData(applicationUpdate.getId());
			applicationToUpdate.setName(applicationUpdate.getName());
			applicationToUpdate.setToken(applicationUpdate.getToken());

			applicationToUpdate.setHosts(CollectionsUtility.updateCollection(
					applicationUpdate.getHosts(), applicationToUpdate.getHosts(),
					new CollectionsUtility.CollectionItemDeleteStrategy<ApplicationHost>()
			{
				@Override
				public void delete(ApplicationHost itemToDelete) throws
						CollectionMergeException
				{
					try
					{
						applicationHostDAO.delete(itemToDelete.getId());
					}
					catch (Exception e)
					{
						throw new CollectionMergeException(e);
					}
				}
			}, new CollectionsUtility.CollectionItemUpdateStrategy<ApplicationHost>()
			{
				@Override
				public void update(ApplicationHost itemToUpdate,
						ApplicationHost itemToCopy) throws
						CollectionMergeException
				{
					itemToUpdate.setApplication(itemToCopy.getApplication());
					itemToUpdate.setHostNameOrIpAddress(itemToCopy.getHostNameOrIpAddress());
				}
			}));

			applicationDAO.update(applicationToUpdate);
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
		ApplicationVO foundApplicationVO = null;
		try
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(Application.QUERY_PARAM_NAME, applicationName);
			parameters.put(Application.QUERY_PARAM_TOKEN, token);
			Application foundApplication = applicationDAO.findUniqueDataUsingNamedQuery(Application.NAMED_QUERY_FIND_BY_NAME_AND_TOKEN, parameters);
			foundApplicationVO = SecurityEntityValueObjectConverter.toApplicationVO(foundApplication);
		}
		catch (Exception e)
		{
			throw new ApplicationManagerException(e);
		}
		return foundApplicationVO;
	}

	@Override
	public void deleteApplication(ApplicationVO applicationVO) throws
			ApplicationManagerException
	{
		try
		{
			applicationDAO.delete(applicationVO.getId());
		}
		catch (Exception ex)
		{
			throw new ApplicationManagerException(ex);
		}
	}

	@Override
	public List<ApplicationVO> retrieveAllApplication()
	{
		List<Application> applicationList = applicationDAO.findAllData();
		List<ApplicationVO> applicationVOs = new ArrayList<ApplicationVO>();
		if (null != applicationList && !applicationList.isEmpty())
		{
			for (Application application : applicationList)
			{
				applicationVOs.add(SecurityEntityValueObjectConverter.toApplicationVO(application));
			}
		}
		return applicationVOs;
	}
}
