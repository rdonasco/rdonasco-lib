/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.home.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.config.view.ConfigDataView;
import com.rdonasco.config.view.ConfigDataViewController;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.authentication.services.SessionSecurityChecker;
import com.rdonasco.security.home.views.FeatureHomeButton;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class SettingsHomeButtonController implements
		HomeViewButtonController
{

	private static final long serialVersionUID = 1L;

	private static final String CONSTANTS_SETTINGS = "Settings";

	private static final String ACTION_VIEW = "view";

	@Inject
	private FeatureHomeButton featureButton;

	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@Inject
	private ApplicationPopupProvider popupProvider;

	@Inject
	private Instance<ConfigDataViewController> configDataViewControllerProvider;

	@Inject
	private Instance<HomeFrameViewController> homeFrameViewControllers;

	private ConfigDataView configView;

	@Inject
	private SessionSecurityChecker sessionSecurityChecker;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			doTheRefresh();
			featureButton.addListener(new Button.ClickListener()
			{
				@Override
				public void buttonClick(Button.ClickEvent event)
				{
					sessionSecurityChecker.checkAccess(CONSTANTS_SETTINGS, ACTION_VIEW);
					homeFrameViewControllers.get().setWorkspaceContent(getWorkspaceContent());
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
		featureButton.setCaption(I18NResource.localize(CONSTANTS_SETTINGS));
		featureButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICONS_32x32_SETTINGS));
	}

	private void doTheRefresh() throws WidgetException
	{
		refreshView();
	}

	private ConfigDataView getConfigView()
	{
		if (null == configView)
		{
			configView = configDataViewControllerProvider.get().getControlledView();
			configView.setHeight(400f, Sizeable.UNITS_PIXELS);
		}
		return configView;
	}

	private Component getWorkspaceContent()
	{
		Panel panel = new Panel(I18NResource.localize(CONSTANTS_SETTINGS));
		panel.setStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
		panel.addComponent(getConfigView());
		return panel;
	}
}
