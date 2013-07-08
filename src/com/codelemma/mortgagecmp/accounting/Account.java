package com.codelemma.mortgagecmp.accounting;

import java.util.ArrayList;
import java.util.Collections;

public class Account {

    private ArrayList<Mortgage> mortgages = new ArrayList<Mortgage>();
    private ArrayList<Mortgage> comparisonList = new ArrayList<Mortgage>();
    private int longestLoanTerm = 0;
    private Mortgage currentMortgage;

    public void addMortgage(Mortgage mortgage) {
    	mortgages.add(mortgage);
    }  
    
    public void removeMortgage(Mortgage mortgage) {
    	if (mortgage != null) {
    		mortgages.remove(mortgage);
    	}
    }
    
    public Iterable<Mortgage> getMortgages() {
    	return Collections.unmodifiableList(mortgages);
    }   
    
    public Mortgage getMortgageById(int id) {
        for (Mortgage mortgage: mortgages) {
            if (mortgage.getId() == id) {
                return mortgage;
            }
        }
    	return null;
    }
    
    public int getMortgagesSize() {
    	return mortgages.size();
    }
    
    public void setMortgagesToCompare(int[] ids) {

    }
    
	public void addToComparisonList(Mortgage mortgage) {
		comparisonList.add(mortgage);
	}
	
	public void clearComparisonList() {
		comparisonList.clear();
	}

	public void removeFromComparisonList(Mortgage mortgage) {
		comparisonList.remove(mortgage);
	}	
	
	public ArrayList<Mortgage> getMortgagesToCompare() {
		return comparisonList;
	}
	
	public Mortgage getLongestMortgage() {
		/* Return mortgage from comparisonList with longest loan term */
		int currentTermLen = 0;
		Mortgage currentLongestMortgage = null;
		for (Mortgage m : comparisonList) {
			int term = m.getTotalTermMonths();
			if (m.getTotalTermMonths() >= currentTermLen) {
				currentLongestMortgage = m;
				currentTermLen = term; 
			}
		}
		longestLoanTerm = currentTermLen;
		return currentLongestMortgage;
	}
	
	public int getLongestLoanTerm() {
		return longestLoanTerm;
	}
    
    public boolean mortgageExists(Mortgage mortgage) {
    	for (Mortgage m : mortgages) {
    		if (m == mortgage) {
    			return true;
    		}
    	}
    	return false;
    }	
    
	public void plotComparisonOutstandingPrincipal(PlotVisitor visitor) {
		visitor.plotComparisonOutstandingPrincipal(this);
	}

	public void plotComparisonTotalInterest(PlotVisitor visitor) {
		visitor.plotComparisonTotalInterest(this);
	}

	public void plotComparisonRates(PlotVisitor visitor) {
		visitor.plotComparisonRates(this);
	}
	
	public void histogramLoanBreakdownMulti(HistogramVisitor visitor) {
		visitor.plotLoanBreakdownComparison(this);
	}
	
    public Mortgage getCurrentMortgage() {
    	return currentMortgage;
    }
    
    public void setCurrentMortgage(Mortgage mortgage) {
    	currentMortgage = mortgage;
    }
}