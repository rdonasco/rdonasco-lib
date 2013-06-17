/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.vo;


public class UserCapabilityVOBuilder 
{
	private Long id;
	private UserSecurityProfileVO userProfile;
	private CapabilityVO capability;

	public UserCapabilityVOBuilder()
	{
	}

	public UserCapabilityVOBuilder setId(Long id)
	{
		this.id = id;
		return this;
	}

	public UserCapabilityVOBuilder setUserProfile(UserSecurityProfileVO userProfile)
	{
		this.userProfile = userProfile;
		return this;
	}

	public UserCapabilityVOBuilder setCapability(CapabilityVO capability)
	{
		this.capability = capability;
		return this;
	}

	public UserCapabilityVO createUserCapabilityVO()
	{
		return new UserCapabilityVO(id, userProfile, capability);
	}

}
