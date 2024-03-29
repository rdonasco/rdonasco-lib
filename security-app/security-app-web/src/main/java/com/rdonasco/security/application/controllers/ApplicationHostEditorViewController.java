/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 02-Aug-2013
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

import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.datamanager.listeditor.controller.ListEditorViewPanelController;
import com.rdonasco.security.application.vo.ApplicationHostItemVO;
import com.rdonasco.security.application.vo.ApplicationHostItemVOBuilder;
import com.rdonasco.security.vo.ApplicationHostVO;

/**
 *
 * @author Roy F. Donasco
 */
public class ApplicationHostEditorViewController extends ListEditorViewPanelController<ApplicationHostItemVO>
{
	private static final long serialVersionUID = 1L;
	private static final String[] COLUMN_HEADERS =
	{
		I18NResource.localize("Hosts")
	};
	private static final String[] VISIBLE_COLUMNS =
	{
		"hostNameOrIpAddress"
	};

	@Override
	public String[] getColumnHeaders()
	{
		return COLUMN_HEADERS;
	}

	@Override
	public String[] getVisibleColumns()
	{
		return VISIBLE_COLUMNS;
	}

	@Override
	public ApplicationHostItemVO createNewListEditorItem()
	{
		ApplicationHostVO applicationHostVO = new ApplicationHostVO();
		applicationHostVO.setHostNameOrIpAddress(I18NResource.localize("New host or IP"));
		ApplicationHostItemVO applicationHostItemVO = new ApplicationHostItemVOBuilder()
				.setApplicationHostVO(applicationHostVO)
				.createApplicationHostItemVO();
		return applicationHostItemVO;
	}

	@Override
	public String getItemName()
	{
		return I18NResource.localize("Application Host");
	}

	@Override
	public String getListName()
	{
		return I18NResource.localize("Hosts");
	}
}
