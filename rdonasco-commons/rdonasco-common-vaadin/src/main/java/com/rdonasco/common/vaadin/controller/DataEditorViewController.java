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
package com.rdonasco.common.vaadin.controller;

import com.rdonasco.common.vaadin.view.ControlledView;
import com.vaadin.data.util.BeanItem;

/**
 *
 * @author Roy F. Donasco
 */
public interface DataEditorViewController<T extends ControlledView, D> extends
		ViewController<T>
{
	void setCurrentItem(
			BeanItem<D> currentItem);

	BeanItem<D> getCurrentItem();

	void configureButtonListeners();

	void changeViewToEditMode();

	void changeViewToDisplayMode();

	void discardChanges();

	void saveChanges();

	void configureInitialButtonState();
}
