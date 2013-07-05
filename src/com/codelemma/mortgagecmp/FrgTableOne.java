package com.codelemma.mortgagecmp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.HistoryMortgage;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.codelemma.mortgagecmp.accounting.TableVisitor;

public class FrgTableOne extends SherlockFragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("FrgTableOne.onCreateView()", "called");
        return inflater.inflate(R.layout.frg_table_one, container, false);
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
        	TableVisitor tableMaker = new TableMaker(getSherlockActivity(), dates);
        	historyMortgage.makeTable(tableMaker);
		} else {
			LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.frg_table_one);
	    	ll.removeAllViews();
		}
    }
}