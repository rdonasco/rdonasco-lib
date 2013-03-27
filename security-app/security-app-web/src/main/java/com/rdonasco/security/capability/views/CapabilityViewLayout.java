/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.view.ControlledView;
import static com.vaadin.terminal.Sizeable.UNITS_PERCENTAGE;
import static com.vaadin.terminal.Sizeable.UNITS_PIXELS;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityViewLayout extends HorizontalLayout implements
		ControlledView
{

	private static final long serialVersionUID = 1L;
	private VerticalLayout leftPanel = new VerticalLayout();
	private VerticalLayout centerPanel = new VerticalLayout();
	private VerticalLayout rightPanel = new VerticalLayout();

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		leftPanel.setWidth(250, UNITS_PIXELS);
		rightPanel.setWidth(200, UNITS_PIXELS);
		leftPanel.setMargin(true);
		rightPanel.setMargin(true);
		centerPanel.setSizeUndefined();
		centerPanel.setMargin(true);
		addComponent(leftPanel);
		addComponent(centerPanel);
		addComponent(rightPanel);
		setExpandRatio(centerPanel, 1); // occupy all remaining space;
		setSizeUndefined();
	}

	public void setLeftPanelContent(Component leftComponent)
	{
		leftPanel.removeAllComponents();
		leftPanel.addComponent(leftComponent);
		leftPanel.setExpandRatio(leftComponent, 1);
	}
}
