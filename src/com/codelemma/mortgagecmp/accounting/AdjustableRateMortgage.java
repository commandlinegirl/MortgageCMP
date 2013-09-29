package com.codelemma.mortgagecmp.accounting;

import java.math.BigDecimal;

public class AdjustableRateMortgage extends Mortgage {

	private int adjustment_period;
	private int months_between_adjustments;
	private BigDecimal max_single_rate_adjustment;
	private BigDecimal interest_rate_cap;
	private BigDecimal max_single_rate_adjustment_decimal_monthly;
	private BigDecimal interest_rate_cap_decimal_monthly;

	private int arm_type;
	
	private BigDecimal current_interest_rate_decimal_monthly;
	private BigDecimal monthly_payment_constant;
	private BigDecimal max_monthly_payment_constant; // principal+interest
	private int max_monthly_payment_constant_month; // principal+interest
	private BigDecimal min_monthly_payment_constant; // principal+interest
	private int min_monthly_payment_constant_month; // principal+interest
	private BigDecimal principal_plus_interest;
	private BigDecimal principal_plus_interest_plus_extra_payment;

	protected AdjustableRateMortgage(Builder builder) {
		super(builder);
		adjustment_period = builder.adjustment_period;
		months_between_adjustments = builder.months_between_adjustments;
		max_single_rate_adjustment = builder.max_single_rate_adjustment;
		max_single_rate_adjustment_decimal_monthly = builder.max_single_rate_adjustment.divide(new BigDecimal(1200), Money.DECIMALS, Money.ROUNDING_MODE);
		interest_rate_cap = builder.interest_rate_cap;
		interest_rate_cap_decimal_monthly = builder.interest_rate_cap.divide(new BigDecimal(1200), Money.DECIMALS, Money.ROUNDING_MODE);

		arm_type = builder.arm_type;
		
		monthly_payment_constant = calculateMonthlyPaymentConstant();
		max_monthly_payment_constant = monthly_payment_constant;
	    min_monthly_payment_constant = monthly_payment_constant;
		principal_plus_interest = monthly_payment_constant;
		principal_plus_interest_plus_extra_payment = principal_plus_interest.add(this.getMonthlyExtraPayment());
		current_interest_rate_decimal_monthly = this.getInterestRateDecimalMonthly();
	}

	@Override
	protected BigDecimal calculateMonthlyPaymentConstant() {
		if(this.getTotalTermMonths() == 0) {
			return BigDecimal.ZERO;
		}

		if (this.getInterestRateDecimalMonthly().compareTo(BigDecimal.ZERO) == 0) {
			return Money.scale(this.getLoanAmount().divide(new BigDecimal(this.getTotalTermMonths()), Money.DECIMALS, Money.ROUNDING_MODE));
		}

		BigDecimal factor = (this.getInterestRateDecimalMonthly().add(Money.ONE)).pow(this.getTotalTermMonths());
		BigDecimal factor_minus_one = factor.subtract(Money.ONE);
		try {
			factor.divide(factor_minus_one, Money.DECIMALS, Money.ROUNDING_MODE);
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
		}

		return Money.scale(this.getInterestRateDecimalMonthly().multiply(this.getLoanAmount()
				.multiply(factor.divide(factor_minus_one, Money.DECIMALS, Money.ROUNDING_MODE))));
	}
	
	protected BigDecimal calculateMonthlyPaymentConstant(
			int total_terms_months, 
			BigDecimal interest_rate_decimal_monthly,
			BigDecimal loan_amount) {
		
		if(total_terms_months == 0) {
			return BigDecimal.ZERO;
		}

		if (interest_rate_decimal_monthly.compareTo(BigDecimal.ZERO) == 0) {
			return Money.scale(loan_amount.divide(new BigDecimal(total_terms_months), Money.DECIMALS, Money.ROUNDING_MODE));
		}

		BigDecimal factor = (interest_rate_decimal_monthly.add(Money.ONE)).pow(total_terms_months);
		BigDecimal factor_minus_one = factor.subtract(Money.ONE);
		try {
			factor.divide(factor_minus_one, Money.DECIMALS, Money.ROUNDING_MODE);
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
		}

		return Money.scale(interest_rate_decimal_monthly.multiply(loan_amount
				.multiply(factor.divide(factor_minus_one, Money.DECIMALS, Money.ROUNDING_MODE))));
	}

	@Override
	protected void advance(int year, int month) {

		if (((this.getMonthCounter()-1) >= adjustment_period) && ((this.getMonthCounter()-1) % months_between_adjustments) == 0) {			
			
			BigDecimal next_interest_rate = current_interest_rate_decimal_monthly.add(max_single_rate_adjustment_decimal_monthly);
			current_interest_rate_decimal_monthly = 
					next_interest_rate.compareTo(interest_rate_cap_decimal_monthly) < 0 ? next_interest_rate : interest_rate_cap_decimal_monthly;
			this.setInterestRateDecimalMonthly(current_interest_rate_decimal_monthly);
			principal_plus_interest = calculateMonthlyPaymentConstant(
					this.getTotalTermMonths() - this.getMonthCounter() + 1, 
					current_interest_rate_decimal_monthly,
					this.getOutstandingLoan());
			principal_plus_interest_plus_extra_payment = principal_plus_interest.add(this.getMonthlyExtraPayment());
			
		}
		// perform normal calculations

		if (this.getMonthCounter() <= this.getTotalTermMonths() && this.getOutstandingLoan().compareTo(Money.ZERO) == 1) { 

			this.incrementNumberOfPayments();
			// Calculate interests to be paid this month and total interest paid so far
			this.setInterestPaid(Money.scale(this.getOutstandingLoan().multiply(this.getInterestRateDecimalMonthly()))); //same
			this.setTotalInterestPaid(this.getTotalInterestPaid().add(this.getInterestPaid())); //same
			
			if (this.getOutstandingLoan().compareTo(principal_plus_interest_plus_extra_payment) == -1) {
				principal_plus_interest_plus_extra_payment = this.getOutstandingLoan().add(this.getInterestPaid());
			}

			/* If loan-to-value (LTV) ratio (the ratio of a loan to a value of
			 * an asset purchased) is < 20%, add pmi to the monthly payment
			 */
			this.calculateTaxInsurancePMI();

			this.setCurrentMonthTotalPayment(principal_plus_interest_plus_extra_payment);
			this.setPrincipalPaid(principal_plus_interest_plus_extra_payment.subtract(this.getInterestPaid()));
			this.setTotalPrincipalPaid(this.getTotalPrincipalPaid().add(this.getPrincipalPaid()));
			this.setOutstandingLoan(this.getOutstandingLoan().subtract(this.getPrincipalPaid()));
			this.setTotalExtraPayment(this.getTotalExtraPayment().add(this.getMonthlyExtraPayment()));
			incrementMonthCounter();
			
			// set min and max payment			
			if (principal_plus_interest_plus_extra_payment.compareTo(max_monthly_payment_constant) > 0) {
				max_monthly_payment_constant = principal_plus_interest_plus_extra_payment;
				max_monthly_payment_constant_month = this.getMonthCounter();
			}
			if (principal_plus_interest_plus_extra_payment.compareTo(min_monthly_payment_constant) < 0) {
				min_monthly_payment_constant = principal_plus_interest_plus_extra_payment;
				min_monthly_payment_constant_month = this.getMonthCounter();
			}
			
		} else {
			this.setValuesAfterCalculation();
		}
	}

	@Override
	public BigDecimal getMonthlyPaymentConstant() {
		return monthly_payment_constant;
	}

	@Override
	public BigDecimal getMonthlyTotalPaymentNonPMI() {
		return principal_plus_interest
				.add(this.getMonthlyExtraPayment())
				.add(this.getMonthlyPropertyInsurance())
				.add(this.getMonthlyPropertyTax());
	}

	@Override
	public String getType() {
		return MortgageNameConstants.ADJUSTABLE_RATE_MORTGAGE;
	}

	@Override
	public void listMonthlyPayment(MonthlyPaymentListingVisitor mplv) {
		mplv.listMonthlyPaymentBreakdown(this);
	}

	@Override
	public void fillInput(FillInputVisitor fiv) {
		fiv.fillInput(this);
	}
	
	@Override
	public void writeSummary(SummaryVisitor sv) {
		sv.writeSummary(this);
	}
	
	public void setCurrentInterestRateDecimalMonthly(BigDecimal interest_rate) {
		current_interest_rate_decimal_monthly = interest_rate;
	}
	
	public BigDecimal getMinPrincipalInterestPayment() {
		return min_monthly_payment_constant;
	}

	public int getMonthWithMinPrincipalInterestPayment() {
		return Math.max(min_monthly_payment_constant_month-2, 0);		
	}
	
	public BigDecimal getMaxPrincipalInterestPayment() {
		return max_monthly_payment_constant;
	}
	
	public int getMonthWithMaxPrincipalInterestPayment() {
		return Math.max(max_monthly_payment_constant_month-2, 0);
	}
	
	public int getAdjustmentPeriod() {
		return adjustment_period;
	}

	public int getMonthsBetweenAdjustments() {
		return months_between_adjustments;
	}

	public BigDecimal getMaxSingleRateAdjustment() {
		return max_single_rate_adjustment;
	}

	public BigDecimal getInterestRateCap() {
		return interest_rate_cap;
	}

	public BigDecimal getPrincipalInterestExtraBeforeAdjustment() {
		return monthly_payment_constant.add(this.getMonthlyExtraPayment());
	}
	
	public int getARMType() {
		return arm_type;
	}
	
	public static class Builder extends Mortgage.Builder<Builder> {

		private int adjustment_period;
		private int months_between_adjustments;
		private BigDecimal max_single_rate_adjustment;
		private BigDecimal interest_rate_cap;
		private int arm_type;

		@Override
		protected Builder getThis() {
			return this;
		}
		
		@Override
		public AdjustableRateMortgage build() {
			return new AdjustableRateMortgage(this);
		}

		public int getAdjustmentPeriod() {
			return adjustment_period;
		}
				
		public Builder adjustment_period(int adjustment_period) {
			this.adjustment_period = adjustment_period; 
			return this;
		}
		
		public int getMonthsBetweenAdjustments() {
			return months_between_adjustments;
		}
				
		public Builder months_between_adjustments(int months_between_adjustments) {
			this.months_between_adjustments = months_between_adjustments; 
			return this;
		}
		
		public BigDecimal getMaxSingleRateAdjustment() {
			return max_single_rate_adjustment;
		}

		public Builder max_single_rate_adjustment(BigDecimal max_single_rate_adjustment) {
			this.max_single_rate_adjustment = max_single_rate_adjustment;
			return this;
		}

		public BigDecimal getInterestRateCap() {
			return interest_rate_cap;
		}

		public Builder interest_rate_cap(BigDecimal interest_rate_cap) {
			this.interest_rate_cap = interest_rate_cap; 
			return this;
		}

		public Builder arm_type(int arm_type) {
			this.arm_type = arm_type; 
			return this;
		}
	}
}