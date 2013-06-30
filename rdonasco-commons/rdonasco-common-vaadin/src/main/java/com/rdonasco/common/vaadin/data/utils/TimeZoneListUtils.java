/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 30-Jun-2013
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

package com.rdonasco.common.vaadin.data.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 *
 * @author Roy F. Donasco
 */
public class TimeZoneListUtils 
{
	public static List<String> getTimeZoneList()
	{
		List<String> timeZones = new ArrayList<String>();
		for (String timeZone : TimeZone.getAvailableIDs())
		{
			timeZones.add(timeZone);
		}
		return timeZones;
	}
}
