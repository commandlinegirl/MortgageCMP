package com.codelemma.mortgagecmp.accounting;

import java.util.ArrayList;
import java.util.Collections;



import android.util.SparseArray;

public class Account {

    private ArrayList<Mortgage> mortgages = new ArrayList<Mortgage>();
    private ArrayList<Mortgage> comparisonList = new ArrayList<Mortgage>();
    private SparseArray<Mortgage> mortgage_ids = new SparseArray<Mortgage>();
    private int mortgage_id = 0;
    private int longestLoanTerm = 0;
 
    public void addMortgage(Mortgage mortgage) {
    	mortgage.setId(mortgage_id);        
    	mortgages.add(mortgage);
        mortgage_ids.put(mortgage_id, mortgage);
        mortgage_id++;      
    }  
    
    public void removeMortgage(Mortgage mortgage) {
    	if (mortgage != null) {
    		mortgages.remove(mortgage);
        	mortgage_ids.remove(mortgage.getId());
    	}
    }
    
    public Iterable<Mortgage> getMortgages() {
    	return Collections.unmodifiableList(mortgages);
    }   
    
    public Mortgage getMortgageById(int id) {
    	return mortgage_ids.get(id);
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
	
}
