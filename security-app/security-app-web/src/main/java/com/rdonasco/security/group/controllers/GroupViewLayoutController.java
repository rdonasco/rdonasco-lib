/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 17-May-2013
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
package com.rdonasco.security.group.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.security.authorization.interceptors.Secured;
import com.rdonasco.security.authorization.interceptors.SecuredCapability;
import com.rdonasco.security.common.utils.ActionConstants;
import com.rdonasco.security.common.views.ThreeColumnFlexibleCenterViewLayout;
import com.rdonasco.security.group.utils.GroupConstants;
import com.rdonasco.security.group.vo.GroupItemVO;
import com.rdonasco.security.role.controllers.AvailableRolesViewController;
import com.rdonasco.security.role.controllers.AvailableRolesViewControllerBuilder;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Table;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class GroupViewLayoutController implements
		ViewController<ThreeColumnFlexibleCenterViewLayout>
{

	private static final long serialVersionUID = 1L;
	private static final String RESOURCE_USER_GROUP = "user group";

	private static final String ACTION_VIEW = "view";

	@Inject
	private ThreeColumnFlexibleCenterViewLayout groupViewLayout;

	@Inject
	private GroupListPanelViewController groupListPanelViewController;

	@Inject
	private GroupEditorViewController groupEditorViewController;

	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@Inject
	private AvailableRolesViewControllerBuilder availableRolesViewControllerBuilder;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			groupViewLayout.initWidget();
			groupViewLayout.setLeftPanelContent(groupListPanelViewController.getControlledView());
			groupViewLayout.setCenterPanelContent(groupEditorViewController.getControlledView().getForm());
			groupListPanelViewController.getControlledView().getGroupListTable()
					.addListener(new Property.ValueChangeListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(Property.ValueChangeEvent event)
				{
					Table tableSource = groupListPanelViewController.getControlledView().getGroupListTable();
					BeanItem<GroupItemVO> beanItem = (BeanItem) tableSource.getItem(tableSource.getValue());
					groupEditorViewController.setItemDataSource(beanItem);
				}
			});
			groupEditorViewController.setGroupItemTableContainer(groupListPanelViewController.getGroupItemTableContainer());
			final AvailableRolesViewController availableRolesViewController = availableRolesViewControllerBuilder.build();
			groupViewLayout.addRightPanelContent(availableRolesViewController.getControlledView());
			availableRolesViewController.allowDraggingMultipleRows();

		}
		catch (Exception e)
		{
			exceptionPopupProvider.popUpErrorException(e);
		}
	}

	@Secured
	@SecuredCapability(action = ActionConstants.VIEW, resource = GroupConstants.RESOURCE_GROUPS, useExceptionHandler = false)
	@Override
	public ThreeColumnFlexibleCenterViewLayout getControlledView()
	{
		return groupViewLayout;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
