/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.captcha.views;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Embedded;

/**
 *
 * @author Roy F. Donasco
 */
public class EmbeddedCaptcha extends Embedded
{
	private static final long serialVersionUID = 1L;
	private String captchaText;

	public EmbeddedCaptcha()
	{
	}

	public EmbeddedCaptcha(String caption)
	{
		super(caption);
	}

	public EmbeddedCaptcha(String caption, Resource source)
	{
		super(caption, source);
	}

	public String getCaptchaText()
	{
		return captchaText;
	}
}
