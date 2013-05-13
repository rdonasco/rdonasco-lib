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
package com.rdonasco.security.capability.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class AvailableCapabilitiesViewControllerBuilder
{

	private static final Logger LOG = Logger.getLogger(AvailableCapabilitiesViewControllerBuilder.class.getName());

	@Inject
	private AvailableCapabilitiesViewController availableCapabilitiesViewController;

	private boolean built = false;

	@Inject
	private CapabilityDataManagerDecorator capabilityManager;

	public AvailableCapabilitiesViewController build()
	{
		if (!built)
		{
			DataManagerContainer capabilityDataContainer = new DataManagerContainer(CapabilityItemVO.class);
			availableCapabilitiesViewController.setDataContainer(capabilityDataContainer);
			capabilityDataContainer.setDataManager(createAvailableCapabilitiesDataManager());
			availableCapabilitiesViewController.getControlledView().setReadOnly(true);
			availableCapabilitiesViewController.initializeControlledViewBehavior();
			built = true;
		}
		return availableCapabilitiesViewController;
	}

	private DataManager<CapabilityItemVO> createAvailableCapabilitiesDataManager()
	{
		return new DataManager<CapabilityItemVO>()
		{
			@Override
			public void deleteData(CapabilityItemVO data) throws
					DataAccessException
			{
				LOG.log(Level.FINE, "deleteData not supported");
			}

			@Override
			public CapabilityItemVO loadData(CapabilityItemVO data) throws
					DataAccessException
			{
				// To change body of generated methods, choose Tools | Templates.
				// TODO: Complete code for method loadData
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public List<CapabilityItemVO> retrieveAllData() throws
					DataAccessException
			{
				return capabilityManager.retrieveAllData();
			}

			@Override
			public CapabilityItemVO saveData(CapabilityItemVO data) throws
					DataAccessException
			{
				// To change body of generated methods, choose Tools | Templates.
				// TODO: Complete code for method saveData
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public void updateData(CapabilityItemVO data) throws
					DataAccessException
			{
				// To change body of generated methods, choose Tools | Templates.
				// TODO: Complete code for method updateData
				throw new UnsupportedOperationException("Not supported yet.");
			}
		};
	}
}
