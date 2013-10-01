/*
 * Copyright 2012 Roy F. Donasco.
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

package com.rdonasco.config.view;

import com.vaadin.Application;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.view.NotificationFactory;

/**
 *
 * @author Roy F. Donasco
 */
public class ConfigDataViewController implements ViewController<ConfigDataView>
{
	private static final long serialVersionUID = 1L;

	private Application application;
	@Inject
	public void setApplication(Application application)
	{
		this.application = application;
	}
	
	@Inject
	private ConfigDataView configDataView;
	
	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			configDataView.initWidget();
		}
		catch (WidgetInitalizeException ex)
		{
			Logger.getLogger(ConfigDataViewController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
			application.getMainWindow().showNotification(NotificationFactory.createHumanErrorNotification(ex.getMessage()));
		}
	}

	@Override
	public ConfigDataView getControlledView()
	{
		return configDataView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		try
		{
			configDataView.getConfigDataContainer().refreshData();
		}
		catch(Exception e)
		{
			throw new WidgetException(e);
		}
	}

}
