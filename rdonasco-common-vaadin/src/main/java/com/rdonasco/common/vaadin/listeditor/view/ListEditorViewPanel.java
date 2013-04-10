/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 10-Apr-2013
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

package com.rdonasco.common.vaadin.listeditor.view;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.rdonasco.common.vaadin.view.terminal.StreamResourceBuilder;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Roy F. Donasco
 */
public class ListEditorViewPanel extends Panel implements ControlledView
{
	private static final long serialVersionUID = 1L;
	private Table editorTable = new Table();
	private Button addItemButton = new Button();

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		setCaption(I18NResource.localize("List Editor"));
		setStyleName(ListEditorTheme.CSS_PANEL_BUBBLE);
		addItemButton.setCaption(I18NResource.localize("Add new item"));
		addItemButton.setWidth(100f, UNITS_PERCENTAGE);
		addItemButton.setIcon(getAddItemButtonIcon());
		addItemButton.addStyleName(ListEditorTheme.CSS_SMALL);
		editorTable.addStyleName(ListEditorTheme.CSS_TABLE_SMALL_STRIPED);
		editorTable.addStyleName(ListEditorTheme.CSS_TABLE_BORDERLESS);
		VerticalLayout layout = (VerticalLayout) getContent();
		layout.addComponent(addItemButton);
		layout.addComponent(editorTable);
		editorTable.setSizeFull();
		layout.setExpandRatio(editorTable, 1);
		layout.setSpacing(true);
	}

	public Table getEditorTable()
	{
		return editorTable;
	}

	public Button getAddItemButton()
	{
		return addItemButton;
	}

	private Resource getAddItemButtonIcon()
	{
		String buttonIcon = ListEditorTheme.RESOURCE_ICON_ADD;
		return new StreamResourceBuilder()
				.setReferenceClass(this.getClass())
				.setRelativeResourcePath(buttonIcon)
				.setApplication(getApplication())
				.createStreamResource();
	}
}
