package com.codelemma.mortgagecmp.accounting;

import java.math.BigDecimal;

public class FixedRateFixedPrincipalMortgage extends Mortgage {

	private final BigDecimal monthly_payment_constant;
	private BigDecimal principal;
	private BigDecimal principal_plus_extra_payment;

	public static class Builder extends Mortgage.Builder<Builder> {

		@Override
		protected Builder getThis() {
			return this;
		}
		
		@Override
		public FixedRateFixedPrincipalMortgage build() {
			// here check if all build setters called
			return new FixedRateFixedPrincipalMortgage(this);
		}
	}

	private FixedRateFixedPrincipalMortgage(Builder builder) {
		super(builder);
		monthly_payment_constant = calculateMonthlyPaymentConstant();
		principal = monthly_payment_constant;
		principal_plus_extra_payment = principal.add(this.getMonthlyExtraPayment());		
	}

	@Override
	protected BigDecimal calculateMonthlyPaymentConstant() {
		// calculate monthly, fixed principal
		if(this.getTotalTermMonths() == 0) {
			return BigDecimal.ZERO;
		}
		return Money.scale(this.getLoanAmount().divide(
				new BigDecimal(this.getTotalTermMonths()), Money.DECIMALS, Money.ROUNDING_MODE));
	}

    @Override
	public void advance(int year, int month) {
		if (this.getMonthCounter() <= this.getTotalTermMonths() && this.getOutstandingLoan().compareTo(Money.ZERO) == 1) {
			this.incrementNumberOfPayments();

			// Calculate interests to be paid this month and total interest paid so far
			this.setInterestPaid(Money.scale(this.getOutstandingLoan().multiply(this.getInterestRateDecimalMonthly()))); //same
			this.setTotalInterestPaid(this.getTotalInterestPaid().add(this.getInterestPaid())); //same

			BigDecimal principal_plus_interest_plus_extra_payment = principal_plus_extra_payment.add(this.getInterestPaid());

			if (this.getOutstandingLoan().compareTo(principal_plus_interest_plus_extra_payment) == -1) {
				principal_plus_interest_plus_extra_payment = this.getOutstandingLoan().add(this.getInterestPaid());
				//principal_plus_extra_payment = this.getOutstandingLoan();
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
		} else {
			this.setValuesAfterCalculation();
		}
	}

	@Override
	public BigDecimal getMonthlyTotalPaymentNonPMI() {
		return monthly_payment_constant
				.add(this.getInterestPaid())
				.add(this.getMonthlyExtraPayment())
				.add(this.getMonthlyPropertyInsurance())
				.add(this.getMonthlyPropertyTax());
	}

	@Override
	public BigDecimal getMonthlyPaymentConstant() {
		return monthly_payment_constant;
	}

    @Override
	public String getType() {
		return MortgageNameConstants.FIXED_RATE_FIXED_PRINCIPAL;
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
	
	public BigDecimal getMinMonthlyInterest() {
		if (this.getNumberOfPayments() < 1) {
			return BigDecimal.ZERO;
		}
		return this.getHistory().getInterestsPaidHistory()[Math.max(0, this.getNumberOfPayments()-2)]; //TODO: exceptions!!
	}

	public BigDecimal getMaxMonthlyInterest() {
		if (this.getNumberOfPayments() < 1) {
			return BigDecimal.ZERO;
		}
		return this.getHistory().getInterestsPaidHistory()[0];
	}
}