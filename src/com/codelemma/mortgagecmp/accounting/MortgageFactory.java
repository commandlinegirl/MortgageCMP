package com.codelemma.mortgagecmp.accounting;

import java.util.HashMap;

public interface MortgageFactory {

	Mortgage createMortgage(HashMap<String, String> data) throws MortgageFactoryException;
	
	
	public static class MortgageFactoryException extends Exception {
		
	}
}