/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 11-May-2013
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

package com.rdonasco.security.role.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.common.views.ThreeColumnFlexibleCenterViewLayout;
import com.vaadin.ui.Panel;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class RoleViewLayoutController implements
		ViewController<ThreeColumnFlexibleCenterViewLayout>
{
	private static final long serialVersionUID = 1L;

	@Inject
	private ThreeColumnFlexibleCenterViewLayout roleViewLayout;

	@Inject
	private RoleEditorViewController roleEditorViewController;

	@Inject
	private RoleListPanelViewController roleListPanelViewController;

	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			roleViewLayout.initWidget();
			roleViewLayout.setLeftPanelContent(roleListPanelViewController.getControlledView());
			roleViewLayout.setCenterPanelContent(roleEditorViewController.getControlledView());
			final Panel dummyPanel = new Panel("dummy right panel");
			dummyPanel.setStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
			roleViewLayout.addRightPanelContent(dummyPanel);
		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	@Override
	public ThreeColumnFlexibleCenterViewLayout getControlledView()
	{
		return roleViewLayout;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method refreshView
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
