/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 30-Jul-2013
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
package com.rdonasco.security.application.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.OnAttachStrategy;
import com.rdonasco.common.vaadin.view.ButtonUtil;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import static com.vaadin.terminal.Sizeable.UNITS_PIXELS;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import javax.enterprise.context.Dependent;
import org.vaadin.addon.formbinder.ViewBoundForm;

/**
 *
 * @author Roy F. Donasco
 */
@Dependent
public class ApplicationEditorView extends VerticalLayout implements
		ControlledView
{

	private static final long serialVersionUID = 1L;
	private Panel applicationInfoPanel;
	private TextField nameField;
	private TextField tokenField;
	private HorizontalLayout buttonsLayout;
	private Button cancelButton;
	private Button editButton;
	private Button saveButton;
	private Button generateTokenButton;
	private Form form;
	private OnAttachStrategy attachStrategy;

	public ApplicationEditorView()
	{
		applicationInfoPanel = new Panel(I18NResource.localize("Application Editor"));
		nameField = new TextField(I18NResource.localize("Name"));
		tokenField = new TextField(I18NResource.localize("Token"));
		buttonsLayout = new HorizontalLayout();
		cancelButton = new Button();
		saveButton = new Button();
		editButton = new Button();
		generateTokenButton = new Button();
	}

	public OnAttachStrategy getAttachStrategy()
	{
		return attachStrategy;
	}

	public void setAttachStrategy(OnAttachStrategy attachStrategy)
	{
		this.attachStrategy = attachStrategy;
	}

	public Button getCancelButton()
	{
		return cancelButton;
	}

	public Button getEditButton()
	{
		return editButton;
	}

	public Button getSaveButton()
	{
		return saveButton;
	}

	public Form getForm()
	{
		if (form == null)
		{
			form = new ViewBoundForm(this);
		}
		return form;
	}

	public void hideButtons()
	{
		ButtonUtil.hideButtons(getSaveButton(), getEditButton(), getCancelButton());
	}

	@Override
	public void attach()
	{
		super.attach();
		if (getAttachStrategy() != null)
		{
			getAttachStrategy().onAttachOf(this);
		}
	}

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		applicationInfoPanel.addStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
		configureApplicationInfoFields();
		configureEditorButtons();
		setSpacing(true);
		addComponent(applicationInfoPanel);
		addComponent(buttonsLayout);
		getForm().setReadOnly(true);
		getForm().setWriteThrough(true);
	}

	private void configureApplicationInfoFields()
	{
		nameField.setWidth(200f, UNITS_PIXELS);
		applicationInfoPanel.addComponent(nameField);
		((VerticalLayout) applicationInfoPanel.getContent()).setSpacing(true);
		HorizontalLayout tokenFieldLayout = new HorizontalLayout();
		tokenFieldLayout.setSpacing(true);
		tokenField.setWidth(300, UNITS_PIXELS);
		tokenFieldLayout.addComponent(tokenField);
		generateTokenButton.addStyleName(BaseTheme.BUTTON_LINK);
		generateTokenButton.setDescription(I18NResource.localize("Generate token"));
		generateTokenButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_32x32_RELOAD));
		tokenFieldLayout.addComponent(generateTokenButton);
		tokenFieldLayout.setComponentAlignment(generateTokenButton, Alignment.BOTTOM_LEFT);
		applicationInfoPanel.addComponent(tokenFieldLayout);
	}

	private void configureEditorButtons()
	{
		editButton.setCaption(I18NResource.localize("Edit"));
		editButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_16x16_EDIT));
		saveButton.setCaption(I18NResource.localize("Save"));
		saveButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_16x16_SAVE));
		cancelButton.setCaption(I18NResource.localize("Cancel"));
		cancelButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_16x16_CANCEL));

		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(editButton);
		buttonsLayout.addComponent(saveButton);
		buttonsLayout.addComponent(cancelButton);
	}

	public TextField getNameField()
	{
		return nameField;
	}

	public TextField getTokenField()
	{
		return tokenField;
	}

	public Button getGenerateTokenButton()
	{
		return generateTokenButton;
	}
}
