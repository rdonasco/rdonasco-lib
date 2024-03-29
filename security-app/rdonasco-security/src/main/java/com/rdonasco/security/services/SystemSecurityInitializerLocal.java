/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 24-Feb-2013
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

import com.rdonasco.security.exceptions.SystemSecurityInitializationException;
import javax.ejb.Local;

/**
 *
 * @author Roy F. Donasco
 */
@Local
public interface SystemSecurityInitializerLocal
{
	static final String DEFAULT_CAPABILITY_ELEMENT_XPATH = "/system/capability/default";
	static final int ELEMENT_XPATH = 0;
	static final int ELEMENT_CAPABILITY_TITLE = 1;
	static final int ELEMENT_RESOURCE = 2;
	static final String ATTRIBUTE_RESOURCE = "resource";
	static final String ATTRIBUTE_ACTION = "action";
	
	static final String[][] DEFAULT_CAPABILITY_ELEMENTS = 
	{
		{DEFAULT_CAPABILITY_ELEMENT_XPATH,"Logon To System","system","logon","logoff"}
	};
	
	void initializeDefaultSystemAccessCapabilities() throws SystemSecurityInitializationException;

}
