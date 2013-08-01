/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 14-Apr-2013
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
import com.rdonasco.datamanager.listeditor.controller.ListEditorAttachStrategy;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import static com.vaadin.terminal.Sizeable.UNITS_PERCENTAGE;
import static com.vaadin.terminal.Sizeable.UNITS_PIXELS;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Roy F. Donasco
 */
public class UserListPanelView extends Panel implements ControlledView
{

	private static final long serialVersionUID = 1L;
	private Table dataViewListTable;
	private Button addUserButton = new Button();
	private Button refreshButton = new Button();
	private ListEditorAttachStrategy attachStrategy;

	public UserListPanelView()
	{
		setCaption(I18NResource.localize("Users"));
		setStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
	}

	public Table getDataViewListTable()
	{
		return dataViewListTable;
	}

	public Button getAddUserButton()
	{
		return addUserButton;
	}

	public Button getRefreshButton()
	{
		return refreshButton;
	}


	public void setDataViewListTable(Table dataViewListTable)
	{
		this.dataViewListTable = dataViewListTable;
		this.dataViewListTable.addStyleName(SecurityDefaultTheme.CSS_TABLE_SMALL_STRIPED);
		this.dataViewListTable.addStyleName(SecurityDefaultTheme.CSS_TABLE_BORDERLESS);
	}

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		getAddUserButton().setCaption(I18NResource.localize("Add new user"));
		getAddUserButton().setIcon(new ThemeResource(SecurityDefaultTheme.ICON_16x16_ADD));
		getAddUserButton().setWidth(100, UNITS_PERCENTAGE);
		getAddUserButton().addStyleName(SecurityDefaultTheme.CSS_SMALL);
		getRefreshButton().setCaption("Refresh");
		getRefreshButton().setIcon(new ThemeResource(SecurityDefaultTheme.ICON_16x16_REFRESH));
		getRefreshButton().addStyleName(SecurityDefaultTheme.CSS_SMALL);
		VerticalLayout content = ((VerticalLayout) getContent());
		content.setMargin(true);
		content.setHeight(600, UNITS_PIXELS);
		content.removeAllComponents();
		if (null != getDataViewListTable())
		{
			getDataViewListTable().setSizeFull();
			HorizontalLayout buttonLayout = new HorizontalLayout();
			buttonLayout.setSpacing(true);
			buttonLayout.setWidth(100F, UNITS_PERCENTAGE);
			buttonLayout.addComponent(getAddUserButton());
			buttonLayout.addComponent(getRefreshButton());
			buttonLayout.setExpandRatio(getAddUserButton(), 1);
			content.addComponent(buttonLayout);
			content.addComponent(getDataViewListTable());
			content.setExpandRatio(getDataViewListTable(), 1);
			content.setSpacing(true);
		}
	}

	public void setAttachStrategy(ListEditorAttachStrategy attachStrategy)
	{
		this.attachStrategy = attachStrategy;
	}

	@Override
	public void attach()
	{
		super.attach();
		if (attachStrategy != null)
		{
			attachStrategy.attached(this);
		}
	}
}
