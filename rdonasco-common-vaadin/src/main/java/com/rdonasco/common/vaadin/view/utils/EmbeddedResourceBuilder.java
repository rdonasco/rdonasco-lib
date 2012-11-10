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
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.ThemeResource;
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
	private String resourceId;
	private Float height;
	private int units = Sizeable.UNITS_PIXELS;
	private Float width;

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
	
	public EmbeddedResourceBuilder setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
		return this;
	}
	
	public EmbeddedResourceBuilder setHeight(float height, int units)
	{
		this.height = height;
		this.units = units;
		return this;
	}

	public Embedded createEmbedded() throws InvalidBuilderParameter
	{
		if (null == application)
		{
			throw new InvalidBuilderParameter("application not set");
		}
		if (null == name)
		{
			throw new InvalidBuilderParameter("name not set");
		}
		Embedded embedded = null;
		if(null != bytes)
		{
			embedded = createUsingStreamSource();
		}
		else if(null != resourceId)
		{
			embedded = createUsingResourceId();
		}
		embedded.setDescription(description);
		embedded.setAlternateText(alternateText);
		if(null != height)
		{
			embedded.setHeight(height, units);
		}
		if(null != width)
		{
			embedded.setWidth(width, units);
		}
		
		return embedded;
	}

	private Embedded createUsingStreamSource()
	{
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
		Embedded embedded = new Embedded(caption, resource);

		return embedded;
	}

	private Embedded createUsingResourceId()
	{
		return new Embedded(caption,new ThemeResource(resourceId));
	}
}
