/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.home.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.config.services.ConfigDataManagerVODecoratorRemote;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.home.views.FeatureHomeButton;
import com.rdonasco.security.user.controllers.UserViewLayoutController;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class UserManagerHomeButtonController implements
		HomeViewButtonController
{
	private static final long serialVersionUID = 1L;

	@Inject
	private FeatureHomeButton featureButton;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	@Inject
	private Instance<UserViewLayoutController> userViewLayoutControllerInstances;
	private UserViewLayoutController userViewLayoutController;
	@Inject
	private Instance<HomeFrameViewController> homeFrameViewControllers;
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
					try
					{
						homeFrameViewControllers.get().setWorkspaceContent(getUserViewLayoutController().getControlledView());
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

	public UserViewLayoutController getUserViewLayoutController()
	{
		if (null == userViewLayoutController)
		{
			userViewLayoutController = userViewLayoutControllerInstances.get();
		}
		return userViewLayoutController;
	}


	@Override
	public FeatureHomeButton getControlledView()
	{
		return featureButton;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		featureButton.setCaption(I18NResource.localize("Manage Users"));
		featureButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_32x32_USERS));
	}

	private void doTheRefresh() throws WidgetException
	{
		refreshView();
	}

	@Override
	public boolean isActivated()
	{
		return configDataManager.loadValue(new StringBuilder(CONFIG_PREFIX)
				.append("userManager").append(CONFIG_IS_ACTIVE).toString()
				, Boolean.class, true);
	}
	
	
}
