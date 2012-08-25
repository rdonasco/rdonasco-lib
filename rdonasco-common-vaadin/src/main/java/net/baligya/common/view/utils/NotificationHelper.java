/*
 * Copyright 2012 Roy F. Donasco.
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

package net.baligya.common.view.utils;

import com.vaadin.Application;
import javax.inject.Inject;
import net.baligya.common.view.NotificationFactory;

/**
 *
 * @author Roy F. Donasco
 */
public class NotificationHelper 
{
	@Inject 
	private Application application;

	public void showHumanErrorNotification(String message)
	{
		application.getMainWindow().showNotification(
				NotificationFactory.createHumanErrorNotification(message));
	}

	public void showHumanWarningNotification(String message)
	{
		application.getMainWindow().showNotification(
				NotificationFactory.createWarningNotification(message));
	}

	public void showHumanNotification(String message)
	{
		application.getMainWindow().showNotification(
				NotificationFactory.createHumanNotification(message));
	}	
	
	public void showTrayNotification(String message)
	{
		application.getMainWindow().showNotification(
				NotificationFactory.createTrayNotification(message));
		
	}
}
