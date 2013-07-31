/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 31-Jul-2013
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

package com.rdonasco.common.vaadin.controller.utils;

import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.DataEditorViewController;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;

/**
 *
 * @author Roy F. Donasco
 */
public final class DataEditorButtonBehaviorBuilder
{
	private DataEditorViewController dataEditorViewController;
	private Button editButton;
	private Button saveButton;
	private Button cancelButton;

	public DataEditorButtonBehaviorBuilder setDataEditorViewController(
			DataEditorViewController controller)
	{
		this.dataEditorViewController = controller;
		return this;
	}

	public DataEditorButtonBehaviorBuilder setEditButton(Button editButton)
	{
		this.editButton = editButton;
		return this;
	}

	public DataEditorButtonBehaviorBuilder setSaveButton(Button saveButton)
	{
		this.saveButton = saveButton;
		return this;
	}

	public DataEditorButtonBehaviorBuilder setCancelButton(Button cancelButton)
	{
		this.cancelButton = cancelButton;
		return this;
	}

	public void build()
	{
		cancelButton.addListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				dataEditorViewController.discardChanges();
				dataEditorViewController.changeViewToDisplayMode();
			}
		});
		editButton.addListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				dataEditorViewController.changeViewToEditMode();
			}
		});
		saveButton.addListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				dataEditorViewController.saveChanges();
			}
		});

		// configure shortcut listeners
		int[] keyModifiers = new int[]
		{
			ShortcutAction.ModifierKey.CTRL
		};
		editButton.setDescription(I18NResource.localize("edit shortcut key"));
		editButton.addShortcutListener(
				new ShortcutListener(null,
				ShortcutAction.KeyCode.E, keyModifiers)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target)
			{
				dataEditorViewController.changeViewToEditMode();
			}
		});
		saveButton.setDescription(I18NResource.localize("save shortcut key"));
		saveButton.addShortcutListener(
				new ShortcutListener(null,
				ShortcutAction.KeyCode.S, keyModifiers)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target)
			{
				dataEditorViewController.saveChanges();
			}
		});
		cancelButton.setDescription(I18NResource.localize("cancel shortcut key"));
		cancelButton.addShortcutListener(
				new ShortcutListener(null,
				ShortcutAction.KeyCode.ESCAPE, null)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target)
			{
				dataEditorViewController.discardChanges();
			}
		});
	}
}
