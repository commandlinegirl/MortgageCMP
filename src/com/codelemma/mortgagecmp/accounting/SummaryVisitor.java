package com.codelemma.mortgagecmp.accounting;

public interface SummaryVisitor {

	public void writeSummary(FixedRateFixedPrincipalMortgage mortgage);
	public void writeSummary(FixedRateVariablePrincipalMortgage mortgage);
	public void writeSummary(AdjustableRateMortgage mortgage);
}
