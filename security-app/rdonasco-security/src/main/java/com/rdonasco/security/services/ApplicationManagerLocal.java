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

import com.rdonasco.security.exceptions.ApplicationManagerException;
import com.rdonasco.security.vo.ApplicationVO;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Roy F. Donasco
 */
@Local
public interface ApplicationManagerLocal
{

	ApplicationVO createNewApplication(final ApplicationVO newApplicationVO)
			throws ApplicationManagerException;

	void updateApplication(final ApplicationVO applicationToUpdate)
			throws ApplicationManagerException;

	ApplicationVO loadApplicationWithID(Long id) throws
			ApplicationManagerException;

	ApplicationVO loadApplicationByNameAndToken(String applicationName,
			String token) throws
			ApplicationManagerException;

	public void deleteApplication(ApplicationVO applicationVO) throws
			ApplicationManagerException;

	public List<ApplicationVO> retrieveAllApplication();
}
