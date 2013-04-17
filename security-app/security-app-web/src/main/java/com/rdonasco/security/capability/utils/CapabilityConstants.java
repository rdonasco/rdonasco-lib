/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.utils;

import com.rdonasco.common.i18.I18NResource;

/**
 *
 * @author Roy F. Donasco
 */
public interface CapabilityConstants
{

	public static final Object[] TABLE_VISIBLE_COLUMNS = new Object[]
	{
		"embeddedIcon", "title"
	};
	public static final String[] TABLE_VISIBLE_HEADERS = new String[]
	{
		"",
		I18NResource.localize("Title")
	};
}
