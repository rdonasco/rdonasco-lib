package com.rdonasco.security.home.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.config.services.ConfigDataManagerVODecoratorRemote;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.group.controllers.GroupViewLayoutController;
import static com.rdonasco.security.home.controllers.HomeViewButtonController.CONFIG_IS_ACTIVE;
import static com.rdonasco.security.home.controllers.HomeViewButtonController.CONFIG_PREFIX;
import com.rdonasco.security.home.views.FeatureHomeButton;
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
public class GroupManagerHomeButtonController implements
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
	private Instance<GroupViewLayoutController> viewLayoutProvider;
	private GroupViewLayoutController groupViewLayoutController;
	@EJB
	private ConfigDataManagerVODecoratorRemote configDataManager;

	public GroupViewLayoutController getGroupViewLayoutController()
	{
		if (null == groupViewLayoutController)
		{
			groupViewLayoutController = viewLayoutProvider.get();
		}
		return groupViewLayoutController;
	}

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
						homeFrameViewControllers.get().setWorkspaceContent(getGroupViewLayoutController().getControlledView());
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
		featureButton.setCaption(I18NResource.localize("Manage Groups"));
		featureButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_32x32_GROUPS));
	}

	private void doTheRefresh() throws WidgetException
	{
		refreshView();
	}

	@Override
	public boolean isActivated()
	{
		return configDataManager.loadValue(new StringBuilder(CONFIG_PREFIX)
				.append("groupManager").append(CONFIG_IS_ACTIVE).toString(), Boolean.class, true);
	}
}
