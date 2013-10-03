/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 13-May-2013
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
package com.rdonasco.security.role.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.security.role.vo.RoleItemVO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class AvailableRolesViewControllerBuilder
{
	private static final Logger LOG = Logger.getLogger(AvailableRolesViewControllerBuilder.class.getName());

	@Inject
	private AvailableRolesViewController availableRolesViewController;

	private boolean built = false;

	@Inject
	private RoleDataManagerDecorator roleManager;

	public AvailableRolesViewController build()
	{
		if (!built)
		{
			DataManagerContainer roleDataContainer = new DataManagerContainer(RoleItemVO.class);
			availableRolesViewController.setDataContainer(roleDataContainer);
			roleDataContainer.setDataManager(createAvailableRolesDataManager());
			availableRolesViewController.getControlledView().setReadOnly(true);
			availableRolesViewController.initializeControlledViewBehavior();
			built = true;
		}
		return availableRolesViewController;
	}

	private DataManager<RoleItemVO> createAvailableRolesDataManager()
	{
		return new DataManager<RoleItemVO>()
		{
			@Override
			public void deleteData(RoleItemVO data) throws
					DataAccessException
			{
				LOG.log(Level.FINE, "deleteData not supported");
			}

			@Override
			public RoleItemVO loadData(RoleItemVO data) throws
					DataAccessException
			{
				throw new UnsupportedOperationException("Not supported intentionally.");
			}

			@Override
			public List<RoleItemVO> retrieveAllData() throws
					DataAccessException
			{
				return roleManager.retrieveAllData();
			}

			@Override
			public RoleItemVO saveData(RoleItemVO data) throws
					DataAccessException
			{
				throw new UnsupportedOperationException("Not supported intentionally.");
			}

			@Override
			public void updateData(RoleItemVO data) throws
					DataAccessException
			{
				throw new UnsupportedOperationException("Not supported intentionally.");
			}
		};
	}
}
