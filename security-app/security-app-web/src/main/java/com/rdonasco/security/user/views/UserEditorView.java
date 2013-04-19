/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 19-Apr-2013
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
import com.vaadin.data.Validator;
import static com.vaadin.terminal.Sizeable.UNITS_PIXELS;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addon.formbinder.ViewBoundForm;

/**
 *
 * @author Roy F. Donasco
 */
public class UserEditorView extends VerticalLayout implements ControlledView
{

	private static final long serialVersionUID = 1L;
	private Panel employeeDetailPanel = new Panel();
	private TextField logonIdField = new TextField();
	private PasswordField passwordField = new PasswordField();
	private PasswordField retypedPasswordField = new PasswordField();
	private TabSheet otherDetailTab = new TabSheet();
	private HorizontalLayout buttonsLayout = new HorizontalLayout();
	private HorizontalLayout capabilitiesLayout = new HorizontalLayout();
	private HorizontalLayout rolesLayout = new HorizontalLayout();
	private HorizontalLayout groupsLayout = new HorizontalLayout();
	private Button editButton = new Button();
	private Button saveButton = new Button();
	private Button cancelButton = new Button();
	private Form form;

	public Button getEditButton()
	{
		return editButton;
	}

	public Button getSaveButton()
	{
		return saveButton;
	}

	public Button getCancelButton()
	{
		return cancelButton;
	}

	public TextField getLogonIdField()
	{
		return logonIdField;
	}

	public PasswordField getPasswordField()
	{
		return passwordField;
	}

	public PasswordField getRetypedPasswordField()
	{
		return retypedPasswordField;
	}

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		configureUserDetailFields();

		otherDetailTab.addTab(capabilitiesLayout, I18NResource.localize("Capabilities"), new ThemeResource(SecurityDefaultTheme.ICON_32x32_CAPABILITIES));
		otherDetailTab.addTab(rolesLayout, I18NResource.localize("Roles"), new ThemeResource(SecurityDefaultTheme.ICON_32x32_ROLES));
		otherDetailTab.addTab(groupsLayout, I18NResource.localize("Groups"), new ThemeResource(SecurityDefaultTheme.ICON_32x32_GROUPS));

		editButton.setCaption(I18NResource.localize("Edit"));
		editButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICONS_16x16_EDIT));
		saveButton.setCaption(I18NResource.localize("Save"));
		saveButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICONS_16x16_SAVE));
		cancelButton.setCaption(I18NResource.localize("Cancel"));
		cancelButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICONS_16x16_CANCEL));
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(editButton);
		buttonsLayout.addComponent(saveButton);
		buttonsLayout.addComponent(cancelButton);

		setSpacing(true);
		addComponent(employeeDetailPanel);
		addComponent(otherDetailTab);
		addComponent(buttonsLayout);
		getForm().setReadOnly(true);
		getForm().setWriteThrough(true);
	}

	private void configureUserDetailFields()
	{
		((VerticalLayout) employeeDetailPanel.getContent()).setSpacing(true);
		employeeDetailPanel.setCaption(I18NResource.localize("User Editor"));
		employeeDetailPanel.addStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
		logonIdField.setCaption(I18NResource.localize("Logon ID"));
		logonIdField.setRequired(true);
		logonIdField.setRequiredError(I18NResource.localize("Logon ID is required"));
		logonIdField.setWidth(200f, UNITS_PIXELS);

		passwordField.setCaption(I18NResource.localize("Password"));
		passwordField.setRequired(true);
		passwordField.setRequiredError(I18NResource.localize("Password is required"));

		retypedPasswordField.setCaption(I18NResource.localize("Retype Password"));
		retypedPasswordField.setRequired(true);
		retypedPasswordField.setRequiredError(I18NResource.localize("Retyped Password is required"));
		retypedPasswordField.addValidator(new Validator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(Object value) throws
					Validator.InvalidValueException
			{
				if (!isValid(value))
				{
					throw new InvalidValueException(I18NResource.localize("Password mismatch"));
				}
			}

			@Override
			public boolean isValid(Object value)
			{
				boolean itIsValid = true;
				if (passwordField.getValue() != null)
				{
					itIsValid = passwordField.getValue().equals(retypedPasswordField.getValue());
				}
				return itIsValid;
			}
		});
		employeeDetailPanel.addComponent(logonIdField);
		employeeDetailPanel.addComponent(passwordField);
		employeeDetailPanel.addComponent(retypedPasswordField);
		setReadOnly(true);
	}

	public Form getForm()
	{
		if (form == null)
		{
			form = new ViewBoundForm(this);
		}
		return form;
	}

	@Override
	public void setReadOnly(boolean readOnly)
	{
		super.setReadOnly(readOnly);
		logonIdField.setReadOnly(readOnly);
		passwordField.setReadOnly(readOnly);
		retypedPasswordField.setReadOnly(readOnly);
	}
}
