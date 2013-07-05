package com.codelemma.mortgagecmp.accounting;

import java.util.HashMap;

public class UniversalMortgageFactory implements MortgageFactory {

	private HashMap<String, MortgageFactory> mortgage_factories;

	public UniversalMortgageFactory(HashMap<String, MortgageFactory> mortgage_factories) {
		this.mortgage_factories = mortgage_factories;
	}

	@Override
	public Mortgage createMortgage(HashMap<String, String> data) throws MortgageFactoryException {
		String factory_name = data.get("type");
        return mortgage_factories.get(factory_name).createMortgage(data);
	}
}