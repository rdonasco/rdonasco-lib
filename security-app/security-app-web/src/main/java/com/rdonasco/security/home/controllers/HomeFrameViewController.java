/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.home.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.security.home.views.HomeFrameViewLayout;
import com.rdonasco.security.authentication.services.LoggedOnSessionProvider;
import com.vaadin.ui.Component;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
@SessionScoped
public class HomeFrameViewController implements
		ViewController<HomeFrameViewLayout>, Serializable
{

	private static final long serialVersionUID = 1L;

	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@Inject
	private HomeFrameViewLayout homeFrame;

	@Inject
	private DefaultContentViewController homeViewController;

	@Inject
	private ToolbarController toolbarController;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			homeFrame.initWidget();
			refreshView();
		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
		catch (WidgetException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	@Override
	public HomeFrameViewLayout getControlledView()
	{
		return homeFrame;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		homeFrame.setToolbarContent(toolbarController.getControlledView());
		toolbarController.refreshView();
		setWorkspaceContent((Component) homeViewController.getControlledView());
	}

	public void setWorkspaceContent(Component workspaceContent)
	{
		homeFrame.setWorkspaceContent(workspaceContent);
	}
}
