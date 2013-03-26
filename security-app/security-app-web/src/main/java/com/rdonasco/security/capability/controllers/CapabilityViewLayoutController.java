/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.security.app.controllers.ApplicationExceptionPopupProvider;
import com.rdonasco.security.capability.views.CapabilityViewLayout;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityViewLayoutController implements
		ViewController<CapabilityViewLayout>
{
	private static final long serialVersionUID = 1L;
	@Inject
	private CapabilityViewLayout capabilityViewLayout;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	@Inject
	private CapabilityListPanelController capabilityListPanelController;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			// add other contents here
			capabilityViewLayout.initWidget();
			capabilityViewLayout.setLeftPanelContent(capabilityListPanelController.getControlledView());
		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	@Override
	public CapabilityViewLayout getControlledView()
	{
		return capabilityViewLayout;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method refreshView
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
