package com.rdonasco.security.home.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.home.views.FeatureHomeButton;
import com.rdonasco.security.role.controllers.RoleViewLayoutController;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class RoleManagerHomeButtonController implements
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
	private Instance<RoleViewLayoutController> viewLayoutProvider;

	private RoleViewLayoutController roleViewLayoutController;

	public RoleViewLayoutController getRoleViewLayoutController()
	{
		if (roleViewLayoutController == null)
		{
			roleViewLayoutController = viewLayoutProvider.get();
		}
		return roleViewLayoutController;
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
				private static final long serialVersionUID = 1L;
				@Override
				public void buttonClick(Button.ClickEvent event)
				{
					homeFrameViewControllers.get().setWorkspaceContent(getRoleViewLayoutController().getControlledView());
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
		featureButton.setCaption(I18NResource.localize("Manage Roles"));
		featureButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_32x32_ROLES));
	}

	private void doTheRefresh() throws WidgetException
	{
		refreshView();
	}
}
