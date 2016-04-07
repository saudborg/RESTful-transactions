package com.sauloborges.number26.exception;

public class TransactionNotFoundException extends Exception {

	private static final long serialVersionUID = 7498954487695684150L;
	
	public TransactionNotFoundException(Long transactionId) {
		super("This transaction cannot be parent, it does't exists: " + transactionId);
	}

}
