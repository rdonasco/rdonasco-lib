/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.sample;

import com.rdonasco.config.services.ConfigDataManagerVODecoratorRemote;
import com.rdonasco.security.authentication.factories.LogonServiceFactory;
import com.rdonasco.security.authentication.services.DefaultLogonService;
import com.rdonasco.security.authentication.services.LoggedOnSession;
import com.rdonasco.security.authentication.services.LoggedOnSessionProvider;
import com.rdonasco.security.authentication.services.LogonService;
import com.rdonasco.security.exceptions.LogonServiceNotFoundException;
import com.rdonasco.security.vo.LogonVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
