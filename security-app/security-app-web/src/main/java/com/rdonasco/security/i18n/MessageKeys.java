/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 17-Apr-2013
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
package com.rdonasco.security.i18n;

/**
 *
 * @author Roy F. Donasco
 */
public interface MessageKeys
{
	static final String ARE_YOU_SURE = "Are you sure?";
	static final String DO_YOU_REALLY_WANT_TO_DELETE_THIS = "Do you really want to delete this?";
	static final String YES = "Yes";
	static final String NO = "No";
	// capabilitiy message keys
	static final String CAPABILITY_DELETED = "Capability deleted";
	static final String UNABLE_TO_ADD_NEW_CAPABILITY = "Unable to add new capability";
	// user message keys
	static final String USER_PROFILE_DELETED = "User profile deleted";
	public static String UNABLE_TO_ADD_NEW_USER = "Unable to add new user";

	// role message keys
	public static final String ROLE_DELETED = "Role deleted";
	public static final String NEW_ROLE = "New Role";

	public static final String UNABLE_TO_ADD_NEW_ROLE = "Unable to add new role";
}
