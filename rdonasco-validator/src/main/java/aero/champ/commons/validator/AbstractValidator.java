/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aero.champ.commons.validator;

import com.rdonasco.common.i18.I18NResource;

/**
 *
 * @author Roy F. Donasco
 */
public abstract class AbstractValidator<T> implements Validator<T>
{
	private String invalidValueMessage;

	public void setInvalidValueMessage(String invalidValueMessage)
	{
		this.invalidValueMessage = invalidValueMessage;
	}

	public abstract boolean isValid(T valueToValidate);

	public void validate(T valueToValidate) throws InvalidValueException
	{
		if(!isValid(valueToValidate))
		{
			throw new InvalidValueException(I18NResource.localizeWithParameter(invalidValueMessage,valueToValidate));
		}
	}

	


}
