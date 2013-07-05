package com.codelemma.mortgagecmp.accounting;

import java.math.BigDecimal;

import android.util.Log;

public class FixedRateFixedPrincipalMortgage extends Mortgage {

	private final BigDecimal monthly_payment_constant;
	private BigDecimal principal;

	public static class Builder extends Mortgage.Builder {

		public FixedRateFixedPrincipalMortgage build() {
			// here check if all build setters called
			return new FixedRateFixedPrincipalMortgage(this);
		}
	}

	private FixedRateFixedPrincipalMortgage(Builder builder) {
		super(builder);
		monthly_payment_constant = calculateMonthlyPaymentConstant();
		principal = monthly_payment_constant;
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

			if (this.getOutstandingLoan().compareTo(principal.add(this.getMonthlyExtraPayment())) == -1) {
				principal = this.getOutstandingLoan().add(this.getInterestPaid());
			}

			/* If loan-to-value (LTV) ratio (the ratio of a loan to a value of
			 * an asset purchased) is < 20%, add pmi to the monthly payment
			 */
			this.calculateTaxInsurancePMI();

			this.setCurrentMonthTotalPayment(principal.add(this.getInterestPaid()));
			Log.d("principal.add(this.getMonthlyExtraPayment()", principal.add(this.getMonthlyExtraPayment()).toString());
			this.setPrincipalPaid(principal.add(this.getMonthlyExtraPayment()));
			this.setTotalPrincipalPaid(this.getTotalPrincipalPaid().add(this.getPrincipalPaid()));
			this.setOutstandingLoan(this.getOutstandingLoan().subtract(this.getPrincipalPaid()));
			this.setTotalExtraPayment(this.getTotalExtraPayment().add(this.getMonthlyExtraPayment()));
			incrementMonthCounter();
		} else {
			this.setValuesAfterCalculation();
		}
	}

    protected BigDecimal getMonthlyMinInterestPlusPrincipal() {
    	return null;
    }

    protected BigDecimal getMonthlyMinInterestPlusPrincipalMonthNumber() {
    	return null;
    }
    
    protected BigDecimal getMonthlyMaxInterestPlusPrincipal() {
    	return null;
    }

    protected BigDecimal getMonthlyMaxInterestPlusPrincipalMonthNumber() {
    	return null;
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
				.add(this.getInterestPaid())
				.add(this.getMonthlyExtraPayment())
				.add(this.getMonthlyPropertyInsurance())
				.add(this.getMonthlyPropertyTax());
	}
	
	@Override
	public BigDecimal getMonthlyPaymentConstant() {
		return monthly_payment_constant;
	}
	

}