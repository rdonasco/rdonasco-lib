/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.dao;

import com.rdonasco.security.model.Action;


public class ActionDAOJPAImpl extends AbstractSecurityDAO<Action> implements
		ActionDAO
{

	@Override
	public Class getDataClass()
	{
		return Action.class;
	}


}
