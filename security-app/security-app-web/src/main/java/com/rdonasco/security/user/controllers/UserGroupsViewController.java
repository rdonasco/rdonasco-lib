/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 15-May-2013
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
import com.rdonasco.security.common.utils.ActionConstants;
import com.rdonasco.security.common.views.ListItemIconCellStyleGenerator;
import com.rdonasco.security.group.vo.GroupItemVO;
import com.rdonasco.security.user.utils.UserConstants;
import com.rdonasco.security.user.views.UserGroupsView;
import com.vaadin.data.util.BeanItemContainer;
import com.rdonasco.security.user.vo.UserGroupItemVO;
import com.rdonasco.security.user.vo.UserGroupItemVOBuilder;
import com.rdonasco.security.user.vo.UserSecurityProfileItemVO;
import com.rdonasco.security.vo.UserGroupVO;
import com.rdonasco.security.vo.UserGroupVOBuilder;
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
public class UserGroupsViewController implements
		ViewController<UserGroupsView>
{

	private static final Logger LOG = Logger.getLogger(UserGroupsViewController.class.getName());

	private static final long serialVersionUID = 1L;

	private static final String CONSTANT_ICON = "icon";

	private static final String COLUMN_GROUP_NAME = "group.name";

	@Inject
	private UserGroupsView userGroupsView;

	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@Inject
	private ApplicationPopupProvider popupProvider;

	@Inject
	private SessionSecurityChecker sessionSecurityChecker;

	private BeanItemContainer<UserGroupItemVO> userGroupItemContainer = new BeanItemContainer(UserGroupItemVO.class);

	private BeanItem<UserSecurityProfileItemVO> currentProfile;

	private DropHandler userGroupsDropHandler;

	private AcceptCriterion validDraggedObjectSource = AcceptAll.get();

	private boolean editEnabled;

	private final String[] editableColumns = new String[]
	{
		CONSTANT_ICON, COLUMN_GROUP_NAME
	};

	private final String[] readOnlyColumns = new String[]
	{
		COLUMN_GROUP_NAME
	};

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			userGroupsView.initWidget();
			configureUserGroupsTable();
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
	public UserGroupsView getControlledView()
	{
		return userGroupsView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		populateItemContainer();
	}

	private void configureUserGroupsTable()
	{
		getControlledView().getUserGroupsTable().setContainerDataSource(userGroupItemContainer);
		userGroupItemContainer.addNestedContainerProperty(COLUMN_GROUP_NAME);
		getControlledView().getUserGroupsTable().setVisibleColumns(editableColumns);
		getControlledView().getUserGroupsTable().setColumnHeaders(new String[]
		{
			"", I18NResource.localize("Name")
		});
		getControlledView().getUserGroupsTable().setCellStyleGenerator(new ListItemIconCellStyleGenerator());
		userGroupsDropHandler = new DropHandler()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void drop(DragAndDropEvent dropEvent)
			{
				try
				{
					final DataBoundTransferable transferredData = (DataBoundTransferable) dropEvent.getTransferable();
					if (null != transferredData && transferredData.getItemId() instanceof GroupItemVO)
					{
						sessionSecurityChecker.checkCapabilityTo(ActionConstants.ADD, UserConstants.RESOURCE_USER_GROUPS);
						LOG.log(Level.FINE, "drop allowed at user group panel");
						final GroupItemVO droppedCapabilityItemVO = (GroupItemVO) transferredData.getItemId();
						final UserGroupVO newUserGroupVO = new UserGroupVOBuilder()
								.setGroup(droppedCapabilityItemVO.getSecurityGroupVO())
								.setUserProfile(currentProfile.getBean().getUserSecurityProfileVO())
								.createUserGroupVO();
						userGroupItemContainer.addItem(prepareUserGroupItemVO(newUserGroupVO));
					}
					else
					{
						LOG.log(Level.FINE, "invalid data dropped in user group panel");
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
		getControlledView().getUserGroupsTable().setDropHandler(userGroupsDropHandler);
	}

	public boolean isEditEnabled()
	{
		return editEnabled;
	}

	public void enableEditing()
	{
		editEnabled = true;
		getControlledView().getUserGroupsTable().setVisibleColumns(editableColumns);
		getControlledView().setReadOnly(false);
		getControlledView().getUserGroupsTable().setDropHandler(userGroupsDropHandler);
	}

	public void disableEditing()
	{
		editEnabled = false;
		getControlledView().getUserGroupsTable().setVisibleColumns(readOnlyColumns);
		getControlledView().setReadOnly(true);
		getControlledView().getUserGroupsTable().setDropHandler(null);
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
		userGroupItemContainer.removeAllItems();
		getControlledView().getUserGroupsTable().setSelectable(true);
		for (UserGroupVO userGroup : currentProfile.getBean().getGroups())
		{
			UserGroupItemVO userGroupItemVO = prepareUserGroupItemVO(userGroup);
			userGroupItemContainer.addItem(userGroupItemVO);
		}
	}

	void discardChanges()
	{
		getControlledView().getUserGroupsTable().discard();
	}

	public void commit()
	{
		List<UserGroupVO> editedCapabilities = new ArrayList<UserGroupVO>();
		for (UserGroupItemVO roleItem : userGroupItemContainer.getItemIds())
		{
			editedCapabilities.add(roleItem.getUserGroupVO());
		}
		currentProfile.getItemProperty("groups").setValue(editedCapabilities);
	}

	private UserGroupItemVO prepareUserGroupItemVO(UserGroupVO userGroup)
	{
		Embedded icon = IconHelper.createDeleteIcon("Remove group");
		final UserGroupItemVO userRoleItemVO = new UserGroupItemVOBuilder()
				.setIcon(icon)
				.setUserGroupVO(userGroup)
				.createUserGroupItemVO();
		icon.addListener(new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void click(MouseEvents.ClickEvent event)
			{
				try
				{
					sessionSecurityChecker.checkCapabilityTo(ActionConstants.DELETE, UserConstants.RESOURCE_USER_GROUPS);
					if (!getControlledView().isReadOnly() && !userGroupItemContainer.removeItem(userRoleItemVO))
					{
						popupProvider.popUpError(I18NResource.localizeWithParameter("Unable to remove group _", userRoleItemVO));

					}
				}
				catch (Exception e)
				{
					exceptionPopupProvider.popUpErrorException(e);
				}
			}
		});
		return userRoleItemVO;
	}
}
