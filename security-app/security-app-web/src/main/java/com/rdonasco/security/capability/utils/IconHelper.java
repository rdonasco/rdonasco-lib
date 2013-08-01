/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.utils;

import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;

/**
 *
 * @author Roy F. Donasco
 */
public final class IconHelper
{

	public static Embedded createDeleteIcon(String toolTip)
	{
		Embedded icon = new Embedded(null, new ThemeResource(SecurityDefaultTheme.ICON_16x16_DELETE));
		icon.setDescription(I18NResource.localize(toolTip));
		return icon;
	}
}
