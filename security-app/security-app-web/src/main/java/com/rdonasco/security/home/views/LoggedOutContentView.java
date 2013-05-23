/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 22-May-2013
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

package com.rdonasco.security.home.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Roy F. Donasco
 */
public class LoggedOutContentView extends VerticalLayout implements
		ContentView
{
	private static final long serialVersionUID = 1L;

	private LoginForm logonForm = new LoginForm();

	public LoginForm getLogonForm()
	{
		return logonForm;
	}

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		addComponent(logonForm);
		setComponentAlignment(logonForm, Alignment.MIDDLE_CENTER);
		setMargin(true, true, true, true);
		setSpacing(true);
		logonForm.setSizeUndefined();
		setSizeFull();
	}
}
