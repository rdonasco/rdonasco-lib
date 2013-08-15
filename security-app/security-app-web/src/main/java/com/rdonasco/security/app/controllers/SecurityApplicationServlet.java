/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.app.controllers;

import com.rdonasco.security.services.SystemSecurityInitializerRemote;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import javax.ejb.EJB;
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
	
	@EJB
	private SystemSecurityInitializerRemote securityInitializerRemote;
	
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
		securityInitializerRemote.initializeDefaultSystemAccessCapabilities();
		return SecurityApplication.class;
	}
}
