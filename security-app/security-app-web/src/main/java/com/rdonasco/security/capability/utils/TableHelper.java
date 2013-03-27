/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.utils;

import com.rdonasco.datamanager.theme.DataManagerTheme;
import com.vaadin.ui.Table;

/**
 *
 * @author Roy F. Donasco
 */
public final class TableHelper
{
	public static void setupTable(Table table)
	{
		table.addStyleName(DataManagerTheme.STYLE_RECORD_LIST);
		/*
		 * Make table selectable, react immediatedly to user events, and pass events to the
		 * controller (our main application)
		 */
		table.setSelectable(true);
		table.setImmediate(true);

		/* We don't want to allow users to de-select a row */
		table.setNullSelectionAllowed(false);

		// set additional table features
		table.setColumnCollapsingAllowed(true);
		table.setColumnReorderingAllowed(true);
	}
}
