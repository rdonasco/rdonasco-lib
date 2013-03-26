/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.home.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

/**
 *
 * @author Roy F. Donasco
 */
public class ToolbarView extends HorizontalLayout implements ControlledView
{
	private static final long serialVersionUID = 1L;
	HorizontalLayout leftToolbarLayout = new HorizontalLayout();
	HorizontalLayout rightToolbarLayout = new HorizontalLayout();

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		setHeight(50, UNITS_PIXELS);
		setWidth(100, UNITS_PERCENTAGE);
		addComponent(leftToolbarLayout);
		addComponent(rightToolbarLayout);
		setComponentAlignment(leftToolbarLayout, Alignment.MIDDLE_LEFT);
		setComponentAlignment(rightToolbarLayout, Alignment.MIDDLE_RIGHT);
		setMargin(true);
	}

	public void addLeftToolbarComponent(Component componentToAdd)
	{
		leftToolbarLayout.addComponent(componentToAdd);
	}

	public void addRightToolbarComponent(Component componentToAdd)
	{
		rightToolbarLayout.addComponent(componentToAdd);
	}
}
