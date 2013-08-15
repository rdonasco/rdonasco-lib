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
import com.rdonasco.security.capability.controllers.CapabilityViewLayoutController;
import static com.rdonasco.security.home.controllers.HomeViewButtonController.CONFIG_IS_ACTIVE;
import static com.rdonasco.security.home.controllers.HomeViewButtonController.CONFIG_PREFIX;
import com.rdonasco.security.home.views.FeatureHomeButton;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CapabilityManagerHomeButtonController implements
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
	private Instance<CapabilityViewLayoutController> viewLayoutProvider;
	private CapabilityViewLayoutController capabilityViewLayoutController;
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
						homeFrameViewControllers.get().setWorkspaceContent(getCapabilityViewLayoutController().getControlledView());
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

	protected CapabilityViewLayoutController getCapabilityViewLayoutController()
	{
		if (capabilityViewLayoutController == null)
		{
			capabilityViewLayoutController = viewLayoutProvider.get();
		}
		else
		{
			try
			{
				capabilityViewLayoutController.refreshView();
			}
			catch (WidgetException ex)
			{
				exceptionPopupProvider.popUpErrorException(ex);
			}
		}
		return capabilityViewLayoutController;
	}
@Override
	public boolean isActivated()
	{
		return configDataManager.loadValue(new StringBuilder(CONFIG_PREFIX)
				.append("capabilityManager").append(CONFIG_IS_ACTIVE).toString()
				, Boolean.class, true);
	}	
}
