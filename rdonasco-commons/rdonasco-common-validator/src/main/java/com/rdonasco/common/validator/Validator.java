/*
 * Inspired by Vaadin Validator implementation
 */
package com.rdonasco.common.validator;

/**
 *
 * @author Roy F. Donasco
 */
public interface Validator<T>
{
	boolean isValid(T valueToValidate);
	void validate(T valueToValidate) throws InvalidValueException;
}
