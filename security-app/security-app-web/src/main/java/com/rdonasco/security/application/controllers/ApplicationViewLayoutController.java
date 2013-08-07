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
package com.rdonasco.security.application.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.common.vaadin.view.layouts.ThreeColumnFlexibleCenterViewLayout;
import com.rdonasco.security.authorization.interceptors.Secured;
import com.rdonasco.security.authorization.interceptors.SecuredCapability;
import com.rdonasco.common.vaadin.view.layouts.TwoColumnFlexibleRightColumnViewLayout;
import com.rdonasco.security.application.utils.ApplicationConstants;
import com.rdonasco.security.common.utils.ActionConstants;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Table;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class ApplicationViewLayoutController implements
		ViewController<ThreeColumnFlexibleCenterViewLayout>
{

	private static final long serialVersionUID = 1L;
	@Inject
	private ThreeColumnFlexibleCenterViewLayout viewLayout;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	@Inject
	private ApplicationListPanelViewController dataListPanelController;
	@Inject
	private ApplicationEditorViewController applicationEditorViewController;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			viewLayout.initWidget();
			viewLayout.setCenterPanelContent(applicationEditorViewController.getControlledView().getForm());
			dataListPanelController.getRecordListTable().addListener(new Property.ValueChangeListener()
			{
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange(Property.ValueChangeEvent event)
				{
					Table tableSource = dataListPanelController.getRecordListTable();
					applicationEditorViewController.setCurrentItem((BeanItem) tableSource.getItem(tableSource.getValue()));
				}
			});
			applicationEditorViewController.setDataManagerContainer(dataListPanelController.getItemTableContainer());
			viewLayout.setLeftPanelContent(dataListPanelController.getControlledView());
			applicationEditorViewController.setApplicationListPanelViewController(dataListPanelController);
			viewLayout.addRightPanelContent(applicationEditorViewController.getHostEditorViewController().getControlledView());
		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	@Secured
	@SecuredCapability(action = ActionConstants.VIEW, resource = ApplicationConstants.RESOURCE_APPLICATION, useExceptionHandler = false)
	@Override
	public ThreeColumnFlexibleCenterViewLayout getControlledView()
	{
		return viewLayout;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method refreshView
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
