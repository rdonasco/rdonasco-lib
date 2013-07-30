/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.home.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.application.controllers.ApplicationViewLayoutController;
import com.rdonasco.security.home.views.FeatureHomeButton;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class ApplicationManagerHomeButtonController implements
		HomeViewButtonController
{
	private static final long serialVersionUID = 1L;

	@Inject
	private FeatureHomeButton featureButton;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@Inject
	private Instance<HomeFrameViewController> homeFrameViewControllers;

	@Inject
	private ApplicationViewLayoutController applicationViewLayoutController;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			doTheRefresh();
			featureButton.addListener(new Button.ClickListener()
			{
				private static final long serialVersionUID = 1L;
				@Override
				public void buttonClick(Button.ClickEvent event)
				{
					try
					{
						homeFrameViewControllers.get().setWorkspaceContent(getApplicationViewLayoutController().getControlledView());
					}
					catch (Exception e)
					{
						exceptionPopupProvider.popUpErrorException(e);
					}
				}
			});
		}
		catch (WidgetException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	public ApplicationViewLayoutController getApplicationViewLayoutController()
	{
		return applicationViewLayoutController;
	}


	@Override
	public FeatureHomeButton getControlledView()
	{
		return featureButton;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		featureButton.setCaption(I18NResource.localize("Manage Applications"));
		featureButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICONS_32x32_APPLICATIONS));
	}

	private void doTheRefresh() throws WidgetException
	{
		refreshView();
	}
}
