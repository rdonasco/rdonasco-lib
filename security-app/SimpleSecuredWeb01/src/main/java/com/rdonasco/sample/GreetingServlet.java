/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.sample;

import com.rdonasco.security.authentication.services.SessionSecurityChecker;
import com.rdonasco.security.authorization.interceptors.SecurityExceptionHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Instance;
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
@WebServlet(name = "GreetingServlet", urlPatterns =
{
	"/greeting"
})
public class GreetingServlet extends HttpServlet implements
		SecurityExceptionHandler
{

	private static final Logger LOG = Logger.getLogger(GreetingServlet.class.getName());

	private static final long serialVersionUID = 1L;

	@Inject
	private SampleLoggedOnSessionProvider loggedOnSessionProvider;

	@Inject
	private SessionSecurityChecker sessionSecurityChecker;

	@Inject
	private Instance<GreetingServiceDecorator> greetingServiceInstances;
	private GreetingServiceDecorator greetingServiceInstance;

	public GreetingServiceDecorator getGreetingServiceInstance()
	{
		if (null == greetingServiceInstance)
		{
			greetingServiceInstance = greetingServiceInstances.get();
		}
		return greetingServiceInstance;
	}

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
			out.println("<title>Servlet GreetingServlet</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet GreetingServlet at " + request.getContextPath() + "</h1>");
			out.println(printHello());
			out.println("<br/>");
			out.println(printHelloFromEJB());
			out.println("<br/>");
			out.println(new StringBuilder()
					.append("<a href='").append(request.getContextPath()).append("/").append("'>Home</a>")
					);
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

	private String printHello()
	{
		String message = "can't say it";
		try
		{
			sessionSecurityChecker.hasTheCapabilityTo("print", "hello");
			message = "hello " + loggedOnSessionProvider.getLoggedOnSession().getLoggedOnUser().getLogonId();
		}
		catch (Exception e)
		{
			LOG.log(Level.WARNING, e.getMessage(), e);
		}
		return message;
	}


	public String printHelloFromEJB()
	{
		String message = "can't say it from EJB";
		try
		{
			message = getGreetingServiceInstance().getGreetingMessage("ejbMessage");
		}
		catch (Exception e)
		{
			LOG.log(Level.WARNING, e.getMessage(), e);
		}
		return message;
	}

	@Override
	public void handleSecurityException(Throwable e)
	{
		LOG.log(Level.WARNING, e.getMessage(), e);
	}
}
