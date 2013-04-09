/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 09-Apr-2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rdonasco.common.vaadin.captcha.views;

import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.captcha.builders.EmbeddedCaptchaBuilder;
import com.vaadin.data.Validator;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import java.io.InputStream;

/**
 *
 * @author Roy F. Donasco
 */
public class CaptchaField extends VerticalLayout
{

	private static final long serialVersionUID = 1L;
	private TextField captchaTextField;
	private EmbeddedCaptchaBuilder captchaBuilder = new EmbeddedCaptchaBuilder();
	private VerticalLayout captchaHolder = new VerticalLayout();
	private Button refreshButton = new Button();

	public CaptchaField()
	{
		setCaption(I18NResource.localize("Captcha"));
		captchaTextField = new TextField();
	}

	public CaptchaField(TextField textField)
	{
		this.captchaTextField = textField;
		setCaption(captchaTextField.getCaption());
		textField.setCaption(null);
	}

	private void wireCatpchaImageAndTextField()
	{
		configureRefreshButton();
		captchaBuilder.setApplication(getApplication());
		addComponent(captchaHolder);
		HorizontalLayout fieldLayout = new HorizontalLayout();
		fieldLayout.addComponent(captchaTextField);
		fieldLayout.addComponent(refreshButton);
		addComponent(fieldLayout);
		captchaTextField.setNullRepresentation("");
		captchaTextField.setDescription(I18NResource.localize("Please type the words printed in the captcha image"));
		this.captchaTextField.addValidator(new Validator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(Object value) throws
					Validator.InvalidValueException
			{
				if (!isValid(value))
				{
					throw new Validator.InvalidValueException(I18NResource.localize("Invalid captcha value"));
				}
			}

			@Override
			public boolean isValid(Object value)
			{
				return captchaBuilder.getCaptchaValue().equals(value);
			}
		});
	}

	public void generateCaptchaImage()
	{
		captchaBuilder.setApplication(getApplication());
		captchaBuilder.setCaption(null);
		captchaHolder.removeAllComponents();
		captchaHolder.addComponent(captchaBuilder.createEmbeddedCaptcha());
	}

	public TextField getCaptchaTextField()
	{
		return captchaTextField;
	}

	@Override
	public void attach()
	{
		super.attach();
		wireCatpchaImageAndTextField();
		generateCaptchaImage();

	}

	private Resource getRefreshIconResource()
	{
		StreamResource.StreamSource source = new StreamResource.StreamSource()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public InputStream getStream()
			{
				return getIconResourceInputStream();
			}
		};
		StreamResource resource = new StreamResource(source, "captchaRefresh.png", getApplication());

		return resource;
	}

	private InputStream getIconResourceInputStream()
	{
		return getClass().getResourceAsStream("images/refresh.png");
	}

	private void configureRefreshButton()
	{
		refreshButton.setIcon(getRefreshIconResource());
		refreshButton.setStyleName(BaseTheme.BUTTON_LINK);
		refreshButton.setDescription(I18NResource.localize("Refresh Captcha Image"));
		refreshButton.addListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				generateCaptchaImage();
			}
		});
	}
}
