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

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.security.capability.controllers.CapabilityDataManagerDecorator;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.rdonasco.security.user.views.UserEditorView;
import com.rdonasco.security.user.vo.UserSecurityProfileItemVO;
import com.vaadin.data.Buffered;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.dd.acceptcriteria.SourceIs;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import java.util.List;
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
	@Inject
	private CapabilityDataManagerDecorator capabilityManager;
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
			getControlledView().initWidget();
			configureInitialButtonState();
			configureButtonListeners();
			configureFieldValidators();
			configureCapabilitiesTab();
			changeViewToViewMode();

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
		changeViewToViewMode();
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
				changeViewToEditMode();
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
					changeViewToViewMode();
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
		changeViewToViewMode();
	}

	private void configureInitialButtonState()
	{
		getControlledView().hideButtons();
	}

	private void configureCapabilitiesTab()
	{
		getControlledView().getCapabilitiesLayout().setSpacing(true);
		getControlledView().getCapabilitiesLayout().setMargin(true, true, true, true);
		DataManagerContainer capabilityDataContainer = new DataManagerContainer(CapabilityItemVO.class);
		availableCapabilitiesViewController.setDataContainer(capabilityDataContainer);
		capabilityDataContainer.setDataManager(createAvailableCapabilitiesDataManager());
		availableCapabilitiesViewController.getControlledView().setReadOnly(true);
		availableCapabilitiesViewController.initializeControlledViewBehavior();

		getControlledView().getCapabilitiesLayout()
				.addComponent(userCapabilitiesViewController.getControlledView());
		getControlledView().getCapabilitiesLayout()
				.addComponent(availableCapabilitiesViewController.getControlledView());
		getControlledView().getCapabilitiesLayout()
				.setExpandRatio(userCapabilitiesViewController.getControlledView(), 0.5f);
		getControlledView().getCapabilitiesLayout()
				.setExpandRatio(availableCapabilitiesViewController.getControlledView(), 0.5f);

		// fix the size of the panels
		float panelHeight = 300;
		userCapabilitiesViewController.getControlledView().setHeight(panelHeight, Sizeable.UNITS_PIXELS);
		availableCapabilitiesViewController.getControlledView().setHeight(panelHeight, Sizeable.UNITS_PIXELS);

		// configure drag and drop of capabilities
		userCapabilitiesViewController.setValidDraggedObjectSource(new SourceIs(availableCapabilitiesViewController.getControlledView().getEditorTable()));

	}

	private void setViewToReadOnly(boolean readOnly)
	{
		getControlledView().setReadOnly(readOnly);
		getControlledView().getLogonIdField().setReadOnly(readOnly);
		getControlledView().getPasswordField().setReadOnly(readOnly);
		getControlledView().getRetypedPasswordField().setReadOnly(readOnly);
		getControlledView().getSaveButton().setReadOnly(readOnly);
		getControlledView().getCancelButton().setReadOnly(readOnly);
		getControlledView().getEditButton().setReadOnly(!readOnly);
	}

	public void changeViewToEditMode()
	{
		setViewToReadOnly(false);
		getControlledView().getSaveButton().setVisible(true);
		getControlledView().getCancelButton().setVisible(true);
		getControlledView().getEditButton().setVisible(false);
		getControlledView().getPasswordField().setVisible(true);
		getControlledView().getRetypedPasswordField().setVisible(true);
		userCapabilitiesViewController.enableEditing();
	}

	public void changeViewToViewMode()
	{
		setViewToReadOnly(true);
		getControlledView().getSaveButton().setVisible(false);
		getControlledView().getCancelButton().setVisible(false);
		getControlledView().getEditButton().setVisible(true);
		getControlledView().getPasswordField().setVisible(false);
		getControlledView().getRetypedPasswordField().setVisible(false);
		userCapabilitiesViewController.disableEditing();
	}

	private DataManager<CapabilityItemVO> createAvailableCapabilitiesDataManager()
	{
		return new DataManager<CapabilityItemVO>()
		{
			@Override
			public void deleteData(CapabilityItemVO data) throws
					DataAccessException
			{
				LOG.log(Level.FINE, "deleteData not supported");
			}

			@Override
			public CapabilityItemVO loadData(CapabilityItemVO data) throws
					DataAccessException
			{
				// To change body of generated methods, choose Tools | Templates.
				// TODO: Complete code for method loadData
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public List<CapabilityItemVO> retrieveAllData() throws
					DataAccessException
			{
				return capabilityManager.retrieveAllData();
			}

			@Override
			public CapabilityItemVO saveData(CapabilityItemVO data) throws
					DataAccessException
			{
				// To change body of generated methods, choose Tools | Templates.
				// TODO: Complete code for method saveData
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public void updateData(CapabilityItemVO data) throws
					DataAccessException
			{
				// To change body of generated methods, choose Tools | Templates.
				// TODO: Complete code for method updateData
				throw new UnsupportedOperationException("Not supported yet.");
			}
		};
	}
}
