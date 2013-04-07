/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.captcha.builders;

import com.rdonasco.security.captcha.exceptions.InvalidCaptchaParameterException;
import java.awt.Color;
import java.awt.Font;
import nl.jamiecraane.imagegenerator.Margin;
import nl.jamiecraane.imagegenerator.Style;
import nl.jamiecraane.imagegenerator.TextImage;
import nl.jamiecraane.imagegenerator.impl.TextImageImpl;

class TextImageBuilder
{

	private Integer width = 140;
	private Integer height = 20;
	private Font font = new Font("Sans-Serif", Font.BOLD, 16);
	private Color fontColor = Color.BLUE;
	private Style fontStyle = Style.PLAIN;
	private Margin margin = new Margin(5, 5);
	private String text;

	public TextImageBuilder()
	{
	}

	public TextImageBuilder setWidth(Integer width)
	{
		if (width != null)
		{
			this.width = width;
		}
		return this;
	}

	public TextImageBuilder setHeight(Integer height)
	{
		if (height != null)
		{
			this.height = height;
		}
		return this;
	}

	public TextImageBuilder setFont(Font font)
	{
		if (font != null)
		{
			this.font = font;
		}
		return this;
	}

	public TextImageBuilder setFontColor(Color fontColor)
	{
		if (fontColor != null)
		{
			this.fontColor = fontColor;
		}
		return this;
	}

	public TextImageBuilder setFontStyle(Style fontStyle)
	{
		if (fontStyle != null)
		{
			this.fontStyle = fontStyle;
		}
		return this;
	}

	public TextImageBuilder setMargin(Margin margin)
	{
		if (margin != null)
		{
			this.margin = margin;
		}
		return this;
	}

	public TextImageBuilder setText(String text)
	{
		this.text = text;
		return this;
	}

	public TextImage createTextImage()
	{
		if (null == text || text.isEmpty())
		{
			throw new InvalidCaptchaParameterException("text is mandatory");
		}
		TextImage textImage = new TextImageImpl(width, height, margin);
		textImage.useFont(font);
		textImage.useColor(fontColor);
		textImage.useFontStyle(fontStyle);
		textImage.write(text);
		return textImage;
	}
}
