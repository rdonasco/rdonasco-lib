/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 17-Apr-2013
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
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.security.common.views.TwoColumnFlexibleRightColumnViewLayout;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class UserViewLayoutController implements
		ViewController<TwoColumnFlexibleRightColumnViewLayout>
{

	private static final long serialVersionUID = 1L;
	@Inject
	private TwoColumnFlexibleRightColumnViewLayout userViewLayout;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	@Inject
	private UserListPanelViewController userListPanelController;
	@Inject
	private UserEditorViewController userEditorViewController;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			userViewLayout.initWidget();
			userViewLayout.setLeftPanelContent(userListPanelController.getControlledView());
			userViewLayout.setCenterPanelContent(userEditorViewController.getControlledView().getForm());
			userListPanelController.getUserListTable().addListener(new Property.ValueChangeListener()
			{
				@Override
				public void valueChange(Property.ValueChangeEvent event)
				{
					Table tableSource = userListPanelController.getUserListTable();
					userEditorViewController.setItemDataSource(tableSource.getItem(tableSource.getValue()));
				}
			});
		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	@Override
	public TwoColumnFlexibleRightColumnViewLayout getControlledView()
	{
		return userViewLayout;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method refreshView
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
