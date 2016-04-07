package com.sauloborges.number26.exception;

public class TransactionNotBeParentForItSelfExcepetion extends Exception {
	
	private static final long serialVersionUID = 1L;

	public TransactionNotBeParentForItSelfExcepetion() {
		super("The transaction parent cannot be the same of transaction");
	}

}
