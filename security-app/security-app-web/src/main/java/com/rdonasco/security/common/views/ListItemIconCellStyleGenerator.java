/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 20-May-2013
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
package com.rdonasco.security.common.views;

import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.vaadin.ui.Table;

/**
 *
 * @author Roy F. Donasco
 */
public class ListItemIconCellStyleGenerator implements Table.CellStyleGenerator
{

	private static final long serialVersionUID = 1L;

	@Override
	public String getStyle(Object itemId, Object propertyId)
	{
		String style = null;
		if ("icon".equals(propertyId))
		{
			style = SecurityDefaultTheme.CSS_ICON_IN_A_CELL;
		}
		else if (propertyId != null)
		{
			style = SecurityDefaultTheme.CSS_FULL_WIDTH;
		}
		return style;
	}
}
