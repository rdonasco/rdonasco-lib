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
package com.rdonasco.security.role.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.security.role.views.RoleEditorView;
import com.rdonasco.security.role.vo.RoleItemVO;
import com.vaadin.data.Buffered;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class RoleEditorViewController implements ViewController<RoleEditorView>
{

	private static final Logger LOG = Logger.getLogger(RoleEditorViewController.class.getName());

	private static final long serialVersionUID = 1L;

	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@Inject
	private ApplicationPopupProvider popupProvider;

	@Inject
	private RoleEditorView roleEditorView;

	private BeanItem<RoleItemVO> currentItem;

	private DataManagerContainer<RoleItemVO> roleItemTableContainer;

	private Button.ClickListener cancelClickListener = new Button.ClickListener()
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(Button.ClickEvent event)
		{
			discardChanges();
		}
	};

	private Button.ClickListener editClickListener = new Button.ClickListener()
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(Button.ClickEvent event)
		{
			changeViewToEditMode();
		}
	};

	private Button.ClickListener saveClickListener = new Button.ClickListener()
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(Button.ClickEvent event)
		{
			LOG.log(Level.FINE, "saveButton clicked");
			saveChanges();

		}
	};

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			roleEditorView.initWidget();
			configureButtonListenters();
		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	public void setRoleItemTableContainer(
			DataManagerContainer<RoleItemVO> roleItemTableContainer)
	{
		this.roleItemTableContainer = roleItemTableContainer;
	}


	private void saveChanges()
	{
		try
		{
			// TODO: commit role capabilities
			getControlledView().getForm().commit();
			changeViewToViewMode();
			LOG.log(Level.FINE, "roleItemTableContainer == null : {0}", roleItemTableContainer == null);
			LOG.log(Level.FINE, "currentItem == null : {0}", currentItem == null);
			roleItemTableContainer.updateItem(getCurrentItem().getBean());
			popupProvider.popUpInfo(I18NResource.localizeWithParameter("Role _ saved", getCurrentItem().getBean().getName()));
		}
		catch (Exception ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	private void discardChanges() throws Buffered.SourceException
	{
		getControlledView().getForm().discard();
		// TODO: discard changes on the capabilities
		setCurrentItem(currentItem);
		changeViewToViewMode();
	}

	public BeanItem<RoleItemVO> getCurrentItem()
	{
		return currentItem;
	}

	public void setCurrentItem(
			BeanItem<RoleItemVO> currentItem)
	{
		this.currentItem = currentItem;
		changeViewToViewMode();
	}

	@Override
	public RoleEditorView getControlledView()
	{
		return roleEditorView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method refreshView
		throw new UnsupportedOperationException("Not supported yet.");
	}

	void setItemDataSource(
			BeanItem<RoleItemVO> beamItem)
	{
		getControlledView().getForm().setItemDataSource(beamItem);
		setCurrentItem(beamItem);
	}

	void changeViewToViewMode()
	{
		getControlledView().getForm().setReadOnly(true);
		getControlledView().getSaveButton().setVisible(false);
		getControlledView().getCancelButton().setVisible(false);
		getControlledView().getEditButton().setVisible(true);
	}

	private void changeViewToEditMode()
	{
		getControlledView().getForm().setReadOnly(false);
		getControlledView().getSaveButton().setVisible(true);
		getControlledView().getCancelButton().setVisible(true);
		getControlledView().getEditButton().setVisible(false);
	}

	private void configureButtonListenters()
	{
		getControlledView().getEditButton().addListener(editClickListener);
		getControlledView().getCancelButton().addListener(cancelClickListener);
		getControlledView().getSaveButton().addListener(saveClickListener);
	}
}
