package com.codelemma.mortgagecmp.accounting;

public interface AccountSaver {
	void saveAccount(Account account) throws AccountSaverException;
	void saveMortgage(Mortgage mortgage) throws AccountSaverException;	
	void removeMortgage(Mortgage mortgage) throws AccountSaverException;
	void clear() throws AccountSaverException;

	static class AccountSaverException extends Exception {
		public AccountSaverException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
