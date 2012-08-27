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
package com.rdonasco.common.vaadin.view.utils;

import com.rdonasco.common.exceptions.InvalidBuilderParameter;
import com.vaadin.Application;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Embedded;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 *
 * @author Roy F. Donasco
 */
public class EmbeddedResourceBuilder
{
	private byte[] bytes;
	private String caption;
	private String description;
	private Application application;
	private String name;
	private String alternateText;

	public EmbeddedResourceBuilder()
	{
	}

	public EmbeddedResourceBuilder setApplication(Application application)
	{
		this.application = application;
		return this;
	}

	public EmbeddedResourceBuilder setByteSource(byte[] bytes)
	{
		this.bytes = bytes;
		return this;
	}

	public EmbeddedResourceBuilder setCaption(String caption)
	{
		this.caption = caption;
		return this;
	}

	public EmbeddedResourceBuilder setDescription(String description)
	{
		this.description = description;
		return this;
	}
	
	public EmbeddedResourceBuilder setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public EmbeddedResourceBuilder setAlternateText(String alternateText)
	{
		this.alternateText = alternateText;
		return this;
	}

	public Embedded createEmbedded() throws InvalidBuilderParameter
	{		
		if(null == bytes)
		{
			throw new InvalidBuilderParameter("bytes to build not set");
		}
		if(null == application)
		{
			throw new InvalidBuilderParameter("application not set");
		}	
		if(null == name)
		{
			throw new InvalidBuilderParameter("name not set");
		}
		
		StreamResource.StreamSource streamSource = new StreamResource.StreamSource()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public InputStream getStream()
			{
				return new ByteArrayInputStream(bytes);
			}
		};
		StreamResource resource = new StreamResource(streamSource, name, application);
		Embedded embedded = new Embedded(caption,resource);
		embedded.setDescription(description);
		embedded.setAlternateText(alternateText);
		return embedded;
	}
}
