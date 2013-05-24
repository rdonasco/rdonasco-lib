/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.home.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.config.services.ConfigDataManagerVODecoratorRemote;
import com.rdonasco.security.authentication.factories.SecuredLogonServiceFactory;
import com.rdonasco.security.home.views.ContentView;
import com.rdonasco.security.home.views.FeatureHomeButton;
import com.rdonasco.security.home.views.LoggedOnContentView;
import com.rdonasco.security.home.views.LoggedOutContentView;
import com.rdonasco.security.authentication.services.LoggedOnSessionProvider;
import com.rdonasco.security.exceptions.LogonServiceNotFoundException;
import com.rdonasco.security.authentication.services.LogonService;
import com.rdonasco.security.vo.LogonVO;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.LoginForm;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
@SessionScoped
public class DefaultContentViewController implements
		ViewController<ContentView>,
		Serializable
{

	private static final Logger LOG = Logger.getLogger(DefaultContentViewController.class.getName());

	private static final long serialVersionUID = 1L;

	@Inject
	private ApplicationExceptionPopupProvider applicationExceptionPopupProvider;

	@Inject
	private ApplicationPopupProvider applicationPopupProvider;

	@Inject
	private Instance<HomeViewButtonController<FeatureHomeButton>> buttonControllersProviders;

	@Inject
	private LoggedOnContentView loggedOnContentView;

	@Inject
	private LoggedOutContentView loggedOutContentView;

	@Inject
	private LoggedOnSessionProvider loggedOnSessionProvider;

	@Inject
	private SecuredLogonServiceFactory securedLogonServiceFactory;

	@EJB
	private ConfigDataManagerVODecoratorRemote configDataManager;

	@Inject
	private Instance<HomeFrameViewController> homeFrameViewControllerProvider;

	@Inject
	private LogonService logonService;

	private LogonService getLogonService() throws LogonServiceNotFoundException
	{
//		if (null == logonService)
//		{
//			logonService = securedLogonServiceFactory.createLogonService(configDataManager.loadValue("/security/logonService", String.class, SecuredLogonServiceDecorator.LOGON_SERVICE));
//		}
		return logonService;
	}

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			loggedOnContentView.initWidget();
			for (HomeViewButtonController<FeatureHomeButton> buttonController : buttonControllersProviders)
			{
				loggedOnContentView.addComponent((Component) buttonController.getControlledView());
			}
			loggedOutContentView.initWidget();
			loggedOutContentView.getLogonForm().addListener(new LoginForm.LoginListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onLogin(LoginForm.LoginEvent event)
				{
					try
					{
						LogonVO logonVO = new LogonVO(event.getLoginParameter("username"), event.getLoginParameter("password"));
						getLogonService().logon(logonVO);
						homeFrameViewControllerProvider.get().refreshView();
					}
					catch (Throwable ex)
					{
						applicationPopupProvider.popUpError(ex.getMessage());
					}
				}
			});
		}
		catch (WidgetInitalizeException ex)
		{
			LOG.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	@Override
	public ContentView getControlledView()
	{
		ContentView contentView;
		if (loggedOnSessionProvider.getLoggedOnSession().isLoggedOn())
		{
			contentView = loggedOnContentView;
		}
		else
		{
			contentView = loggedOutContentView;
		}
		return contentView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		getHomeView().removeAllComponents();
		getControlledView().initWidget();
	}

	private AbstractLayout getHomeView()
	{
		return (AbstractLayout) getControlledView();
	}
}
