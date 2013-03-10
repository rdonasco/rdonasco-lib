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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Roy F. Donasco
 */
public class UserSecurityProfileVO implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String logonId;
	private String password;
	private String registrationToken;
	private Date registrationTokenExpiration;
	private Collection<UserCapabilityVO> capabilities;

	public UserSecurityProfileVO(Long id, String loginId, String password,
			String token, Date expiryDate,
			Collection<UserCapabilityVO> capabilities)
	{
		this.id = id;
		this.logonId = loginId;
		this.password = password;
		this.capabilities = capabilities;
		this.registrationToken = token;
		this.registrationTokenExpiration = expiryDate;
		if(null != capabilities)
		{
			for(UserCapabilityVO userCapabilityVO : capabilities)
			{
				userCapabilityVO.setUserProfile(this);
			}
		}
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getLogonId()
	{
		return logonId;
	}

	public void setLogonId(String loginId)
	{
		this.logonId = loginId;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getRegistrationToken()
	{
		return registrationToken;
	}

	public void setRegistrationToken(String registrationToken)
	{
		this.registrationToken = registrationToken;
	}

	public Date getRegistrationTokenExpiration()
	{
		return registrationTokenExpiration;
	}

	public void setRegistrationTokenExpiration(Date registrationTokenExpiration)
	{
		this.registrationTokenExpiration = registrationTokenExpiration;
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
		boolean isEqual = true;
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof UserSecurityProfileVO))
		{
			isEqual = false;
		}
		UserSecurityProfileVO other = (UserSecurityProfileVO) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
		{
			isEqual = false;
		}
		return isEqual;
	}

	public boolean isTokenExpired()
	{
		Calendar now = Calendar.getInstance();
		boolean isExpired = (getRegistrationToken() == null || getRegistrationToken().isEmpty() || getRegistrationTokenExpiration() == null);
		if (!isExpired)
		{
			Calendar expiry = Calendar.getInstance();
			expiry.setTime(getRegistrationTokenExpiration());
			isExpired = now.after(expiry);
		}
		return isExpired;
	}

	@Override
	public String toString()
	{
		return "UserSecurityProfileVO{" + "id=" + id + ", loginId=" + logonId + ", capabilities=" + capabilities + '}';
	}

	private void ensureCapabilitiesAreInitialized()
	{
		if(null == capabilities)
		{
			capabilities = new ArrayList<UserCapabilityVO>();
		}
	}
	
	public void addCapbility(UserCapabilityVO userCapabilityVO)
	{
		ensureCapabilitiesAreInitialized();
		capabilities.add(userCapabilityVO);
		userCapabilityVO.setUserProfile(this);
	}


}
