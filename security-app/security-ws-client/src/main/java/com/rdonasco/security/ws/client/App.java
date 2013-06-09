package com.rdonasco.security.ws.client;

import com.rdonasco.security.webservices.UserSecurityProfileVO;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App
{

	private static final Logger LOG = Logger.getLogger(App.class.getName());

	private class WebServiceClient implements Runnable
	{
		private static final String ADMIN = "admin";

		private int maxCallCount;

		public WebServiceClient(int maxCallCount)
		{
			this.maxCallCount = maxCallCount;
		}

		@Override
		public void run()
		{
			try
			{
				checkLogon();
				checkAccess();

			}
			catch (Exception ex)
			{
				LOG.log(Level.SEVERE, ex.getMessage(), ex);
			}

		}

		private void checkAccess()
		{
			// Call Web Service Operation
			com.rdonasco.security.webservices.SystemSecurityManager service = new com.rdonasco.security.webservices.SystemSecurityManager();
			com.rdonasco.security.webservices.SystemSecurityManagerWebService port = service.getSystemSecurityManagerWebServicePort();
			// TODO initialize WS operation arguments here;
			java.lang.String action = "logon";
			java.lang.String resource = "system";
			for (int i = 0; i < maxCallCount; i++)
			{
				try
				{
					UserSecurityProfileVO userSecurityProfileVO = port.logon(ADMIN, ADMIN);
					port.checkUserCapability(userSecurityProfileVO, action, resource);
					LOG.log(Level.INFO, "access allowed");
				}
				catch (Exception e)
				{
					LOG.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}

		private void checkLogon()
		{

			try
			{ // Call Web Service Operation
				com.rdonasco.security.webservices.SystemSecurityManager service = new com.rdonasco.security.webservices.SystemSecurityManager();
				com.rdonasco.security.webservices.SystemSecurityManagerWebService port = service.getSystemSecurityManagerWebServicePort();
				// TODO initialize WS operation arguments here
				java.lang.String logonID = ADMIN;
				java.lang.String password = ADMIN;
				// TODO process result here
				com.rdonasco.security.webservices.UserSecurityProfileVO result = port.logon(logonID, password);
				System.out.println("Result = " + result);
			}
			catch (Exception e)
			{
				LOG.log(Level.SEVERE, e.getMessage(), e);
			}

		}
	}

	public static void main(String[] args)
	{
		new App().startClients();
	}

	public void startClients()
	{
		Thread clientOne = new Thread(new WebServiceClient(5));
		clientOne.start();

	}
}
