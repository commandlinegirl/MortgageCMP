package com.codelemma.mortgagecmp.accounting;

public interface HistogramVisitor { 
    void histogramMortgage(Mortgage mortgage);
	void plotLoanBreakdownComparison(Account account);
}
