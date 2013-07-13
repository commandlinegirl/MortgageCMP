package com.codelemma.mortgagecmp.accounting;

import java.math.BigDecimal;

public abstract class Mortgage {

	private final String name;
	private final BigDecimal purchase_price;
	private final BigDecimal downpayment;	
	private final BigDecimal interest_rate;	
	private final Integer term_years; 
	private final Integer term_months; // in months
	private final BigDecimal property_insurance;	
	private final BigDecimal property_tax;
	private final BigDecimal pmi_rate;
	private final BigDecimal closing_fees;
	private final BigDecimal extra_payment;
	private final int extra_payment_frequency;

	private final BigDecimal loan_amount;
	private final BigDecimal interest_rate_decimal_monthly;
	private final Integer total_term_months; // in months
	private final BigDecimal monthly_extra_payment;	
	private final BigDecimal monthly_property_insurance;
	private final BigDecimal monthly_property_tax;
	private final BigDecimal monthly_pmi_amount;
	private final BigDecimal pmi_decimal_monthly;

	private BigDecimal current_month_payment;
	private BigDecimal curent_month_tax_insurance_pmi;
	private BigDecimal tax_insurance_zeropmi;
	private BigDecimal tax_insurance_nonzeropmi;
	
	private BigDecimal total_tax;
	private BigDecimal total_pmi;
	private BigDecimal total_principal;
	private BigDecimal total_extra_payment;
	private BigDecimal total_insurance;
	private BigDecimal total_interests;
	private BigDecimal outstanding_loan;

	private BigDecimal principal_paid;
	private BigDecimal interests_paid;

	private Integer number_of_payments = 0;
	private int last_pmi_payment_month;
	private int month_counter = 1;

	private static int next_id = 0;
	private int id = next_id;
	private int previous_id;
	
	private HistoryMortgage history;

	protected Mortgage(Builder builder) {
		next_id++;
		name = builder.name;
		purchase_price = builder.purchase_price;
		downpayment = builder.downpayment;
		interest_rate = builder.interest_rate;
		term_years = builder.term_years;
		term_months = builder.term_months;
		property_insurance = builder.property_insurance;
		property_tax = builder.property_tax;
		pmi_rate = builder.pmi_rate;
		closing_fees = builder.closing_fees;
		extra_payment = builder.extra_payment;
		extra_payment_frequency = builder.extra_payment_frequency;

		loan_amount = Money.scale(purchase_price.subtract(downpayment));
		outstanding_loan = loan_amount;
		
		monthly_extra_payment = extra_payment.divide(
				new BigDecimal(extra_payment_frequency), Money.DECIMALS, Money.ROUNDING_MODE);

        interest_rate_decimal_monthly = interest_rate.divide(new BigDecimal(1200), Money.DECIMALS, Money.ROUNDING_MODE);

		total_term_months = term_years * 12 + term_months;

		monthly_property_insurance = property_insurance.divide(new BigDecimal(12), Money.DECIMALS, Money.ROUNDING_MODE);
		total_insurance = monthly_property_insurance.multiply(new BigDecimal(total_term_months));

		monthly_property_tax = property_tax.divide(new BigDecimal(12), Money.DECIMALS, Money.ROUNDING_MODE);
		total_tax = monthly_property_tax.multiply(new BigDecimal(total_term_months));

		pmi_decimal_monthly = pmi_rate.divide(new BigDecimal(1200), Money.DECIMALS, Money.ROUNDING_MODE);
		monthly_pmi_amount = Money.getPercentage(loan_amount, pmi_decimal_monthly);
		
		tax_insurance_zeropmi = monthly_property_insurance.add(monthly_property_tax);
		tax_insurance_nonzeropmi = monthly_property_insurance.add(monthly_property_tax).add(monthly_pmi_amount);

		history = new HistoryMortgage(this, total_term_months);
	}
	
	protected abstract void advance(int year, int month);
	protected abstract BigDecimal calculateMonthlyPaymentConstant();
	public abstract BigDecimal getMonthlyPaymentConstant();
	protected abstract BigDecimal getMonthlyInterestPlusPrincipal();
	protected abstract BigDecimal getMonthlyPrincipalPlusExtraMoney();
	public abstract BigDecimal getMonthlyTotalPaymentNonPMI(); // principal+interest+extra+insurance+tax
	public abstract String getType();
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getPreviousId() {
		return previous_id;
	}
	
	public void setPreviousId(int id) {
		previous_id = id;
	}
	
	public void recalculate(int index, int year, int month) {
		advance(year, month);
		history.add(index, year, this);
	}

	public void initialize() {
		month_counter = 1;
		number_of_payments = 0;
		last_pmi_payment_month = -1;

		outstanding_loan = loan_amount;		
		curent_month_tax_insurance_pmi = BigDecimal.ZERO;
		principal_paid = BigDecimal.ZERO;
		interests_paid = BigDecimal.ZERO;
		current_month_payment = BigDecimal.ZERO;
		total_pmi = BigDecimal.ZERO;
		total_extra_payment = BigDecimal.ZERO;
		total_interests = BigDecimal.ZERO;
		total_principal = BigDecimal.ZERO;
		history.initialize();
	}

	public BigDecimal getLoanAmount() {
		return loan_amount;
	}

	public BigDecimal getPurchasePrice() {
		return purchase_price;
	}

	public BigDecimal getDownpayment() {
		return downpayment;
	}
	
	public BigDecimal getTotalPayment() {
		return loan_amount
				.add(total_interests)
				.add(total_insurance)
				.add(total_tax)
				.add(total_pmi)
				.add(closing_fees);
	}
	
	public BigDecimal getCurrentMonthTotalPayment() {
		return current_month_payment;
	}

	public void setCurrentMonthTotalPayment(BigDecimal interest_plus_principal) {
		current_month_payment = interest_plus_principal
				.add(curent_month_tax_insurance_pmi);
	}

	public void setValuesAfterCalculation() {
		principal_paid = Money.ZERO;
		interests_paid = Money.ZERO;
		curent_month_tax_insurance_pmi = Money.ZERO;
		current_month_payment = Money.ZERO;
		outstanding_loan = BigDecimal.ZERO;
	}

	/////////////////////////////////
	
	public String getName() {
		return name;
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
	
	/* Loan length */
	
	public int getMonthCounter() {
		return month_counter;
	}

	public void incrementMonthCounter() {
		month_counter++;
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

	public int getNumberOfPayments() {
		return number_of_payments;
	}

	public void incrementNumberOfPayments() {
		number_of_payments++;
	}

	/* Principal */
	
	public BigDecimal getPrincipalPaid() {
		return principal_paid;
	}
	
	public void setPrincipalPaid(BigDecimal principal_paid) {
		this.principal_paid = principal_paid;
	}
	
	public BigDecimal getOutstandingLoan() {
		return outstanding_loan;
	}

	public void setOutstandingLoan(BigDecimal outstanding_loan) {
		this.outstanding_loan = outstanding_loan;
	}

	public BigDecimal getTotalPrincipalPaid() {
		return total_principal;
	}

	public void setTotalPrincipalPaid(BigDecimal total_principal) {
		this.total_principal = total_principal;
	}
	
	public BigDecimal getPrincipalFraction() {
		if (getTotalPayment().compareTo(BigDecimal.ZERO) != 0) {
		    return Money.scale(loan_amount.divide(getTotalPayment(),
				    Money.DECIMALS, Money.ROUNDING_MODE).multiply(Money.HUNDRED));
		} else {
		    return BigDecimal.ZERO;
		}
	}
	
	/* Interest */
	
	public BigDecimal getInterestPaid() {
		return interests_paid;
	}

	public void setInterestPaid(BigDecimal interests_paid) {
		this.interests_paid = interests_paid;
	}

	public BigDecimal getTotalInterestPaid() {
		return total_interests;
	}

	public void setTotalInterestPaid(BigDecimal total_interests) {
		this.total_interests = total_interests;
	}
	
	public BigDecimal getInterestRate() {
		return interest_rate;
	}
	
	public BigDecimal getInterestRateDecimalMonthly() {
		return interest_rate_decimal_monthly;
	}
	
	public BigDecimal getInterestFraction() {
		if (getTotalPayment().compareTo(BigDecimal.ZERO) != 0) {
     		return Money.scale(total_interests.divide(getTotalPayment(),
	    			Money.DECIMALS, Money.ROUNDING_MODE).multiply(Money.HUNDRED));
		} else {
			return BigDecimal.ZERO;
		}
	}
	
	
	/* Insurance */

	public BigDecimal getYearlyPropertyInsurance() {
		return property_insurance;
	}
	
	public BigDecimal getTotalInsurance() {
		return total_insurance;
	}
	
	public BigDecimal getMonthlyPropertyInsurance() {
		return monthly_property_insurance;
	}
	
	/* Tax */

	public BigDecimal getYearlyPropertyTax() {
		return property_tax;
	}

	public BigDecimal getTotalPropertyTax() {
		return total_tax;
	}
	
	public BigDecimal getMonthlyPropertyTax() {
		return monthly_property_tax;
	}
	
	/* PMI */
	
	public BigDecimal getPMI() {
		return pmi_rate;
	}

	public BigDecimal getMonthlyPMI() {
		if (last_pmi_payment_month == -1) {
			return BigDecimal.ZERO;
		} else {
		    return monthly_pmi_amount;
		}
	}

	public BigDecimal getTotalPMI() {
		return total_pmi;
	}
	
	public int lastPmiPaymentMonth() {
	    return Math.max(0, last_pmi_payment_month-1);
	}
	
	/* Closing fees */
	
	public BigDecimal getClosingFees() {
		return closing_fees; 
	}
	
	/* Additional cost (sum of totals: insurance, tax, pmi, closing fees) */
	
	public BigDecimal getTotalTaxInsurancePMIClosingFees() {
		return total_tax
		.add(total_insurance)
		.add(total_pmi)
		.add(closing_fees);
	}
	
	public BigDecimal getMonthlyTaxInsurancePMISum() {
		return curent_month_tax_insurance_pmi;
	}

	public BigDecimal calculateTaxInsurancePMI() { 
		BigDecimal ltv = (total_principal.add(downpayment)).divide(
				purchase_price, Money.DECIMALS, Money.ROUNDING_MODE);
		if (ltv.compareTo(new BigDecimal("0.2")) == -1) {
			curent_month_tax_insurance_pmi = tax_insurance_nonzeropmi;
			total_pmi = total_pmi.add(monthly_pmi_amount);
			last_pmi_payment_month++;
		} else {
			curent_month_tax_insurance_pmi = tax_insurance_zeropmi;
		}
		return curent_month_tax_insurance_pmi;
	}

	public BigDecimal getTotalTaxInsurancePMIClosingFeesFraction() {
		if (getTotalPayment().compareTo(BigDecimal.ZERO) != 0) {		
		    return Money.scale(getTotalTaxInsurancePMIClosingFees().divide(getTotalPayment(),
			    	Money.DECIMALS, Money.ROUNDING_MODE).multiply(Money.HUNDRED));
		} else {
			return BigDecimal.ZERO;
		}
	}
	
	/* Extra payment */
	
	public int getExtraPaymentFrequency() {
		return extra_payment_frequency;
	}
	
	public BigDecimal getMonthlyExtraPayment() {
		return monthly_extra_payment;
	}

	public BigDecimal getExtraPayment() {
		return extra_payment;
	}
	
	public BigDecimal getTotalExtraPayment() {
		return total_extra_payment;
	}

	public void setTotalExtraPayment(BigDecimal total_extra_payment) {
		this.total_extra_payment = total_extra_payment;
	}

	public BigDecimal getExtraPaymentFraction() {
		if (getTotalPayment().compareTo(BigDecimal.ZERO) != 0) {
    		return Money.scale(total_extra_payment.divide(getTotalPayment(),
	    			Money.DECIMALS, Money.ROUNDING_MODE).multiply(Money.HUNDRED));
		} else {
			return BigDecimal.ZERO;
		}
	}
	
	public static abstract class Builder {
		
		private String name;
		private BigDecimal purchase_price;
		private BigDecimal downpayment;
		private BigDecimal interest_rate;
		private Integer term_years;
		private Integer term_months;
		private BigDecimal property_insurance;
		private BigDecimal property_tax;
		private BigDecimal pmi_rate;
		private BigDecimal closing_fees;
		private BigDecimal extra_payment;
		private Integer extra_payment_frequency;
		
		public String getName() {
			return name;
		}

		public BigDecimal getPurchasePrice() {
			return purchase_price;
		}

		public BigDecimal getDownpayment() {
			return downpayment;
		}
		
		public BigDecimal getInterestRate() {
			return interest_rate;
		}
		
		public Integer getTermYears() {
			return term_years;
		}
		
		public Integer getTermMonths() {
			return term_months;
		}
		
		public BigDecimal getPropertyInsurance() {
			return property_insurance;
		}
		
		public BigDecimal getPropertyTax() {
			return property_tax;
		}
		
		public BigDecimal getPMIRate() {
			return pmi_rate;
		}
		
		public BigDecimal getClosingFees() {
			return closing_fees;
		}
		
		
		public Builder name(String name) {
			this.name = name; 
			return this;
		}

		public Builder purchase_price(BigDecimal purchase_price) {
			this.purchase_price = purchase_price; 
			return this;
		}

		public Builder downpayment(BigDecimal downpayment) {
			this.downpayment = downpayment; 
			return this;
		}

		public Builder interest_rate(BigDecimal interest_rate) {
			this.interest_rate = interest_rate; 
			return this;
		}

		public Builder term_years(Integer term_years) {
			this.term_years = term_years; 
			return this;
		}

		public Builder term_months(Integer term_months) {
			this.term_months = term_months; 
			return this;
		}

		public Builder property_insurance(BigDecimal property_insurance) {
			this.property_insurance = property_insurance; 
			return this;
		}

		public Builder property_tax(BigDecimal property_tax) {
			this.property_tax = property_tax; 
			return this;
		}

		public Builder pmi_rate(BigDecimal pmi_rate) {
			this.pmi_rate = pmi_rate; 
			return this;
		}

		public Builder closing_fees(BigDecimal closing_fees) {
			this.closing_fees = closing_fees; 
			return this;
		}

		public Builder extra_payment(BigDecimal extra_payment) {
			this.extra_payment = extra_payment; 
			return this;
		}

		public Builder extra_payment_frequency(Integer extra_payment_frequency) {
			this.extra_payment_frequency = extra_payment_frequency; 
			return this;
		}
		
		public abstract Mortgage build();
	}

}
