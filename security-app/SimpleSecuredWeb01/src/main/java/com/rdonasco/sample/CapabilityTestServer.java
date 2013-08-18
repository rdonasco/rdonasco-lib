/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.sample;

import com.rdonasco.security.authentication.services.LoggedOnSessionProvider;
import com.rdonasco.security.authentication.services.SessionSecurityChecker;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Roy F. Donasco
 */
@WebServlet(name = "CapabilityTestServer", urlPatterns =
{
	"/capabilityTest"
})
public class CapabilityTestServer extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	public static final String ACTION = "action";
	public static final String RESOURCE = "resource";
	public static final String MESSAGE = "message";
	private static final Logger LOG = Logger.getLogger(CapabilityTestServer.class.getName());

	
	@Inject
	private SessionSecurityChecker sessionSecurityChecker;
	
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
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/capabilityTest.jsp");
		try
		{
			final String action = request.getParameter(ACTION);
			final String resource = request.getParameter(RESOURCE);
			request.setAttribute(ACTION, action);
			request.setAttribute(RESOURCE, resource);
			request.setAttribute(MESSAGE, "testing");
			if(null == action || action.isEmpty() || null == resource || resource.isEmpty())
			{
				throw new ServletException("Empty parameters");
			}
				
			sessionSecurityChecker.checkCapabilityTo(action, resource);
			request.setAttribute(MESSAGE, "allowed");
		}
		catch(Exception e)
		{
			LOG.log(Level.WARNING, e.getMessage(), e);
			if(null == e.getCause())
			{
				request.setAttribute(MESSAGE, e.getMessage());
			}
			else
			{
				request.setAttribute(MESSAGE, e.getCause().getMessage());
			}
		}
		finally
		{			
		}
		requestDispatcher.forward(request, response);
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
}
