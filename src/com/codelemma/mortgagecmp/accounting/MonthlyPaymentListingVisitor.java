package com.codelemma.mortgagecmp.accounting;

public interface MonthlyPaymentListingVisitor {

	public void listMonthlyPaymentBreakdown(
			FixedRateVariablePrincipalMortgage mortgage);

	public void listMonthlyPaymentBreakdown(
			FixedRateFixedPrincipalMortgage mortgage);		
	
	public void listMonthlyPaymentBreakdown(
			AdjustableRateMortgage mortgage);
}