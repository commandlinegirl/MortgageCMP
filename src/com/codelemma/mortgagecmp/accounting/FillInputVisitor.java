package com.codelemma.mortgagecmp.accounting;

public interface FillInputVisitor {
	public void fillInput(
			FixedRateVariablePrincipalMortgage mortgage);

	public void fillInput(
			FixedRateFixedPrincipalMortgage mortgage);		
	
	public void fillInput(
			AdjustableRateMortgage mortgage);
	}
