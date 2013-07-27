/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 27-Jul-2013
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
package com.rdonasco.security.application.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.security.application.vo.ApplicationItemVO;
import com.rdonasco.security.application.vo.ApplicationItemVOBuilder;
import com.rdonasco.security.capability.utils.IconHelper;
import com.rdonasco.security.common.controllers.ClickListenerProvider;
import com.rdonasco.security.services.ApplicationManagerLocal;
import com.rdonasco.security.user.vo.UserSecurityProfileItemVO;
import com.rdonasco.security.vo.ApplicationVO;
import com.vaadin.ui.Embedded;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author Roy F. Donasco
 */
public class ApplicationDataManager implements DataManager<ApplicationItemVO>
{

	private ApplicationManagerLocal applicationManager;

	private ClickListenerProvider clickListenerProvider;

	public ClickListenerProvider getClickListenerProvider()
	{
		return clickListenerProvider;
	}

	public void setClickListenerProvider(
			ClickListenerProvider clickListenerProvider)
	{
		this.clickListenerProvider = clickListenerProvider;
	}

	private void addOptionalClickListener(Embedded icon,
			ApplicationItemVO applicationItemVO)
	{
		if (getClickListenerProvider() != null)
		{
			icon.addListener(getClickListenerProvider().provideClickListenerFor(applicationItemVO));
		}
	}

	@EJB
	public void setApplicationManager(ApplicationManagerLocal applicationManager)
	{
		this.applicationManager = applicationManager;
	}

	@Override
	public void deleteData(ApplicationItemVO data) throws DataAccessException
	{
		try
		{
			applicationManager.deleteApplication(data.getApplicationVO());
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
	}

	@Override
	public ApplicationItemVO loadData(ApplicationItemVO data) throws
			DataAccessException
	{
		ApplicationItemVO applicationItemVO;
		try
		{
			Embedded icon = IconHelper.createDeleteIcon(I18NResource.localize("Delete"));
			final ApplicationVO applicationVO = applicationManager.loadApplicationWithID(data.getId());
			applicationItemVO = createApplicationItemVOWithClickListener(applicationVO, icon);
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return applicationItemVO;
	}

	@Override
	public List<ApplicationItemVO> retrieveAllData() throws DataAccessException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method retrieveAllData
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ApplicationItemVO saveData(ApplicationItemVO data) throws
			DataAccessException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method saveData
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void updateData(ApplicationItemVO data) throws DataAccessException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method updateData
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private ApplicationItemVO createApplicationItemVOWithClickListener(
			final ApplicationVO applicationVO, Embedded icon)
	{
		ApplicationItemVO applicationItemVO = new ApplicationItemVOBuilder()
				.setApplicationVO(applicationVO)
				.setIcon(null)
				.createApplicationItemVO();
		addOptionalClickListener(icon, applicationItemVO);
		return applicationItemVO;
	}
}
