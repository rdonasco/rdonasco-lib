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
import com.rdonasco.security.role.vo.RoleItemVO;
import com.rdonasco.security.user.utils.UserConstants;
import com.rdonasco.security.user.views.UserRolesView;
import com.vaadin.data.util.BeanItemContainer;
import com.rdonasco.security.user.vo.UserRoleItemVO;
import com.rdonasco.security.user.vo.UserRoleItemVOBuilder;
import com.rdonasco.security.user.vo.UserSecurityProfileItemVO;
import com.rdonasco.security.vo.UserRoleVO;
import com.rdonasco.security.vo.UserRoleVOBuilder;
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
public class UserRolesViewController implements
		ViewController<UserRolesView>
{

	private static final Logger LOG = Logger.getLogger(UserRolesViewController.class.getName());

	private static final long serialVersionUID = 1L;

	private static final String CONSTANT_ICON = "icon";

	private static final String COLUMN_ROLE_NAME = "role.name";

	@Inject
	private UserRolesView userRolesView;

	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@Inject
	private ApplicationPopupProvider popupProvider;

	@Inject
	private SessionSecurityChecker sessionSecurityChecker;

	private BeanItemContainer<UserRoleItemVO> userRoleItemContainer = new BeanItemContainer(UserRoleItemVO.class);

	private BeanItem<UserSecurityProfileItemVO> currentProfile;

	private DropHandler userRolesDropHandler;

	private AcceptCriterion validDraggedObjectSource = AcceptAll.get();

	private boolean editEnabled;

	private final String[] editableColumns = new String[]
	{
		CONSTANT_ICON, COLUMN_ROLE_NAME
	};

	private final String[] readOnlyColumns = new String[]
	{
		COLUMN_ROLE_NAME
	};

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			userRolesView.initWidget();
			configureUserRolesTable();
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
	public UserRolesView getControlledView()
	{
		return userRolesView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		populateItemContainer();
	}

	private void configureUserRolesTable()
	{
		getControlledView().getUserRolesTable().setContainerDataSource(userRoleItemContainer);
		userRoleItemContainer.addNestedContainerProperty(COLUMN_ROLE_NAME);
		getControlledView().getUserRolesTable().setVisibleColumns(editableColumns);
		getControlledView().getUserRolesTable().setColumnHeaders(new String[]
		{
			"", I18NResource.localize("Name")
		});
		getControlledView().getUserRolesTable().setCellStyleGenerator(new ListItemIconCellStyleGenerator());
		userRolesDropHandler = new DropHandler()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void drop(DragAndDropEvent dropEvent)
			{
				try
				{
					// TODO: Enable adding multiple dragged items
					final DataBoundTransferable transferredData = (DataBoundTransferable) dropEvent.getTransferable();
					if (null != transferredData && transferredData.getItemId() instanceof RoleItemVO)
					{
						sessionSecurityChecker.checkCapabilityTo(ActionConstants.ADD, UserConstants.RESOURCE_USER_ROLES);
						LOG.log(Level.FINE, "drop allowed at user capability panel");
						final RoleItemVO droppedCapabilityItemVO = (RoleItemVO) transferredData.getItemId();
						Embedded icon = IconHelper.createDeleteIcon(I18NResource.localize("Remove Role"));
						final UserRoleItemVO newUserRoleItemVO = new UserRoleItemVOBuilder()
								.setIcon(icon)
								.setUserRoleVO(new UserRoleVOBuilder()
								.setRole(droppedCapabilityItemVO.getRoleVO())
								.setUserProfile(currentProfile.getBean().getUserSecurityProfileVO())
								.createUserRoleVO())
								.createUserRoleItemVO();
						BeanItem<UserRoleItemVO> addedItem = userRoleItemContainer.addItem(newUserRoleItemVO);
						LOG.log(Level.FINE, "addedItem = {0}", addedItem);
						icon.addListener(prepareDeleteRoleClickListener(newUserRoleItemVO));
					}
					else
					{
						LOG.log(Level.FINE, "invalid data dropped in user role panel");
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
		getControlledView().getUserRolesTable().setDropHandler(userRolesDropHandler);
	}

	public boolean isEditEnabled()
	{
		return editEnabled;
	}

	public void enableEditing()
	{
		editEnabled = true;
		getControlledView().getUserRolesTable().setVisibleColumns(editableColumns);
		getControlledView().setReadOnly(false);
		getControlledView().getUserRolesTable().setDropHandler(userRolesDropHandler);
	}

	public void disableEditing()
	{
		editEnabled = false;
		getControlledView().getUserRolesTable().setVisibleColumns(readOnlyColumns);
		getControlledView().setReadOnly(true);
		getControlledView().getUserRolesTable().setDropHandler(null);
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
		userRoleItemContainer.removeAllItems();
		getControlledView().getUserRolesTable().setSelectable(true);
		for (UserRoleVO userRole : currentProfile.getBean().getRoles())
		{
			Embedded icon = IconHelper.createDeleteIcon("Remove role");
			final UserRoleItemVO userRoleItemVO = new UserRoleItemVOBuilder()
					.setIcon(icon)
					.setUserRoleVO(userRole)
					.createUserRoleItemVO();
			userRoleItemContainer.addItem(userRoleItemVO);
			icon.addListener(prepareDeleteRoleClickListener(userRoleItemVO));
		}
	}

	void discardChanges()
	{
		getControlledView().getUserRolesTable().discard();
	}

	public void commit()
	{
		List<UserRoleVO> editedCapabilities = new ArrayList<UserRoleVO>();
		for (UserRoleItemVO roleItem : userRoleItemContainer.getItemIds())
		{
			editedCapabilities.add(roleItem.getUserRoleVO());
		}
		currentProfile.getItemProperty("roles").setValue(editedCapabilities);
	}

	private MouseEvents.ClickListener prepareDeleteRoleClickListener(
			final UserRoleItemVO userRoleItemVO)
	{
		return new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void click(MouseEvents.ClickEvent event)
			{
				try
				{
					sessionSecurityChecker.checkCapabilityTo(ActionConstants.DELETE, UserConstants.RESOURCE_USER_ROLES);
					if (!getControlledView().isReadOnly() && !userRoleItemContainer.removeItem(userRoleItemVO))
					{
						popupProvider.popUpError(I18NResource.localizeWithParameter("Unable to remove role _", userRoleItemVO));

					}
				}
				catch (Exception e)
				{
					exceptionPopupProvider.popUpErrorException(e);
				}
			}
		};
	}
}
