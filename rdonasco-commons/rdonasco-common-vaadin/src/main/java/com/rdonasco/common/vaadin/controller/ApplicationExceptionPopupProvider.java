/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.common.vaadin.controller;

/**
 *
 * @author Roy F. Donasco
 */
public interface ApplicationExceptionPopupProvider
{

	void popUpInfoException(Throwable t);

	void popUpErrorException(Throwable t);

	void popUpWarningException(Throwable t);

	void popUpDebugException(Throwable t);
}
