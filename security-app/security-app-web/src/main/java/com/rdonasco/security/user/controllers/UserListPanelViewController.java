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

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.datamanager.utils.TableHelper;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.rdonasco.security.common.controllers.ClickListenerProvider;
import com.rdonasco.security.i18n.MessageKeys;
import com.rdonasco.security.user.utils.UserConstants;
import com.rdonasco.security.user.views.UserListPanelView;
import com.rdonasco.security.user.vo.UserSecurityProfileItemVO;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import de.steinwedel.vaadin.MessageBox;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class UserListPanelViewController implements
		ViewController<UserListPanelView>
{

	private static final long serialVersionUID = 1L;
	@Inject
	private UserListPanelView userListPanelView;
	@Inject
	private UserDataManagerDecorator userDataManager;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	@Inject
	private ApplicationPopupProvider popupProvider;
	private Table userListTable = new Table();
	private DataManagerContainer<UserSecurityProfileItemVO> userItemTableContainer = new DataManagerContainer(UserSecurityProfileItemVO.class);

	public UserListPanelViewController()
	{
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
			setupDeleteClickListener();
			userListTable.setContainerDataSource(userItemTableContainer);
			userListTable.setVisibleColumns(UserConstants.TABLE_VISIBLE_COLUMNS);
			userListTable.setColumnHeaders(UserConstants.TABLE_VISIBLE_HEADERS);
			userListPanelView.setDataViewListTable(userListTable);
			userListPanelView.initWidget();
			userListPanelView.getAddUserButton().addListener(new Button.ClickListener()
			{
				@Override
				public void buttonClick(Button.ClickEvent event)
				{
					// To change body of generated methods, choose Tools | Templates.
					// TODO: Complete code for method buttonClick
					throw new UnsupportedOperationException("Not supported yet.");
				}
			});
		}
		catch (WidgetInitalizeException ex)
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
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method refreshView
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void setupDeleteClickListener()
	{
		userDataManager.setClickListenerProvider(new ClickListenerProvider<CapabilityItemVO>()
		{
			@Override
			public MouseEvents.ClickListener provideClickListenerFor(
					final CapabilityItemVO data)
			{
				MouseEvents.ClickListener clickListener = new MouseEvents.ClickListener()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void click(MouseEvents.ClickEvent event)
					{
						MessageBox messageBox = new MessageBox(userListPanelView.getWindow(),
								I18NResource.localize(MessageKeys.ARE_YOU_SURE),
								MessageBox.Icon.QUESTION,
								I18NResource.localize(MessageKeys.DO_YOU_REALLY_WANT_TO_DELETE_THIS),
								new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, I18NResource.localize(MessageKeys.YES)),
								new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, I18NResource.localize(MessageKeys.NO)));
						messageBox.show(new MessageBox.EventListener()
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
				};
				return clickListener;

			}
		});
	}
}
