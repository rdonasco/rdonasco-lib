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
package com.rdonasco.security.group.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.security.group.vo.GroupItemVO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class AvailableGroupsViewControllerBuilder
{
	private static final Logger LOG = Logger.getLogger(AvailableGroupsViewControllerBuilder.class.getName());

	@Inject
	private AvailableGroupsViewController availableGroupViewController;

	private boolean built = false;

	@Inject
	private GroupDataManagerDecorator groupManager;

	public AvailableGroupsViewController build()
	{
		if (!built)
		{
			DataManagerContainer groupDataContainer = new DataManagerContainer(GroupItemVO.class);
			availableGroupViewController.setDataContainer(groupDataContainer);
			groupDataContainer.setDataManager(createAvailableGroupDataManager());
			availableGroupViewController.getControlledView().setReadOnly(true);
			availableGroupViewController.initializeControlledViewBehavior();
			built = true;
		}
		return availableGroupViewController;
	}

	private DataManager<GroupItemVO> createAvailableGroupDataManager()
	{
		return new DataManager<GroupItemVO>()
		{
			@Override
			public void deleteData(GroupItemVO data) throws
					DataAccessException
			{
				LOG.log(Level.FINE, "deleteData not supported");
			}

			@Override
			public GroupItemVO loadData(GroupItemVO data) throws
					DataAccessException
			{
				// To change body of generated methods, choose Tools | Templates.
				// TODO: Complete code for method loadData
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public List<GroupItemVO> retrieveAllData() throws
					DataAccessException
			{
				return groupManager.retrieveAllData();
			}

			@Override
			public GroupItemVO saveData(GroupItemVO data) throws
					DataAccessException
			{
				// To change body of generated methods, choose Tools | Templates.
				// TODO: Complete code for method saveData
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public void updateData(GroupItemVO data) throws
					DataAccessException
			{
				// To change body of generated methods, choose Tools | Templates.
				// TODO: Complete code for method updateData
				throw new UnsupportedOperationException("Not supported yet.");
			}
		};
	}
}
