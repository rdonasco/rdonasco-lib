/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 31-Jul-2013
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
package com.rdonasco.security.application.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.common.vaadin.controller.DataEditorViewController;
import com.rdonasco.common.vaadin.controller.utils.DataEditorButtonBehaviorBuilder;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.security.application.utils.ApplicationConstants;
import com.rdonasco.security.application.utils.ApplicationTokenGenerator;
import com.rdonasco.security.application.views.ApplicationEditorView;
import com.rdonasco.security.application.vo.ApplicationHostItemVO;
import com.rdonasco.security.application.vo.ApplicationHostItemVOBuilder;
import com.rdonasco.security.application.vo.ApplicationItemVO;
import com.rdonasco.security.authentication.services.SessionSecurityChecker;
import com.rdonasco.security.common.utils.ActionConstants;
import com.rdonasco.security.vo.ApplicationHostVO;
import com.vaadin.data.Buffered;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class ApplicationEditorViewController implements
		DataEditorViewController<ApplicationEditorView, ApplicationItemVO>
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(ApplicationEditorViewController.class.getName());
	@Inject
	private SessionSecurityChecker sessionSecurityChecker;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	@Inject
	private ApplicationEditorView editorView;
	private DataManagerContainer<ApplicationItemVO> dataManagerContainer;
	private BeanItem<ApplicationItemVO> currentItem;
	private List<ApplicationHostItemVO> currentHosts;
	private Integer hostIndex = 0;
	@Inject
	private ApplicationPopupProvider popupProvider;
	@Inject
	private ApplicationTokenGenerator applicationTokenGenerator;
	private ApplicationHostEditorViewController hostEditorViewController;
	private ApplicationListPanelViewController applicationListPanelViewController;
	@Inject
	private Instance<ApplicationHostEditorViewController> hostEditorViewControllers;
	private DataManager<ApplicationHostItemVO> hostEditorDataManager = new DataManager<ApplicationHostItemVO>()
	{
		@Override
		public void deleteData(ApplicationHostItemVO data) throws
				DataAccessException
		{
			sessionSecurityChecker.checkCapabilityTo(ActionConstants.DELETE, ApplicationConstants.RESOURCE_APPLICATION_HOST);
			currentHosts.remove(data);
		}

		@Override
		public ApplicationHostItemVO loadData(ApplicationHostItemVO data)
				throws DataAccessException
		{
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public List<ApplicationHostItemVO> retrieveAllData() throws
				DataAccessException
		{
			currentHosts = new ArrayList<ApplicationHostItemVO>();
			hostIndex = 0;
			if (null != currentItem)
			{
				final List<ApplicationHostVO> listOfHosts = currentItem.getBean().getApplicationVO().getHosts();
				for (ApplicationHostVO applicationHostVO : listOfHosts)
				{
					currentHosts.add(new ApplicationHostItemVOBuilder()
							.setApplicationHostVO(applicationHostVO)
							.setViewIndex(hostIndex++)
							.createApplicationHostItemVO());
				}
			}
			return currentHosts;
		}

		@Override
		public ApplicationHostItemVO saveData(ApplicationHostItemVO data)
				throws DataAccessException
		{
			sessionSecurityChecker.checkCapabilityTo(ActionConstants.ADD, ApplicationConstants.RESOURCE_APPLICATION_HOST);
			data.setViewIndex(hostIndex++);
			currentHosts.add(data);
			return data;
		}

		@Override
		public void updateData(ApplicationHostItemVO data) throws
				DataAccessException
		{
			sessionSecurityChecker.checkCapabilityTo(ActionConstants.EDIT, ApplicationConstants.RESOURCE_APPLICATION_HOST);
			int dataIndex = currentHosts.indexOf(data);
			if (dataIndex != -1)
			{
				currentHosts.get(dataIndex).setHostNameOrIpAddress(data.getHostNameOrIpAddress());
			}
			else
			{
				LOG.log(Level.FINE, "no data to update, index = {0}", dataIndex);
			}
		}
	};

	public void setApplicationListPanelViewController(
			ApplicationListPanelViewController applicationListPanelViewController)
	{
		this.applicationListPanelViewController = applicationListPanelViewController;
	}

	public ApplicationHostEditorViewController getHostEditorViewController()
	{
		if (null == hostEditorViewController)
		{
			hostEditorViewController = hostEditorViewControllers.get();
		}
		return hostEditorViewController;
	}

	public void setDataManagerContainer(
			DataManagerContainer<ApplicationItemVO> dataManagerContainer)
	{
		this.dataManagerContainer = dataManagerContainer;
	}

	@Override
	public void setCurrentItem(
			BeanItem<ApplicationItemVO> currentItem)
	{
		this.currentItem = currentItem;
		getControlledView().getForm().setItemDataSource(currentItem);
		changeViewToDisplayMode();
		refreshHostEditorViewController();
	}

	@Override
	public BeanItem<ApplicationItemVO> getCurrentItem()
	{
		return currentItem;
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
			configureHostEditorController();
			changeViewToDisplayMode();
		}
		catch (Exception e)
		{
			exceptionPopupProvider.popUpErrorException(e);
		}
	}

	@Override
	public ApplicationEditorView getControlledView()
	{
		return editorView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void configureInitialButtonState()
	{
		getControlledView().hideButtons();
	}

	@Override
	public void configureButtonListeners()
	{
		new DataEditorButtonBehaviorBuilder()
				.setDataEditorViewController(this)
				.setCancelButton(getControlledView().getCancelButton())
				.setEditButton(getControlledView().getEditButton())
				.setSaveButton(getControlledView().getSaveButton())
				.build();
		getControlledView().getGenerateTokenButton().addListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				getControlledView().getTokenField().setValue(applicationTokenGenerator.generate());
			}
		});
	}

	private void configureFieldValidators()
	{
		getControlledView().getNameField().setRequired(true);
		getControlledView().getNameField().setRequiredError(I18NResource.localize("Application name is required"));
		getControlledView().getTokenField().setRequired(true);
		getControlledView().getTokenField().setRequiredError(I18NResource.localize("Token is required"));
	}

	@Override
	public void changeViewToDisplayMode()
	{
		getControlledView().getForm().setReadOnly(true);
		getControlledView().getEditButton().setVisible(true);
		getControlledView().getSaveButton().setVisible(false);
		getControlledView().getCancelButton().setVisible(false);
		getControlledView().getGenerateTokenButton().setVisible(false);
		getHostEditorViewController().disableEditing();
	}

	@Override
	public void changeViewToEditMode()
	{
		try
		{
			sessionSecurityChecker.checkCapabilityTo(ActionConstants.EDIT,
					ApplicationConstants.RESOURCE_APPLICATION);
			getControlledView().getForm().setReadOnly(false);
			getControlledView().getEditButton().setVisible(false);
			getControlledView().getSaveButton().setVisible(true);
			getControlledView().getCancelButton().setVisible(true);
			getControlledView().getGenerateTokenButton().setVisible(true);
			getHostEditorViewController().enableEditing();
		}
		catch (Exception e)
		{
			exceptionPopupProvider.popUpErrorException(e.getCause());
		}

	}

	@Override
	public void discardChanges() throws Buffered.SourceException
	{
		getControlledView().getForm().discard();
		getHostEditorViewController().getControlledView().getEditorTable().discard();
		setCurrentItem((BeanItem) dataManagerContainer.getItem(currentItem.getBean()));
		try
		{
			applicationListPanelViewController.refreshView();
		}
		catch (Exception e)
		{
			exceptionPopupProvider.popUpErrorException(e);
		}
	}

	@Override
	public void saveChanges()
	{
		try
		{
			getHostEditorViewController().getControlledView().getEditorTable().commit();
			getControlledView().getForm().commit();
			final ArrayList<ApplicationHostVO> updatedHostList = new ArrayList<ApplicationHostVO>(currentHosts.size());
			getCurrentItem().getBean().getApplicationVO()
					.setHosts(updatedHostList);
			for (ApplicationHostItemVO hostItemVO : currentHosts)
			{
				updatedHostList.add(hostItemVO.getApplicationHostVO());
			}
			dataManagerContainer.updateItem(getCurrentItem().getBean());
			changeViewToDisplayMode();
			applicationListPanelViewController.refreshView();
			popupProvider.popUpInfo(I18NResource.localizeWithParameter("Application _ saved", getCurrentItem().getBean().getName()));

		}
		catch (Exception e)
		{
			exceptionPopupProvider.popUpErrorException(e);
		}
	}

	private void configureHostEditorController()
	{
		DataManagerContainer hostDataContainer = new DataManagerContainer(ApplicationHostItemVO.class);
		getHostEditorViewController().setDataContainer(hostDataContainer);
		hostDataContainer.setDataManager(hostEditorDataManager);
		getHostEditorViewController().initializeControlledViewBehavior();
		getHostEditorViewController().getControlledView().getEditorTable().setColumnExpandRatio("hostNameOrIpAddress", 1f);
		getHostEditorViewController().getControlledView().getEditorTable().setWriteThrough(false);
	}

	private void refreshHostEditorViewController()
	{
		try
		{
			getHostEditorViewController().refreshView();
		}
		catch (WidgetException ex)
		{
			LOG.log(Level.WARNING, ex.getMessage(), ex);
		}
	}
}
