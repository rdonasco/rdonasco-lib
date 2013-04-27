/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 24-Apr-2013
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
import com.rdonasco.security.capability.utils.IconHelper;
import com.rdonasco.security.user.views.UserCapabilitiesView;
import com.vaadin.data.util.BeanItemContainer;
import com.rdonasco.security.user.vo.UserCapabilityItemVO;
import com.rdonasco.security.user.vo.UserCapabilityItemVOBuilder;
import com.rdonasco.security.user.vo.UserSecurityProfileItemVO;
import com.rdonasco.security.vo.UserCapabilityVO;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.Embedded;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class UserCapabilitiesViewController implements
		ViewController<UserCapabilitiesView>
{

	private static final Logger LOG = Logger.getLogger(UserCapabilitiesViewController.class.getName());
	private static final long serialVersionUID = 1L;
	@Inject
	private UserCapabilitiesView userCapabilitiesView;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	@Inject
	private ApplicationPopupProvider popupProvider;
	private BeanItemContainer<UserCapabilityItemVO> userCapabilityItemContainer = new BeanItemContainer(UserCapabilityItemVO.class);
	private UserSecurityProfileItemVO currentProfile;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			userCapabilitiesView.initWidget();
			configureUserCapabilitiesTable();
		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}

	}

	public void setCurrentProfile(UserSecurityProfileItemVO profile)
	{
		currentProfile = profile;
		try
		{
			refreshView();
		}
		catch (WidgetException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	@Override
	public UserCapabilitiesView getControlledView()
	{
		return userCapabilitiesView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		populateItemContainer();
	}

	private void configureUserCapabilitiesTable()
	{
		getControlledView().getUserCapabilitiesTable().setContainerDataSource(userCapabilityItemContainer);
		userCapabilityItemContainer.addNestedContainerProperty("capability.title");
		getControlledView().getUserCapabilitiesTable().setVisibleColumns(new String[]
		{
			"icon", "capability.title"
		});
		getControlledView().getUserCapabilitiesTable().setColumnHeaders(new String[]
		{
			"", I18NResource.localize("Title")
		});
	}

	private void populateItemContainer()
	{
		userCapabilityItemContainer.removeAllItems();
		for (UserCapabilityVO userCapability : currentProfile.getCapabilities())
		{
			Embedded icon = IconHelper.createDeleteIcon("Remove capability");
			final UserCapabilityItemVO userCapabilityItemVO = new UserCapabilityItemVOBuilder()
					.setIcon(icon)
					.setUserCapabilityVO(userCapability)
					.createUserCapabilityItemVO();
			userCapabilityItemVO.getCapability().getTitle();
			userCapabilityItemContainer.addItem(userCapabilityItemVO);
			icon.addListener(new MouseEvents.ClickListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void click(MouseEvents.ClickEvent event)
				{
					if (!getControlledView().isReadOnly() && !userCapabilityItemContainer.removeItem(userCapabilityItemVO))
					{
						popupProvider.popUpError(I18NResource.localizeWithParameter("Unable to remove action _", userCapabilityItemVO));

					}
				}
			});
		}
	}

	void discardChanges()
	{
		getControlledView().getUserCapabilitiesTable().discard();
	}
}
