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

import com.rdonasco.common.i18.I18NResource;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Roy F. Donasco
 */
@WebServlet(name = "ConfigServlet", urlPatterns =
{
	"/VAADIN/*"
},loadOnStartup = 1,asyncSupported = true)
public class ConfigServlet extends AbstractApplicationServlet
{
	private static final long serialVersionUID = 1L;
	
	static
	{
		I18NResource.setBundle(java.util.ResourceBundle.getBundle("com/rdonasco/config/i18n/i18nResource"));
	}
	
	@Inject
	private ConfigApplication application;
	
	@Override
	protected Application getNewApplication(HttpServletRequest request) throws
			ServletException
	{
		return application;
	}

	@Override
	protected Class<? extends Application> getApplicationClass() throws
			ClassNotFoundException
	{
		return ConfigApplication.class;
	}


}
