package com.codelemma.mortgagecmp.accounting;

import java.math.BigDecimal;

public class Mortgage  {
 
    private BigDecimal purchase_price;
    private BigDecimal init_interest_rate;
    private BigDecimal interest_rate_decimal_monthly;
    private int term_years; // in months
    private int term_months; // in months
    private int total_term_months; // in months
    private BigDecimal downpayment;
    private BigDecimal base_monthly_payment;
    private BigDecimal monthly_payment;
    private BigDecimal property_insurance;
    private BigDecimal property_tax;
    private BigDecimal pmi;
    private BigDecimal monthly_pmi_amount;    
    private BigDecimal monthly_insurance_amount;  
    private BigDecimal monthly_tax_amount;  
    
    private BigDecimal total_insurance;
    private BigDecimal total_tax;
    private BigDecimal total_pmi;
    
    private BigDecimal additional_cost = Money.ZERO; //tax & insurance
    private BigDecimal additional_cost_with_pmi; 
    private BigDecimal additional_cost_without_pmi; 
    private BigDecimal total_additional_cost = Money.ZERO;
  
    private BigDecimal principal_paid = Money.ZERO;
    private BigDecimal interests_paid = Money.ZERO;
    private BigDecimal total_interests = Money.ZERO;
    private BigDecimal total_principal = Money.ZERO;
    
    private BigDecimal property_insurance_decimal_monthly;
    private BigDecimal property_insurance_decimal;
    private BigDecimal property_tax_decimal_monthly;
    private BigDecimal property_tax_decimal;
    private BigDecimal pmi_decimal_monthly;
    
    private BigDecimal outstanding_loan;   
    private BigDecimal loan_amount;
    private int month_counter = 1;
    private String name;
    private int id;
    
    private BigDecimal extra_payment;
    private int extra_payment_frequency;
    
    private HistoryMortgage history;
    
    public Mortgage(String _name, 
    		BigDecimal _purchase_price,
    		BigDecimal _downpayment,
    		BigDecimal _interest_rate,
    		int _term_years,
    		int _term_months,
    		BigDecimal _property_insurance,
    		BigDecimal _property_tax,
    		BigDecimal _pmi,
    		BigDecimal extra_payment,
    		int extra_payment_frequency) {
    	name = _name;
    	purchase_price = _purchase_price;
    	downpayment = Money.scale(_downpayment);
    	loan_amount = Money.scale(_purchase_price.subtract(_downpayment));
    	outstanding_loan = loan_amount; //TODO check if not < 0!
    	this.extra_payment = extra_payment;
        this.extra_payment_frequency = extra_payment_frequency;
    	init_interest_rate = _interest_rate;
        interest_rate_decimal_monthly = _interest_rate.divide(new BigDecimal(1200), Money.DECIMALS, Money.ROUNDING_MODE);

    	property_insurance = _property_insurance;
    	property_insurance_decimal = _property_insurance.divide(new BigDecimal(100), Money.DECIMALS, Money.ROUNDING_MODE);
    	property_insurance_decimal_monthly = property_insurance_decimal.divide(new BigDecimal(12), Money.DECIMALS, Money.ROUNDING_MODE);
    	monthly_insurance_amount = Money.getPercentage(purchase_price, property_insurance_decimal_monthly);
    	total_insurance = Money.getPercentage(purchase_price, property_insurance_decimal).multiply(new BigDecimal(_term_years)); //TODO: add monthly part here

    	property_tax = _property_tax;
    	property_tax_decimal = _property_tax.divide(new BigDecimal(100), Money.DECIMALS, Money.ROUNDING_MODE);
    	property_tax_decimal_monthly = property_tax_decimal.divide(new BigDecimal(12), Money.DECIMALS, Money.ROUNDING_MODE);
    	monthly_tax_amount = Money.getPercentage(purchase_price, property_tax_decimal_monthly);

    	pmi = _pmi;
    	pmi_decimal_monthly = _pmi.divide(new BigDecimal(1200), Money.DECIMALS, Money.ROUNDING_MODE);
    	monthly_pmi_amount = Money.getPercentage(loan_amount,  pmi_decimal_monthly);

    	term_years = _term_years;
    	term_months = _term_months; 
    	total_term_months = _term_years * 12 + _term_months; 

    	additional_cost_without_pmi = monthly_insurance_amount.add(monthly_tax_amount);
    	additional_cost_with_pmi = monthly_insurance_amount.add(monthly_tax_amount).add(monthly_pmi_amount);

    	calculateMonthlyPayment();
    	monthly_payment = base_monthly_payment;

    	history = new HistoryMortgage(this, total_term_months);
    }
    
    private void calculateMonthlyPayment() {
    	BigDecimal factor = (interest_rate_decimal_monthly.add(Money.ONE)).pow(total_term_months);
    	BigDecimal factor_minus_one = factor.subtract(Money.ONE);
    	try {
    		factor.divide(factor_minus_one, Money.DECIMALS, Money.ROUNDING_MODE);
    	} catch (IllegalArgumentException iae) {
    		iae.printStackTrace();
    	}
    	   	
    	base_monthly_payment = Money.scale(interest_rate_decimal_monthly.multiply(
    			loan_amount.multiply(factor.divide(factor_minus_one, Money.DECIMALS, Money.ROUNDING_MODE))));
    }
    
    public void recalculate(int index, int year, int month) {    	
        advance(year, month);   		
        history.add(index, year, this);
    }

    public void advance(int year, int month) {
    	if (month_counter <= total_term_months && outstanding_loan.compareTo(Money.ZERO) == 1) {
        	// Calculate interests to be paid this month and total interest paid so far
            interests_paid = Money.scale(outstanding_loan.multiply(interest_rate_decimal_monthly));
            total_interests = total_interests.add(interests_paid);
            
            BigDecimal ltv = (total_principal.add(downpayment)).divide(purchase_price, Money.DECIMALS, Money.ROUNDING_MODE);        	
            if (ltv.compareTo(new BigDecimal(0.2)) == -1) {
            	additional_cost = additional_cost_with_pmi;            	 
            	//Log.d("monthly_payment with pmi", monthly_payment.toString());
            	total_pmi = total_pmi.add(monthly_pmi_amount);
            } else {
            	additional_cost = additional_cost_without_pmi;
            	//Log.d("monthly_payment without pmi", monthly_payment.toString());            	
            }

            if (month_counter % extra_payment_frequency == 0) {
            	base_monthly_payment = base_monthly_payment.add(extra_payment);
            }
            //
            if (outstanding_loan.compareTo(monthly_payment) == -1) {
                principal_paid = outstanding_loan.add(interests_paid).subtract(interests_paid);
                monthly_payment = outstanding_loan.add(interests_paid).add(additional_cost);
            } else {
            	monthly_payment = base_monthly_payment.add(additional_cost);
                principal_paid = base_monthly_payment.subtract(interests_paid); 
            }

        	// Calculate total principal paid so far
            total_principal = total_principal.add(principal_paid);

            outstanding_loan = outstanding_loan.subtract(principal_paid);           
        	
            /* If loan-to-value (LTV) ratio (the ratio of a loan to a value of an asset purchased)
             * is < 20%, add pmi to the monthly payment */       
        	total_additional_cost = total_additional_cost.add(additional_cost);
        	total_insurance = total_insurance.add(monthly_insurance_amount);
        	total_tax = total_tax.add(monthly_tax_amount);
        	

        	
            month_counter++;
    	} else {
    		monthly_payment = Money.ZERO;
    		interests_paid = Money.ZERO;
    		principal_paid = Money.ZERO;
    		additional_cost = Money.ZERO;
    		//outstanding_loan = Money.ZERO;    	    		
    	}      
    }

	public void initialize() {
		outstanding_loan = loan_amount;
		month_counter = 1;
    	monthly_payment = base_monthly_payment;
	    principal_paid = Money.ZERO;
	    interests_paid = Money.ZERO;
	    total_interests = Money.ZERO;
	    total_principal = Money.ZERO;
	    total_additional_cost = Money.ZERO;
	    total_insurance = BigDecimal.ZERO;
	    total_tax = Money.ZERO;
	    total_pmi = Money.ZERO;
	}
    
	
	public int getTotalTermMonths() {
		return total_term_months;
	}
	
	public int getTermYears() {
		return term_years;
	}
	
	public int getTermMonths() {
		return term_months;
	}
	
    public BigDecimal getMonthlyPayment() {
    	return monthly_payment;
    }

    public BigDecimal getBaseMonthlyPayment() {
    	return base_monthly_payment;
    }
    
    public BigDecimal getInterestPaid() {
    	return interests_paid;
    }
    
    public BigDecimal getPrincipalPaid() {
        return principal_paid;
    }
    
    public BigDecimal getTotalInterestPaid() {
    	return total_interests;
    }
    
    public BigDecimal getPrincipalFraction() {
    	return Money.scale(loan_amount.divide(getTotalPayment(), Money.DECIMALS, Money.ROUNDING_MODE).multiply(Money.HUNDRED));    	
    }

    public BigDecimal getInterestFraction() {
    	return Money.scale(total_interests.divide(getTotalPayment(), Money.DECIMALS, Money.ROUNDING_MODE).multiply(Money.HUNDRED));    	
    }

    public BigDecimal getExtraCostFraction() {
    	return Money.scale(total_additional_cost.divide(getTotalPayment(), Money.DECIMALS, Money.ROUNDING_MODE).multiply(Money.HUNDRED));    	
    }
    
    public BigDecimal getTotalInsurance() {
    	return total_insurance;
    }
    
    public BigDecimal getTotalPropertyTax() {
    	return total_tax;
    }
    
    public BigDecimal getTotalPMI() {
    	return total_pmi;
    }
    
    public BigDecimal getAdditionalCost() {
    	return additional_cost;
    }
    
    public BigDecimal getTotalAdditionalCost() {
    	return total_additional_cost;
    }
    
    public BigDecimal getRemainingAmount() {
    	return outstanding_loan;
    }
        
    public BigDecimal getPropertyInsurance() {
    	return property_insurance;
    }

    public BigDecimal getPropertyTax() {
    	return property_tax;
    }

    public BigDecimal getPMI() {
    	return pmi;
    }

    public BigDecimal getPropertyInsuranceAmount() {
    	return monthly_insurance_amount;
    }

    public BigDecimal getPropertyTaxAmount() {
    	return monthly_tax_amount;
    }

    public BigDecimal getPMIAmount() {
    	return monthly_pmi_amount;
    }
    
    public BigDecimal getTotalPayment() {
    	return   total_additional_cost.add(loan_amount).add(total_interests);
    }

    
    public BigDecimal getDownpayment() {
    	return downpayment;
    }
    
    public BigDecimal getInterestRate() {
    	return init_interest_rate; 
    }

    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }  
    
    public BigDecimal getValue() {
    	return purchase_price;
    }

	public BigDecimal getAmount() {
		return monthly_payment;
	}
		
	public BigDecimal getLoanAmount() {
		return loan_amount;
	}


	public BigDecimal getPurchasePrice() {
		return purchase_price;
	}

	public BigDecimal getInitAmount() {
		return purchase_price;
	}
		
	public HistoryMortgage getHistory() {
		return history;
	}
	
	public void makeHistogram(HistogramVisitor visitor) {
		visitor.histogramMortgage(this);
	}
	
	public void makePieChart(PieChartVisitor visitor) {
		visitor.piechartMortgage(this);
	}
}
