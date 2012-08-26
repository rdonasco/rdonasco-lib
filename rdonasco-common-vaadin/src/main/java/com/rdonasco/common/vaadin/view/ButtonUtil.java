/*
 * Copyright 2011 Roy F. Donasco.
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
package com.rdonasco.common.vaadin.view;

import com.vaadin.ui.Button;

/**
 *
 * @author Roy F. Donasco
 */
public class ButtonUtil
{

	public static void hideButtons(Button... buttons)
	{
		for (Button button : buttons)
		{
			if (button != null)
			{
				button.setVisible(false);
			}
		}
	}

	public static void showButtons(Button... buttons)
	{
		for (Button button : buttons)
		{
			button.setVisible(true);
			if (!button.isEnabled())
			{
				enableButtons(button);
			}
		}
	}

	public static void enableButtons(Button... buttons)
	{
		for (Button button : buttons)
		{
			button.setEnabled(true);
		}
	}

	public static void disableButtons(Button... buttons)
	{
		for (Button button : buttons)
		{
			button.setEnabled(false);
		}
	}

	public static void setupAllButtonStyle(String style,Button... buttons)
	{
		String[] styles = {style};
		setupAllButtonStyles(styles,buttons);
				
	}
	public static void setupAllButtonStyles(String[] styles,Button... buttons)
	{
		for (Button button : buttons)
		{
			for(String style : styles)
			{
				button.addStyleName(style);
			}
		}
	}
}
