/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.captcha.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.common.vaadin.view.utils.EmbeddedResourceBuilder;
import com.rdonasco.security.captcha.exceptions.InvalidCaptchaParameterException;
import com.rdonasco.security.captcha.views.CaptchaView;
import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Embedded;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import nl.jamiecraane.imagegenerator.TextImage;
import nl.jamiecraane.imagegenerator.imageexporter.ImageType;
import nl.jamiecraane.imagegenerator.imageexporter.ImageWriter;
import nl.jamiecraane.imagegenerator.imageexporter.ImageWriterFactory;

/**
 *
 * @author Roy F. Donasco
 */
public class CaptchaViewController implements ViewController<CaptchaView>
{

	private static final Logger LOG = Logger.getLogger(CaptchaViewController.class.getName());
	private static final long serialVersionUID = 1L;
	private TextImage captchaImage;
	@Inject
	private CaptchaView captchaView;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;
	@Inject
	private Application application;

	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			if (captchaImage == null)
			{
				throw new InvalidCaptchaParameterException("captchaImage is required");
			}
			captchaView.initWidget();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageWriter imageWriter = ImageWriterFactory.getImageWriter(ImageType.PNG);
			imageWriter.writeImageToOutputStream(captchaImage, output);
//			File outFile = new File("/tmp/sample.png");
//			FileOutputStream fos = new FileOutputStream(outFile);
//			captchaImage.createPng(fos);
//			fos.close();
			output.close();
			EmbeddedResourceBuilder resourceBuilder = new EmbeddedResourceBuilder()
					.setByteSource(output.toByteArray())
					.setName("captcha")
					.setDescription("captcha")
					.setApplication(application);
			Embedded embeddedImage = resourceBuilder.createEmbedded();
			captchaView.setContent(embeddedImage);
			captchaView.setHeight(captchaImage.getHeight(), Sizeable.UNITS_PIXELS);
			captchaView.setWidth(captchaImage.getWidth(), Sizeable.UNITS_PIXELS);
		}
		catch (Exception ex)
		{
			LOG.log(Level.WARNING, ex.getMessage(), ex);
			if (null != exceptionPopupProvider)
			{
				exceptionPopupProvider.popUpErrorException(ex);
			}
		}

	}

	@Override
	public CaptchaView getControlledView()
	{
		return captchaView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setCaptchaImage(TextImage captchaImage)
	{
		this.captchaImage = captchaImage;
	}
}
