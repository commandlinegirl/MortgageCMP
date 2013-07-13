package com.codelemma.mortgagecmp.accounting;

import java.util.Map;

public interface MortgageFactory {

	Mortgage createMortgage(Map<String, String> data) throws MortgageFactoryException;
	
	
	public static class MortgageFactoryException extends Exception {
		
	}
}