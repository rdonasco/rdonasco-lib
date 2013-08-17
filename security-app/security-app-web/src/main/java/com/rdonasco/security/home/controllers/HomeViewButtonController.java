/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.home.controllers;

import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.security.home.views.FeatureHomeButton;

/**
 *
 * @author Roy F. Donasco
 */
public interface HomeViewButtonController<T extends FeatureHomeButton> extends
		ViewController<T>
{
	String CONFIG_PREFIX = "/home/buttons/";
	String CONFIG_IS_ACTIVE = "/active";
	boolean isActivated();
}
