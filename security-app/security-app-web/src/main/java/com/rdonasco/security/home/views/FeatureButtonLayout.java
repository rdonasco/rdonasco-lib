/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.home.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;

/**
 *
 * @author Roy F. Donasco
 */
public class FeatureButtonLayout extends VerticalLayout implements
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

	public void addLabel(String labelText)
	{
		Label label = new Label(labelText);
		addComponent(label);
		setComponentAlignment(label, Alignment.MIDDLE_CENTER);
	}
}
