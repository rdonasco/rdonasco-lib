/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.home.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

/**
 *
 * @author Roy F. Donasco
 */
public class HomeViewLayout extends CssLayout implements ControlledView
{
	private static final long serialVersionUID = 1L;

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
	}

	public void addMessage(String message)
	{
		addComponent(new Label(message));
	}
}
