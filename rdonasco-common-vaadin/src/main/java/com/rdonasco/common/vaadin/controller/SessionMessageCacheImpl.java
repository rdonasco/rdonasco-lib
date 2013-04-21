/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 21-Apr-2013
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

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@SessionScoped
public class SessionMessageCacheImpl implements SessionMessageCache,
		Serializable
{
	private static final Logger LOG = Logger.getLogger(SessionMessageCacheImpl.class.getName());

	private static final long serialVersionUID = 1L;
	private List<String> messageQueue = Collections.synchronizedList(new LinkedList<String>());
	@Inject
	private Instance<ApplicationPopupProvider> popupProviderInstances;
	private ApplicationPopupProvider popupProvider;

	public ApplicationPopupProvider getPopupProvider()
	{
		if (popupProvider == null && null != popupProviderInstances)
		{
			popupProvider = popupProviderInstances.get();
		}
		return popupProvider;
	}

	@Override
	public void putMessage(String message)
	{
		messageQueue.add(message);
	}

	@Override
	public void showMessages()
	{
		LOG.log(Level.INFO, "messageQueue.size {0}", messageQueue.size());
		if (getPopupProvider() != null && messageQueue.size() > 0)
		{
			StringBuilder message = new StringBuilder();
			int i = 0;
			for (Iterator<String> iter = messageQueue.iterator(); iter.hasNext();)
			{
				String messageElement = iter.next();
				if ((i++) > 1)
				{
					message.append("<br/>");
				}
				message.append(messageElement);
				iter.remove();
			}
			getPopupProvider().popUpInfo(message.toString());
		}

	}
}
