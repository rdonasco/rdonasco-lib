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
import java.util.Collection;

/**
 *
 * @author Roy F. Donasco
 */
public class UserSecurityProfileVO implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String loginId;
	private String password;
	private Collection<UserCapabilityVO> capabilities;

	public UserSecurityProfileVO(Long id, String loginId, String password,
			Collection<UserCapabilityVO> capabilities)
	{
		this.id = id;
		this.loginId = loginId;
		this.password = password;
		this.capabilities = capabilities;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getLoginId()
	{
		return loginId;
	}

	public void setLoginId(String loginId)
	{
		this.loginId = loginId;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
	
	

	public Collection<UserCapabilityVO> getCapabilityVOList()
	{
		return capabilities;
	}

	public void setCapabilities(Collection<UserCapabilityVO> capabilities)
	{
		this.capabilities = capabilities;
	}	

	@Override
	public int hashCode()
	{
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object)
	{
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof UserSecurityProfileVO))
		{
			return false;
		}
		UserSecurityProfileVO other = (UserSecurityProfileVO) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "UserSecurityProfileVO{" + "id=" + id + ", loginId=" + loginId + ", capabilities=" + capabilities + '}';
	}


}