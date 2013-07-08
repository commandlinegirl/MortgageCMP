package com.codelemma.mortgagecmp.accounting;

public interface AccountFactory {
	Account loadAccount() throws AccountFactoryException;

	static class AccountFactoryException extends Exception {
		public AccountFactoryException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}