/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.controllers;

import com.vaadin.event.MouseEvents;

/**
 *
 * @author Roy F. Donasco
 */
public interface ClickListenerProvider<T>
{
	MouseEvents.ClickListener provideClickListenerFor(T data);
}
