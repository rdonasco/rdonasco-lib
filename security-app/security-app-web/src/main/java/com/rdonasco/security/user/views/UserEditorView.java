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
import com.rdonasco.common.vaadin.validators.FieldMatchedValueValidator;
import com.rdonasco.common.vaadin.view.ButtonUtil;
import com.rdonasco.common.vaadin.controller.OnAttachStrategy;
import static com.vaadin.terminal.Sizeable.UNITS_PIXELS;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
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
	private Panel userDetailPanel;
	private TextField logonIdField;
	private PasswordField passwordField;
	private PasswordField retypedPasswordField;
	private DateField registrationTokenExpirationField;
	private TextField registrationTokenField;

	private TabSheet otherDetailTab;
	private HorizontalLayout buttonsLayout;
	private HorizontalLayout capabilitiesLayout;
	private HorizontalLayout rolesLayout;
	private HorizontalLayout groupsLayout;
	private Button cancelButton;// = new Button();
	private Button editButton;
	private Button saveButton;
	private Form form;
	private OnAttachStrategy onAttachStrategy;

	public UserEditorView()
	{

		this.retypedPasswordField = new PasswordField();
		this.passwordField = new PasswordField();
		this.logonIdField = new TextField();
		this.registrationTokenField = new TextField();
		this.registrationTokenExpirationField = new DateField();

		this.userDetailPanel = new Panel();
		this.otherDetailTab = new TabSheet();
		this.capabilitiesLayout = new HorizontalLayout();
		this.rolesLayout = new HorizontalLayout();
		this.groupsLayout = new HorizontalLayout();
		this.buttonsLayout = new HorizontalLayout();

		this.cancelButton = new Button();
		this.saveButton = new Button();
		this.editButton = new Button();
	}

	public OnAttachStrategy getOnAttachStrategy()
	{
		return onAttachStrategy;
	}

	public void setOnAttachStrategy(OnAttachStrategy onAttachStrategy)
	{
		this.onAttachStrategy = onAttachStrategy;
	}

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

	public DateField getRegistrationTokenExpirationField()
	{
		return registrationTokenExpirationField;
	}

	public TextField getRegistrationTokenField()
	{
		return registrationTokenField;
	}

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		configureUserDetailFields();

		Tab capabilitiesTab = otherDetailTab.addTab(capabilitiesLayout, I18NResource.localize("Capabilities"), new ThemeResource(SecurityDefaultTheme.ICON_16x16_CAPABILITIES));
		capabilitiesTab.getComponent().setSizeFull();

		Tab rolesTab = otherDetailTab.addTab(rolesLayout, I18NResource.localize("Roles"), new ThemeResource(SecurityDefaultTheme.ICON_16x16_ROLES));
		rolesTab.getComponent().setSizeFull();

		Tab groupsTab = otherDetailTab.addTab(groupsLayout, I18NResource.localize("Groups"), new ThemeResource(SecurityDefaultTheme.ICON_16x16_GROUPS));
		groupsTab.getComponent().setSizeFull();
		configureEditorButtons();

		setSpacing(true);
		addComponent(userDetailPanel);
		addComponent(otherDetailTab);
		addComponent(buttonsLayout);
		getForm().setReadOnly(true);
		getForm().setWriteThrough(true);
	}

	private void configureUserDetailFields()
	{
		((VerticalLayout) userDetailPanel.getContent()).setSpacing(true);
		userDetailPanel.setCaption(I18NResource.localize("User Editor"));
		userDetailPanel.addStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
		logonIdField.setCaption(I18NResource.localize("Logon ID"));
		logonIdField.setRequired(true);
		logonIdField.setRequiredError(I18NResource.localize("Logon ID is required"));
		logonIdField.setWidth(200f, UNITS_PIXELS);

		passwordField.setCaption(I18NResource.localize("Password"));
		passwordField.addValidator(FieldMatchedValueValidator.createFieldMatchValidatorWithErrorMessage(retypedPasswordField, I18NResource.localize("Password mismatch")));

		retypedPasswordField.setCaption(I18NResource.localize("Retype Password"));
		retypedPasswordField.addValidator(FieldMatchedValueValidator.createFieldMatchValidatorWithErrorMessage(passwordField, I18NResource.localize("Password mismatch")));

		registrationTokenField.setCaption(I18NResource.localize("Registration Token"));
		registrationTokenField.setWidth(300f, UNITS_PIXELS);
		registrationTokenExpirationField.setCaption(I18NResource.localize("Registration Token Expiry"));
		registrationTokenExpirationField.setResolution(DateField.RESOLUTION_MIN);

		userDetailPanel.addComponent(logonIdField);
		userDetailPanel.addComponent(passwordField);
		userDetailPanel.addComponent(retypedPasswordField);
		userDetailPanel.addComponent(registrationTokenField);
		userDetailPanel.addComponent(registrationTokenExpirationField);
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

	public HorizontalLayout getCapabilitiesLayout()
	{
		return capabilitiesLayout;
	}

	public HorizontalLayout getRolesLayout()
	{
		return rolesLayout;
	}

	public HorizontalLayout getGroupsLayout()
	{
		return groupsLayout;
	}

	public void hideButtons()
	{
		ButtonUtil.hideButtons(getSaveButton(), getEditButton(), getCancelButton());
	}

	@Override
	public void attach()
	{
		super.attach();;
		if (getOnAttachStrategy() != null)
		{
			getOnAttachStrategy().onAttachOf(this);
		}
	}

	private void configureEditorButtons()
	{
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
	}
}
