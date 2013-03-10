/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


public class UserSecurityProfileVOBuilder 
{
	private Long id;
	private String loginId;
	private Collection<UserCapabilityVO> capabilities = new ArrayList<UserCapabilityVO>();
	private String password;
	private Date expiryDate = new Date();
	private String registrationToken;

	public UserSecurityProfileVOBuilder()
	{
	}

	public UserSecurityProfileVOBuilder setId(Long id)
	{
		this.id = id;
		return this;
	}

	public UserSecurityProfileVOBuilder setLoginId(String loginId)
	{
		this.loginId = loginId;
		return this;
	}
	
	public UserSecurityProfileVOBuilder setPassword(String password)
	{
		this.password = password;
		return this;
	}

	public UserSecurityProfileVOBuilder setRegistrationToken(String token)
	{
		this.registrationToken = token;
		return this;
	}

	public UserSecurityProfileVOBuilder setRegistrationTokenExpiry(
			Date expiryDate)
	{
		this.expiryDate = expiryDate;
		return this;
	}

	public UserSecurityProfileVOBuilder setCapabilities(Collection<UserCapabilityVO> capabilities)
	{
		this.capabilities = capabilities;
		return this;
	}
	
	public UserSecurityProfileVOBuilder addCapability(UserCapabilityVO userCapabilityVO)
	{
		this.capabilities.add(userCapabilityVO);
		return this;
	}

	public UserSecurityProfileVO createUserSecurityProfileVO()
	{
		return new UserSecurityProfileVO(id, loginId, password, registrationToken, expiryDate, capabilities);
	}


}
