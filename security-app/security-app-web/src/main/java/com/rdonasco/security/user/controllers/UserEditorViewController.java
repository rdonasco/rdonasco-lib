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
package com.rdonasco.security.user.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.common.vaadin.view.ButtonUtil;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.security.user.views.UserEditorView;
import com.rdonasco.security.user.vo.UserSecurityProfileItemVO;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class UserEditorViewController implements ViewController<UserEditorView>
{

	private static final long serialVersionUID = 1L;
	@Inject
	private UserEditorView editorView;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	private DataManagerContainer<UserSecurityProfileItemVO> userItemTableContainer;

	public void setUserItemTableContainer(
			DataManagerContainer<UserSecurityProfileItemVO> userItemTableContainer)
	{
		this.userItemTableContainer = userItemTableContainer;
	}

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			editorView.initWidget();
			configureFieldValidators();
			configureButtonListeners();
			configureInitialButtonState();

		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	private UserSecurityProfileItemVO getCurrentData()
	{
		BeanItem<UserSecurityProfileItemVO> item = (BeanItem) getControlledView().getForm().getItemDataSource();
		UserSecurityProfileItemVO currentData = item.getBean();
		return currentData;
	}

	@Override
	public UserEditorView getControlledView()
	{
		return editorView;
	}

	public void setItemDataSource(Item itemDataSource)
	{
		getControlledView().getForm().setItemDataSource(itemDataSource);
		getControlledView().changeModeToViewOnly();
	}

	@Override
	public void refreshView() throws WidgetException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method refreshView
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void configureFieldValidators()
	{
		editorView.getPasswordField().addValidator(new Validator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(Object value) throws
					Validator.InvalidValueException
			{
				if (!isValid(value))
				{
					throw new InvalidValueException(I18NResource.localize("Password change is required"));
				}
			}

			@Override
			public boolean isValid(Object value)
			{
				return (!(getCurrentData().isRequirePasswordChange() && (value == null || value.toString().isEmpty())));
			}
		});
		editorView.getRetypedPasswordField().addValidator(new Validator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(Object value) throws
					Validator.InvalidValueException
			{
				if (!isValid(value))
				{
					throw new InvalidValueException(I18NResource.localize("Retyped Password is required"));
				}
			}

			@Override
			public boolean isValid(Object value)
			{
				return !((editorView.getPasswordField().getValue() != null && !editorView.getPasswordField().getValue().toString().isEmpty() && (value == null || value.toString().isEmpty())));
			}
		});
	}

	private void configureButtonListeners()
	{
		editorView.getEditButton().addListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				editorView.changeModeToEdit();
			}
		});
		editorView.getSaveButton().addListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				try
				{
					getControlledView().getForm().commit();
					getControlledView().changeModeToViewOnly();
					userItemTableContainer.updateItem(getCurrentData());
				}
				catch (Exception ex)
				{
					exceptionPopupProvider.popUpErrorException(ex);
				}

			}
		});
	}

	private void configureInitialButtonState()
	{
		ButtonUtil.hideButtons(editorView.getSaveButton(), editorView.getEditButton(), editorView.getCancelButton());
	}
}
