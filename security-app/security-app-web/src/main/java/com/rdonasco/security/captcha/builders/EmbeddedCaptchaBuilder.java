/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.captcha.builders;

import com.rdonasco.security.captcha.builders.TextImageBuilder;
import com.rdonasco.security.captcha.exceptions.InvalidCaptchaParameterException;
import com.rdonasco.security.captcha.views.EmbeddedCaptcha;
import com.rdonasco.security.utils.PasswordGenerator;
import com.vaadin.Application;
import com.vaadin.terminal.StreamResource;
import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import nl.jamiecraane.imagegenerator.Margin;
import nl.jamiecraane.imagegenerator.Style;
import nl.jamiecraane.imagegenerator.TextImage;
import nl.jamiecraane.imagegenerator.imageexporter.ImageType;
import nl.jamiecraane.imagegenerator.imageexporter.ImageWriter;
import nl.jamiecraane.imagegenerator.imageexporter.ImageWriterFactory;

public class EmbeddedCaptchaBuilder
{

	private String caption = "captcha";
	private Application application;
	private Font font;
	private Color fontColor;
	private Style fontStyle;
	private Integer height;
	private Margin margin;
	private Integer width;

	public EmbeddedCaptchaBuilder()
	{
	}

	public EmbeddedCaptchaBuilder setCaption(String caption)
	{
		this.caption = caption;
		return this;
	}	

	public EmbeddedCaptchaBuilder setApplication(Application application)
	{
		this.application = application;
		return this;
	}

	public EmbeddedCaptchaBuilder setFont(Font font)
	{
		this.font = font;
		return this;
	}

	public EmbeddedCaptchaBuilder setFontColor(Color fontColor)
	{
		this.fontColor = fontColor;
		return this;
	}

	public EmbeddedCaptchaBuilder setHeight(Integer height)
	{
		this.height = height;
		return this;
	}

	public EmbeddedCaptchaBuilder setFontStyle(Style fontStyle)
	{
		this.fontStyle = fontStyle;
		return this;
	}

	public EmbeddedCaptchaBuilder setMargin(Margin margin)
	{
		this.margin = margin;
		return this;
	}

	public EmbeddedCaptchaBuilder setWidth(Integer width)
	{
		this.width = width;
		return this;
	}

	public EmbeddedCaptcha createEmbeddedCaptcha()
	{
		EmbeddedCaptcha embeddedCaptcha = null;
		try
		{
			if (null == application)
			{
				throw new Exception("Application object is required to create EmbeddedCaptcha");
			}
			String[] captchaWords = PasswordGenerator.generateCaptcha(5, 2);
			StringBuilder captchaStatement = new StringBuilder();
			for (int i = 0; i < captchaWords.length; i++)
			{
				if (i > 0)
				{
					captchaStatement.append(" ");
				}
				captchaStatement.append(captchaWords[i]);
			}
			TextImageBuilder textBuilder = new TextImageBuilder()
					.setFont(font)
					.setFontColor(fontColor)
					.setFontStyle(fontStyle)
					.setHeight(height)
					.setWidth(width)
					.setMargin(margin)
					.setText(captchaStatement.toString());
			TextImage textImage = textBuilder.createTextImage();
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageWriter imageWriter = ImageWriterFactory.getImageWriter(ImageType.PNG);
			imageWriter.writeImageToOutputStream(textImage, output);
			StreamResource.StreamSource source = new StreamResource.StreamSource()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public InputStream getStream()
				{
					return new ByteArrayInputStream(output.toByteArray());
				}
			};
			StreamResource resource = new StreamResource(source, captchaStatement.toString(), application);
			embeddedCaptcha = new EmbeddedCaptcha(caption, resource);
			embeddedCaptcha.setType(EmbeddedCaptcha.TYPE_IMAGE);

		}
		catch (Exception ex)
		{
			throw new InvalidCaptchaParameterException(ex);
		}
		return embeddedCaptcha;
	}
}
