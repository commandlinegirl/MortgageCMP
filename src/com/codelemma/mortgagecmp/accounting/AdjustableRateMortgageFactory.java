package com.codelemma.mortgagecmp.accounting;

import java.math.BigDecimal;
import java.util.Map;

public class AdjustableRateMortgageFactory implements MortgageFactory  {

	@Override
	public Mortgage createMortgage(Map<String,String> data) {
		
	  	String name = data.get("name");
    	BigDecimal price = new BigDecimal(data.get("purchase_price"));
    	BigDecimal downpayment = new BigDecimal(data.get("downpayment"));
    	BigDecimal interest_rate = new BigDecimal(data.get("interest_rate"));
    	
    	int adjustment_period = Integer.parseInt(data.get("adjustment_period"));; // how long the loan remains at the “initial interest rate”
    	int months_between_adjustments = Integer.parseInt(data.get("months_between_adjustments")); // how often, after the Adjustment Period can the interest rate increase
    	BigDecimal max_single_rate_adjustment = new BigDecimal(data.get("max_single_rate_adjustment"));
    	BigDecimal total_interest_cap = new BigDecimal(data.get("interest_rate_cap"));

    	int term_years = Integer.parseInt(data.get("term_years"));
    	int term_months = Integer.parseInt(data.get("term_months"));

    	BigDecimal property_insurance = new BigDecimal(data.get("property_insurance"));
    	BigDecimal property_tax = new BigDecimal(data.get("property_tax"));
    	BigDecimal pmi_rate = new BigDecimal(data.get("pmi_rate"));
    	BigDecimal closing_fees = new BigDecimal(data.get("closing_fees"));

    	BigDecimal extra_payment = new BigDecimal(data.get("extra_payment"));
    	int extra_payment_frequency = Integer.parseInt(data.get("extra_payment_frequency"));

    	int arm_type = Integer.parseInt(data.get("arm_type"));
    	
    	return new AdjustableRateMortgage.Builder()
    	    .name(name)
    	    .purchase_price(price)
    	    .downpayment(downpayment)
            .interest_rate(interest_rate)
            .adjustment_period(adjustment_period)
            .months_between_adjustments(months_between_adjustments)
            .max_single_rate_adjustment(max_single_rate_adjustment)
            .interest_rate_cap(total_interest_cap)
	    	.term_years(term_years)
	    	.term_months(term_months)
	    	.property_insurance(property_insurance)
	    	.property_tax(property_tax)
	    	.pmi_rate(pmi_rate)
	    	.closing_fees(closing_fees)
	    	.extra_payment(extra_payment)
	    	.extra_payment_frequency(extra_payment_frequency)
	    	.arm_type(arm_type)
    	    .build();
	}
}