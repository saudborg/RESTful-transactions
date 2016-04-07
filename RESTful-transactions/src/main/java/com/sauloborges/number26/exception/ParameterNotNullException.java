package com.sauloborges.number26.exception;

public class ParameterNotNullException extends Exception {

	private static final long serialVersionUID = 7726418008445990341L;
	
	public ParameterNotNullException(String parameter) {
		super("Parameter cannot be null: " + parameter);
	}

}
