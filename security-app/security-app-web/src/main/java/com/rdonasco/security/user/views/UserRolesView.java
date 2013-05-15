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
package com.rdonasco.security.user.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Roy F. Donasco
 */
public class UserRolesView extends Panel implements ControlledView
{

	private static final long serialVersionUID = 1L;
	private Table userRolesTable;

	public UserRolesView()
	{
		userRolesTable = new Table();
	}

	public Table getUserRolesTable()
	{
		return userRolesTable;
	}

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		setCaption(I18NResource.localize("User Roles"));
		setStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
		getUserRolesTable().addStyleName(SecurityDefaultTheme.CSS_TABLE_SMALL_STRIPED);
		getUserRolesTable().addStyleName(SecurityDefaultTheme.CSS_TABLE_BORDERLESS);

		VerticalLayout layout = (VerticalLayout) getContent();
		layout.setSpacing(true);
		layout.addComponent(getUserRolesTable());
		getUserRolesTable().setSizeFull();
		layout.setExpandRatio(getUserRolesTable(), 1);
	}
}
