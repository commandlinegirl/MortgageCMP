package com.codelemma.mortgagecmp.accounting;
public interface PlotVisitor {
	void plotMortgage(HistoryMortgage mortgage);
	void plotComparisonRates(Account account);
	void plotComparisonTotalInterest(Account account);
	void plotComparisonOutstandingPrincipal(Account account);		
}
