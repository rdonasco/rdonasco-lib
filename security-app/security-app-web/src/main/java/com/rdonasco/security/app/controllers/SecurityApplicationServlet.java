/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.app.controllers;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Roy F. Donasco
 */
@WebServlet(name = "SecurityApplicationServlet", urlPatterns =
{
	"/*"
})
public class SecurityApplicationServlet extends AbstractApplicationServlet
{
	private static final long serialVersionUID = 1L;
	@Inject
	private SecurityApplication application;
	@Override
	protected Application getNewApplication(HttpServletRequest request) throws
			ServletException
	{
		return application;
	}

	@Override
	protected Class<? extends Application> getApplicationClass() throws
			ClassNotFoundException
	{
		return SecurityApplication.class;
	}
}
