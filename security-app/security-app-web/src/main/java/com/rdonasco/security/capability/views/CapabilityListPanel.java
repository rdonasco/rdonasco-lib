/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Roy F. Donasco
 */
@Dependent
public class CapabilityListPanel extends Panel implements
		ControlledView
{

	private static final long serialVersionUID = 1L;

	private Table dataViewListTable;

	private Button addCapabilityButton = new Button();

	private Button refreshButton = new Button();

	public CapabilityListPanel()
	{
		setCaption(I18NResource.localize("Capabilities"));
		setStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
	}

	public Table getDataViewListTable()
	{
		return dataViewListTable;
	}

	public Button getAddCapabilityButton()
	{
		return addCapabilityButton;
	}

	public Button getRefreshButton()
	{
		return refreshButton;
	}

	public void setDataViewListTable(Table dataViewListTable)
	{
		this.dataViewListTable = dataViewListTable;
		this.dataViewListTable.addStyleName(SecurityDefaultTheme.CSS_TABLE_SMALL_STRIPED);
		this.dataViewListTable.addStyleName(SecurityDefaultTheme.CSS_TABLE_BORDERLESS);
	}

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		addCapabilityButton.setCaption(I18NResource.localize("Add new capability"));
		addCapabilityButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_16x16_ADD));
		addCapabilityButton.setWidth(100, UNITS_PERCENTAGE);
		addCapabilityButton.addStyleName(SecurityDefaultTheme.CSS_SMALL);
		refreshButton.setCaption(I18NResource.localize("Refresh"));
		refreshButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_16x16_REFRESH));
		refreshButton.addStyleName(SecurityDefaultTheme.CSS_SMALL);
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(addCapabilityButton);
		buttonLayout.addComponent(refreshButton);
		buttonLayout.setExpandRatio(addCapabilityButton, 1);
		VerticalLayout content = ((VerticalLayout) getContent());
		content.setMargin(true);
		content.setHeight(600, UNITS_PIXELS);
		content.removeAllComponents();
		if (null != getDataViewListTable())
		{
			getDataViewListTable().setSizeFull();
			content.addComponent(buttonLayout);
			content.addComponent(getDataViewListTable());
			content.setExpandRatio(getDataViewListTable(), 1);
			content.setSpacing(true);
		}

	}
}
