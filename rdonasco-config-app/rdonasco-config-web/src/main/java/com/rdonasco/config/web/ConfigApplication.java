/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 29-Sep-2013
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
package com.rdonasco.config.web;

import com.rdonasco.config.view.ConfigDataView;
import com.rdonasco.config.view.ConfigDataViewController;
import com.vaadin.Application;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Button;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Roy F. Donasco
 */
@SessionScoped
public class ConfigApplication extends Application
{

	private static final long serialVersionUID = 1L;
	private ConfigDataViewController configDataViewController;

	@Inject
	public void setConfigDataViewController(
			ConfigDataViewController configDataViewController)
	{
		this.configDataViewController = configDataViewController;
	}

	private ConfigDataViewController getConfigDataViewController()
	{
		return configDataViewController;
	}

	@Override
	public void init()
	{
		Window mainWindow = new Window("rdonasco Config");
		setMainWindow(mainWindow);
		final ConfigDataView controlledView = getConfigDataViewController().getControlledView();
//		controlledView.setSizeFull();
		final VerticalLayout mainWindowLayout = (VerticalLayout) mainWindow.getContent();
		Button logoutButton = new Button("logout");
		logoutButton.addListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				logout();
			}
		});
		setTheme("config");
		mainWindowLayout.setMargin(true);
		mainWindowLayout.setSpacing(true);
		mainWindowLayout.addStyleName("mainWindow");
		mainWindowLayout.setSizeFull();
		
		mainWindowLayout.addComponent(logoutButton);
		mainWindowLayout.addComponent(controlledView);
		mainWindowLayout.setExpandRatio(controlledView, 1);				
	}

	public HttpSession getSession()
	{
		WebApplicationContext context = (WebApplicationContext) getContext();
		return context.getHttpSession();
	}

	private void logout()
	{
		close();
		getSession().invalidate();
	}
}
