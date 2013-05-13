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

package com.rdonasco.security.role.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Roy F. Donasco
 */
public class RoleEditorView extends VerticalLayout implements ControlledView
{
	private static final long serialVersionUID = 1L;

	private Panel roleDetailPanel = new Panel();

	private Panel roleCapabilitiesPanel = new Panel();
	public RoleEditorView()
	{

	}

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		roleDetailPanel.setCaption(I18NResource.localize("Role Editor"));
		roleDetailPanel.setStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
		roleCapabilitiesPanel.setCaption(I18NResource.localize("Capabilities of this role"));
		roleCapabilitiesPanel.setStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
		setSpacing(true);

		addComponent(roleDetailPanel);
		addComponent(roleCapabilitiesPanel);
	}
}
