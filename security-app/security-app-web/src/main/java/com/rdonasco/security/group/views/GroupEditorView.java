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
package com.rdonasco.security.group.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addon.formbinder.ViewBoundForm;

/**
 *
 * @author Roy F. Donasco
 */
public class GroupEditorView extends VerticalLayout implements ControlledView
{

	private static final long serialVersionUID = 1L;

	private Panel groupDetailPanel = new Panel();

	private Panel groupRolesPanel = new Panel();

	private Button saveButton = new Button();

	private Button editButton = new Button();

	private Button cancelButton = new Button();

	private TextField nameField = new TextField(I18NResource.localize("Name"));

	private HorizontalLayout buttonLayout = new HorizontalLayout();

	private Table groupRolesTable = new Table();

	private Form form;

	public Form getForm()
	{
		if (form == null)
		{
			form = new ViewBoundForm(this);
			form.setWriteThrough(false);
		}
		return form;
	}

	public GroupEditorView()
	{
	}

	public Table getGroupRolesTable()
	{
		return groupRolesTable;
	}


	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		configureRoleFields();
		configureRoleCapabilityFields();
		configureEditorButtons();
		configureInitialState();

		addComponent(groupDetailPanel);
		addComponent(groupRolesPanel);
		addComponent(buttonLayout);

		setExpandRatio(groupRolesPanel, 1);
		addStyleName(SecurityDefaultTheme.CSS_GROUP_EDITOR);
		setSpacing(true);

	}

	private void configureRoleFields()
	{
		groupDetailPanel.setCaption(I18NResource.localize("Group Editor"));
		groupDetailPanel.setStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
		groupDetailPanel.addComponent(nameField);
	}

	private void configureRoleCapabilityFields()
	{
		groupRolesPanel.setCaption(I18NResource.localize("Roles allowed for this group"));
		groupRolesPanel.setStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
		groupRolesPanel.addComponent(getGroupRolesTable());
		getGroupRolesTable().addStyleName(SecurityDefaultTheme.CSS_TABLE_SMALL_STRIPED);
		getGroupRolesTable().addStyleName(SecurityDefaultTheme.CSS_TABLE_BORDERLESS);
		getGroupRolesTable().setSizeFull();
		((VerticalLayout) groupRolesPanel.getContent()).setExpandRatio(getGroupRolesTable(), 1);
	}

	private void configureEditorButtons()
	{
		editButton.setCaption(I18NResource.localize("Edit"));
		editButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICONS_16x16_EDIT));
		saveButton.setCaption(I18NResource.localize("Save"));
		saveButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICONS_16x16_SAVE));
		cancelButton.setCaption(I18NResource.localize("Cancel"));
		cancelButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICONS_16x16_CANCEL));

		saveButton.addStyleName(SecurityDefaultTheme.CSS_BUTTON_DEFAULT);
		buttonLayout.addComponent(editButton);
		buttonLayout.addComponent(saveButton);
		buttonLayout.addComponent(cancelButton);
		buttonLayout.setSpacing(true);
	}

	public Button getSaveButton()
	{
		return saveButton;
	}

	public Button getEditButton()
	{
		return editButton;
	}

	public Button getCancelButton()
	{
		return cancelButton;
	}

	private void configureInitialState()
	{
		getEditButton().setVisible(false);
		getCancelButton().setVisible(false);
		getSaveButton().setVisible(false);
		nameField.setReadOnly(true);
	}
}
