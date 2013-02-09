/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.dao;

import com.rdonasco.common.dao.BaseDAO;
import com.rdonasco.security.model.Action;


public class ActionDAOJPAImpl extends BaseDAO<Action> implements ActionDAO 
{

	@Override
	public Class getDataClass()
	{
		return Action.class;
	}


}
