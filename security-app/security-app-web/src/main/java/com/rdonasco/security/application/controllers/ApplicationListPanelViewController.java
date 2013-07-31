/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 15-Apr-2013
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
import com.rdonasco.common.utils.RandomTextGenerator;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.config.services.ConfigDataManagerVODecoratorRemote;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.datamanager.listeditor.controller.ListEditorAttachStrategy;
import com.rdonasco.datamanager.utils.TableHelper;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.application.utils.ApplicationConstants;
import com.rdonasco.security.application.views.ApplicationListPanelView;
import com.rdonasco.security.application.vo.ApplicationItemVO;
import com.rdonasco.security.application.vo.ApplicationItemVOBuilder;
import com.rdonasco.security.authentication.services.SessionSecurityChecker;
import com.rdonasco.security.common.builders.DeletePromptBuilder;
import com.rdonasco.security.common.controllers.ClickListenerProvider;
import com.rdonasco.security.common.utils.ActionConstants;
import com.rdonasco.security.i18n.MessageKeys;
import com.rdonasco.security.vo.ApplicationVO;
import com.rdonasco.security.vo.ApplicationVOBuilder;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import de.steinwedel.vaadin.MessageBox;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class ApplicationListPanelViewController implements
		ViewController<ApplicationListPanelView>
{
	private static final Logger LOG = Logger.getLogger(ApplicationListPanelViewController.class.getName());
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ApplicationListPanelView applicationListPanelView;
	
	@Inject
	private ApplicationDataManager dataManager;
	
	@EJB
	private ConfigDataManagerVODecoratorRemote configDataManager;
	
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	
	@Inject
	private ApplicationPopupProvider popupProvider;
	
	private Table recordListTable = new Table();
	
	private DataManagerContainer<ApplicationItemVO> itemVOTableContainer = new DataManagerContainer(ApplicationItemVO.class);
	
	@Inject
	private SessionSecurityChecker sessionSecurityChecker;
	
	public ApplicationListPanelViewController()
	{
	}

	public DataManagerContainer<ApplicationItemVO> getItemTableContainer()
	{
		return itemVOTableContainer;
	}

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			recordListTable.addStyleName(SecurityDefaultTheme.CSS_DATA_TABLE);
			TableHelper.setupTable(recordListTable);
			itemVOTableContainer.setDataManager(dataManager);
			recordListTable.setContainerDataSource(itemVOTableContainer);
			recordListTable.setVisibleColumns(ApplicationConstants.TABLE_VISIBLE_COLUMNS);
			recordListTable.setColumnHeaders(ApplicationConstants.TABLE_VISIBLE_HEADERS);
			applicationListPanelView.setDataViewListTable(recordListTable);
			applicationListPanelView.initWidget();
			applicationListPanelView.getAddButton().addListener(new AddNewRecordClickListener());
			applicationListPanelView.setAttachStrategy(new ListEditorAttachStrategy()
			{
				@Override
				public void attached(Component component)
				{
					refreshList();
					selectTheFirstRecord();
				}
			});
			applicationListPanelView.getRefreshButton().addListener(new Button.ClickListener()
			{
				private static final long serialVersionUID = 1L;
				@Override
				public void buttonClick(Button.ClickEvent event)
				{
					LOG.log(Level.FINE, "refresh button clicked");
					refreshList();
				}
			});
			setupDeleteClickListener();

		}
		catch (Exception ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	@Override
	public ApplicationListPanelView getControlledView()
	{
		return applicationListPanelView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		try
		{
			itemVOTableContainer.refresh();
		}
		catch (DataAccessException ex)
		{
			throw new WidgetException(ex);
		}
	}

	private void setupDeleteClickListener()
	{
		dataManager.setClickListenerProvider(new ClickListenerProvider<ApplicationItemVO>()
		{
			@Override
			public MouseEvents.ClickListener provideClickListenerFor(
					final ApplicationItemVO data)
			{
				MessageBox deletePrompt = new DeletePromptBuilder()
						.setParentWindow(getControlledView().getWindow())
						.createDeletePrompt();
				MouseEvents.ClickListener clickListener = new DeleteDataClickListener(deletePrompt, data);
				return clickListener;
			}
		});
	}

	private void addNewApplication()
	{
		int applicationTokenLength = configDataManager.loadValue(ApplicationConstants.XPATH_DEFAULT_TOKEN_LENGTH, Integer.class, 32);
		ApplicationVO newApplication = new ApplicationVOBuilder()
				.setName("new application")
				.setToken(RandomTextGenerator.generate(applicationTokenLength))
				.createApplicationVO();
		try
		{
			sessionSecurityChecker.checkCapabilityTo(ActionConstants.ADD, ApplicationConstants.RESOURCE_APPLICATION);
			ApplicationItemVO newItemVO = new ApplicationItemVOBuilder()
					.setApplicationVO(newApplication)
					.createApplicationItemVO();
			BeanItem<ApplicationItemVO> item = itemVOTableContainer.addItem(newItemVO);
			recordListTable.setCurrentPageFirstItemId(item.getBean());
			recordListTable.select(item.getBean());
		}
		catch (Exception e)
		{
			LOG.log(Level.WARNING, e.getMessage(), e);
			popupProvider.popUpError(I18NResource.localizeWithParameter(MessageKeys.UNABLE_TO_ADD_NEW_APPLICATION, newApplication.getName()));
		}
	}

	public Table getRecordListTable()
	{
		return recordListTable;
	}

	private class AddNewRecordClickListener implements Button.ClickListener
	{

		private static final long serialVersionUID = 1L;

		public AddNewRecordClickListener()
		{
		}

		@Override
		public void buttonClick(Button.ClickEvent event)
		{
			addNewApplication();
		}
	}

	class DeleteDataClickListener implements MouseEvents.ClickListener
	{

		private static final long serialVersionUID = 1L;
		
		private final MessageBox deletePrompt;
		
		private final ApplicationItemVO data;

		public DeleteDataClickListener(MessageBox deletePrompt,
				ApplicationItemVO data)
		{
			this.deletePrompt = deletePrompt;
			this.data = data;
		}

		@Override
		public void click(MouseEvents.ClickEvent event)
		{
			deletePrompt.show(new MessageBox.EventListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClicked(
						MessageBox.ButtonType buttonType)
				{
					try
					{
						sessionSecurityChecker.checkCapabilityTo(ActionConstants.DELETE, ApplicationConstants.RESOURCE_APPLICATION);
						if (MessageBox.ButtonType.YES.equals(buttonType))
						{
							itemVOTableContainer.removeItem(data);
							popupProvider.popUpInfo(I18NResource.localize(MessageKeys.APPLICATION_DELETED));


						}
					}
					catch (Exception e)
					{
						exceptionPopupProvider.popUpErrorException(e);
					}
					
				}
			});
		}
	}

	private void refreshList()
	{
		try
		{
			refreshView();
		}
		catch (WidgetException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	private void selectTheFirstRecord()
	{
		final Object firstItemId = getControlledView().getDataViewListTable().firstItemId();
		if (null != firstItemId)
		{
			getControlledView().getDataViewListTable().select(firstItemId);
		}
	}
}
