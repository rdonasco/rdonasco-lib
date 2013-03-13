/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.dao;

import com.rdonasco.common.dao.BaseDAO;
import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.UserCapability;
import com.rdonasco.security.model.UserSecurityProfile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Roy F. Donasco
 */
public class UserSecurityProfileDAOJPAImpl extends BaseDAO<UserSecurityProfile> implements UserSecurityProfileDAO
{

	@Override
	public Class<UserSecurityProfile> getDataClass()
	{
		return UserSecurityProfile.class;
	}
	
}
