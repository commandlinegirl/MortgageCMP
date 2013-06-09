package com.codelemma.mortgagecmp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.Account;
import com.codelemma.mortgagecmp.accounting.HistoryMortgage;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.codelemma.mortgagecmp.accounting.PlotVisitor;

public class FrgChartMultiMonthly extends SherlockFragment {
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("FrgChartMulti.onCreateView()", "called");
        return inflater.inflate(R.layout.frg_chart_multi_monthly, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState); 
    	MortgageCMP appState = MortgageCMP.getInstance();
    	Account account = appState.getAccount();
    	
    	/* Check which mortgage has the longest loan term;
    	 * build date list for this term */
    	Mortgage mortgage = account.getLongestMortgage();
    	
    	if (mortgage != null) {
    	    HistoryMortgage historyMortgage = mortgage.getHistory();
    	    String[] dates = historyMortgage.getDates(
    			appState.getSimulationStartYear(), 
    			appState.getSimulationStartMonth());
        
            PlotVisitor plotVisitor = new Plotter(getSherlockActivity(), dates);
            account.plotComparisonRates(plotVisitor);
    	} //TODO: else???
    }
}