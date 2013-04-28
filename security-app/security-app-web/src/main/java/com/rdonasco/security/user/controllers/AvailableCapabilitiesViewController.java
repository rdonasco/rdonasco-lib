/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 24-Apr-2013
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

package com.rdonasco.security.user.controllers;

import com.rdonasco.datamanager.listeditor.controller.ListEditorViewPanelController;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import java.util.logging.Logger;

/**
 *
 * @author Roy F. Donasco
 */
public class AvailableCapabilitiesViewController extends ListEditorViewPanelController<CapabilityItemVO>
{

	private static final Logger LOG = Logger.getLogger(AvailableCapabilitiesViewController.class.getName());
	private static final long serialVersionUID = 1L;
	private static final String CONSTANT_TITLE = "title";

	@Override
	public String[] getColumnHeaders()
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method getColumnHeaders
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String[] getVisibleColumns()
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method getVisibleColumns
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public CapabilityItemVO createNewListEditorItem()
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method createNewListEditorItem
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getItemName()
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method getItemName
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getListName()
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method getListName
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
