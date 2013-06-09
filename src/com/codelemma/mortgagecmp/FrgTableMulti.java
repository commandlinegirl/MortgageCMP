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
import com.codelemma.mortgagecmp.accounting.TableVisitor;

public class FrgTableMulti extends SherlockFragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("FrgTableMulti.onCreateView()", "called");
        return inflater.inflate(R.layout.frg_table_multi, container, false);
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
    	    TableVisitor tableMaker = new TableMaker(getSherlockActivity(), dates);
        	account.makeTable(tableMaker);
    	} //TODO: else???  	    	    	
    }
}