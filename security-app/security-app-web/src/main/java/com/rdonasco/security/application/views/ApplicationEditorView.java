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

import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.OnAttachStrategy;
import com.rdonasco.common.vaadin.view.ButtonUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addon.formbinder.ViewBoundForm;

/**
 *
 * @author Roy F. Donasco
 */
public class ApplicationEditorView extends VerticalLayout
{
	private static final long serialVersionUID = 1L;
	private Panel applicationInfoPanel;
	private TextField nameField;
	private TextField tokenField;
	private HorizontalLayout buttonsLayout;
	private Button cancelButton;
	private Button editButton;
	private Button saveButton;
	private Form form;
	private OnAttachStrategy attachStrategy;

	public ApplicationEditorView()
	{
		applicationInfoPanel = new Panel(I18NResource.localize("Application Editor"));
		nameField = new TextField(I18NResource.localize("Name"));
		tokenField = new TextField(I18NResource.localize("Token"));
		buttonsLayout = new HorizontalLayout();
		cancelButton = new Button(I18NResource.localize("Cancel"));
		saveButton = new Button(I18NResource.localize("Save"));
		editButton = new Button(I18NResource.localize("Edit"));
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
}
