/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.home.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.security.app.controllers.ApplicationExceptionPopupProvider;
import com.rdonasco.security.home.views.FeatureButtonLayout;
import com.vaadin.ui.Label;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class DomainManagerHomeButtonController implements
		HomeViewButtonController
{

	@Inject
	private FeatureButtonLayout featureButtonLayout;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			doTheRefresh();
		}
		catch (WidgetException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	@Override
	public FeatureButtonLayout getControlledView()
	{
		return featureButtonLayout;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		featureButtonLayout.removeAllComponents();
		featureButtonLayout.addComponent(new Label(I18NResource.localize("Manage Domains")));
	}

	private void doTheRefresh() throws WidgetException
	{
		refreshView();
	}
}
