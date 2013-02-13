/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.dao;

import com.rdonasco.common.dao.BaseDAO;
import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.UserSecurityProfile;
import java.util.List;

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
	
	@Override
	public List<Capability> loadCapabilitiesOf(UserSecurityProfile user) throws DataAccessException
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}	
}
