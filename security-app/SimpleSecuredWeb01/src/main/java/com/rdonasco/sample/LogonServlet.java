/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.sample;

import com.rdonasco.config.services.ConfigDataManagerVODecoratorRemote;
import com.rdonasco.security.authentication.factories.LogonServiceFactory;
import com.rdonasco.security.authentication.services.DefaultLogonService;
import com.rdonasco.security.authentication.services.LoggedOnSessionProvider;
import com.rdonasco.security.vo.LogonVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Roy F. Donasco
 */
@WebServlet(name = "LogonServlet", urlPatterns =
{
	"/logon"
})
public class LogonServlet extends HttpServlet
{

	private static final Logger LOG = Logger.getLogger(LogonServlet.class.getName());

	private static final long serialVersionUID = 1L;

	@EJB
	private ConfigDataManagerVODecoratorRemote configDataManager;

	@Inject
	private LogonServiceFactory logonServiceFactory;


	@Inject
	private LoggedOnSessionProvider loggedOnSessionProvider;

	/**
	 * Processes requests for both HTTP
	 * <code>GET</code> and
	 * <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException
	{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try
		{
			/* TODO output your page here. You may use following sample code. */
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet LogonServlet</title>");
			out.println("</head>");
			out.println("<body>");
			out.println(logon(request));
			out.println("<h1>Servlet LogonServlet at " + request.getContextPath() + "</h1>");
			out.println("<a href='"+request.getContextPath()+"'>Home</a>");
			out.println("</body>");
			out.println("</html>");
		}
		finally
		{
			out.close();
		}
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP
	 * <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException
	{
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP
	 * <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException
	{
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo()
	{
		return "Short description";
	}// </editor-fold>

	private String logon(HttpServletRequest request)
	{
		String message = "not logged on";
		try
		{
			String userID = request.getParameter("userID");
			String password = request.getParameter("password");
			setApplicationInfoOnLoggedOnSession();
			UserSecurityProfileVO profile = logonServiceFactory.createLogonService(configDataManager.loadValue("/security/logonService", String.class, DefaultLogonService.SERVICE_ID))
					.logon(new LogonVO(userID, password));
			message = "logged on profile = " + profile.getLogonId();
			loggedOnSessionProvider.getLoggedOnSession().setLoggedOnUser(profile);
			// reset the application information on the new logged on user session.
			setApplicationInfoOnLoggedOnSession();						
		}
		catch (Exception e)
		{
			message = e.getMessage();
			LOG.log(Level.SEVERE, e.getMessage(), e);
		}
		return message;
	}

	private void setApplicationInfoOnLoggedOnSession() throws
			UnknownHostException
	{
		loggedOnSessionProvider.getLoggedOnSession().setApplicationID(451L);
		loggedOnSessionProvider.getLoggedOnSession().setApplicationToken("EH1yeGZgmSXWaYom9giytW2KoZvso6HB");
		loggedOnSessionProvider.getLoggedOnSession().setHostNameOrIpAddress(InetAddress.getLocalHost().getHostName());
	}
}
