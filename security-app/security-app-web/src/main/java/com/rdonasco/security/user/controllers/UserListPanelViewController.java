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
package com.rdonasco.security.user.controllers;

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
import com.rdonasco.security.common.builders.DeletePromptBuilder;
import com.rdonasco.security.common.controllers.ClickListenerProvider;
import com.rdonasco.security.i18n.MessageKeys;
import com.rdonasco.security.user.utils.UserConfigConstants;
import com.rdonasco.security.user.utils.UserConstants;
import com.rdonasco.security.user.views.UserListPanelView;
import com.rdonasco.security.user.vo.UserSecurityProfileItemVO;
import com.rdonasco.security.user.vo.UserSecurityProfileItemVOBuilder;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.rdonasco.security.vo.UserSecurityProfileVOBuilder;
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
public class UserListPanelViewController implements
		ViewController<UserListPanelView>
{

	private static final Logger LOG = Logger.getLogger(UserListPanelViewController.class.getName());
	private static final long serialVersionUID = 1L;
	@Inject
	private UserListPanelView userListPanelView;
	@Inject
	private UserDataManagerDecorator userDataManager;
	@EJB
	private ConfigDataManagerVODecoratorRemote configDataManager;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	@Inject
	private ApplicationPopupProvider popupProvider;
	private Table userListTable = new Table();
	private DataManagerContainer<UserSecurityProfileItemVO> userItemTableContainer = new DataManagerContainer(UserSecurityProfileItemVO.class);

	public UserListPanelViewController()
	{
	}

	public DataManagerContainer<UserSecurityProfileItemVO> getUserItemTableContainer()
	{
		return userItemTableContainer;
	}


	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			userListTable.addStyleName(SecurityDefaultTheme.CSS_DATA_TABLE);
			TableHelper.setupTable(userListTable);
			userItemTableContainer.setDataManager(userDataManager);
			userListTable.setContainerDataSource(userItemTableContainer);
			userListTable.setVisibleColumns(UserConstants.TABLE_VISIBLE_COLUMNS);
			userListTable.setColumnHeaders(UserConstants.TABLE_VISIBLE_HEADERS);
			userListPanelView.setDataViewListTable(userListTable);
			userListPanelView.initWidget();
			userListPanelView.getAddUserButton().addListener(new AddNewUserClickListener());
			userListPanelView.setAttachStrategy(new ListEditorAttachStrategy()
			{
				@Override
				public void attached(Component component)
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
			});
			setupDeleteClickListener();

		}
		catch (Exception ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	@Override
	public UserListPanelView getControlledView()
	{
		return userListPanelView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		try
		{
			userItemTableContainer.refresh();
		}
		catch (DataAccessException ex)
		{
			throw new WidgetException(ex);
		}
	}

	private void setupDeleteClickListener()
	{
		userDataManager.setClickListenerProvider(new ClickListenerProvider<UserSecurityProfileItemVO>()
		{
			@Override
			public MouseEvents.ClickListener provideClickListenerFor(
					final UserSecurityProfileItemVO data)
			{
				MessageBox deletePrompt = new DeletePromptBuilder()
						.setParentWindow(getControlledView().getWindow())
						.createDeletePrompt();
				MouseEvents.ClickListener clickListener = new DeleteUserClickListener(deletePrompt, data);
				return clickListener;
			}
		});
	}

	private void addNewUser()
	{
		int defaultPasswordLength = configDataManager.loadValue(UserConfigConstants.XPATH_DEFAULT_PASSWORD_LENGTH, Integer.class, 8);
		UserSecurityProfileVO newUserProfile = new UserSecurityProfileVOBuilder()
				.setLoginId(I18NResource.localize("new logon id"))
				.setPassword(RandomTextGenerator.generate(defaultPasswordLength))
				.createUserSecurityProfileVO();
		try
		{
			UserSecurityProfileItemVO newItemVO = new UserSecurityProfileItemVOBuilder()
					.setUserSecurityProfileVO(newUserProfile)
					.setRequirePasswordChange(true)
					.createUserSecurityProfileItemVO();
			BeanItem<UserSecurityProfileItemVO> item = userItemTableContainer.addItem(newItemVO);
			userListTable.setCurrentPageFirstItemId(item.getBean());
			userListTable.select(item.getBean());
		}
		catch (Exception e)
		{
			LOG.log(Level.WARNING, e.getMessage(), e);
			popupProvider.popUpError(I18NResource.localizeWithParameter(MessageKeys.UNABLE_TO_ADD_NEW_USER, newUserProfile.getLogonId()));
		}
	}

	public Table getUserListTable()
	{
		return userListTable;
	}

	private class AddNewUserClickListener implements Button.ClickListener
	{

		private static final long serialVersionUID = 1L;

		public AddNewUserClickListener()
		{
		}

		@Override
		public void buttonClick(Button.ClickEvent event)
		{
			addNewUser();
		}
	}

	class DeleteUserClickListener implements MouseEvents.ClickListener
	{

		private static final long serialVersionUID = 1L;
		private final MessageBox deletePrompt;
		private final UserSecurityProfileItemVO data;

		public DeleteUserClickListener(MessageBox deletePrompt,
				UserSecurityProfileItemVO data)
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
					if (MessageBox.ButtonType.YES.equals(buttonType))
					{
						try
						{
							userItemTableContainer.removeItem(data);
							popupProvider.popUpInfo(I18NResource.localize(MessageKeys.USER_PROFILE_DELETED));

						}
						catch (Exception e)
						{
							exceptionPopupProvider.popUpErrorException(e);
						}

					}
				}
			});
		}
	}
}
