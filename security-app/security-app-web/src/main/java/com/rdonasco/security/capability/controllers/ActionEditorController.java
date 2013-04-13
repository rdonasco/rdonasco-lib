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
import com.rdonasco.security.capability.vo.ActionItemVO;
import com.rdonasco.security.capability.vo.ActionItemVOBuilder;
import com.rdonasco.security.vo.ActionVO;

/**
 *
 * @author Roy F. Donasco
 */
public class ActionEditorController extends ListEditorViewPanelController<ActionItemVO>
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
	public ActionItemVO createNewListEditorItem()
	{
		return new ActionItemVOBuilder()
				.setAction(ActionVO.createWithName("new action"))
				.createActionItemVO();
	}

	@Override
	public String getItemName()
	{
		return I18NResource.localize("action");
	}

	@Override
	public String getListName()
	{
		return I18NResource.localize("actions");
	}
}
