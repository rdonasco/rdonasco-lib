/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.home.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.common.vaadin.view.ButtonUtil;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.security.app.controllers.HttpSessionProvider;
import com.rdonasco.security.authentication.services.LoggedOnSessionProvider;
import com.rdonasco.security.home.views.ToolbarView;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
@SessionScoped
public class ToolbarController implements ViewController<ToolbarView>
{
	private static final long serialVersionUID = 1L;

	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	@Inject
	private ToolbarView toolbarView;
	private Button homeLink = new Button(I18NResource.localize("Home"));
	private Button profileLink = new Button(I18NResource.localize("Roy Donasco"));
	private Button logoutLink = new Button(I18NResource.localize("Logout"));
	private VerticalLayout spacer = new VerticalLayout();
	@Inject
	private Instance<HomeFrameViewController> homeFrameViewControllerProvider;
	@Inject
	private Instance<HttpSessionProvider> httpSessionInstanceProvider;
	@Inject
	private DefaultContentViewController homeViewController;

	@Inject
	private LoggedOnSessionProvider loggedOnSessionProvider;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			ButtonUtil.setupAllButtonStyle(BaseTheme.BUTTON_LINK, homeLink, profileLink, logoutLink);
			spacer.setWidth(6, Sizeable.UNITS_PIXELS);
			toolbarView.initWidget();
			toolbarView.addLeftToolbarComponent(homeLink);
			toolbarView.addRightToolbarComponent(profileLink);
			toolbarView.addRightToolbarComponent(spacer);
			toolbarView.addRightToolbarComponent(logoutLink);
			homeLink.addListener(new Button.ClickListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(Button.ClickEvent event)
				{
					homeFrameViewControllerProvider.get().setWorkspaceContent((Component) homeViewController.getControlledView());
				}
			});
			logoutLink.addListener(new Button.ClickListener()
			{
				@Override
				public void buttonClick(Button.ClickEvent event)
				{
					getControlledView().getApplication().close();
					httpSessionInstanceProvider.get().getSession().invalidate();					
				}
			});

		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	@Override
	public ToolbarView getControlledView()
	{
		return toolbarView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		if (loggedOnSessionProvider != null && loggedOnSessionProvider.getLoggedOnSession() != null && loggedOnSessionProvider.getLoggedOnSession().isLoggedOn())
		{
			profileLink.setCaption(loggedOnSessionProvider.getLoggedOnSession().getLoggedOnUser().getLogonId());
			profileLink.setVisible(true);
			logoutLink.setVisible(true);
		}
		else
		{
			profileLink.setVisible(false);
			logoutLink.setVisible(false);
		}

	}
}
