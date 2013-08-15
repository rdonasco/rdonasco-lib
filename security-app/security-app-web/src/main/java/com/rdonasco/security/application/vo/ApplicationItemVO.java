/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 27-Jul-2013
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
package com.rdonasco.security.application.vo;

import com.rdonasco.datamanager.listeditor.view.ListEditorItem;
import com.rdonasco.security.vo.ApplicationVO;
import com.vaadin.ui.Embedded;
import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco
 */
public class ApplicationItemVO implements ListEditorItem,
		Serializable
{

	private static final long serialVersionUID = 1L;

	private Embedded icon;

	private ApplicationVO applicationVO;

	ApplicationItemVO(Embedded icon, ApplicationVO applicationVO)
	{
		this.icon = icon;
		this.applicationVO = applicationVO;
	}

	public ApplicationVO getApplicationVO()
	{
		return applicationVO;
	}

	@Override
	public Embedded getIcon()
	{
		return icon;
	}

	@Override
	public void setIcon(Embedded icon)
	{
		this.icon = icon;
	}

	public Long getId()
	{
		return applicationVO.getId();
	}

	public void setId(Long id)
	{
		applicationVO.setId(id);
	}

	public String getName()
	{
		return applicationVO.getName();
	}

	public void setName(String name)
	{
		applicationVO.setName(name);
	}

	public String getToken()
	{
		return applicationVO.getToken();
	}

	public void setToken(String token)
	{
		applicationVO.setToken(token);
	}

	@Override
	public int hashCode()
	{
		return applicationVO.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean isEqual;
		if (obj != null && getClass().equals(obj.getClass()))
		{
			final ApplicationItemVO other = (ApplicationItemVO) obj;
			if (this.applicationVO != other.applicationVO
					&& (this.applicationVO == null || !this.applicationVO.equals(other.applicationVO)))
			{
				isEqual = false;
			}
			else
			{
				isEqual = true;
			}
		}
		else
		{
			isEqual = false;
		}
		return isEqual;
	}

	@Override
	public String toString()
	{
		return applicationVO.toString();
	}
}
