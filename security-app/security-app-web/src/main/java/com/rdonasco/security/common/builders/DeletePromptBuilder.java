/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 18-Apr-2013
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
package com.rdonasco.security.common.builders;

import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.security.i18n.MessageKeys;
import com.vaadin.ui.Window;
import de.steinwedel.vaadin.MessageBox;

/**
 *
 * @author Roy F. Donasco
 */
public class DeletePromptBuilder
{
	private Window parentWindow;
	private String deletePromptMessage;

	public DeletePromptBuilder setParentWindow(Window parentWindow)
	{
		this.parentWindow = parentWindow;
		return this;
	}

	public MessageBox createDeletePrompt()
	{
		MessageBox messageBox = new MessageBox(parentWindow,
				I18NResource.localize(MessageKeys.ARE_YOU_SURE),
				MessageBox.Icon.QUESTION, preparePromptMessage(),
				new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, I18NResource.localize(MessageKeys.YES)),
				new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, I18NResource.localize(MessageKeys.NO)));
		return messageBox;
	}

	public DeletePromptBuilder setDeletePromptMessage(String deletePromptMessage)
	{
		this.deletePromptMessage = deletePromptMessage;
		return this;
	}

	private String preparePromptMessage()
	{
		String promptMessage;
		if (null == deletePromptMessage)
		{
			promptMessage = I18NResource.localize(MessageKeys.DO_YOU_REALLY_WANT_TO_DELETE_THIS);
		}
		else
		{
			promptMessage = deletePromptMessage;
		}
		return promptMessage;
	}
}
