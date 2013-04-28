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

import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.datamanager.listeditor.controller.ListEditorViewPanelController;
import com.rdonasco.security.capability.vo.CapabilityItemVO;

/**
 *
 * @author Roy F. Donasco
 */
public class AvailableCapabilitiesViewController extends ListEditorViewPanelController<CapabilityItemVO>
{

	private static final long serialVersionUID = 1L;
	private static final String CONSTANT_TITLE = "title";

	@Override
	public String[] getColumnHeaders()
	{
		return new String[]
		{
			I18NResource.localize(CONSTANT_TITLE)
		};
	}

	@Override
	public String[] getVisibleColumns()
	{
		return new String[]
		{
			CONSTANT_TITLE
		};
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
		return I18NResource.localize("Capability");
	}

	@Override
	public String getListName()
	{
		return I18NResource.localize("Capabilities");
	}
}
