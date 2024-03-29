/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 11-May-2013
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
package com.rdonasco.security.role.views;

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
public class RoleListPanelView extends Panel implements
		ControlledView
{
	private ListEditorAttachStrategy attachStrategy;
	private static final long serialVersionUID = 1L;

	private Table roleListTable;

	private Button addRoleButton;

	private Button refreshButton;

	public RoleListPanelView()
	{
		this.roleListTable = new Table();
		this.roleListTable.addStyleName(SecurityDefaultTheme.CSS_TABLE_SMALL_STRIPED);
		this.roleListTable.addStyleName(SecurityDefaultTheme.CSS_TABLE_BORDERLESS);
		this.addRoleButton = new Button(I18NResource.localize("Add new role"));
		this.refreshButton = new Button(I18NResource.localize("Refresh"));
		addRoleButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_16x16_ADD));
		addRoleButton.setWidth(100, UNITS_PERCENTAGE);
		addRoleButton.addStyleName(SecurityDefaultTheme.CSS_SMALL);
		refreshButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_16x16_REFRESH));
		refreshButton.addStyleName(SecurityDefaultTheme.CSS_SMALL);
	}

	public Table getRoleListTable()
	{
		return roleListTable;
	}

	public Button getAddRoleButton()
	{
		return addRoleButton;
	}

	public Button getRefreshButton()
	{
		return refreshButton;
	}

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		setCaption(I18NResource.localize("Roles"));
		setStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
		VerticalLayout content = (VerticalLayout) getContent();
		content.setMargin(true);
		content.setHeight(600, UNITS_PIXELS);
		content.removeAllComponents();
		getRoleListTable().setSizeFull();
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.setWidth(100F, UNITS_PERCENTAGE);
		buttonLayout.addComponent(addRoleButton);
		buttonLayout.addComponent(refreshButton);				
		buttonLayout.setExpandRatio(addRoleButton, 1);
		content.addComponent(buttonLayout);
		content.addComponent(getRoleListTable());
		content.setExpandRatio(getRoleListTable(), 1);
		content.setSpacing(true);
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
