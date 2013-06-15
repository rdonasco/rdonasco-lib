/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.sample;

import com.rdonasco.security.services.SystemSecurityManagerRemote;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.io.IOException;
import javax.ejb.EJB;
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
@WebServlet(name = "ConfirmUserRegistrationServlet", urlPatterns =
{
	"/confirmUserRegistration"
})
public class ConfirmUserRegistrationServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@EJB
	private SystemSecurityManagerRemote systemSecurityManager;

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
		RequestDispatcher dispatcher;
		try
		{
			dispatcher = request.getRequestDispatcher("/logonForm.jsp");
			UserSecurityProfileVO userProfile = systemSecurityManager.findSecurityProfileWithLogonID(request.getParameter("logonId"));
			String token = request.getParameter("registrationToken");

			if (token.equals(userProfile.getRegistrationToken()))
			{
				systemSecurityManager.setupDefaultCapabilitiesForUser(userProfile);
			}
			else
			{
				throw new Exception("Invalid Registration Token");
			}
			// you may also check if the token is already expired;
		}
		catch (Exception e)
		{
			request.setAttribute("exception", e);
			dispatcher = request.getRequestDispatcher("/error.jsp");
		}
		dispatcher.forward(request, response);
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
