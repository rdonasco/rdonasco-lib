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
	static final String DEFAULT_CAPABILITIES =  "/system/capability/default";
	static final String DEFAULT_CAPABILITY_TITLE = "Logon To System";
	static final String DEFAULT_ACCESS_RESOURCE = "system";
	static final String DEFAULT_ACCESS_ACTION = "logon";
	void initializeDefaultSystemAccessCapabilities() throws SystemSecurityInitializationException;
	
}
