/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 27-Jan-2013
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
package com.rdonasco.security.vo;

import java.io.Serializable;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityActionVO implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Long id;
	private CapabilityVO capabilityVO;
	private ActionVO actionVO;

	public CapabilityActionVO(Long id, CapabilityVO capabilityVO,
			ActionVO actionVO)
	{
		this.id = id;
		this.capabilityVO = capabilityVO;
		this.actionVO = actionVO;
	}		

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}	

	public CapabilityVO getCapabilityVO()
	{
		return capabilityVO;
	}

	public void setCapabilityVO(CapabilityVO capabilityVO)
	{
		this.capabilityVO = capabilityVO;
	}

	public ActionVO getActionVO()
	{
		return actionVO;
	}

	public void setActionVO(ActionVO actionVO)
	{
		this.actionVO = actionVO;
	}

	

}
