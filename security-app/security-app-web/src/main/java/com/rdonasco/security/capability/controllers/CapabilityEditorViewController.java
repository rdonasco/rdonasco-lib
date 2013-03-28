/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.security.app.controllers.ApplicationExceptionPopupProvider;
import com.rdonasco.security.capability.views.CapabilityEditorView;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityEditorViewController implements
		ViewController<CapabilityEditorView>, Serializable
{
	private static final long serialVersionUID = 1L;
	@Inject
	private CapabilityEditorView editorView;
	@Inject
	private Instance<ApplicationExceptionPopupProvider> exceptionPopupInstances;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			editorView.initWidget();
		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupInstances.get().popUpErrorException(ex);
		}
	}

	@Override
	public CapabilityEditorView getControlledView()
	{
		return editorView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		editorView.removeAllComponents();
		editorView.initWidget();
	}
}
