/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.dao;

import com.rdonasco.security.model.ApplicationHost;


public class ApplicationHostDAOJPAImpl extends AbstractSecurityDAO<ApplicationHost>
		implements
		ApplicationHostDAO
{

	@Override
	public Class getDataClass()
	{
		return ApplicationHost.class;
	}


}
