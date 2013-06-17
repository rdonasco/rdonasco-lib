/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.common.vaadin.controller;

/**
 *
 * @author Roy F. Donasco
 */
public interface ApplicationPopupProvider
{

	void popUpInfo(String infoMessage);

	void popUpError(String errorMessage);

	void popUpWarning(String warningMessage);

	void showTrayNotification(String trayMessage);
}
