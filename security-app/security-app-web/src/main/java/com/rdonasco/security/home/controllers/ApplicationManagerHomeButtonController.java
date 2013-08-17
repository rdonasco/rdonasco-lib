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
import com.rdonasco.security.application.controllers.ApplicationViewLayoutController;
import static com.rdonasco.security.home.controllers.HomeViewButtonController.CONFIG_IS_ACTIVE;
import static com.rdonasco.security.home.controllers.HomeViewButtonController.CONFIG_PREFIX;
import com.rdonasco.security.home.views.FeatureHomeButton;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
@Dependent
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
	private Instance<ApplicationViewLayoutController> applicationViewLayoutControllers;
	private ApplicationViewLayoutController applicationViewLayoutController;
	private HomeFrameViewController homeFrameViewController;
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
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(Button.ClickEvent event)
				{
					try
					{
						getHomeFrameViewController().setWorkspaceContent(getApplicationViewLayoutController().getControlledView());
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
		if (applicationViewLayoutController == null)
		{
			applicationViewLayoutController = applicationViewLayoutControllers.get();
		}
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
		featureButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_32x32_APPLICATIONS));
	}

	private void doTheRefresh() throws WidgetException
	{
		refreshView();
	}

	private HomeFrameViewController getHomeFrameViewController()
	{
		if (homeFrameViewController == null)
		{
			homeFrameViewController = homeFrameViewControllers.get();
		}
		return homeFrameViewController;
	}

	@Override
	public boolean isActivated()
	{
		return configDataManager.loadValue(new StringBuilder(CONFIG_PREFIX)
				.append("applicationManager").append(CONFIG_IS_ACTIVE).toString(), Boolean.class, true);
	}
}
