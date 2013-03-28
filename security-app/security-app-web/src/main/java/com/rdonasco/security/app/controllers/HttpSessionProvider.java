/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.app.controllers;

import javax.servlet.http.HttpSession;

/**
 *
 * @author Roy F. Donasco
 */
public interface HttpSessionProvider
{

	HttpSession getSession();
}
