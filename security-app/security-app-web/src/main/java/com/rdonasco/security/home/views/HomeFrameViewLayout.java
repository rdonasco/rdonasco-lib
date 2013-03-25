/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.home.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Roy F. Donasco
 */
public class HomeFrameViewLayout extends VerticalLayout implements
		ControlledView
{
	private static final long serialVersionUID = 1L;
	private VerticalLayout toolbarView = new VerticalLayout();
	private VerticalLayout workspaceView = new VerticalLayout();

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		toolbarView.setHeight(40, UNITS_PIXELS);
		toolbarView.setWidth(100, UNITS_PERCENTAGE);
		workspaceView.setSizeFull();
		addComponent(toolbarView);
		addComponent(workspaceView);
		setExpandRatio(workspaceView, 1);
		setSizeFull();
	}

	public void setToolbarContent(Component toolbarContent)
	{
		toolbarView.removeAllComponents();
		toolbarView.addComponent(toolbarContent);
		toolbarView.setExpandRatio(toolbarContent, 1);
	}

	public void setWorkspaceContent(Component workspaceContent)
	{
		workspaceView.removeAllComponents();
		workspaceView.addComponent(workspaceContent);
		workspaceView.setExpandRatio(workspaceContent, 1);

	}
}
