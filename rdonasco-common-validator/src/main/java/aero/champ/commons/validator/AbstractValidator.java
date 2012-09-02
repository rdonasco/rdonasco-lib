/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aero.champ.commons.validator;

/**
 *
 * @author Roy F. Donasco
 */
public abstract class AbstractValidator<T> implements Validator<T>
{
	private String invalidValueMessage = "Invalid value {0}";

	public void setInvalidValueMessage(String invalidValueMessage)
	{
		this.invalidValueMessage = invalidValueMessage;
	}

	public String getInvalidValueMessage()
	{
		return invalidValueMessage;
	}
	
	public abstract boolean isValid(T valueToValidate);

	public void validate(T valueToValidate) throws InvalidValueException
	{
		setInvalidValueMessage("Invalid value {0}".replace("{0}", invalidValueMessage));
		if(!isValid(valueToValidate))
		{
			throw new InvalidValueException(getInvalidValueMessage());
		}
	}

	


}
