package com.codelemma.mortgagecmp.accounting;

/**
 * Factory(dp) for default Account objects. The objects come pre-populated with
 * checking account and other basic settings. This factory is safe in that
 * it throws no exceptions. It is also the place where default configuration
 * for account objects is stored.
 */
public class DefaultAccountFactory implements AccountFactory {
    @Override
    public Account loadAccount() {
    	Account account = new Account();
    	return account;
    }
}