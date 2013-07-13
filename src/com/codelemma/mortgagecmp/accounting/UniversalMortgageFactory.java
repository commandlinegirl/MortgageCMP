package com.codelemma.mortgagecmp.accounting;

import java.util.Map;

public class UniversalMortgageFactory implements MortgageFactory {

	private Map<String, MortgageFactory> mortgage_factories;

	public UniversalMortgageFactory(Map<String, MortgageFactory> mortgage_factories) {
		this.mortgage_factories = mortgage_factories;
	}

	@Override
	public Mortgage createMortgage(Map<String, String> data) throws MortgageFactoryException {
		String factory_name = data.get("type");
        return mortgage_factories.get(factory_name).createMortgage(data);
	}
}