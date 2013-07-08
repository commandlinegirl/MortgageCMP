package com.codelemma.mortgagecmp.accounting;

import java.math.BigDecimal;
import java.util.HashMap;

public class FixedRateVariablePrincipalMortgageFactory implements MortgageFactory {

	@Override
	public Mortgage createMortgage(HashMap<String,String> data) {
	  	String name = data.get("name");
    	BigDecimal price = new BigDecimal(data.get("purchase_price"));
    	BigDecimal downpayment = new BigDecimal(data.get("downpayment"));
    	BigDecimal interest_rate = new BigDecimal(data.get("interest_rate"));
    	int term_years = Integer.parseInt(data.get("term_years"));
    	int term_months = Integer.parseInt(data.get("term_months"));
    	BigDecimal property_insurance = new BigDecimal(data.get("property_insurance"));
    	BigDecimal property_tax = new BigDecimal(data.get("property_tax"));
    	BigDecimal pmi_rate = new BigDecimal(data.get("pmi_rate"));
    	BigDecimal closing_fees = new BigDecimal(data.get("closing_fees"));
    	BigDecimal extra_payment = new BigDecimal(data.get("extra_payment"));
    	int extra_payment_frequency = Integer.parseInt(data.get("extra_payment_frequency"));

    	return new FixedRateVariablePrincipalMortgage.Builder()
    	    .name(name)
    	    .purchase_price(price)
    	    .downpayment(downpayment)
            .interest_rate(interest_rate)
	    	.term_years(term_years)
	    	.term_months(term_months)
	    	.property_insurance(property_insurance)
	    	.property_tax(property_tax)
	    	.pmi_rate(pmi_rate)
	    	.closing_fees(closing_fees)
	    	.extra_payment(extra_payment)
	    	.extra_payment_frequency(extra_payment_frequency)    	       
    	    .build();
	}
}
