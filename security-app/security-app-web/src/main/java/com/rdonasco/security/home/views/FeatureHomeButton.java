/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.home.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.vaadin.ui.Button;
import javax.annotation.PostConstruct;

/**
 *
 * @author Roy F. Donasco
 */
public class FeatureHomeButton extends Button implements
		ControlledView
{
	private static final long serialVersionUID = 1L;

	@Override
	@PostConstruct
	public void initWidget() throws WidgetInitalizeException
	{
		addStyleName(SecurityDefaultTheme.CSS_FEATURE_BUTTON_LAYOUT);
		setSizeUndefined();
		setWidth(160, UNITS_PIXELS);
		setHeight(90, UNITS_PIXELS);
	}

	@Override
	public void setCaption(String labelText)
	{
		super.setCaption(labelText);
	}
}
