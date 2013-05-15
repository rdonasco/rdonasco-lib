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

import com.rdonasco.security.capability.controllers.AvailableCapabilitiesViewController;
import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.security.capability.controllers.AvailableCapabilitiesViewControllerBuilder;
import com.rdonasco.security.user.views.UserEditorView;
import com.rdonasco.security.user.vo.UserSecurityProfileItemVO;
import com.vaadin.data.Buffered;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.dd.acceptcriteria.SourceIs;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
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

	@Inject
	private ApplicationPopupProvider popupProvider;

	private DataManagerContainer<UserSecurityProfileItemVO> userItemTableContainer;

	@Inject
	private UserCapabilitiesViewController userCapabilitiesViewController;

	@Inject
	private UserRolesViewController userRolesViewController;

	@Inject
	private AvailableCapabilitiesViewControllerBuilder availableCapabilitiesViewControllerBuilder;

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
			getControlledView().initWidget();
			configureInitialButtonState();
			configureButtonListeners();
			configureFieldValidators();
			configureCapabilitiesTab();
			configureRolesTab();
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
		userRolesViewController.setCurrentProfile(getCurrentItem());
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
				saveChanges();

			}
		});

		// configure shortcut listeners
		int[] keyModifiers = new int[]
		{
			ShortcutAction.ModifierKey.CTRL
		};
		getControlledView().getEditButton().setDescription(I18NResource.localize("edit shortcut key"));
		getControlledView().getEditButton().addShortcutListener(
				new ShortcutListener(null,
				ShortcutAction.KeyCode.E, keyModifiers)
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target)
			{
				changeViewToEditMode();
			}
		});
		getControlledView().getSaveButton().setDescription(I18NResource.localize("save shortcut key"));
		getControlledView().getSaveButton().addShortcutListener(
				new ShortcutListener(null,
				ShortcutAction.KeyCode.S, keyModifiers)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target)
			{
				saveChanges();
			}
		});
		getControlledView().getCancelButton().setDescription(I18NResource.localize("cancel shortcut key"));
		getControlledView().getCancelButton().addShortcutListener(
				new ShortcutListener(null,
				ShortcutAction.KeyCode.ESCAPE, null)
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target)
			{
				discardChanges();
			}
		});
	}

	private void clearPasswordFields()
	{
		getCurrentItem().getBean().setPassword(null);
		getCurrentItem().getBean().setRetypedPassword(null);
	}

	private void saveChanges()
	{
		try
		{
			userCapabilitiesViewController.commit();
			userRolesViewController.commit();
			getControlledView().getForm().commit();
			changeViewToViewMode();
			userItemTableContainer.updateItem(getCurrentItem().getBean());
			popupProvider.popUpInfo(I18NResource.localizeWithParameter("User _ saved", getCurrentItem().getBean().getLogonId()));
			clearPasswordFields();

		}
		catch (Exception ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	private void discardChanges() throws Buffered.SourceException
	{
		getControlledView().getForm().discard();
		userCapabilitiesViewController.discardChanges();
		userRolesViewController.discardChanges();
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

		getControlledView().getCapabilitiesLayout()
				.addComponent(userCapabilitiesViewController.getControlledView());
		getControlledView().getCapabilitiesLayout()
				.addComponent(getAvailableCapabilitiesViewController().getControlledView());
		getControlledView().getCapabilitiesLayout()
				.setExpandRatio(userCapabilitiesViewController.getControlledView(), 0.5f);
		getControlledView().getCapabilitiesLayout()
				.setExpandRatio(getAvailableCapabilitiesViewController().getControlledView(), 0.5f);

		// fix the size of the panels
		float panelHeight = 300;
		userCapabilitiesViewController.getControlledView().setHeight(panelHeight, Sizeable.UNITS_PIXELS);
		getAvailableCapabilitiesViewController().getControlledView().setHeight(panelHeight, Sizeable.UNITS_PIXELS);

		// configure drag and drop of capabilities
		userCapabilitiesViewController.setValidDraggedObjectSource(new SourceIs(getAvailableCapabilitiesViewController().getControlledView().getEditorTable()));

	}

	private void configureRolesTab()
	{
		getControlledView().getRolesLayout().setSpacing(true);
		getControlledView().getRolesLayout().setMargin(true, true, true, true);
		getControlledView().getRolesLayout()
				.addComponent(userRolesViewController.getControlledView());
		// TODO: Add available roles panel
		getControlledView().getRolesLayout()
				.setExpandRatio(userRolesViewController.getControlledView(), 0.5f);
		// TODO: setup expand ratio of available roles
		// fix the size of the panels
		float panelHeight = 300;
		userRolesViewController.getControlledView().setHeight(panelHeight, Sizeable.UNITS_PIXELS);
		// TODO: Configure drag and drop of roles		

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
		getAvailableCapabilitiesViewController().donotAllowDraggingAnyRow();
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
		getAvailableCapabilitiesViewController().allowDraggingMultipleRows();
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

	protected AvailableCapabilitiesViewController getAvailableCapabilitiesViewController()
	{
		return availableCapabilitiesViewControllerBuilder.build();
	}
}
