/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 17-May-2013
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
package com.rdonasco.security.group.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.rdonasco.datamanager.listeditor.controller.ListEditorAttachStrategy;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import static com.vaadin.terminal.Sizeable.UNITS_PERCENTAGE;
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
public class GroupListPanelView extends Panel implements ControlledView
{

	private static final long serialVersionUID = 1L;

	private ListEditorAttachStrategy attachStrategy;

	private Table groupListTable;

	private Button addGroupButton;

	private Button refreshButton = new Button();

	public GroupListPanelView()
	{
		groupListTable = new Table();
		groupListTable.addStyleName(SecurityDefaultTheme.CSS_TABLE_SMALL_STRIPED);
		groupListTable.addStyleName(SecurityDefaultTheme.CSS_TABLE_BORDERLESS);
		addGroupButton = new Button(I18NResource.localize("Add new group"));
		addGroupButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICONS_16x16_ADD));
		addGroupButton.setWidth(100, UNITS_PERCENTAGE);
		addGroupButton.addStyleName(SecurityDefaultTheme.CSS_SMALL);
		refreshButton.setCaption("Refresh");
		refreshButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICONS_16x16_REFRESH));
		refreshButton.addStyleName(SecurityDefaultTheme.CSS_SMALL);
	}

	public Table getGroupListTable()
	{
		return groupListTable;
	}

	public Button getAddGroupButton()
	{
		return addGroupButton;
	}

	public Button getRefreshButton()
	{
		return refreshButton;
	}

	public void setAttachStrategy(ListEditorAttachStrategy attachStrategy)
	{
		this.attachStrategy = attachStrategy;
	}

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		setCaption(I18NResource.localize("Groups"));
		setStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
		VerticalLayout content = (VerticalLayout) getContent();
		content.setMargin(true);
		content.setHeight(600, UNITS_PIXELS);
		content.removeAllComponents();
		getGroupListTable().setSizeFull();
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.setWidth(100F, UNITS_PERCENTAGE);
		buttonLayout.addComponent(getAddGroupButton());
		buttonLayout.addComponent(getRefreshButton());
		buttonLayout.setExpandRatio(getAddGroupButton(), 1);
		content.addComponent(buttonLayout);

		content.addComponent(getGroupListTable());
		content.setExpandRatio(getGroupListTable(), 1);
		content.setSpacing(true);

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
