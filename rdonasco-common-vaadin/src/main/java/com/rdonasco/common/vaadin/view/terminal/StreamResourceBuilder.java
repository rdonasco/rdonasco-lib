/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 10-Apr-2013
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

package com.rdonasco.common.vaadin.view.terminal;

import com.rdonasco.common.exceptions.BuilderException;
import com.vaadin.Application;
import com.vaadin.terminal.StreamResource;
import java.io.InputStream;

/**
 *
 * @author Roy F. Donasco
 */
public class StreamResourceBuilder 
{

	private Class referenceClass;
	private String relativeResourcePath;
	private Application application;

	public StreamResource createStreamResource()
	{
		if (null == application)
		{
			throw new BuilderException("application object is mandatory");
		}
		if (null == referenceClass)
		{
			throw new BuilderException("referenceClass is manadatory");
		}
		if (null == relativeResourcePath)
		{
			throw new BuilderException("relativeResourcePath is mandatory");
		}
		final InputStream inputStream = referenceClass.getResourceAsStream(relativeResourcePath);
				StreamResource.StreamSource streamSource = new StreamResource.StreamSource()
		{
			@Override
			public InputStream getStream()
			{
				return inputStream;
			}
		};
		StreamResource resource = new StreamResource(streamSource, obtainFilename(), application);

		return resource;
	}

	private String obtainFilename()
	{
		int beginIndex = relativeResourcePath.lastIndexOf("/") + 1;
		return relativeResourcePath.substring(beginIndex);
	}

	public StreamResourceBuilder setReferenceClass(
			Class referenceClass)
	{
		this.referenceClass = referenceClass;
		return this;
	}

	public StreamResourceBuilder setRelativeResourcePath(
			String relativeResourcePath)
	{
		this.relativeResourcePath = relativeResourcePath;
		return this;
	}

	public StreamResourceBuilder setApplication(Application application)
	{
		this.application = application;
		return this;
	}
}
