/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import static com.vaadin.terminal.Sizeable.UNITS_PERCENTAGE;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Roy F. Donasco
 */
public class ActionEditorAndSelectorView extends Panel implements ControlledView
{
	private static final long serialVersionUID = 1L;
	private Table actionEditorTable = new Table();
	private Button addActionButton = new Button();

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		setCaption(I18NResource.localize("Actions"));
		setStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
		addActionButton.setCaption(I18NResource.localize("Add new action"));
		addActionButton.setWidth(100, UNITS_PERCENTAGE);
		addActionButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICONS_16x16_ADD));
		addActionButton.addStyleName(SecurityDefaultTheme.CSS_SMALL);
		actionEditorTable.addStyleName(SecurityDefaultTheme.CSS_TABLE_SMALL_STRIPED);
		actionEditorTable.addStyleName(SecurityDefaultTheme.CSS_TABLE_BORDERLESS);
		VerticalLayout layout = (VerticalLayout) getContent();
		layout.addComponent(addActionButton);
		layout.addComponent(actionEditorTable);
		actionEditorTable.setSizeFull();
		layout.setExpandRatio(actionEditorTable, 1);
		layout.setSpacing(true);
	}

	public Table getActionEditorTable()
	{
		return actionEditorTable;
	}

	public Button getAddActionButton()
	{
		return addActionButton;
	}
}
