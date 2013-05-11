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
import com.rdonasco.common.vaadin.view.ControlledView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Roy F. Donasco
 */
public class RoleListPanelView extends VerticalLayout implements
		ControlledView
{
	private static final long serialVersionUID = 1L;

	private Label dummyContent = new Label("dummy list panel content");

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		addComponent(dummyContent);
	}
}
