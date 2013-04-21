/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 21-Apr-2013
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

package com.rdonasco.common.vaadin.validators;

import com.vaadin.data.Validator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.PasswordField;

/**
 *
 * @author Roy F. Donasco
 */
public class FieldMatchedValueValidator implements Validator
{
	private static final long serialVersionUID = 1L;

	public static FieldMatchedValueValidator createFieldMatchValidatorWithErrorMessage(
			AbstractField referencedField, String errorMessage)
	{
		return new FieldMatchedValueValidator(referencedField, errorMessage);
	}
	private AbstractField referencedField;
	private String errorMessage;

	private FieldMatchedValueValidator(AbstractField referencedField,
			String errorMessage)
	{
		this.referencedField = referencedField;
		this.errorMessage = errorMessage;
	}

	@Override
	public void validate(Object value) throws InvalidValueException
	{
		if (!isValid(value))
		{
			throw new InvalidValueException(errorMessage);
		}
	}

	@Override
	public boolean isValid(Object value)
	{
		return value.equals(referencedField.getValue());
	}
}
