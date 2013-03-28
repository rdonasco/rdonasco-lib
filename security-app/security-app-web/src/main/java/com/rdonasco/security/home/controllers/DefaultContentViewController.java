/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.home.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.security.home.views.FeatureHomeButton;
import com.rdonasco.security.home.views.DefaultContentView;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
@SessionScoped
public class DefaultContentViewController implements ViewController<DefaultContentView>,
		Serializable
{

//	@EJB
//	private SecureWorldLocal secureWorld;// = lookupSecureWorldLocal();
	private static final Logger LOG = Logger.getLogger(DefaultContentViewController.class.getName());
	private static final long serialVersionUID = 1L;
	@Inject
	private DefaultContentView homeView;
	@Inject
	private Instance<HomeViewButtonController<FeatureHomeButton>> buttonControllersProviders;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			homeView.initWidget();
			for (HomeViewButtonController<FeatureHomeButton> buttonController : buttonControllersProviders)
			{
				homeView.addComponent(buttonController.getControlledView());
			}
		}
		catch (WidgetInitalizeException ex)
		{
			LOG.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	@Override
	public DefaultContentView getControlledView()
	{
		return homeView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		homeView.removeAllComponents();
		homeView.initWidget();
	}

}
