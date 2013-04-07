/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.captcha.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Roy F. Donasco
 */
public class CaptchaView extends VerticalLayout implements ControlledView
{
	private static final long serialVersionUID = 1L;
	@Override
	public void initWidget() throws WidgetInitalizeException
	{
	}

	public void setContent(Embedded embeddedImage)
	{
		removeAllComponents();
		addComponent(embeddedImage);
	}
}
