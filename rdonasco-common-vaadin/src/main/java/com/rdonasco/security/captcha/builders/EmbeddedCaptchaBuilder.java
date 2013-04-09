/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.captcha.builders;

import com.rdonasco.common.utils.NumberUtilities;
import com.rdonasco.common.utils.RandomTextGenerator;
import com.rdonasco.security.captcha.exceptions.InvalidCaptchaParameterException;
import com.rdonasco.security.captcha.views.EmbeddedCaptcha;
import com.vaadin.Application;
import com.vaadin.terminal.StreamResource;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
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

	private static final Logger LOG = Logger.getLogger(EmbeddedCaptchaBuilder.class.getName());
	@SuppressWarnings("deprecation")
	private TextWrapper textWrapper = new GreedyTextWrapper();
	private String caption = "captcha";
	private Application application;
	private Integer width = 300;
	private Integer height = 44;
	private Font font = new Font("Courier", Font.BOLD, 36);
	private Style fontStyle = Style.PLAIN;
	private Margin margin = new Margin(18, 14, 10, 14);
	private String captchaValue;
	private Color[] fontColors =
	{
		Color.BLACK,
		Color.BLUE,
		Color.CYAN,
		Color.DARK_GRAY,
		Color.GRAY,
		Color.GREEN,
		Color.LIGHT_GRAY,
		Color.MAGENTA,
		Color.ORANGE,
		Color.PINK,
		Color.RED
	};

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

	public EmbeddedCaptchaBuilder setFontColors(Color[] fontColors)
	{
		this.fontColors = fontColors;
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

	public String getCaptchaValue()
	{
		return captchaValue;
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
			String[] captchaWords = RandomTextGenerator.generateCaptcha(5, 2);
			StringBuilder captchaStatement = new StringBuilder();
			for (int i = 0; i < captchaWords.length; i++)
			{
				if (i > 0)
				{
					captchaStatement.append(" ");
				}
				captchaStatement.append(captchaWords[i]);
			}
			captchaValue = captchaStatement.toString();
			TextImage textImage = new TextImageImpl(width, height, margin);
			textImage.useFont(font);
			textImage.useFontStyle(fontStyle);
			textImage.setTextAligment(Alignment.JUSTIFY);
			textImage.useTextWrapper(textWrapper);
			addWaterMark("../images/brick.png", textImage);
			int xCharWidth = 2;
			for (int charNo = 0; charNo < captchaStatement.length(); charNo++)
			{
				textImage.useColor(getNextColor());
				String stringToWrite = captchaStatement.substring(charNo, charNo + 1);
				LOG.log(Level.INFO, "char: {0}", stringToWrite);
				textImage.addHSpace(xCharWidth).write(stringToWrite);
			}
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
			StreamResource resource = new StreamResource(source, String.valueOf(NumberUtilities.generateRandomLongValue()), application);
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

	private Color getNextColor()
	{
		int colorIndex = NumberUtilities.generateRandomIntValue(0, fontColors.length - 1);
		return fontColors[colorIndex];
	}
}
