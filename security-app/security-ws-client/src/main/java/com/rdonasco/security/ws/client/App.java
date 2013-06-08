package com.rdonasco.security.ws.client;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App
{
	private static final Logger LOG = Logger.getLogger(App.class.getName());

	public static void main(String[] args)
	{
		System.out.println("Hello World!");

		try
		{ // Call Web Service Operation
			com.rdonasco.security.webservices.SystemSecurityManagerWebService_Service service = new com.rdonasco.security.webservices.SystemSecurityManagerWebService_Service();
			com.rdonasco.security.webservices.SystemSecurityManagerWebService port = service.getSystemSecurityManagerWebServicePort();
			// TODO initialize WS operation arguments here
			com.rdonasco.security.webservices.AccessRightsVO accessRight = new com.rdonasco.security.webservices.AccessRightsVO();

			// TODO process result here
			java.lang.Boolean result = port.hasAccessRights(accessRight);
			System.out.println("Result = " + result);
		}
		catch (Exception ex)
		{
			LOG.log(Level.SEVERE, ex.getMessage(), ex);
		}

	}
}
