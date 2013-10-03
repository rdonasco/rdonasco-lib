/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.sample;

import com.rdonasco.security.authentication.services.LoggedOnSession;
import com.rdonasco.security.authentication.services.LoggedOnSessionProvider;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
@SessionScoped
public class SampleLoggedOnSessionProvider implements LoggedOnSessionProvider,
		Serializable
{

	private static final long serialVersionUID = 1L;

	@Inject
	private LoggedOnSession loggedOnSession;

	@Override
	public LoggedOnSession getLoggedOnSession()
	{
		return loggedOnSession;
	}
}
