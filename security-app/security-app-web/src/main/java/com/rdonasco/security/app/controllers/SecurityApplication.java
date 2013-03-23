/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.app.controllers;

import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.view.utils.NotificationHelper;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.home.controllers.HomeViewController;
import com.vaadin.Application;
import com.vaadin.ui.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
@SessionScoped
public class SecurityApplication extends Application implements
		ApplicationExceptionPopupProvider
{

	private static final Logger LOG = Logger.getLogger(SecurityApplication.class.getName());
	private static final long serialVersionUID = 1L;

	static
	{
		I18NResource.setBundle(java.util.ResourceBundle.getBundle("com/rdonasco/security/i18n/i18nResource"));
	}
	@Inject
	private HomeViewController homeViewController;
	private NotificationHelper notificationHelper;

	@Override
	public void init()
	{
		notificationHelper = new NotificationHelper();
		notificationHelper.setApplication(this);
		LOG.log(Level.INFO, "current theme: ", getTheme());
		Window mainWindow = new Window("rdonasco Security");
		setMainWindow(mainWindow);
		mainWindow.addComponent(homeViewController.getControlledView());
		setTheme(SecurityDefaultTheme.THEME_NAME);
	}

	@Override
	public void popUpInfoException(Throwable t)
	{
		LOG.log(Level.INFO, t.getMessage(), t);
		notificationHelper.showHumanNotification(t.getMessage());
	}

	@Override
	public void popUpErrorException(Throwable t)
	{
		LOG.log(Level.SEVERE, t.getMessage(), t);
		notificationHelper.showHumanErrorNotification(t.getMessage());
	}

	@Override
	public void popUpWarningException(Throwable t)
	{
		LOG.log(Level.WARNING, t.getMessage(), t);
		notificationHelper.showHumanWarningNotification(t.getMessage());

	}

	@Override
	public void popUpDebugException(Throwable t)
	{
		LOG.log(Level.FINE, t.getMessage(), t);
		notificationHelper.showHumanNotification(t.getMessage());

	}
}
