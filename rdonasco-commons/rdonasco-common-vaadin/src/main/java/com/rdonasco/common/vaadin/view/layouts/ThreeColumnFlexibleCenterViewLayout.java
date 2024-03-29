/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.common.vaadin.view.layouts;

import com.rdonasco.common.vaadin.themes.DefaultThemeCssConstants;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.view.ControlledView;
import static com.vaadin.terminal.Sizeable.UNITS_PERCENTAGE;
import static com.vaadin.terminal.Sizeable.UNITS_PIXELS;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Roy F. Donasco
 */
@Dependent
public class ThreeColumnFlexibleCenterViewLayout extends HorizontalLayout implements
		ControlledView
{

	private static final long serialVersionUID = 1L;
	private VerticalLayout leftPanel = new VerticalLayout();
	private VerticalLayout centerPanel = new VerticalLayout();
	private VerticalLayout rightPanel = new VerticalLayout();

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		addStyleName(DefaultThemeCssConstants.CSS_THREE_COLUMN_VIEW_LAYOUT);
		setSizeFull();
		leftPanel.setWidth(250, UNITS_PIXELS);
		leftPanel.setHeight(100F, UNITS_PERCENTAGE);
		leftPanel.setMargin(true, false, true, true);
		leftPanel.addStyleName(DefaultThemeCssConstants.CSS_LEFT_PANEL);

		centerPanel.setSizeFull();
		centerPanel.setMargin(true);
		centerPanel.addStyleName(DefaultThemeCssConstants.CSS_CENTER_PANEL);

		rightPanel.setWidth(250, UNITS_PIXELS);
		rightPanel.setMargin(true, true, true, false);
		rightPanel.addStyleName(DefaultThemeCssConstants.CSS_RIGHT_PANEL);
		rightPanel.setSpacing(true);

		addComponent(leftPanel);
		addComponent(centerPanel);
		addComponent(rightPanel);

		setExpandRatio(centerPanel, 1); // occupy all remaining space;

	}

	public void setLeftPanelWidth(float width, int unit)
	{
		leftPanel.setWidth(width, unit);
	}

	public void setRightPanelWidth(float width, int unit)
	{
		rightPanel.setWidth(width, unit);
	}

	public void setLeftPanelContent(Component leftComponent)
	{
		leftPanel.removeAllComponents();
		leftPanel.addComponent(leftComponent);
		leftPanel.setExpandRatio(leftComponent, 1);
	}

	public void setCenterPanelContent(Component centerComponent)
	{
		centerPanel.removeAllComponents();
		centerPanel.addComponent(centerComponent);
		centerPanel.setExpandRatio(centerComponent, 1);
	}

	public void addRightPanelContent(Component rightPanelContent)
	{
		rightPanel.addComponent(rightPanelContent);
	}
}
