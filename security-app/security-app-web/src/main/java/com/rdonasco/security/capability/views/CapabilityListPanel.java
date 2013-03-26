/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.rdonasco.datamanager.view.DataViewListTable;
import com.vaadin.ui.Panel;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityListPanel extends Panel implements ControlledView
{
	private static final long serialVersionUID = 1L;
	private DataViewListTable dataViewListTable;

	public CapabilityListPanel()
	{
		super(I18NResource.localize("Capabilities"));
	}

	public DataViewListTable getDataViewListTable()
	{
		return dataViewListTable;
	}

	public void setDataViewListTable(DataViewListTable dataViewListTable)
	{
		this.dataViewListTable = dataViewListTable;
	}


	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		try
		{
			removeAllComponents();
			setSizeFull();
			if (null != getDataViewListTable())
			{
				addComponent(getDataViewListTable());
				getDataViewListTable().setSizeFull();
			}
			
		}
		catch (Exception e)
		{
			throw new WidgetInitalizeException(e);
		}
	}
}
