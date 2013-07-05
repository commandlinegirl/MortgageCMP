package com.codelemma.mortgagecmp.accounting;

import java.math.BigDecimal;

public class FixedRateVariablePrincipalMortgage extends Mortgage {

	private final BigDecimal monthly_payment_constant;
	private BigDecimal principal_plus_interest;

	public static class Builder extends Mortgage.Builder {

		public FixedRateVariablePrincipalMortgage build() {
			if (this.getName() == null) {
			}
			return new FixedRateVariablePrincipalMortgage(this);
		}
	}

	private FixedRateVariablePrincipalMortgage(Builder builder) {
		super(builder);
		monthly_payment_constant = calculateMonthlyPaymentConstant();
		principal_plus_interest = monthly_payment_constant;
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

    @Override
	public void advance(int year, int month) {
		if (this.getMonthCounter() <= this.getTotalTermMonths() && this.getOutstandingLoan().compareTo(Money.ZERO) == 1) { 

			this.incrementNumberOfPayments();
			// Calculate interests to be paid this month and total interest paid so far
			this.setInterestPaid(Money.scale(this.getOutstandingLoan().multiply(this.getInterestRateDecimalMonthly()))); //same
			this.setTotalInterestPaid(this.getTotalInterestPaid().add(this.getInterestPaid())); //same
			
			if (this.getOutstandingLoan().compareTo(principal_plus_interest.add(this.getMonthlyExtraPayment())) == -1) {
				principal_plus_interest = this.getOutstandingLoan().add(this.getInterestPaid());
			}

			/* If loan-to-value (LTV) ratio (the ratio of a loan to a value of
			 * an asset purchased) is < 20%, add pmi to the monthly payment
			 */
			this.calculateTaxInsurancePMI();

			this.setCurrentMonthTotalPayment(principal_plus_interest);
			this.setPrincipalPaid(principal_plus_interest.add(this.getMonthlyExtraPayment()).subtract(this.getInterestPaid()));
			this.setTotalPrincipalPaid(this.getTotalPrincipalPaid().add(this.getPrincipalPaid()));
			this.setOutstandingLoan(this.getOutstandingLoan().subtract(this.getPrincipalPaid()));
			this.setTotalExtraPayment(this.getTotalExtraPayment().add(this.getMonthlyExtraPayment()));
			incrementMonthCounter();
		} else {
			this.setValuesAfterCalculation();
		}
	}

	@Override
	protected BigDecimal getMonthlyInterestPlusPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BigDecimal getMonthlyPrincipalPlusExtraMoney() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getMonthlyTotalPaymentNonPMI() {
		return monthly_payment_constant
				.add(this.getMonthlyExtraPayment())
				.add(this.getMonthlyPropertyInsurance())
				.add(this.getMonthlyPropertyTax());
	}

	@Override
	public BigDecimal getMonthlyPaymentConstant() {
		return monthly_payment_constant;
	}
	

}