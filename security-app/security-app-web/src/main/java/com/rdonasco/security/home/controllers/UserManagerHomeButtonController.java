/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.home.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.home.views.FeatureHomeButton;
import com.rdonasco.security.user.controllers.UserViewLayoutController;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class UserManagerHomeButtonController implements
		HomeViewButtonController
{

	@Inject
	private FeatureHomeButton featureButton;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	@Inject
	private ApplicationPopupProvider popupProvider;
	@Inject
	private Instance<UserViewLayoutController> userViewLayoutControllerInstances;
	private UserViewLayoutController userViewLayoutController;
	@Inject
	private Instance<HomeFrameViewController> homeFrameViewControllers;

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
					homeFrameViewControllers.get().setWorkspaceContent(getUserViewLayoutController().getControlledView());
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
}
