/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.captcha.builders;

import com.rdonasco.security.captcha.exceptions.InvalidCaptchaParameterException;
import com.rdonasco.security.captcha.views.EmbeddedCaptcha;
import com.rdonasco.security.utils.PasswordGenerator;
import com.vaadin.Application;
import com.vaadin.terminal.StreamResource;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import nl.jamiecraane.imagegenerator.Alignment;
import nl.jamiecraane.imagegenerator.Margin;
import nl.jamiecraane.imagegenerator.Style;
import nl.jamiecraane.imagegenerator.TextImage;
import nl.jamiecraane.imagegenerator.TextWrapper;
import nl.jamiecraane.imagegenerator.imageexporter.ImageType;
import nl.jamiecraane.imagegenerator.imageexporter.ImageWriter;
import nl.jamiecraane.imagegenerator.imageexporter.ImageWriterFactory;
import nl.jamiecraane.imagegenerator.impl.GreedyTextWrapper;
import nl.jamiecraane.imagegenerator.impl.TextImageImpl;

public class EmbeddedCaptchaBuilder
{

	@SuppressWarnings("deprecation")
	private TextWrapper textWrapper = new GreedyTextWrapper();
	private String caption = "captcha";
	private Application application;
	private Integer width = 150;
	private Integer height = 22;
//	private Font font = new Font("Sans-Serif", Font.BOLD, 24);
	private Font font = new Font("Courier-New", Font.BOLD, 24);
	private Color fontColor = Color.BLUE;
	private Style fontStyle = Style.PLAIN;
	private Margin margin = new Margin(4, 4, 4, 4);

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
			TextImage textImage = new TextImageImpl(width, height, margin);
			textImage.useFont(font);
			textImage.useColor(fontColor);
			textImage.useFontStyle(fontStyle);
			textImage.setTextAligment(Alignment.JUSTIFY);
			textImage.useTextWrapper(textWrapper);
			textImage.wrap(true).write(captchaStatement.toString());
			addWaterMark("../images/watermark.png", textImage);
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

	private void addWaterMark(String resourceRelativePath, TextImage textImage)
			throws IOException
	{
		InputStream waterMarkStream = getClass().getResourceAsStream(resourceRelativePath);
		BufferedImage waterMark = ImageIO.read(waterMarkStream);
		for (int x = 0; x < this.width; x += waterMark.getWidth())
		{
			for (int y = 0; y < height; y += waterMark.getHeight())
			{
				textImage.write(waterMark, x, y);
			}
		}

	}
}
