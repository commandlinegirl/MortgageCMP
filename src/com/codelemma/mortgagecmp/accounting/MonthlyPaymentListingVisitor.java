package com.codelemma.mortgagecmp.accounting;

public interface MonthlyPaymentListingVisitor {
	public void listMonthlyPayment(Mortgage mortgage);

	public void listConstantPayment(
			FixedRateVariablePrincipalMortgage mortgage);

	public void listConstantPrincipal(
			FixedRateFixedPrincipalMortgage mortgage);		
}
