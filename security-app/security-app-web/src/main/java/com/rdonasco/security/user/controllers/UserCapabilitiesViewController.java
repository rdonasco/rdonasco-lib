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
import com.rdonasco.security.authentication.services.SessionSecurityChecker;
import com.rdonasco.security.capability.utils.IconHelper;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.rdonasco.security.common.utils.ActionConstants;
import com.rdonasco.security.common.views.ListItemIconCellStyleGenerator;
import com.rdonasco.security.user.utils.UserConstants;
import com.rdonasco.security.user.views.UserCapabilitiesView;
import com.vaadin.data.util.BeanItemContainer;
import com.rdonasco.security.user.vo.UserCapabilityItemVO;
import com.rdonasco.security.user.vo.UserCapabilityItemVOBuilder;
import com.rdonasco.security.user.vo.UserSecurityProfileItemVO;
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserCapabilityVOBuilder;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.Embedded;
import java.util.ArrayList;
import java.util.List;
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

	private static final String CONSTANT_ICON = "icon";

	private static final String COLUMN_CAPABILITY_TITLE = "capability.title";

	@Inject
	private UserCapabilitiesView userCapabilitiesView;

	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@Inject
	private ApplicationPopupProvider popupProvider;

	@Inject
	private SessionSecurityChecker sessionSecurityChecker;

	private BeanItemContainer<UserCapabilityItemVO> userCapabilityItemContainer = new BeanItemContainer(UserCapabilityItemVO.class);

	private BeanItem<UserSecurityProfileItemVO> currentProfile;

	private DropHandler userCapabilitiesDropHandler;

	private AcceptCriterion validDraggedObjectSource = AcceptAll.get();

	private boolean editEnabled;

	private final String[] editableColumns = new String[]
	{
		CONSTANT_ICON, COLUMN_CAPABILITY_TITLE
	};

	private final String[] readOnlyColumns = new String[]
	{
		COLUMN_CAPABILITY_TITLE
	};

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			userCapabilitiesView.initWidget();
			configureUserCapabilitiesTable();
			disableEditing();
		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}

	}

	public void setCurrentProfile(BeanItem<UserSecurityProfileItemVO> profile)
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
		userCapabilityItemContainer.addNestedContainerProperty(COLUMN_CAPABILITY_TITLE);
		getControlledView().getUserCapabilitiesTable().setCellStyleGenerator(new ListItemIconCellStyleGenerator());
		getControlledView().getUserCapabilitiesTable().setVisibleColumns(editableColumns);
		getControlledView().getUserCapabilitiesTable().setColumnHeaders(new String[]
		{
			"", I18NResource.localize("Title")
		});
		userCapabilitiesDropHandler = new DropHandler()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void drop(DragAndDropEvent dropEvent)
			{
				try
				{
					final DataBoundTransferable transferredData = (DataBoundTransferable) dropEvent.getTransferable();
					if (null != transferredData && transferredData.getItemId() instanceof CapabilityItemVO)
					{
						sessionSecurityChecker.checkCapabilityTo(ActionConstants.ADD, UserConstants.RESOURCE_USER_CAPABILITIES);
						LOG.log(Level.FINE, "drop allowed at user capability panel");
						final CapabilityItemVO droppedCapabilityItemVO = (CapabilityItemVO) transferredData.getItemId();
						final UserCapabilityVO newUserCapabilityVO = new UserCapabilityVOBuilder()
								.setCapability(droppedCapabilityItemVO.getCapabilityVO())
								.setUserProfile(currentProfile.getBean().getUserSecurityProfileVO())
								.createUserCapabilityVO();
						userCapabilityItemContainer.addItem(prepareUserCapabilityItemVO(newUserCapabilityVO));
					}
					else
					{
						LOG.log(Level.FINE, "invalid data dropped in user capability panel");
					}
				}
				catch (Exception e)
				{
					exceptionPopupProvider.popUpErrorException(e);
				}
			}

			@Override
			public AcceptCriterion getAcceptCriterion()
			{
				return AcceptAll.get();
			}
		};
		getControlledView().getUserCapabilitiesTable().setDropHandler(userCapabilitiesDropHandler);
	}

	public boolean isEditEnabled()
	{
		return editEnabled;
	}

	public void enableEditing()
	{
		editEnabled = true;
		getControlledView().getUserCapabilitiesTable().setVisibleColumns(editableColumns);
		getControlledView().setReadOnly(false);
		getControlledView().getUserCapabilitiesTable().setDropHandler(userCapabilitiesDropHandler);
	}

	public void disableEditing()
	{
		editEnabled = false;
		getControlledView().getUserCapabilitiesTable().setVisibleColumns(readOnlyColumns);
		getControlledView().setReadOnly(true);
		getControlledView().getUserCapabilitiesTable().setDropHandler(null);
	}

	public AcceptCriterion getValidDraggedObjectSource()
	{
		return validDraggedObjectSource;
	}

	public void setValidDraggedObjectSource(
			AcceptCriterion validDraggedObjectSource)
	{
		this.validDraggedObjectSource = validDraggedObjectSource;
	}

	private void populateItemContainer()
	{
		userCapabilityItemContainer.removeAllItems();
		getControlledView().getUserCapabilitiesTable().setSelectable(true);
		for (UserCapabilityVO userCapability : currentProfile.getBean().getCapabilities())
		{
			UserCapabilityItemVO userCapabilityItemVO = prepareUserCapabilityItemVO(userCapability);
			userCapabilityItemContainer.addItem(userCapabilityItemVO);
		}
	}

	void discardChanges()
	{
		getControlledView().getUserCapabilitiesTable().discard();
	}

	public void commit()
	{
		List<UserCapabilityVO> editedCapabilities = new ArrayList<UserCapabilityVO>();
		for (UserCapabilityItemVO capabilityItem : userCapabilityItemContainer.getItemIds())
		{
			editedCapabilities.add(capabilityItem.getUserCapabilityVO());
		}
		currentProfile.getItemProperty("capabilities").setValue(editedCapabilities);
	}

	private UserCapabilityItemVO prepareUserCapabilityItemVO(
			UserCapabilityVO userCapability)
	{
		Embedded icon = IconHelper.createDeleteIcon("Remove capability");
		final UserCapabilityItemVO userCapabilityItemVO = new UserCapabilityItemVOBuilder()
				.setIcon(icon)
				.setUserCapabilityVO(userCapability)
				.createUserCapabilityItemVO();
		icon.addListener(new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void click(MouseEvents.ClickEvent event)
			{
				try
				{
					sessionSecurityChecker.checkCapabilityTo(ActionConstants.DELETE, UserConstants.RESOURCE_USER_CAPABILITIES);
					if (!getControlledView().isReadOnly() && !userCapabilityItemContainer.removeItem(userCapabilityItemVO))
					{
						popupProvider.popUpError(I18NResource.localizeWithParameter("Unable to remove capability _", userCapabilityItemVO));

					}
				}
				catch (Exception e)
				{
					exceptionPopupProvider.popUpErrorException(e);
				}
			}
		});
		return userCapabilityItemVO;
	}
}
