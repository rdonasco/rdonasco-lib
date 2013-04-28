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
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.common.vaadin.controller.OnAttachStrategy;
import com.rdonasco.security.user.views.UserEditorView;
import com.rdonasco.security.user.vo.UserSecurityProfileItemVO;
import com.vaadin.data.Buffered;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class UserEditorViewController implements ViewController<UserEditorView>
{

	private static final Logger LOG = Logger.getLogger(UserEditorViewController.class.getName());
	private static final long serialVersionUID = 1L;
	@Inject
	private UserEditorView editorView;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	private DataManagerContainer<UserSecurityProfileItemVO> userItemTableContainer;
	@Inject
	private UserCapabilitiesViewController userCapabilitiesViewController;
	@Inject
	private AvailableCapabilitiesViewController availableCapabilitiesViewController;
	private BeanItem<UserSecurityProfileItemVO> currentItem;
	private Button.ClickListener cancelClickListener = new Button.ClickListener()
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(Button.ClickEvent event)
		{
			discardChanges();
		}
	};

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
			configureInitialButtonState();
			configureButtonListeners();
			configureFieldValidators();
			configureCapabilitiesTab();

		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	private BeanItem<UserSecurityProfileItemVO> getCurrentItem()
	{
		return currentItem;
	}

	private void setCurrentItem(BeanItem<UserSecurityProfileItemVO> currentItem)
	{
		this.currentItem = currentItem;
		userCapabilitiesViewController.setCurrentProfile(getCurrentItem());
		getControlledView().changeModeToViewOnly();
	}

	@Override
	public UserEditorView getControlledView()
	{
		return editorView;
	}

	public void setItemDataSource(Item itemDataSource)
	{
		getControlledView().getForm().setItemDataSource(itemDataSource);
		BeanItem<UserSecurityProfileItemVO> item = (BeanItem) getControlledView().getForm().getItemDataSource();
		setCurrentItem(item);
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
				return (!(getCurrentItem().getBean().isRequirePasswordChange() && (value == null || value.toString().isEmpty())));
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
		getControlledView().getCancelButton().addListener(cancelClickListener);
		getControlledView().getEditButton().addListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				getControlledView().changeModeToEdit();
			}
		});
		getControlledView().getSaveButton().addListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				LOG.log(Level.FINE, "saveButton clicked");
				try
				{
					userCapabilitiesViewController.commit();
					getControlledView().getForm().commit();
					getControlledView().changeModeToViewOnly();
					userItemTableContainer.updateItem(getCurrentItem().getBean());
				}
				catch (Exception ex)
				{
					exceptionPopupProvider.popUpErrorException(ex);
				}

			}
		});


	}

	private void discardChanges() throws Buffered.SourceException
	{
		getControlledView().getForm().discard();
		userCapabilitiesViewController.discardChanges();
		setCurrentItem(currentItem);
		getControlledView().changeModeToViewOnly();
	}

	private void configureInitialButtonState()
	{
		getControlledView().hideButtons();
	}

	private void configureCapabilitiesTab()
	{
		getControlledView().getCapabilitiesLayout().setSpacing(true);
		getControlledView().getCapabilitiesLayout().setMargin(true, true, true, true);
		getControlledView().getCapabilitiesLayout()
				.addComponent(userCapabilitiesViewController.getControlledView());
		getControlledView().getCapabilitiesLayout()
				.addComponent(availableCapabilitiesViewController.getControlledView());
		getControlledView().getCapabilitiesLayout()
				.setExpandRatio(userCapabilitiesViewController.getControlledView(), 0.5f);
		getControlledView().getCapabilitiesLayout()
				.setExpandRatio(availableCapabilitiesViewController.getControlledView(), 0.5f);


	}
}
