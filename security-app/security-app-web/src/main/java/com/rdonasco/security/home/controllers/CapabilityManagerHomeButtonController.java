/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.home.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.security.app.controllers.ApplicationExceptionPopupProvider;
import com.rdonasco.security.app.controllers.ApplicationPopupProvider;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.capability.controllers.CapabilityViewLayoutController;
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
public class CapabilityManagerHomeButtonController implements
		HomeViewButtonController
{

	@Inject
	private FeatureHomeButton featureButton;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
//	@Inject
//	private ApplicationPopupProvider popupProvider;
	@Inject
	private Instance<HomeFrameViewController> homeFrameViewControllers;
	@Inject
	private CapabilityViewLayoutController capabilityViewLayoutController;

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
					homeFrameViewControllers.get().setWorkspaceContent(capabilityViewLayoutController.getControlledView());
				}
			});
		}
		catch (WidgetException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	@Override
	public FeatureHomeButton getControlledView()
	{
		return featureButton;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		featureButton.setCaption(I18NResource.localize("Manage Capabilities"));
		featureButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_32x32_CAPABILITIES));
	}

	private void doTheRefresh() throws WidgetException
	{
		refreshView();
	}
}
