/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 20-Jul-2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rdonasco.security.services;

import com.rdonasco.security.dao.ApplicationDAO;
import com.rdonasco.security.exceptions.ApplicationManagerException;
import com.rdonasco.security.vo.ApplicationVO;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class ApplicationManager implements 
		ApplicationManagerLocal
{
	private ApplicationDAO applicationDAO;

	@Inject
	public void setApplicationDAO(ApplicationDAO applicationDAO)
	{
		this.applicationDAO = applicationDAO;
	}

	@Override
	public ApplicationVO createNewApplication(ApplicationVO newApplicationVO)
			throws ApplicationManagerException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method createNewApplication
		throw new UnsupportedOperationException("Not supported yet.");
	}
	// Add business logic below. (Right-click in editor and choose
	// "Insert Code > Add Business Method")

}
