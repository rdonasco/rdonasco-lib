/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.dao;

import com.rdonasco.security.model.UserSecurityProfile;

/**
 *
 * @author Roy F. Donasco
 */
public class UserSecurityProfileDAOJPAImpl extends AbstractSecurityDAO<UserSecurityProfile>
		implements UserSecurityProfileDAO
{

	@Override
	public Class<UserSecurityProfile> getDataClass()
	{
		return UserSecurityProfile.class;
	}
	
}
