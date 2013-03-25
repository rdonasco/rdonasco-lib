/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.app.controllers;

import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.view.utils.NotificationHelper;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.home.controllers.HomeFrameViewController;
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
		ApplicationExceptionPopupProvider, ApplicationPopupProvider
{

	private static final Logger LOG = Logger.getLogger(SecurityApplication.class.getName());
	private static final long serialVersionUID = 1L;

	static
	{
		I18NResource.setBundle(java.util.ResourceBundle.getBundle("com/rdonasco/security/i18n/i18nResource"));
	}
	@Inject
	private HomeFrameViewController homeFrameController;
	private NotificationHelper notificationHelper;

	@Override
	public void init()
	{
		notificationHelper = new NotificationHelper();
		notificationHelper.setApplication(this);
		LOG.log(Level.INFO, "current theme: ", getTheme());
		Window mainWindow = new Window("rdonasco Security");
		setMainWindow(mainWindow);
		mainWindow.addComponent(homeFrameController.getControlledView());
		setTheme(SecurityDefaultTheme.THEME_NAME);
	}

	@Override
	public void popUpInfoException(Throwable t)
	{
		LOG.log(Level.INFO, t.getMessage(), t);
		popUpInfo(t.getMessage());
	}

	@Override
	public void popUpErrorException(Throwable t)
	{
		LOG.log(Level.SEVERE, t.getMessage(), t);
		popUpError(t.getMessage());
	}

	@Override
	public void popUpWarningException(Throwable t)
	{
		LOG.log(Level.WARNING, t.getMessage(), t);
		popUpWarning(t.getMessage());

	}

	@Override
	public void popUpDebugException(Throwable t)
	{
		LOG.log(Level.FINE, t.getMessage(), t);
		popUpInfo(t.getMessage());

	}

	@Override
	public void popUpInfo(String infoMessage)
	{
		notificationHelper.showHumanNotification(infoMessage);
	}

	@Override
	public void popUpError(String errorMessage)
	{
		notificationHelper.showHumanErrorNotification(errorMessage);
	}

	@Override
	public void popUpWarning(String warningMessage)
	{
		notificationHelper.showHumanWarningNotification(warningMessage);
	}

	@Override
	public void showTrayNotification(String trayMessage)
	{
		notificationHelper.showTrayNotification(trayMessage);
	}
}
