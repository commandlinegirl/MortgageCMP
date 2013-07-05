package com.codelemma.mortgagecmp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;



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
    	
    	/* Check which mortgage has the longest loan term;
    	 * build date list for this term */
    	Mortgage mortgage = appState.getAccount().getLongestMortgage();
    	
    	if (mortgage != null) {
    	    HistoryMortgage historyMortgage = mortgage.getHistory();
    	    String[] dates = historyMortgage.getDates(
    			appState.getSimulationStartYear(), 
    			appState.getSimulationStartMonth());
        
            PlotVisitor plotVisitor = new Plotter(getSherlockActivity(), dates);
            appState.getAccount().plotComparisonRates(plotVisitor);
    	} 
    	
    	if (appState.getAccount().getMortgagesToCompare().size() == 0) {
    		LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.frg_chart_multi_monthly);
    		ll.removeAllViews();
    	}
    }
}