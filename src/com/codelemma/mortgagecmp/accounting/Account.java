package com.codelemma.mortgagecmp.accounting;

import java.util.ArrayList;
import java.util.Collections;

import com.codelemma.mortgagecmp.MortgageCMP;

public class Account {

    private ArrayList<Mortgage> mortgages = new ArrayList<Mortgage>();
    private ArrayList<Mortgage> comparisonList = new ArrayList<Mortgage>();
    private int longestLoanTerm = 0;
    private Mortgage currentMortgage;
    private Mortgage longestTermMortgage;

    public void addMortgage(Mortgage mortgage) {
    	mortgages.add(mortgage);
    }  
    
    public void removeMortgage(Mortgage mortgage) {
    	if (mortgage != null) {
    		mortgages.remove(mortgage);
    	}
    }

    public void removeMortgage(int id) {
    	removeMortgage(getMortgageById(id));
    }
    
    public Iterable<Mortgage> getMortgages() {
    	return Collections.unmodifiableList(mortgages);
    }   
    
    public Mortgage getMortgageById(int id) {
    	if (id < 0) {
    		return null;
    	}
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
    
	public void addToComparisonList(Mortgage mortgage) {
		comparisonList.add(mortgage);
	}
	
	public void clearComparisonList() {
		comparisonList.clear();
	}

	public void removeMortgages() {
		setCurrentMortgage(null);
		mortgages.clear();
	    clearComparisonList();
	    setLongestLoanTerm(0);
	    setLongestMortgage(null);
	}
	
	public void removeFromComparisonList(Mortgage mortgage) {
		comparisonList.remove(mortgage);
	}	
	
	public ArrayList<Mortgage> getMortgagesToCompare() {
		return comparisonList;
	}
	
	public Mortgage getLongestMortgage() {
        return longestTermMortgage;
	}
	
	public int getLongestLoanTerm() {
		return longestLoanTerm;
	}
    
	
	public void setLongestMortgage(Mortgage mortgage) {
        longestTermMortgage = mortgage;
	}
	
	public void setLongestLoanTerm(int term) {
		longestLoanTerm = term;
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