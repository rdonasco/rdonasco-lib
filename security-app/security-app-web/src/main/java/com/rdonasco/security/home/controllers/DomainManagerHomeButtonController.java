/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.home.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.config.services.ConfigDataManagerVODecoratorRemote;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import static com.rdonasco.security.home.controllers.HomeViewButtonController.CONFIG_IS_ACTIVE;
import static com.rdonasco.security.home.controllers.HomeViewButtonController.CONFIG_PREFIX;
import com.rdonasco.security.home.views.FeatureHomeButton;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class DomainManagerHomeButtonController implements
		HomeViewButtonController
{

	private static final long serialVersionUID = 1L;
	@Inject
	private FeatureHomeButton featureButton;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	@Inject
	private ApplicationPopupProvider popupProvider;
	@EJB
	private ConfigDataManagerVODecoratorRemote configDataManager;

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
					popupProvider.popUpInfo("Domain Manager Clicked. This feature is still a work in progress");
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
		featureButton.setCaption(I18NResource.localize("Manage Domains"));
		featureButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_32x32_DOMAINS));
	}

	private void doTheRefresh() throws WidgetException
	{
		refreshView();
	}

	@Override
	public boolean isActivated()
	{
		return configDataManager.loadValue(new StringBuilder(CONFIG_PREFIX)
				.append("domainManager").append(CONFIG_IS_ACTIVE).toString(), Boolean.class, true);
	}
}
