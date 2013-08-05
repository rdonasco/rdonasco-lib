/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 02-Aug-2013
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
import com.rdonasco.security.vo.ApplicationHostVO;
import com.vaadin.ui.Embedded;
import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco
 */
public class ApplicationHostItemVO implements ListEditorItem, Serializable
{
	private static final long serialVersionUID = 1L;
	private Embedded icon;
	private ApplicationHostVO applicationHostVO;

	public ApplicationHostItemVO()
	{
	}

	/**
	 * Creates new instance of the Application Host VO
	 *
	 * @param applicationHostVO the encapsulated application host VO
	 */
	public ApplicationHostItemVO(ApplicationHostVO applicationHostVO)
	{
		this.applicationHostVO = applicationHostVO;
	}

	public Long getId()
	{
		return applicationHostVO.getId();
	}

	public void setId(Long id)
	{
		applicationHostVO.setId(id);
	}

	public String getHostNameOrIpAddress()
	{
		return applicationHostVO.getHostNameOrIpAddress();
	}

	public void setHostNameOrIpAddress(String hostNameOrIpAddress)
	{
		applicationHostVO.setHostNameOrIpAddress(hostNameOrIpAddress);
	}

	public ApplicationHostVO getApplicationHostVO()
	{
		ApplicationHostVO clonedHost = new ApplicationHostVO();
		clonedHost.setHostNameOrIpAddress(getHostNameOrIpAddress());
		clonedHost.setId(getId());
		return clonedHost;
	}


	@Override
	public String toString()
	{
		return applicationHostVO.toString();
	}

	@Override
	public int hashCode()
	{
		return applicationHostVO.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean isEqual = true;
		if (obj == null)
		{
			isEqual = false;
		}
		else if (getClass() != obj.getClass())
		{
			isEqual = false;
		}
		{
			final ApplicationHostItemVO other = (ApplicationHostItemVO) obj;
			if (other == null)
			{
				isEqual = false;
			}
			else if (this.applicationHostVO == null || other.applicationHostVO == null
					|| !this.applicationHostVO.equals(other.applicationHostVO))
			{
				isEqual = false;
			}
		}
		return isEqual;
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
}
