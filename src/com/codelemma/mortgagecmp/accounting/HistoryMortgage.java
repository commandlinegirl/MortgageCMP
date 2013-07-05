package com.codelemma.mortgagecmp.accounting;

import java.math.BigDecimal;
import java.util.HashMap;

import android.util.Log;
import android.util.SparseArray;


public class HistoryMortgage {

	private BigDecimal[] monthlyPaymentHistory;
	private BigDecimal[] interestsPaidHistory;
	private BigDecimal[] principalPaidHistory;
	private BigDecimal[] totalInterestsHistory;	
	private BigDecimal[] additionalCostHistory;	
	private BigDecimal[] remainingAmountHistory;	
	
	private SparseArray<BigDecimal> monthlyPaymentYearly;
	private SparseArray<BigDecimal> interestsPaidYearly;
	private SparseArray<BigDecimal> principalPaidYearly;
	private SparseArray<BigDecimal> additionalCostYearly;
	private SparseArray<BigDecimal> totalInterestsAtYearEnd;
	private SparseArray<BigDecimal> remainingAmountAtYearEnd;

    private int simLength;
	private Months[] months = Months.values(); 
	//private String[] dates; // each elements is of "Jan 2013" format
	private String name;
	private int id;
	
	public HistoryMortgage(Mortgage mortgage, int simLength) {		
		monthlyPaymentHistory = new BigDecimal[simLength];
		interestsPaidHistory = new BigDecimal[simLength];
		totalInterestsHistory = new BigDecimal[simLength];
		principalPaidHistory = new BigDecimal[simLength];
		additionalCostHistory = new BigDecimal[simLength];		
		remainingAmountHistory = new BigDecimal[simLength];
		
		monthlyPaymentYearly = new SparseArray<BigDecimal>();
		interestsPaidYearly = new SparseArray<BigDecimal>();
		principalPaidYearly = new SparseArray<BigDecimal>();
		additionalCostYearly = new SparseArray<BigDecimal>();
		totalInterestsAtYearEnd = new SparseArray<BigDecimal>();
		remainingAmountAtYearEnd = new SparseArray<BigDecimal>();
		
		name = mortgage.getName();
		id = mortgage.getId();
    	this.simLength = simLength;
    	//dates = new String[simLength];
	}

    public String[] getDates(int startYear, int startMonth) {
    	String[] dates = new String[simLength];    	
    	int i;
    	int y = startYear;
    	int m = startMonth;
    	
    		for (i = 0; i < simLength; i++) {
        		dates[i] = months[m]+" "+y;
        		if (m == 11) {
                    m = 0;
                    y += 1;
                } else {
                    m++;
                }        	
        	}    	    	
    	return dates;
    }
    
    public int getSimLength() {
    	return simLength;
    }
    
	public void add(int index, int year, Mortgage acctElement) {
		Mortgage mortgage = (Mortgage) acctElement;
		try {
		    
		    principalPaidHistory[index] = mortgage.getPrincipalPaid();
		    
			Log.d("HISOTYR mortgage.getPrincipalPaid()", mortgage.getPrincipalPaid().toString());

		    
		    interestsPaidHistory[index] = mortgage.getInterestPaid();
		    additionalCostHistory[index] = mortgage.getMonthlyTaxInsurancePMISum();
		    monthlyPaymentHistory[index] = mortgage.getCurrentMonthTotalPayment();
		    remainingAmountHistory[index] = mortgage.getOutstandingLoan();	
		    
		    totalInterestsHistory[index] = mortgage.getTotalInterestPaid();
		    
		    addToYearlyHistory(principalPaidYearly, year, mortgage.getPrincipalPaid());
		    addToYearlyHistory(interestsPaidYearly, year, mortgage.getInterestPaid());
		    addToYearlyHistory(additionalCostYearly, year, mortgage.getMonthlyTaxInsurancePMISum());
		    addToYearlyHistory(monthlyPaymentYearly, year, mortgage.getCurrentMonthTotalPayment());
		    setValueInYearlyHistory(totalInterestsAtYearEnd, year, mortgage.getTotalInterestPaid());
		    setValueInYearlyHistory(remainingAmountAtYearEnd, year, mortgage.getOutstandingLoan());
					    
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	public void addToYearlyHistory(SparseArray<BigDecimal> dictionary, int year, BigDecimal value) {
		BigDecimal oldValue = dictionary.get(year);
		if (oldValue == null) {
			dictionary.put(year, value);
	    } else {
	    	dictionary.put(year, oldValue.add(value));
	    }
	}
	
	public void setValueInYearlyHistory(SparseArray<BigDecimal> dictionary, int year, BigDecimal value) {
	    dictionary.put(year, value);
	}
	
	public void makeTable(TableVisitor visitor) {
		visitor.makeTableMortgage(this);		
	}

	public BigDecimal[] getMonthlyPaymentHistory() {
		return monthlyPaymentHistory;
	}

	public BigDecimal[] getInterestsPaidHistory() {
		return interestsPaidHistory;
	}

	public BigDecimal[] getTotalInterestsHistory() {
		return totalInterestsHistory;
	}
	
	public BigDecimal[] getPrincipalPaidHistory() {
		return principalPaidHistory;
	}
	
	public BigDecimal[] getAdditionalCostHistory() {
		return additionalCostHistory;
	}
	
	public BigDecimal[] getRemainingAmountHistory() {
		return remainingAmountHistory;
	}
	
	
	public SparseArray<BigDecimal> getMonthlyPaymentYearly() {
		return monthlyPaymentYearly;
	}
	
	public SparseArray<BigDecimal> getInterestsPaidYearly() {
		return interestsPaidYearly;
	}
	
	public SparseArray<BigDecimal> getPrincipalPaidYearly() {
		return principalPaidYearly;
	}
	
	public SparseArray<BigDecimal> getAdditionalCostYearly() {
		return additionalCostYearly;
	}
	
	public SparseArray<BigDecimal> getTotalInterestsAtYearEnd() {
		return totalInterestsAtYearEnd;
	}
	
	public SparseArray<BigDecimal> getRemainingAmountAtYearEnd() {
		return remainingAmountAtYearEnd;
	}
	

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
	
    public String toString() {
		return name;
	}
	
	public boolean isNonEmpty() {
		return remainingAmountHistory.length > 0;	
    }

	public void plot(PlotVisitor visitor) {
		visitor.plotMortgage(this);
	}
}
