/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 17-Apr-2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rdonasco.common.vaadin.view.layouts;

import com.rdonasco.common.vaadin.themes.DefaultThemeCssConstants;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Roy F. Donasco
 */
public class TwoColumnFlexibleRightColumnViewLayout extends HorizontalLayout implements ControlledView
{

	private static final long serialVersionUID = 1L;
	private static final VerticalLayout leftPanel = new VerticalLayout();
	private static final VerticalLayout centerPanel = new VerticalLayout();

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		addStyleName(DefaultThemeCssConstants.CSS_TWO_COLUMN_VIEW_LAYOUT);
		setSizeFull();
		leftPanel.setWidth(250f, UNITS_PIXELS);
		leftPanel.setHeight(100f, UNITS_PERCENTAGE);
		leftPanel.setMargin(true, false, true, true);
		leftPanel.addStyleName(DefaultThemeCssConstants.CSS_LEFT_PANEL);

		centerPanel.setSizeFull();
		centerPanel.setMargin(true, true, true, true);
		centerPanel.addStyleName(DefaultThemeCssConstants.CSS_CENTER_PANEL);

		addComponent(leftPanel);
		addComponent(centerPanel);
		setExpandRatio(centerPanel, 1);
	}

	public void setLeftPanelWidth(float width, int unit)
	{
		leftPanel.setWidth(width, unit);
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
}
