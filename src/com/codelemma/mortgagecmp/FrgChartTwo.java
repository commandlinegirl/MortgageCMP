package com.codelemma.mortgagecmp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.HistogramVisitor;
import com.codelemma.mortgagecmp.accounting.HistoryMortgage;
import com.codelemma.mortgagecmp.accounting.Mortgage;

public class FrgChartTwo extends SherlockFragment {
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("FrgChartTwo.onCreateView()", "called");
        return inflater.inflate(R.layout.frg_chart_two, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState); 
    	MortgageCMP appState = MortgageCMP.getInstance(); 	    	
    	Mortgage mortgage = appState.getCurrentMortgage();  
    	
        if (mortgage != null) {
        	HistoryMortgage historyMortgage = mortgage.getHistory();
        	String[] dates = historyMortgage.getDates(
        			appState.getSimulationStartYear(), 
        			appState.getSimulationStartMonth());
            
            HistogramVisitor histogramVisitor = new HistogramMaker(getSherlockActivity(), dates);
            mortgage.makeHistogram(histogramVisitor);
        }
    }
}
