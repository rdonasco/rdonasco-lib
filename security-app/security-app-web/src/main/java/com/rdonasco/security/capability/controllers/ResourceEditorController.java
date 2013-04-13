/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 12-Apr-2013
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

import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.datamanager.listeditor.controller.ListEditorViewPanelController;
import com.rdonasco.security.capability.vo.ResourceItemVO;
import com.rdonasco.security.capability.vo.ResourceItemVOBuilder;
import com.rdonasco.security.vo.ResourceVOBuilder;

/**
 *
 * @author Roy F. Donasco
 */
public class ResourceEditorController extends ListEditorViewPanelController<ResourceItemVO>
{

	private static final long serialVersionUID = 1L;
	private static final String CONSTANT_NAME = "name";

	@Override
	public String[] getColumnHeaders()
	{
		return new String[]
		{
			I18NResource.localize(CONSTANT_NAME)
		};
	}

	@Override
	public String[] getVisibleColumns()
	{
		return new String[]
		{
			CONSTANT_NAME
		};
	}

	@Override
	public ResourceItemVO createNewListEditorItem()
	{
		return new ResourceItemVOBuilder()
				.setResource(new ResourceVOBuilder()
				.setName("new resource")
				.createResourceVO())
				.createResourceItemVO();
	}

	@Override
	public String getItemName()
	{
		return I18NResource.localize("resource");
	}

	@Override
	public String getListName()
	{
		return I18NResource.localize("resources");
	}
}
