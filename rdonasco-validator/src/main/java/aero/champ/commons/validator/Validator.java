/*
 * Inspired by Vaadin Validator implementation
 */
package aero.champ.commons.validator;

/**
 *
 * @author Roy F. Donasco
 */
public interface Validator<T>
{
	boolean isValid(T valueToValidate);
	void validate(T valueToValidate) throws InvalidValueException;
	void setInvalidValueMessage(String invalidValueMessage);
}
