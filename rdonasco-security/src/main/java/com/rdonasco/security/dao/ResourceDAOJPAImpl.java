/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.dao;

import com.rdonasco.common.dao.BaseDAO;
import com.rdonasco.security.model.Resource;


public class ResourceDAOJPAImpl extends BaseDAO<Resource> implements ResourceDAO 
{

	@Override
	public Class getDataClass()
	{
		return Resource.class;
	}


}
