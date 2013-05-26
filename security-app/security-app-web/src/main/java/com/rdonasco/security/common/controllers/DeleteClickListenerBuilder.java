/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 12-May-2013
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
package com.rdonasco.security.common.controllers;

import com.rdonasco.security.common.builders.DeletePromptBuilder;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.Component;
import de.steinwedel.vaadin.MessageBox;

/**
 *
 * @author Roy F. Donasco
 */
public class DeleteClickListenerBuilder<T>
{

	private DeleteListener<T> deleteListener;

	private Component component;

	private String deletePromptMessage;

	public DeleteClickListenerBuilder<T> setDeleteListener(
			DeleteListener<T> listener)
	{
		this.deleteListener = listener;
		return this;
	}

	public DeleteClickListenerBuilder<T> setComponent(Component component)
	{
		this.component = component;
		return this;
	}

	public DeleteClickListenerBuilder<T> setDeletePromptMessage(
			String deletePrompt)
	{
		this.deletePromptMessage = deletePrompt;
		return this;
	}

	public ClickListenerProvider<T> createClickListenerProvider()
	{
		if (null == component)
		{
			throw new IllegalArgumentException("component must be provided");
		}
		ClickListenerProvider clickListenerProvider = new ClickListenerProvider<T>()
		{
			@Override
			public MouseEvents.ClickListener provideClickListenerFor(
					final T data)
			{
				MouseEvents.ClickListener clickListener = new MouseEvents.ClickListener()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void click(MouseEvents.ClickEvent event)
					{
						new DeletePromptBuilder()
								.setParentWindow(component.getWindow())
								.setDeletePromptMessage(deletePromptMessage)
								.createDeletePrompt()
								.show(new MessageBox.EventListener()
						{
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClicked(
									MessageBox.ButtonType buttonType)
							{
								if (MessageBox.ButtonType.YES.equals(buttonType) && null != deleteListener)
								{
									deleteListener.delete(data);
								}
							}
						});
					}
				};
				return clickListener;
			}
		};

		return clickListenerProvider;
	}

	public interface DeleteListener<T>
	{

		void delete(T itemToDelete);
	}
}
