package com.codelemma.mortgagecmp.accounting;

import java.math.BigDecimal;
import java.util.HashMap;

public class FixedRateFixedPrincipalMortgageFactory implements MortgageFactory {

	@Override
	public Mortgage createMortgage(HashMap<String,String> data) {
	  	String name = data.get("mortgage_name");
    	BigDecimal price = new BigDecimal(data.get("mortgage_purchase_price"));
    	BigDecimal downpayment = new BigDecimal(data.get("mortgage_downpayment"));
    	BigDecimal interest_rate = new BigDecimal(data.get("mortgage_interest_rate"));
    	int term_years = Integer.parseInt(data.get("mortgage_term_years"));
    	int term_months = Integer.parseInt(data.get("mortgage_term_months"));
    	BigDecimal property_insurance = new BigDecimal(data.get("mortgage_property_insurance"));
    	BigDecimal property_tax = new BigDecimal(data.get("mortgage_property_tax"));
    	BigDecimal pmi = new BigDecimal(data.get("mortgage_pmi"));
    	BigDecimal closing_fees = new BigDecimal(data.get("mortgage_closing_fees"));
    	BigDecimal extra_payment = new BigDecimal(data.get("mortgage_extra_payment"));
    	int extra_payment_frequency = Integer.parseInt(data.get("mortgage_extra_payment_frequency"));

    	return new FixedRateFixedPrincipalMortgage.Builder()
    	    .name(name)
    	    .purchase_price(price)
    	    .downpayment(downpayment)
            .interest_rate(interest_rate)
	    	.term_years(term_years)
	    	.term_months(term_months)
	    	.property_insurance(property_insurance)
	    	.property_tax(property_tax)
	    	.pmi_rate(pmi)
	    	.closing_fees(closing_fees)
	    	.extra_payment(extra_payment)
	    	.extra_payment_frequency(extra_payment_frequency)    	       
    	    .build();
	}
}
