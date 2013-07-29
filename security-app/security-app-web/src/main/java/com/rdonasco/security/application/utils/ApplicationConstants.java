/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 29-Jul-2013
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

package com.rdonasco.security.application.utils;

import com.rdonasco.common.i18.I18NResource;

/**
 *
 * @author Roy F. Donasco
 */
public final class ApplicationConstants
{
	public static final String[] TABLE_VISIBLE_COLUMNS =
	{
		"name", "token"
	};
	public static final String[] TABLE_VISIBLE_HEADERS =
	{
		"", I18NResource.localize("application name")
	};

	public static final String XPATH_DEFAULT_TOKEN_LENGTH = "/application/token/default/length";

	public static final String RESOURCE_APPLICATIONS = "applications";
}
