/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 24-Apr-2013
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
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.security.user.views.AvailableCapabilitiesView;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class AvailableCapabilitiesViewController implements
		ViewController<AvailableCapabilitiesView>
{

	private static final Logger LOG = Logger.getLogger(AvailableCapabilitiesViewController.class.getName());
	private static final long serialVersionUID = 1L;
	@Inject
	private AvailableCapabilitiesView availableCapabilitiesView;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			availableCapabilitiesView.initWidget();
		}
		catch (WidgetException e)
		{
			exceptionPopupProvider.popUpErrorException(e);
		}
	}

	@Override
	public AvailableCapabilitiesView getControlledView()
	{
		return availableCapabilitiesView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method refreshView
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
