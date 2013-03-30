/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Roy F. Donasco
 */
public class ResourcesEditorAndSelectorView extends Panel implements
		ControlledView
{
	private static final long serialVersionUID = 1L;
	private Table resourceEditorTable = new Table();

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		setCaption(I18NResource.localize("Resources"));
		setStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
		resourceEditorTable.addStyleName(SecurityDefaultTheme.CSS_TABLE_SMALL_STRIPED);
		resourceEditorTable.addStyleName(SecurityDefaultTheme.CSS_TABLE_BORDERLESS);
		VerticalLayout layout = (VerticalLayout) getContent();
		layout.addComponent(resourceEditorTable);
		resourceEditorTable.setSizeFull();
		layout.setExpandRatio(resourceEditorTable, 1);
	}

	public Table getResourceEditorTable()
	{
		return resourceEditorTable;
	}
}
