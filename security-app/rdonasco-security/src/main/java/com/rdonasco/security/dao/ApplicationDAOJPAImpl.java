/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.dao;

import com.rdonasco.security.model.Application;


public class ApplicationDAOJPAImpl extends AbstractSecurityDAO<Application>
		implements
		ApplicationDAO
{

	@Override
	public Class getDataClass()
	{
		return Application.class;
	}


}
