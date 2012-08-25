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
package net.baligya.common.view;

import com.vaadin.ui.Window.Notification;
import com.rdonasco.common.i18.I18NResource;

/**
 *
 * @author Roy F. Donasco
 */
public class NotificationFactory
{
    public static Notification createFromException(int type,Exception e)
    {        
        return new Notification(createCaption(type), e.getLocalizedMessage(), type);
    }
    
    public static Notification createHumanNotification(String caption, String message)
    {
        return new Notification(caption, message,Notification.TYPE_HUMANIZED_MESSAGE);
    }    
	
    public static Notification createHumanNotification(String message)
    {
        return new Notification(I18NResource.localize(createCaption(-1)), message,Notification.TYPE_HUMANIZED_MESSAGE);
    }    	
    
    public static Notification createHumanErrorNotification(String caption, String message)
    {
        return new Notification(caption, message,Notification.TYPE_ERROR_MESSAGE);
    }    
	
    public static Notification createHumanErrorNotification(String message)
    {
        return new Notification(I18NResource.localize(createCaption(Notification.TYPE_ERROR_MESSAGE)), message,Notification.TYPE_ERROR_MESSAGE);
    } 	
    
    public static Notification createWarningNotification(String caption, String message)
    {
        return new Notification(caption, message,Notification.TYPE_WARNING_MESSAGE);
    }
	
    public static Notification createWarningNotification(String message)
    {
        return new Notification(I18NResource.localize(createCaption(Notification.TYPE_WARNING_MESSAGE)), message,Notification.TYPE_WARNING_MESSAGE);
    }
	
    
    public static Notification createTrayNotification(String caption, String message)
    {
        return new Notification(caption, message,Notification.TYPE_TRAY_NOTIFICATION);
    }
	
    public static Notification createTrayNotification(String message)
    {
        return new Notification(I18NResource.localize(createCaption(-1)), message,Notification.TYPE_TRAY_NOTIFICATION);
    }	

	private static String createCaption(int type)
	{
		String caption;
		switch (type)
		{
			case Notification.TYPE_ERROR_MESSAGE : caption = "Error";
				break;
			case Notification.TYPE_WARNING_MESSAGE : caption = "Warning";
				break;
			default: caption = "Note";
		}
		return caption;
	}
}
