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
package com.rdonasco.datamanager.controller;

import com.vaadin.Application;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.datamanager.view.DataManagerView;

/**
 *
 * @author Roy F. Donasco
 */
public interface DataManagerViewController extends ViewController<DataManagerView>
{

    /**
	 * Used to create the instance with all handlers configured.
	 * @return fully instantiated DataManagerView with all handlers configured.
	 */
	@Override
	DataManagerView getControlledView();
    /**
	 * Returns an instance of Vaadin Application
	 * @return instance of Vaadin Application
	 */
	Application getApplication();
    
}
